
import java.io.*;
import java.util.*;

/**
 * 
 */
public class MarkService {

    /**
     * Default constructor
     */
    public MarkService() {
    }

    /**
     * 
     */
    private MarkRepository markRepository;

    /**
     * 
     */
    private EnrollmentRepository enrollmentRepository;

    /**
     * 
     */
    private TeacherRepository teacherRepository;

    /**
     * @param teacherId 
     * @param studentId 
     * @param courseId 
     * @param value 
     * @param type 
     * @param comment 
     * @return
     */
    public Mark assignMark(Long teacherId, Long studentId, Long courseId, double value, MarkType type, String comment) {
        // TODO implement here
        return null;
    }

    /**
     * @param markId 
     * @param newValue 
     * @param comment 
     * @return
     */
    public Mark updateMark(Long markId, double newValue, String comment) {
        // TODO implement here
        return null;
    }

    /**
     * @param markId 
     * @return
     */
    public void deleteMark(Long markId) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @param courseId 
     * @return
     */
    public List<Mark> getStudentMarks(Long studentId, Long courseId) {
        // TODO implement here
        return null;
    }

    /**
     * @param courseId 
     * @return
     */
    public List<Mark> getCourseMarks(Long courseId) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @param courseId 
     * @return
     */
    public double calculateFinalGrade(Long studentId, Long courseId) {
        // TODO implement here
        return 0.0d;
    }

    /**
     * @param courseId 
     * @return
     */
    public MarkStatistics getMarkStatistics(Long courseId) {
        // TODO implement here
        return null;
    }

}