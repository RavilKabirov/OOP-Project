
import java.io.*;
import java.util.*;

/**
 * 
 */
public class MessageService {

    /**
     * Default constructor
     */
    public MessageService() {
    }

    /**
     * 
     */
    private MessageRepository messageRepository;

    /**
     * 
     */
    private EmployeeRepository employeeRepository;

    /**
     * @param senderId 
     * @param recipientId 
     * @param subject 
     * @param body 
     * @return
     */
    public Message sendMessage(Long senderId, Long recipientId, String subject, String body) {
        // TODO implement here
        return null;
    }

    /**
     * @param employeeId 
     * @return
     */
    public List<Message> getInbox(Long employeeId) {
        // TODO implement here
        return null;
    }

    /**
     * @param employeeId 
     * @return
     */
    public List<Message> getSentMessages(Long employeeId) {
        // TODO implement here
        return null;
    }

    /**
     * @param messageId 
     * @return
     */
    public Optional<Message> getMessageById(Long messageId) {
        // TODO implement here
        return null;
    }

    /**
     * @param messageId 
     * @param employeeId 
     * @return
     */
    public void markAsRead(Long messageId, Long employeeId) {
        // TODO implement here
        return null;
    }

    /**
     * @param messageId 
     * @param employeeId 
     * @return
     */
    public void deleteMessage(Long messageId, Long employeeId) {
        // TODO implement here
        return null;
    }

    /**
     * @param employeeId1 
     * @param employeeId2 
     * @return
     */
    public List<Message> getConversation(Long employeeId1, Long employeeId2) {
        // TODO implement here
        return null;
    }

}