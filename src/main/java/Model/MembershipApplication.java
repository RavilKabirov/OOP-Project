import java.util.*;
import java.time.*;

public class MembershipApplication implements java.io.Serializable{
    private Long id;
    private Student student;
    private Organization organization;
    private String body;
    private ApplicationStatus applicationStatus;
    private LocalDateTime submittedAt;

    public MembershipApplication(){}

    public void reject(){
        applicationStatus=ApplicationStatus.REJECTED;
    }
    public void approve(){
        applicationStatus=ApplicationStatus.APPROVED;
    }
    public ApplicationStatus getApplicationStatus(){
        return applicationStatus;
    }
    public void setApplicationStatus(ApplicationStatus a){
        applicationStatus=a;
    }

    public LocalDateTime getSubmittedAt(){
        return submittedAt;
    }
    public void setSubmittedAt(LocalDateTime a){
        submittedAt=a;
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String toString(){
        return student+" "+applicationStatus;
    }
}
