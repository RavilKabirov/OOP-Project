
public class DepartmentReport {

    private final Long         departmentId;
    private final String       departmentName;
    private final ReportPeriod period;
    private final int          totalTeachers;
    private final int          totalCourses;
    private final int          totalEnrollments;
    private final int          completedEnrollments;
    private final double       averageGpa;

    public DepartmentReport(Long departmentId, String departmentName,
                            ReportPeriod period, int totalTeachers,
                            int totalCourses, int totalEnrollments,
                            int completedEnrollments, double averageGpa) {
        this.departmentId         = departmentId;
        this.departmentName       = departmentName;
        this.period               = period;
        this.totalTeachers        = totalTeachers;
        this.totalCourses         = totalCourses;
        this.totalEnrollments     = totalEnrollments;
        this.completedEnrollments = completedEnrollments;
        this.averageGpa           = averageGpa;
    }

    public Long         getDepartmentId()         { return departmentId; }
    public String       getDepartmentName()       { return departmentName; }
    public ReportPeriod getPeriod()               { return period; }
    public int          getTotalTeachers()        { return totalTeachers; }
    public int          getTotalCourses()         { return totalCourses; }
    public int          getTotalEnrollments()     { return totalEnrollments; }
    public int          getCompletedEnrollments() { return completedEnrollments; }
    public double       getAverageGpa()           { return averageGpa; }

    @Override
    public String toString() {
        return String.format(
            "DepartmentReport{dept='%s', period=%s, teachers=%d, courses=%d, " +
            "enrollments=%d, completed=%d, avgGPA=%.2f}",
            departmentName, period.getLabel(), totalTeachers, totalCourses,
            totalEnrollments, completedEnrollments, averageGpa);
    }
}