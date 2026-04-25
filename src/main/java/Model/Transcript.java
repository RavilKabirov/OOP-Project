import java.io.*;
import java.util.*;

public class Transcript implements Serializable{

    private Student student;
    private List<Enrollment>allEnrollments;

    public Transcript() {
        this.allEnrollments=new ArrayList<>();
    }

    public Transcript(Student student) {
        this.student=student;
        this.allEnrollments=new ArrayList<>();
    }

    public double calculateGPA() {
        if (allEnrollments==null||allEnrollments.isEmpty()) {
            return 0;
        }
        double total=0;
        int count=0;

        for (Enrollment enrollment:allEnrollments) {
            if (enrollment.getMark()!=null) {
                total+=enrollment.getMark().calculateTotal();
                count++;
            }
        }
        if (count==0) {
            return 0;
        }
        return total / count;
    }
    public List<Enrollment> getEnrollments() {
        return allEnrollments;
    }

    public void addEnrollment(Enrollment enrollment) {
        if (enrollment!=null) {
            allEnrollments.add(enrollment);
        }
    }
    public void generateReport() {
        System.out.println("Student:"+student);
        System.out.println("GPA:"+calculateGPA());

        for (Enrollment enrollment:allEnrollments) {
            System.out.println(enrollment);
        }
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student=student;
    }

    public String toString() {
        return "Transcript{"+"student="+student+", GPA="+calculateGPA() +'}';
    }
}
