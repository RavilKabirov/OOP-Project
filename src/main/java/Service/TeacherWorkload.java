import java.util.*;

public class TeacherWorkload {
	private Teacher teacher;
	private Semester semester;
	private List<Course> courses;
	private int totalHours;
	public TeacherWorkload(Teacher teacher, Semester semester) {
		this.teacher = teacher;
		this.semester = semester;
		this.courses = new ArrayList<>();
		this.totalHours = 0;
	}
	
	public Teacher getTeacher() {return teacher;}
	public Semester getSemester() {return semester;}
	public List<Course> getCourses(){return courses;}
	public int getTotalHours() {return totalHours;}
	
	public void addCourse(Course course, int hours) {
		courses.add(course);
		totalHours+= hours;
	}
	
	@Override
	public String toString() {
		return "TeacherWorkload{teacher=" + teacher.getFullName() + 
				", semester=" + semester + 
				", courses=" + courses + 
				", totalHours=" + totalHours + "}";
	}
}