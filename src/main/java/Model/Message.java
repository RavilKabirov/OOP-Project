
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Message {

    /**
     * Default constructor
     */
    public Message() {
    }

    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private Employee sender;

    /**
     * 
     */
    private Employee resipient;

    /**
     * 
     */
    private String subject;

    /**
     * 
     */
    private String body;

    /**
     * 
     */
    private LocalDateTime sentAt;

    /**
     * 
     */
    private Boolean isRead;

    /**
     * 
     */
    private Boolean isDeletedBySender;

    /**
     * 
     */
    private Boolean isDeletedByRecipient;



    /**
     * @return
     */
    public Long getId() {
        // TODO implement here
        return null;
    }

    /**
     * @param value
     */
    public void setId(Long value) {
        // TODO implement here
    }

    /**
     * @return
     */
    public Employee getSender() {
        // TODO implement here
        return null;
    }

    /**
     * @param value
     */
    public void setSender(Employee value) {
        // TODO implement here
    }

    /**
     * @return
     */
    public Employee getResipient() {
        // TODO implement here
        return null;
    }

    /**
     * @param value
     */
    public void setResipient(Employee value) {
        // TODO implement here
    }

    /**
     * @return
     */
    public String getSubject() {
        // TODO implement here
        return "";
    }

    /**
     * @param value
     */
    public void setSubject(String value) {
        // TODO implement here
    }

    /**
     * @return
     */
    public String getBody() {
        // TODO implement here
        return "";
    }

    /**
     * @param value
     */
    public void setBody(String value) {
        // TODO implement here
    }

    /**
     * @return
     */
    public LocalDateTime getSentAt() {
        // TODO implement here
        return null;
    }

    /**
     * @param value
     */
    public void setSentAt(LocalDateTime value) {
        // TODO implement here
    }

    /**
     * @return
     */
    public Boolean getIsRead() {
        // TODO implement here
        return null;
    }

    /**
     * @param value
     */
    public void setIsRead(Boolean value) {
        // TODO implement here
    }

    /**
     * 
     */
    public void markAsRead() {
        // TODO implement here
    }

    /**
     * 
     */
    public void deleteForSender() {
        // TODO implement here
    }

    /**
     * 
     */
    public void deleteForRecipient() {
        // TODO implement here
    }

    /**
     * 
     */
    public void isVisibleTo() {
        // TODO implement here
    }

}