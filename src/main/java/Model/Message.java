import java.time.LocalDateTime;
public class Message {

    private Long id;
    private Long senderId;      
    private Long recipientId;   
    private String subject;
    private String body;
    private LocalDateTime sentAt;
    private boolean isRead;
    private boolean deletedBySender;
    private boolean deletedByRecipient;

    public Message() {
        this.sentAt = LocalDateTime.now();
        this.isRead = false;
        this.deletedBySender = false;
        this.deletedByRecipient = false;
    }

    public Message(Long senderId, Long recipientId, String subject, String body) {
        this();
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.subject = subject;
        this.body = body;
    }


    public Long getId()  { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long v){ this.senderId = v; }

    public Long getRecipientId(){ return recipientId; }
    public void setRecipientId(Long v){ this.recipientId = v; }

    public String getSubject() { return subject; }
    public void setSubject(String v){ this.subject = v; }

    public String getBody(){ return body; }
    public void setBody(String v){ this.body = v; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime v){ this.sentAt = v; }

    public boolean isRead(){ return isRead; }
    public void  setRead(boolean v) { this.isRead = v; }

    public boolean isDeletedBySender() { return deletedBySender; }
    public void setDeletedBySender(boolean v) { this.deletedBySender = v; }

    public boolean isDeletedByRecipient() { return deletedByRecipient; }
    public void setDeletedByRecipient(boolean v) { this.deletedByRecipient = v; }

    @Override
    public String toString() {
        return "Message{id=" + id  + ", from=" + senderId  + ", to="   + recipientId  + ", subject='" + subject + "'"  + ", read=" + isRead  + ", sentAt=" + sentAt + "}";
    }
}