import java.util.List;

public class TeacherWorkloadReport {

    private final Long         teacherId;
    private final String       teacherName;
    private final Semester     semester;
    private final int          totalCourses;       // courses assigned to teacher
    private final int          totalStudents;      // unique students across all courses
    private final int          totalEnrollments;   // enrollment records in the period
    private final List<String> courseNames;        // names of assigned courses

    public TeacherWorkloadReport(Long teacherId, String teacherName,
                                 Semester semester, int totalCourses,
                                 int totalStudents, int totalEnrollments,
                                 List<String> courseNames) {
        this.teacherId        = teacherId;
        this.teacherName      = teacherName;
        this.semester         = semester;
        this.totalCourses     = totalCourses;
        this.totalStudents    = totalStudents;
        this.totalEnrollments = totalEnrollments;
        this.courseNames      = courseNames;
    }

    public Long         getTeacherId()        { return teacherId; }
    public String       getTeacherName()      { return teacherName; }
    public Semester     getSemester()         { return semester; }
    public int          getTotalCourses()     { return totalCourses; }
    public int          getTotalStudents()    { return totalStudents; }
    public int          getTotalEnrollments() { return totalEnrollments; }
    public List<String> getCourseNames()      { return courseNames; }

    @Override
    public String toString() {
        return String.format(
            "TeacherWorkloadReport{teacher='%s', semester=%s, courses=%d, " +
            "students=%d, enrollments=%d, courseList=%s}",
            teacherName, semester, totalCourses,
            totalStudents, totalEnrollments, courseNames);
    }
}