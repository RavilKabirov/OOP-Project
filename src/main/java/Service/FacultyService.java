
import java.io.*;
import java.util.*;

/**
 * 
 */
public class FacultyService {

    /**
     * Default constructor
     */
    public FacultyService() {
    }

    /**
     * 
     */
    private FacultyRepository facultyRepository;

    /**
     * 
     */
    private DepartmentRepository departmentRepository;

    /**
     * 
     */
    private TeacherRepository teacherRepository;

    /**
     * @param data 
     * @return
     */
    public Faculty createFaculty(FacultyCreationRequest data) {
        // TODO implement here
        return null;
    }

    /**
     * @param facultyId 
     * @param data 
     * @return
     */
    public Faculty updateFaculty(Long facultyId, FacultyUpdateRequest data) {
        // TODO implement here
        return null;
    }

    /**
     * @param facultyId 
     * @return
     */
    public void deleteFaculty(Long facultyId) {
        // TODO implement here
        return null;
    }

    /**
     * @param facultyId 
     * @return
     */
    public Optional<Faculty> getFacultyById(Long facultyId) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Faculty> getAllFaculties() {
        // TODO implement here
        return null;
    }

    /**
     * @param facultyId 
     * @param teacherId 
     * @return
     */
    public void assignDean(Long facultyId, Long teacherId) {
        // TODO implement here
        return null;
    }

    /**
     * @param facultyId 
     * @return
     */
    public List<Department> getFacultyDepartments(Long facultyId) {
        // TODO implement here
        return null;
    }

    /**
     * @param nameKeyword 
     * @return
     */
    public List<Faculty> searchFaculties(String nameKeyword) {
        // TODO implement here
        return null;
    }

}