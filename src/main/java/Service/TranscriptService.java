
import java.io.*;
import java.util.*;

/**
 * 
 */
public class TranscriptService {

    /**
     * Default constructor
     */
    public TranscriptService() {
    }

    /**
     * 
     */
    private EnrollmentRepository enrollmentRepository;

    /**
     * 
     */
    private MarkRepository markRepository;

    /**
     * 
     */
    private StudentRepository studentRepository;

    /**
     * @param studentId 
     * @return
     */
    public Transcript generateTranscript(Long studentId) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @return
     */
    public List<Enrollment> getCompletedCourses(Long studentId) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @return
     */
    public double calculateGPA(Long studentId) {
        // TODO implement here
        return 0.0d;
    }

    /**
     * @param studentId 
     * @return
     */
    public int getTotalCreditsEarned(Long studentId) {
        // TODO implement here
        return 0;
    }

}