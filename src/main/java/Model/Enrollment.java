import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

public class Enrollment {

    private Long id;
    private Student student;
    private Course course;
    private LocalDateTime enrollmentDate;
    private EnrollmentStatus status;
    private Mark mark;

    public Enrollment() {
        this.enrollmentDate=LocalDateTime.now(); 
    }
    
    
    public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public EnrollmentStatus getStatus() {
        return status;
    }
    public void setStatus(EnrollmentStatus value) {
        this.status=value;
    }
    public Mark getMark() {
        return mark;
    }
    public void setMark(Mark value) {
        this.mark=value;
    }
    public Student getStudent() {
        return student;
    }
    public void setStudent(Student value) {
        this.student=value;
    }
    public Course getCourse() {
        return course;
    }
    public void setCourse(Course value) {
        this.course=value;
    }


    @Override
    public String toString() {
        return "Enrollment{"+"id="+id+", student="+(student!=null ? student.toString():"null")+", course="+(course != null ? course.getName() : "null") +
                ", status=" + status +
                '}';
    }
}
