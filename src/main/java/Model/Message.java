import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

public class Message{

    private Long id;
    private Employee sender;
    private Employee recipient;
    private String subject;
    private String body;
    private LocalDateTime sentAt;
    private Boolean isRead;
    private Boolean isDeletedBySender;
    private Boolean isDeletedByRecipient;

    public Message(){
        this.isRead=false;
        this.isDeletedBySender=false;
        this.isDeletedByRecipient=false;
        this.sentAt=LocalDateTime.now();
    }

    public Long getId(){
        return id;
    }
    public void setId(Long a){
        this.id=a;
    }
    public Employee getSender(){
        return sender;
    }
    public void setSender(Employee a){
        this.sender=a;
    }
    public Employee getRecipient(){
        return recipient;
    }
    public void setRecipient(Employee a){
        this.recipient=a;
    }

    public String getSubject(){
        return subject;
    }

    public void setSubject(String a){
        this.subject=a;
    }
    public String getBody(){
        return body;
    }
    public void setBody(String a){
        this.body=a;
    }
    public LocalDateTime getSentAt(){
        return sentAt;
    }

    public void setSentAt(LocalDateTime a){
        this.sentAt=a;
    }

    public Boolean getIsRead(){
        return isRead;
    }
    public void setIsRead(Boolean a){
        this.isRead=a;
    }
    public Boolean getIsDeletedBySender(){
        return isDeletedBySender;
    }

    public void setIsDeletedBySender(Boolean a){
        this.isDeletedBySender=a;
    }

    public Boolean getIsDeletedByRecipient(){
        return isDeletedByRecipient;
    }

    public void setIsDeletedByRecipient(Boolean a){
        this.isDeletedByRecipient=a;
    }

    public void markAsRead(){
        this.isRead=true;
    }

    public void deleteForSender(){
        this.isDeletedBySender=true;
    }

    public void deleteForRecipient(){
        this.isDeletedByRecipient=true;
    }

    public boolean isVisibleTo(Employee a){
        if(a==sender){
            return !isDeletedBySender;
        }
        if(a==recipient){
            return !isDeletedByRecipient;
        }
        return false;
    }

    public String toString(){
        return id+" "+subject;
    }
}
