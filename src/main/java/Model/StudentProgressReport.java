import java.util.List;

public class StudentProgressReport {

    private final String       studentId;
    private final String       studentName;
    private final int          yearOfStudy;
    private final double       gpa;
    private final int          totalCreditsEarned;
    private final int          completedCourses;
    private final int          activeCourses;
    private final int          droppedCourses;
    private final List<String> completedCourseNames;

    public StudentProgressReport(String studentId, String studentName,
                                 int yearOfStudy, double gpa,
                                 int totalCreditsEarned, int completedCourses,
                                 int activeCourses, int droppedCourses,
                                 List<String> completedCourseNames) {
        this.studentId           = studentId;
        this.studentName         = studentName;
        this.yearOfStudy         = yearOfStudy;
        this.gpa                 = gpa;
        this.totalCreditsEarned  = totalCreditsEarned;
        this.completedCourses    = completedCourses;
        this.activeCourses       = activeCourses;
        this.droppedCourses      = droppedCourses;
        this.completedCourseNames = completedCourseNames;
    }

    public String       getStudentId()           { return studentId; }
    public String       getStudentName()         { return studentName; }
    public int          getYearOfStudy()         { return yearOfStudy; }
    public double       getGpa()                 { return gpa; }
    public int          getTotalCreditsEarned()  { return totalCreditsEarned; }
    public int          getCompletedCourses()    { return completedCourses; }
    public int          getActiveCourses()       { return activeCourses; }
    public int          getDroppedCourses()      { return droppedCourses; }
    public List<String> getCompletedCourseNames(){ return completedCourseNames; }

    @Override
    public String toString() {
        return String.format(
            "StudentProgressReport{student='%s' (%s), year=%d, GPA=%.2f, " +
            "credits=%d, completed=%d, active=%d, dropped=%d}",
            studentName, studentId, yearOfStudy, gpa,
            totalCreditsEarned, completedCourses, activeCourses, droppedCourses);
    }
}