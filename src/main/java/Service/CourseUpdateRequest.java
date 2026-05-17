public class CourseUpdateRequest {
    private String name;
    private Integer credits;
    private CourseType courseType;

    public CourseUpdateRequest(String name, Integer credits, CourseType courseType) {
        this.name = name;
        this.credits = credits;
        this.courseType = courseType;
    }

    public String getName() { return name; }
    public Integer getCredits() { return credits; }
    public CourseType getCourseType() { return courseType; }
}