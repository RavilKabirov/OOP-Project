
import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class Student extends User {
    private String studentId;
    private Major major;
    private int yearOfStudy;
    private double gpa;
    private int totalCredits;
    private List<Enrollment> curEnrollments;
    private Transcript transcript;

    public Student() {
    }

    /**
     * @return
     */
    public String getStudentId() {
        // TODO implement here
        return studentId;
    }

    /**
     * @param value
     */
    public void setStudentId(String value) {
        this.studentId=value;
    }

    /**
     * @return
     */
    public Major getMajor() {
        return major;
    }

    /**
     * @param value
     */
    public void setMajor(Major value) {
        this.major=value;
    }

    /**
     * @return
     */
    public int getYearOfStudy() {
        return yearOfStudy;
    }

    /**
     * @param value
     */
    public void setYearOfStudy(int value) {
        this.yearOfStudy=value;
    }

    /**
     * @return
     */
    public double getGpa() {
        return gpa;
    }

    /**
     * @param value
     */
    public void setGpa(double value) {
        this.gpa=value;
    }

    /**
     * @return
     */
    public Boolean enrollInCourse() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public int getTotalCredits() {
        return totalCredits;
    }

    /**
     * @param value
     */
    public void setTotalCredits(int value) {
        this.totalCredits=value;
    }

}