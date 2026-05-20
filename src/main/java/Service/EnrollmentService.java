import java.util.*;

public class EnrollmentService {

    private EnrollmentRepository enrollmentRepository;
    private StudentRepository studentRepository;
    private CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Enrollment enrollStudent(String studentId, String courseId) {
        Student student = studentRepository.findByStudentId(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        Course course = courseRepository.findByCourseId(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        for (Enrollment existing : enrollmentRepository.findByStudent(studentId)) {
            EnrollmentStatus s = existing.getStatus();
            if (existing.getCourse() != null && existing.getCourse().getCourseId().equals(courseId) && (s == EnrollmentStatus.ACTIVE || s == EnrollmentStatus.PENDING)) {
                // Если уже есть активная запись, второй раз на тот же курс нельзя.
                throw new IllegalStateException("Student " + studentId + " is already enrolled in " + courseId);
            }
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        enrollment = enrollmentRepository.save(enrollment);
        course.addEnrollment(enrollment);

        System.out.println("[EnrollmentService] Enrolled " + student.getFullName()  + " in " + course.getName());
        return enrollment;
    }

    public void dropCourse(String studentId, String courseId) {
        Course course = courseRepository.findByCourseId(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        Enrollment enrollment = enrollmentRepository.findByStudentAndCourse(studentId, course).orElseThrow(() -> new IllegalStateException("No active enrollment found for student " + studentId + " in course " + courseId));

        if (enrollment.getStatus() == EnrollmentStatus.REJECTED) {
            throw new IllegalStateException("Enrollment is already dropped.");
        }

        enrollment.setStatus(EnrollmentStatus.REJECTED);
        enrollmentRepository.save(enrollment);
        course.removeEnrollment(enrollment);

        System.out.println("[EnrollmentService] Dropped student " + studentId + " from " + course.getName());
    }

    public List<Enrollment> getStudentEnrollments(String studentId) {
        studentRepository.findByStudentId(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        return enrollmentRepository.findByStudent(studentId);
    }

    public List<Enrollment> getCourseRoster(String courseId) {
        Course course = courseRepository.findByCourseId(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        return course.getEnrollments();
    }

    public void completeCourse(Long enrollmentId, double first, double second, double fin) {
        if (fin + first + second < 0.0 || fin + first + second > 100.0) {
            throw new IllegalArgumentException("Grade must be between 0 and 100.");
        }

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));

        if (enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            throw new IllegalStateException("Can only complete an ACTIVE enrollment.");
        }

        enrollment.setStatus(EnrollmentStatus.COMPLETED);

        Mark mark = new Mark(first, second, fin);
        enrollment.setMark(mark);

        enrollmentRepository.save(enrollment);

        System.out.println("[EnrollmentService] Completed enrollment #" + enrollmentId + " with grade " + mark.calculateTotal());
    }

    public EnrollmentStatus getEnrollmentStatus(String studentId, String courseId) {
        Course course = courseRepository.findByCourseId(courseId).orElse(null);
        if (course == null) return null;

        return enrollmentRepository.findByStudentAndCourse(studentId, course).map(Enrollment::getStatus).orElse(null);
    }

    public boolean isStudentEnrolled(String studentId, String courseId) {
        EnrollmentStatus status = getEnrollmentStatus(studentId, courseId);
        return status == EnrollmentStatus.ACTIVE;
    }

    public boolean isEligibleForRetake(String studentId, String courseId) {
        return isEligibleForRetake(findLatestEnrollmentWithMark(studentId, courseId));
    }

    public Enrollment requestRetake(String studentId, String courseId) {
        Enrollment enrollment = findLatestEnrollmentWithMark(studentId, courseId);
        if (!isEligibleForRetake(enrollment)) {
            // На retake можно только если 2 attestations меньше 30 вместе или final меньше 20.
            throw new IllegalStateException("Student is not eligible for retake.");
        }
        enrollment.setStatus(EnrollmentStatus.RETAKE);
        enrollmentRepository.save(enrollment);
        if (enrollment.getCourse() != null) {
            enrollment.getCourse().removeEnrollment(enrollment);
        }
        return enrollment;
    }

    private boolean isEligibleForRetake(Enrollment enrollment) {
        Mark mark = enrollment.getMark();
        if (mark == null) {
            return false;
        }
        double attestations = mark.getFirstAttestation() + mark.getSecondAttestation();
        return attestations < 30.0 || mark.getFinal() < 20.0;
    }

    private Enrollment findLatestEnrollmentWithMark(String studentId, String courseId) {
        studentRepository.findByStudentId(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        courseRepository.findByCourseId(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        List<Enrollment> enrollments = enrollmentRepository.findByStudent(studentId);
        for (int i = enrollments.size() - 1; i >= 0; i--) {
            Enrollment e = enrollments.get(i);
            if (e.getCourse() != null && e.getCourse().getCourseId().equals(courseId) && e.getMark() != null) {
                return e;
            }
        }
        throw new IllegalStateException("No graded enrollment found for student " + studentId + " in course " + courseId);
    }
}
