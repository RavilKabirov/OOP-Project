import java.util.Map;

public class HRReport {

    private final int                      totalTeachers;
    private final int                      totalDepartments;
    private final Map<String, Integer>     teachersByPosition; 
    private final Map<String, Integer>     teachersByDepartment; 
    private final double                   avgCoursesPerTeacher;
    private final int                      teachersWithNoCourses;

    public HRReport(int totalTeachers, int totalDepartments,
                    Map<String, Integer> teachersByPosition,
                    Map<String, Integer> teachersByDepartment,
                    double avgCoursesPerTeacher,
                    int teachersWithNoCourses) {
        this.totalTeachers         = totalTeachers;
        this.totalDepartments      = totalDepartments;
        this.teachersByPosition    = teachersByPosition;
        this.teachersByDepartment  = teachersByDepartment;
        this.avgCoursesPerTeacher  = avgCoursesPerTeacher;
        this.teachersWithNoCourses = teachersWithNoCourses;
    }

    public int                  getTotalTeachers()         { return totalTeachers; }
    public int                  getTotalDepartments()      { return totalDepartments; }
    public Map<String,Integer>  getTeachersByPosition()    { return teachersByPosition; }
    public Map<String,Integer>  getTeachersByDepartment()  { return teachersByDepartment; }
    public double               getAvgCoursesPerTeacher()  { return avgCoursesPerTeacher; }
    public int                  getTeachersWithNoCourses() { return teachersWithNoCourses; }

    @Override
    public String toString() {
        return String.format(
            "HRReport{teachers=%d, departments=%d, avgCourses=%.2f, " +
            "noCoursesTeachers=%d, byPosition=%s, byDept=%s}",
            totalTeachers, totalDepartments, avgCoursesPerTeacher,
            teachersWithNoCourses, teachersByPosition, teachersByDepartment);
    }
}