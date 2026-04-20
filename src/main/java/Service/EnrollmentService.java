
import java.io.*;
import java.util.*;

/**
 * 
 */
public class EnrollmentService {

    /**
     * Default constructor
     */
    public EnrollmentService() {
    }

    /**
     * 
     */
    private EnrollmentRepository enrollmentRepository;

    /**
     * 
     */
    private StudentRepository studentRepository;

    /**
     * 
     */
    private CourseRepository courseRepository;

    /**
     * @param studentId 
     * @param courseId 
     * @return
     */
    public Enrollment enrollStudent(Long studentId, Long courseId) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @param courseId 
     * @return
     */
    public void dropCourse(Long studentId, Long courseId) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @return
     */
    public List<Enrollment> getStudentEnrollments(Long studentId) {
        // TODO implement here
        return null;
    }

    /**
     * @param courseId 
     * @return
     */
    public List<Enrollment> getCourseRoster(Long courseId) {
        // TODO implement here
        return null;
    }

    /**
     * @param enrollmentId 
     * @param finalGrade 
     * @return
     */
    public void completeCourse(Long enrollmentId, double finalGrade) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @param courseId 
     * @return
     */
    public EnrollmentStatus getEnrollmentStatus(Long studentId, Long courseId) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @param courseId 
     * @return
     */
    public boolean isStudentEnrolled(Long studentId, Long courseId) {
        // TODO implement here
        return false;
    }

}