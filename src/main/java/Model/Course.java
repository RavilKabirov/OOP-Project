import java.io.*;
import java.util.*;

public class Course{
    private String courseId;
    private String name;
    private int credits;
    private List<Teacher> teachers;
    private List<Enrollment> enrollments;
    private CourseType courseType;

    public Course() {
        this.teachers=new ArrayList<>();
        this.enrollments=new ArrayList<>();
    }

    public String getCourseId(){
        return courseId;
    }
    
    public CourseType getCourseType() {
		return courseType;
	}

	public void setCourseType(CourseType courseType) {
		this.courseType = courseType;
	}

	public void setCourseId(String value) {
        this.courseId=value;
    }
    public String getName(){
        return name;
    }
    public void setName(String value) {
        this.name=value;
    }
    public int getCredits() {
        return credits;
    }
    public void setCredits(int value) {
        this.credits = value;
    }
    public List<Teacher> getTeachers() {
        return teachers;
    }
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
    public void addTeacher(Teacher value) {
        if (value!=null) {
            teachers.add(value);
        }
    }
    public void removeTeacher(Teacher value) {
        teachers.remove(value);
    }
    public void addEnrollment(Enrollment value) {
        if (value != null) {
            enrollments.add(value);
        }
    }

    public void removeEnrollment(Enrollment value) {
        enrollments.remove(value);
    }

}
