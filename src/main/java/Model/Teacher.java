
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Teacher extends Employee {
    private List<Course> courses;
    private TeacherPosition teacherPosition;

    public Teacher() {
    	this.courses=new ArrayList<>();
    }

    /**
     * @return
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * @param value
     */
    public void addCourse(Course value) {
        courses.add(value);
    }

    /**
     * @param value
     */
    public void removeCourse(Course value) {
        courses.remove(value);
    }

    /**
     * @return
     */
    public TeacherPosition getTeacherPosition() {
        return teacherPosition;
    }

    /**
     * @param value
     */
    public void setTeacherPosition(TeacherPosition value) {
        this.teacherPosition=value;
    }

}