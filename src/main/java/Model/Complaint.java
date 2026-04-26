import java.util.*;
import java.time.*;

public class Complaint{
    private Long id;
    private Teacher sender;
    private ManagerForComplaint recipient;
    private Student targetStudent;
    private UrgencyLevel urgencyLevel;
    private ComplaintStatus complaintStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Complaint(){}

    public Long getId(){
        return id;
    }
    public void setId(Long a){
        id=a;
    }
    public Teacher getSender(){
        return sender;
    }
    public void setSender(Teacher a){
        sender=a;
    }
    public ManagerForComplaint getRecipient(){
        return recipient;
    }

    public void setRecipient(ManagerForComplaint a){
        recipient=a;
    }

    public Student getTargetStudent(){
        return targetStudent;
    }

    public void setTargetStudent(Student a){
        targetStudent=a;
    }

    public UrgencyLevel getUrgencyLevel(){
        return urgencyLevel;
    }

    public void setUrgencyLevel(UrgencyLevel a){
        urgencyLevel=a;
    }

    public ComplaintStatus getComplaintStatus(){
        return complaintStatus;
    }

    public void setComplaintStatus(ComplaintStatus a){
        complaintStatus=a;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime a){
        createdAt=a;
    }
    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime a){
        updatedAt=a;
    }
    public void updateStatus(ComplaintStatus a){
        complaintStatus=a;
        updatedAt=LocalDateTime.now();
    }
    public Boolean isResolved(){
        return complaintStatus==ComplaintStatus.RESOLVED;
    }

    public String toString(){
        return sender+" -> "+targetStudent;
    }
}
