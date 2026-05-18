import java.util.List;

public class FacultyReport {

    private final Long         facultyId;
    private final String       facultyName;
    private final ReportPeriod period;
    private final int          totalDepartments;
    private final int          totalTeachers;
    private final int          totalStudents;
    private final int          totalEnrollments;
    private final int          completedEnrollments;
    private final double       averageGpa;          // across all graded enrollments

    public FacultyReport(Long facultyId, String facultyName, ReportPeriod period,
                         int totalDepartments, int totalTeachers, int totalStudents,
                         int totalEnrollments, int completedEnrollments,
                         double averageGpa) {
        this.facultyId            = facultyId;
        this.facultyName          = facultyName;
        this.period               = period;
        this.totalDepartments     = totalDepartments;
        this.totalTeachers        = totalTeachers;
        this.totalStudents        = totalStudents;
        this.totalEnrollments     = totalEnrollments;
        this.completedEnrollments = completedEnrollments;
        this.averageGpa           = averageGpa;
    }

    public Long         getFacultyId()            { return facultyId; }
    public String       getFacultyName()          { return facultyName; }
    public ReportPeriod getPeriod()               { return period; }
    public int          getTotalDepartments()     { return totalDepartments; }
    public int          getTotalTeachers()        { return totalTeachers; }
    public int          getTotalStudents()        { return totalStudents; }
    public int          getTotalEnrollments()     { return totalEnrollments; }
    public int          getCompletedEnrollments() { return completedEnrollments; }
    public double       getAverageGpa()           { return averageGpa; }

    @Override
    public String toString() {
        return String.format(
            "FacultyReport{faculty='%s', period=%s, departments=%d, teachers=%d, " +
            "students=%d, enrollments=%d, completed=%d, avgGPA=%.2f}",
            facultyName, period.getLabel(), totalDepartments, totalTeachers,
            totalStudents, totalEnrollments, completedEnrollments, averageGpa);
    }
}