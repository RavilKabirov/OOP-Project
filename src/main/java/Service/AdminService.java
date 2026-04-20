
import java.io.*;
import java.util.*;

/**
 * 
 */
public class AdminService {

    /**
     * Default constructor
     */
    public AdminService() {
    }

    /**
     * 
     */
    private UserRepository userRepository;

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
    private FacultyRepository facultyRepository;

    /**
     * 
     */
    private NewsRepository newsRepository;

    /**
     * 
     */
    private LogRepository logRepository;

    /**
     * @param userData 
     * @return
     */
    public User createUser(UserCreationRequest userData) {
        // TODO implement here
        return null;
    }

    /**
     * @param userId 
     * @param userData 
     * @return
     */
    public User updateUser(Long userId, UserUpdateRequest userData) {
        // TODO implement here
        return null;
    }

    /**
     * @param userId 
     * @return
     */
    public void blockUser(Long userId) {
        // TODO implement here
        return null;
    }

    /**
     * @param userId 
     * @return
     */
    public void unblockUser(Long userId) {
        // TODO implement here
        return null;
    }

    /**
     * @param userId 
     * @return
     */
    public void deleteUser(Long userId) {
        // TODO implement here
        return null;
    }

    /**
     * @param userId 
     * @param newRole 
     * @return
     */
    public void changeUserRole(Long userId, UserRole newRole) {
        // TODO implement here
        return null;
    }

    /**
     * @param filters 
     * @return
     */
    public List<LogEntry> viewSystemLogs(LogFilters filters) {
        // TODO implement here
        return null;
    }

    /**
     * @param action 
     * @param courseData 
     * @return
     */
    public void manageCourseCatalog(CatalogAction action, CourseRequest courseData) {
        // TODO implement here
        return null;
    }

    /**
     * @param settings 
     * @return
     */
    public void configureSystemSettings(SystemSettings settings) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public SystemStats getSystemStatistics() {
        // TODO implement here
        return null;
    }

}