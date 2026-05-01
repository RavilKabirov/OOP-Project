import java.util.*;

public class EnrollmentRepository {

    private List<Enrollment> enrollments;
    private long nextId = 1;

    public EnrollmentRepository() {
        this.enrollments = new ArrayList<>();
    }

    public Enrollment save(Enrollment enrollment) {
        if (enrollment.getId() == null) {
            enrollment.setId(nextId++);
            enrollments.add(enrollment);
        } else {
            for (int i = 0; i < enrollments.size(); i++) {
                if (enrollments.get(i).getId().equals(enrollment.getId())) {
                    enrollments.set(i, enrollment);
                    return enrollment;
                }
            }
            enrollments.add(enrollment); 
        }
        return enrollment;
    }

    public Optional<Enrollment> findById(Long id) {
        return enrollments.stream()
                .filter(e -> e.getId() != null && e.getId().equals(id))
                .findFirst();
    }

    public List<Enrollment> findByStudent(String studentId) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.getStudent() != null && e.getStudent().getStudentId().equals(studentId)) {
                result.add(e);
            }
        }
        return result;
    }

    public List<Enrollment> findByCourse(Long courseId) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.getCourse() != null && Objects.hashCode(e.getCourse()) == courseId.intValue()) {
                result.add(e);
            }
        }
        return result;
    }

    public Optional<Enrollment> findByStudentAndCourse(String studentId, Long courseId) {
        return enrollments.stream()
                .filter(e -> e.getStudent() != null && e.getCourse() != null
                        && e.getStudent().getStudentId().equals(studentId)
                        && Objects.hashCode(e.getCourse()) == courseId.intValue())
                .findFirst();
    }

    public Optional<Enrollment> findByStudentAndCourse(String studentId, Course course) {
        return enrollments.stream()
                .filter(e -> e.getStudent() != null && e.getCourse() != null
                        && e.getStudent().getStudentId().equals(studentId)
                        && e.getCourse() == course)
                .findFirst();
    }

    public List<Enrollment> findByStatus(EnrollmentStatus status) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (status.equals(e.getStatus())) {
                result.add(e);
            }
        }
        return result;
    }

    public List<Enrollment> findAll() {
        return new ArrayList<>(enrollments);
    }

    public void deleteById(Long id) {
        enrollments.removeIf(e -> e.getId() != null && e.getId().equals(id));
    }
}
