
import java.io.*;
import java.util.*;

/**
 * 
 */
public class EnrollmentRepository {

    /**
     * Default constructor
     */
    public EnrollmentRepository() {
    }

    /**
     * 
     */
    private List<Enrollment> enrollments;

    /**
     * @param enrollment 
     * @return
     */
    public Enrollment save(Enrollment enrollment) {
        // TODO implement here
        return null;
    }

    /**
     * @param id 
     * @return
     */
    public Optional<Enrollment> findById(Long id) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @return
     */
    public List<Enrollment> findByStudent(String studentId) {
        // TODO implement here
        return null;
    }

    /**
     * @param courseId 
     * @return
     */
    public List<Enrollment> findByCourse(Long courseId) {
        // TODO implement here
        return null;
    }

    /**
     * @param sId 
     * @param cId 
     * @return
     */
    public Optional<Enrollment> findByStudentAndCourse(String sId, Long cId) {
        // TODO implement here
        return null;
    }

    /**
     * @param status 
     * @return
     */
    public List<Enrollment> findByStatus(EnrollmentStatus status) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Enrollment> findAll() {
        // TODO implement here
        return null;
    }

    /**
     * @param id 
     * @return
     */
    public void deleteById(Long id) {
        // TODO implement here
        return null;
    }

}