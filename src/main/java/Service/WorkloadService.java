
import java.io.*;
import java.util.*;

/**
 * 
 */
public class WorkloadService {

    /**
     * Default constructor
     */
    public WorkloadService() {
    }

    /**
     * 
     */
    private CourseRepository courseRepository;

    /**
     * 
     */
    private TeacherRepository teacherRepository;

    /**
     * 
     */
    private DepartmentRepository departmentRepository;

    /**
     * @param teacherId 
     * @param courseId 
     * @param hours 
     * @return
     */
    public void assignTeacherToCourse(Long teacherId, Long courseId, int hours) {
        // TODO implement here
        return null;
    }

    /**
     * @param teacherId 
     * @param courseId 
     * @return
     */
    public void removeTeacherFromCourse(Long teacherId, Long courseId) {
        // TODO implement here
        return null;
    }

    /**
     * @param deptId 
     * @return
     */
    public WorkloadReport getDepartmentWorkload(Long deptId) {
        // TODO implement here
        return null;
    }

    /**
     * @param facultyId 
     * @return
     */
    public WorkloadReport getFacultyWorkload(Long facultyId) {
        // TODO implement here
        return null;
    }

    /**
     * @param facultyId 
     * @param approvedBy 
     * @return
     */
    public void approveWorkload(Long facultyId, Long approvedBy) {
        // TODO implement here
        return null;
    }

    /**
     * @param teacherId 
     * @param semester 
     * @return
     */
    public TeacherWorkload getTeacherWorkload(Long teacherId, Semester semester) {
        // TODO implement here
        return null;
    }

}