import java.util.ArrayList;
import java.util.List;

public class TranscriptService {

    private final EnrollmentRepository enrollmentRepository;
    private final MarkRepository       markRepository;
    private final StudentRepository    studentRepository;

    public TranscriptService(EnrollmentRepository enrollmentRepository,
                             MarkRepository markRepository,
                             StudentRepository studentRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.markRepository       = markRepository;
        this.studentRepository    = studentRepository;
    }

    public Transcript generateTranscript(String studentId) {
        Student student = requireStudent(studentId);

        Transcript transcript = new Transcript(student);

        List<Enrollment> allEnrollments =
                enrollmentRepository.findByStudent(student.getStudentId());

        for (Enrollment enrollment : allEnrollments) {

            if (enrollment.getMark() == null && enrollment.getId() != null) {
                Mark mark = markRepository.findByEnrollment(enrollment.getId());
                if (mark != null) {
                    enrollment.setMark(mark);
                }
            }
            transcript.addEnrollment(enrollment);
        }

        System.out.println("[TranscriptService] Generated transcript for "
                + student.getFullName()
                + " — " + allEnrollments.size() + " enrollment(s), GPA="
                + String.format("%.2f", transcript.calculateGPA()));

        return transcript;
    }

    public List<Enrollment> getCompletedCourses(String studentId) {
        Student student = requireStudent(studentId);
        List<Enrollment> completed = new ArrayList<>();
        for (Enrollment e : enrollmentRepository.findByStudent(student.getStudentId())) {
            if (e.getStatus() == EnrollmentStatus.COMPLETED) {
                completed.add(e);
            }
        }

        System.out.println("[TranscriptService] " + student.getFullName()
                + " has " + completed.size() + " completed course(s).");
        return completed;
    }

    
    public double calculateGPA(String studentId) {
        Transcript transcript = generateTranscript(studentId);
        double gpa = transcript.calculateGPA();
        System.out.printf("[TranscriptService] GPA for student '%s': %.2f%n",
                studentId, gpa);
        return gpa;
    }

    
    public int getTotalCreditsEarned(String studentId) {
        List<Enrollment> completed = getCompletedCourses(studentId);

        int total = 0;
        for (Enrollment e : completed) {
            if (e.getCourse() != null) {
                total += e.getCourse().getCredits();
            }
        }

        System.out.println("[TranscriptService] Total credits earned: " + total);
        return total;
    }

    private Student requireStudent(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found with studentId: " + studentId));
    }
}
