import java.util.*;

public class MarkService {

    private final MarkRepository       markRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final TeacherRepository    teacherRepository;

    public MarkService(MarkRepository markRepository,
                       EnrollmentRepository enrollmentRepository,
                       TeacherRepository teacherRepository) {
        this.markRepository       = markRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.teacherRepository    = teacherRepository;
    }

    public Mark assignMark(Long teacherId, String studentId, String courseId,
                           double value, MarkType type, String comment) {
        validateScore(value, type);
        Teacher teacher = teacherRepository.findAll().stream()
                .filter(t -> t.getId() != null && t.getId().equals(teacherId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Teacher not found with id: " + teacherId));

        boolean teachesCourse = teacher.getCourses().stream()
                .anyMatch(c -> c.getCourseId().equals(courseId));
        if (!teachesCourse) {
            throw new IllegalStateException(
                    "Teacher " + teacherId + " does not teach course " + courseId);
        }

        Enrollment enrollment = findActiveEnrollment(studentId, courseId);

        Mark mark = markRepository.findByEnrollment(enrollment.getId());
        if (mark == null) {
            mark = new Mark(0, 0, 0);
            mark.setEnrollmentId(enrollment.getId());
        }
        applyScore(mark, type, value);
        markRepository.save(mark);
        enrollment.setMark(mark);
        enrollmentRepository.save(enrollment);
        System.out.printf("[MarkService] Assigned %s=%.2f for student %s in course %s%s%n",
                type, value, studentId, courseId,
                (comment != null && !comment.isEmpty() ? " [" + comment + "]" : ""));
        return mark;
    }


    public Mark updateMark(Long enrollmentId, MarkUpdateRequest request) {
        Mark mark = requireMark(enrollmentId);

        applyScore(mark, request.getType(), request.getNewValue());
        markRepository.save(mark);

        System.out.printf("[MarkService] Updated mark for enrollment #%d: %s=%.2f%s%n",
                enrollmentId, request.getType(), request.getNewValue(),
                (request.getComment() != null ? " [" + request.getComment() + "]" : ""));
        return mark;
    }

    

    public void deleteMark(Long enrollmentId) {
        requireMark(enrollmentId);
        markRepository.deleteByEnrollmentId(enrollmentId);
        enrollmentRepository.findById(enrollmentId).ifPresent(e -> {
            e.setMark(null);
            enrollmentRepository.save(e);
        });

        System.out.println("[MarkService] Deleted mark for enrollment #" + enrollmentId);
    }

    public List<Mark> getStudentMarks(String studentId, String courseId) {
        List<Mark> result = new ArrayList<>();
        for (Enrollment e : enrollmentRepository.findByStudent(studentId)) {
            if (e.getCourse() != null && e.getCourse().getCourseId().equals(courseId)) {
                Mark m = markRepository.findByEnrollment(e.getId());
                if (m != null) result.add(m);
            }
        }
        return result;
    }


    public List<Mark> getCourseMarks(String courseId) {
        List<Mark> result = new ArrayList<>();
        for (Mark m : markRepository.findAll()) {
            Enrollment e = enrollmentRepository.findById(m.getEnrollmentId()).orElse(null);
            if (e != null && e.getCourse() != null
                    && e.getCourse().getCourseId().equals(courseId)) {
                result.add(m);
            }
        }
        return result;
    }

    public double calculateFinalGrade(String studentId, String courseId) {
        List<Mark> marks = getStudentMarks(studentId, courseId);
        if (marks.isEmpty()) return 0.0;
        return marks.get(0).calculateTotal();
    }

    public MarkStatistics getMarkStatistics(String courseId) {
        List<Mark> marks = getCourseMarks(courseId);
        if (marks.isEmpty()) return null;

        double sumTotal = 0, highest = Double.MIN_VALUE, lowest = Double.MAX_VALUE;
        double sumFirst = 0, sumSecond = 0, sumFinal = 0;
        int pass = 0, fail = 0;

        for (Mark m : marks) {
            double total = m.calculateTotal();
            sumTotal  += total;
            sumFirst  += m.getFirstAttestation();
            sumSecond += m.getSecondAttestation();
            sumFinal  += m.getFinal();
            if (total > highest) highest = total;
            if (total < lowest)  lowest  = total;
            if (total >= 50) pass++; else fail++;
        }

        int n = marks.size();
        return new MarkStatistics(
                courseId, n,
                sumTotal  / n,
                highest,
                lowest,
                sumFirst  / n,
                sumSecond / n,
                sumFinal  / n,
                pass, fail);
    }

    private Enrollment findActiveEnrollment(String studentId, String courseId) {
        return enrollmentRepository.findByStudent(studentId).stream()
                .filter(e -> e.getCourse() != null
                          && e.getCourse().getCourseId().equals(courseId)
                          && e.getStatus() == EnrollmentStatus.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No active enrollment found for student " + studentId
                        + " in course " + courseId));
    }

    private Mark requireMark(Long enrollmentId) {
        Mark mark = markRepository.findByEnrollment(enrollmentId);
        if (mark == null) {
            throw new IllegalArgumentException(
                    "No mark found for enrollment #" + enrollmentId);
        }
        return mark;
    }

    private void validateScore(double value, MarkType type) {
        if ((type == MarkType.FIRST_ATTESTATION || type == MarkType.SECOND_ATTESTATION) && (value < 0.0 || value > 30.0)) {
            throw new IllegalArgumentException(
                    "Score must be between 0 and 30 for attestations, got: " + value);
        }
        if ((type == MarkType.FINAL_EXAM) && (value < 0.0 || value > 40.0)){
            throw new IllegalArgumentException(
                    "Score must be between 0 and 40 for final exam, got: " + value);
        }
    }

    private void applyScore(Mark mark, MarkType type, double value) {
        switch (type) {
            case FIRST_ATTESTATION:
                mark.setFirstAttestation(value);
                break;
            case SECOND_ATTESTATION:
                mark.setSecondAttestation(value);
                break;
            case FINAL_EXAM:
                mark.setFinal(value);
                break;
            default:
                throw new IllegalArgumentException("Unknown MarkType: " + type);
        }
    }
}
