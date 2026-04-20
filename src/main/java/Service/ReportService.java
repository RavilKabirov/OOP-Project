
import java.io.*;
import java.util.*;

/**
 * 
 */
public class ReportService {

    /**
     * Default constructor
     */
    public ReportService() {
    }

    /**
     * 
     */
    private EnrollmentRepository enrollmentRepository;

    /**
     * 
     */
    private MarkRepository markRepository;

    /**
     * 
     */
    private TeacherRepository teacherRepository;

    /**
     * 
     */
    private StudentRepository studentRepository;

    /**
     * 
     */
    private CourseRepository courseRepository;

    /**
     * 
     */
    private DepartmentRepository departmentRepository;

    /**
     * @param facultyId 
     * @param period 
     * @return
     */
    public FacultyReport generateFacultyReport(Long facultyId, ReportPeriod period) {
        // TODO implement here
        return null;
    }

    /**
     * @param deptId 
     * @param period 
     * @return
     */
    public DepartmentReport generateDepartmentReport(Long deptId, ReportPeriod period) {
        // TODO implement here
        return null;
    }

    /**
     * @param teacherId 
     * @param semester 
     * @return
     */
    public TeacherWorkloadReport generateTeacherWorkloadReport(Long teacherId, Semester semester) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @return
     */
    public StudentProgressReport generateStudentProgressReport(Long studentId) {
        // TODO implement here
        return null;
    }

    /**
     * @param courseId 
     * @return
     */
    public CoursePerformanceReport generateCoursePerformanceReport(Long courseId) {
        // TODO implement here
        return null;
    }

    /**
     * @param period 
     * @return
     */
    public EnrollmentStatistics generateEnrollmentStatistics(ReportPeriod period) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public HRReport generateHRReport() {
        // TODO implement here
        return null;
    }

    /**
     * @param report 
     * @param format 
     * @return
     */
    public File exportReport(Report report, ExportFormat format) {
        // TODO implement here
        return null;
    }

}