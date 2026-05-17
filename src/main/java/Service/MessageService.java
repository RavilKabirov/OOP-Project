import java.util.List;
import java.util.Optional;


public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository    userRepository;
    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public Message sendMessage(Long senderId, Long recipientId, String subject, String body) {
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Message subject must not be blank.");
        }
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Message body must not be blank.");
        }
        if (senderId.equals(recipientId)) {
            throw new IllegalStateException("An employee cannot message themselves.");
        }

        Employee sender = requireEmployee(senderId,    "Sender");
        Employee recipient = requireEmployee(recipientId, "Recipient");
        Message message = new Message(senderId, recipientId, subject, body);
        messageRepository.save(message);
        System.out.printf("[MessageService] Message #%d: %s → %s | \"%s\"%n",
                message.getId(), sender.getFullName(),
                recipient.getFullName(), subject);
        return message;
    }
    public List<Message> getInbox(Long employeeId) {
        requireEmployee(employeeId, "Employee");
        return messageRepository.findInboxByRecipient(employeeId);
    }
    public List<Message> getSentMessages(Long employeeId) {
        requireEmployee(employeeId, "Employee");
        return messageRepository.findSentBySender(employeeId);
    }
    
    public Optional<Message> getMessageById(Long messageId) {
        return messageRepository.findById(messageId);
    }
    
    public void markAsRead(Long messageId, Long employeeId) {
        requireEmployee(employeeId, "Employee");
        Message message = requireMessage(messageId);
        if (!message.getRecipientId().equals(employeeId)) {
            throw new IllegalArgumentException(
                    "Employee " + employeeId + " is not the recipient of message #" + messageId);
        }
        message.setRead(true);
        messageRepository.save(message);
        System.out.println("[MessageService] Message #" + messageId + " marked as read.");
    }
    public void deleteMessage(Long messageId, Long employeeId) {
        requireEmployee(employeeId, "Employee");
        Message message = requireMessage(messageId);

        boolean isSender    = message.getSenderId().equals(employeeId);
        boolean isRecipient = message.getRecipientId().equals(employeeId);

        if (!isSender && !isRecipient) {
            throw new IllegalArgumentException(
                    "Employee " + employeeId + " has no access to message #" + messageId);
        }
        if (isSender)    message.setDeletedBySender(true);
        if (isRecipient) message.setDeletedByRecipient(true);

        messageRepository.save(message);
        System.out.println("[MessageService] Message #" + messageId
                + " deleted for employee " + employeeId + ".");
    }

    public List<Message> getConversation(Long employeeId1, Long employeeId2) {
        requireEmployee(employeeId1, "Employee 1");
        requireEmployee(employeeId2, "Employee 2");
        return messageRepository.findConversation(employeeId1, employeeId2);
    }

    private Employee requireEmployee(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        role + " not found with id: " + userId));
        if (!(user instanceof Employee)) {
            throw new IllegalArgumentException(
                    role + " with id " + userId + " is not an Employee "
                    + "(type: " + user.getClass().getSimpleName() + "). "
                    + "Only employees can send and receive messages.");
        }
        return (Employee) user;
    }

    private Message requireMessage(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Message not found with id: " + messageId));
    }
}