
public class EnrollmentStatistics {

    private final ReportPeriod period;
    private final int          totalEnrollments;
    private final int          activeEnrollments;
    private final int          completedEnrollments;
    private final int          droppedEnrollments;
    private final int          pendingEnrollments;
    private final int          uniqueStudents;
    private final int          uniqueCourses;

    public EnrollmentStatistics(ReportPeriod period,
                                int totalEnrollments, int activeEnrollments,
                                int completedEnrollments, int droppedEnrollments,
                                int pendingEnrollments, int uniqueStudents,
                                int uniqueCourses) {
        this.period               = period;
        this.totalEnrollments     = totalEnrollments;
        this.activeEnrollments    = activeEnrollments;
        this.completedEnrollments = completedEnrollments;
        this.droppedEnrollments   = droppedEnrollments;
        this.pendingEnrollments   = pendingEnrollments;
        this.uniqueStudents       = uniqueStudents;
        this.uniqueCourses        = uniqueCourses;
    }

    public ReportPeriod getPeriod()               { return period; }
    public int getTotalEnrollments()              { return totalEnrollments; }
    public int getActiveEnrollments()             { return activeEnrollments; }
    public int getCompletedEnrollments()          { return completedEnrollments; }
    public int getDroppedEnrollments()            { return droppedEnrollments; }
    public int getPendingEnrollments()            { return pendingEnrollments; }
    public int getUniqueStudents()                { return uniqueStudents; }
    public int getUniqueCourses()                 { return uniqueCourses; }

    @Override
    public String toString() {
        return String.format(
            "EnrollmentStatistics{period=%s, total=%d, active=%d, " +
            "completed=%d, dropped=%d, pending=%d, students=%d, courses=%d}",
            period.getLabel(), totalEnrollments, activeEnrollments,
            completedEnrollments, droppedEnrollments, pendingEnrollments,
            uniqueStudents, uniqueCourses);
    }
}