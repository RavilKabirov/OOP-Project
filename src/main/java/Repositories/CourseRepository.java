
import java.io.*;
import java.util.*;

/**
 * 
 */
public class CourseRepository {

    /**
     * Default constructor
     */
    public CourseRepository() {
    }

    /**
     * 
     */
    private List<Course> courses;

    /**
     * @param course 
     * @return
     */
    public Course save(Course course) {
        // TODO implement here
        return null;
    }

    /**
     * @param courseId 
     * @return
     */
    public Optional<Course> findByCourseId(String courseId) {
        // TODO implement here
        return null;
    }

    /**
     * @param teacherId 
     * @return
     */
    public List<Course> findByTeacher(String teacherId) {
        // TODO implement here
        return null;
    }

    /**
     * @param type 
     * @return
     */
    public List<Course> findByCourseType(CourseType type) {
        // TODO implement here
        return null;
    }

    /**
     * @param titleKeyword 
     * @return
     */
    public List<Course> searchCourses(String titleKeyword) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Course> findAll() {
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