
import java.io.*;
import java.util.*;

/**
 * 
 */
public class DepartmentService {

    /**
     * Default constructor
     */
    public DepartmentService() {
    }

    /**
     * 
     */
    private DepartmentRepository departmentRepository;

    /**
     * 
     */
    private FacultyRepository facultyRepository;

    /**
     * 
     */
    private TeacherRepository teacherRepository;

    /**
     * @param data 
     * @return
     */
    public Department createDepartment(DepartmentCreationRequest data) {
        // TODO implement here
        return null;
    }

    /**
     * @param deptId 
     * @param data 
     * @return
     */
    public Department updateDepartment(Long deptId, DepartmentUpdateRequest data) {
        // TODO implement here
        return null;
    }

    /**
     * @param deptId 
     * @return
     */
    public void deleteDepartment(Long deptId) {
        // TODO implement here
        return null;
    }

    /**
     * @param deptId 
     * @return
     */
    public Optional<Department> getDepartmentById(Long deptId) {
        // TODO implement here
        return null;
    }

    /**
     * @param facultyId 
     * @return
     */
    public List<Department> getDepartmentsByFaculty(Long facultyId) {
        // TODO implement here
        return null;
    }

    /**
     * @param deptId 
     * @param teacherId 
     * @return
     */
    public void assignHead(Long deptId, Long teacherId) {
        // TODO implement here
        return null;
    }

    /**
     * @param deptId 
     * @return
     */
    public List<Teacher> getDepartmentTeachers(Long deptId) {
        // TODO implement here
        return null;
    }

    /**
     * @param nameKeyword 
     * @return
     */
    public List<Department> searchDepartments(String nameKeyword) {
        // TODO implement here
        return null;
    }

}