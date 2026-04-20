
import java.io.*;
import java.util.*;

/**
 * 
 */
public class TeacherService {

    /**
     * Default constructor
     */
    public TeacherService() {
    }

    /**
     * 
     */
    private TeacherRepository teacherRepository;

    /**
     * 
     */
    private DepartmentRepository departmentRepository;

    /**
     * 
     */
    private CourseRepository courseRepository;

    /**
     * @param id 
     * @return
     */
    public Optional<Teacher> getTeacherById(Long id) {
        // TODO implement here
        return null;
    }

    /**
     * @param employeeId 
     * @return
     */
    public Optional<Teacher> getTeacherByEmployeeId(String employeeId) {
        // TODO implement here
        return null;
    }

    /**
     * @param teacherId 
     * @param profileData 
     * @return
     */
    public Teacher updateTeacherProfile(Long teacherId, TeacherProfileUpdateRequest profileData) {
        // TODO implement here
        return null;
    }

    /**
     * @param teacherId 
     * @return
     */
    public List<Course> getTeacherCourses(Long teacherId) {
        // TODO implement here
        return null;
    }

    /**
     * @param deptId 
     * @return
     */
    public List<Teacher> getTeachersByDepartment(Long deptId) {
        // TODO implement here
        return null;
    }

    /**
     * @param teacherId 
     * @param deptId 
     * @return
     */
    public void assignTeacherToDepartment(Long teacherId, Long deptId) {
        // TODO implement here
        return null;
    }

    /**
     * @param keyword 
     * @return
     */
    public List<Teacher> searchTeachers(String keyword) {
        // TODO implement here
        return null;
    }

}