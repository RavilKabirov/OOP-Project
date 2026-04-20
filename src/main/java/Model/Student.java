
import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class Student extends User {

    /**
     * Default constructor
     */
    public Student() {
    }

    /**
     * 
     */
    private String studentId;

    /**
     * 
     */
    private Major major;

    /**
     * 
     */
    private int yearOfStudy;

    /**
     * 
     */
    private double gpa;

    /**
     * 
     */
    private int totalCredits;

    /**
     * 
     */
    private List<Enrollment> curEnrollments;

    /**
     * 
     */
    private Transcript transcript;






    /**
     * @return
     */
    public String getStudentId() {
        // TODO implement here
        return "";
    }

    /**
     * @param value
     */
    public void setStudentId(String value) {
        // TODO implement here
    }

    /**
     * @return
     */
    public Major getMajor() {
        // TODO implement here
        return null;
    }

    /**
     * @param value
     */
    public void setMajor(Major value) {
        // TODO implement here
    }

    /**
     * @return
     */
    public int getYearOfStudy() {
        // TODO implement here
        return 0;
    }

    /**
     * @param value
     */
    public void setYearOfStudy(int value) {
        // TODO implement here
    }

    /**
     * @return
     */
    public double getGpa() {
        // TODO implement here
        return 0.0d;
    }

    /**
     * @param value
     */
    public void setGpa(double value) {
        // TODO implement here
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
        // TODO implement here
        return 0;
    }

    /**
     * @param value
     */
    public void setTotalCredits(int value) {
        // TODO implement here
    }

}