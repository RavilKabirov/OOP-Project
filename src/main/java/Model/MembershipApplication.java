import java.util.*;
import java.time.*;

public class MembershipApplication{
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

    public String toString(){
        return student+" "+applicationStatus;
    }
}
