import java.util.*;
public class MessageRepository {

    private final List<Message> messages;
    private long nextId = 1;

    public MessageRepository() {
        this.messages = new ArrayList<>();
    }

    public Message save(Message message) {
        if (message.getId() == null) {
            message.setId(nextId++);
            messages.add(message);
        } else {
            for (int i = 0; i < messages.size(); i++) {
                if (messages.get(i).getId().equals(message.getId())) {
                    messages.set(i, message);
                    return message;
                }
            }
            messages.add(message);
        }
        return message;
    }

    public Optional<Message> findById(Long id) {
        return messages.stream()
                .filter(m -> m.getId() != null && m.getId().equals(id))
                .findFirst();
    }

    public List<Message> findInboxByRecipient(Long recipientId) {
        List<Message> result = new ArrayList<>();
        for (Message m : messages) {
            if (recipientId.equals(m.getRecipientId()) && !m.isDeletedByRecipient()) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Message> findSentBySender(Long senderId) {
        List<Message> result = new ArrayList<>();
        for (Message m : messages) {
            if (senderId.equals(m.getSenderId()) && !m.isDeletedBySender()) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Message> findConversation(Long viewerId, Long otherId) {
        List<Message> result = new ArrayList<>();
        for (Message m : messages) {
            boolean sentByViewer = viewerId.equals(m.getSenderId())
                    && otherId.equals(m.getRecipientId())
                    && !m.isDeletedBySender();
            boolean receivedByViewer = viewerId.equals(m.getRecipientId())
                    && otherId.equals(m.getSenderId())
                    && !m.isDeletedByRecipient();
            if (sentByViewer || receivedByViewer) {
                result.add(m);
            }
        }
        result.sort(Comparator.comparing(Message::getSentAt));
        return result;
    }

    public List<Message> findAll() {
        return new ArrayList<>(messages);
    }

    
}