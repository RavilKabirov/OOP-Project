import java.util.List;

public class CoursePerformanceReport {

    private final String courseId;
    private final String courseName;
    private final int    credits;
    private final int    totalEnrollments;
    private final int    completedEnrollments;
    private final int    activeEnrollments;
    private final int    droppedEnrollments;
    private final double averageTotal;    
    private final double highestTotal;
    private final double lowestTotal;
    private final int    passCount;      
    private final int    failCount;        

    public CoursePerformanceReport(String courseId, String courseName, int credits,
                                   int totalEnrollments, int completedEnrollments,
                                   int activeEnrollments, int droppedEnrollments,
                                   double averageTotal, double highestTotal,
                                   double lowestTotal, int passCount, int failCount) {
        this.courseId             = courseId;
        this.courseName           = courseName;
        this.credits              = credits;
        this.totalEnrollments     = totalEnrollments;
        this.completedEnrollments = completedEnrollments;
        this.activeEnrollments    = activeEnrollments;
        this.droppedEnrollments   = droppedEnrollments;
        this.averageTotal         = averageTotal;
        this.highestTotal         = highestTotal;
        this.lowestTotal          = lowestTotal;
        this.passCount            = passCount;
        this.failCount            = failCount;
    }

    public String getCourseId()             { return courseId; }
    public String getCourseName()           { return courseName; }
    public int    getCredits()              { return credits; }
    public int    getTotalEnrollments()     { return totalEnrollments; }
    public int    getCompletedEnrollments() { return completedEnrollments; }
    public int    getActiveEnrollments()    { return activeEnrollments; }
    public int    getDroppedEnrollments()   { return droppedEnrollments; }
    public double getAverageTotal()         { return averageTotal; }
    public double getHighestTotal()         { return highestTotal; }
    public double getLowestTotal()          { return lowestTotal; }
    public int    getPassCount()            { return passCount; }
    public int    getFailCount()            { return failCount; }

    @Override
    public String toString() {
        return String.format(
            "CoursePerformanceReport{course='%s' (%s), enrollments=%d, " +
            "completed=%d, active=%d, dropped=%d, " +
            "avg=%.2f, high=%.2f, low=%.2f, pass=%d, fail=%d}",
            courseName, courseId, totalEnrollments,
            completedEnrollments, activeEnrollments, droppedEnrollments,
            averageTotal, highestTotal, lowestTotal, passCount, failCount);
    }
}