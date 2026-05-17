
public class CourseCreationRequest {
	private String courseId;
	private String name;
	private int credits;
	private CourseType courseType;
	
	public CourseCreationRequest(String courseId, String name, int credits, CourseType courseType) {
		this.courseId = courseId;
		this.name = name;
		this.credits = credits;
		this.courseType = courseType;
	}
	
	public String getCourseId() { return courseId; }
    public String getName() { return name; }
    public int getCredits() { return credits; }
    public CourseType getCourseType() { return courseType; }
}
