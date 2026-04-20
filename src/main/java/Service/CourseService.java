
import java.io.*;
import java.util.*;

/**
 * 
 */
public class CourseService {

    /**
     * Default constructor
     */
    public CourseService() {
    }

    /**
     * 
     */
    private CourseRepository courseRepository;

    /**
     * 
     */
    private DepartmentRepository departmentRepository;

    /**
     * 
     */
    private TeacherRepository teacherRepository;

    /**
     * @param courseData 
     * @return
     */
    public Course createCourse(CourseCreationRequest courseData) {
        // TODO implement here
        return null;
    }

    /**
     * @param courseId 
     * @param courseData 
     * @return
     */
    public Course updateCourse(Long courseId, CourseUpdateRequest courseData) {
        // TODO implement here
        return null;
    }

    /**
     * @param courseId 
     * @return
     */
    public void deleteCourse(Long courseId) {
        // TODO implement here
        return null;
    }

    /**
     * @param courseId 
     * @return
     */
    public Course getCourseById(Long courseId) {
        // TODO implement here
        return null;
    }

    /**
     * @param filters 
     * @return
     */
    public List<Course> searchCourses(CourseSearchFilters filters) {
        // TODO implement here
        return null;
    }

    /**
     * @param courseId 
     * @param teacherId 
     * @return
     */
    public void assignInstructor(Long courseId, Long teacherId) {
        // TODO implement here
        return null;
    }

    /**
     * @param departmentId 
     * @return
     */
    public List<Course> getCoursesByDepartment(Long departmentId) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @return
     */
    public List<Course> getAvailableCoursesForStudent(Long studentId) {
        // TODO implement here
        return null;
    }

}