import java.time.LocalDateTime;

public class TechSupportRequest implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Employee requester;
    private String title;
    private String description;
    private TechSupportRequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long handledById;

    public TechSupportRequest() {
        this.status = TechSupportRequestStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Employee getRequester() { return requester; }
    public void setRequester(Employee requester) { this.requester = requester; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TechSupportRequestStatus getStatus() { return status; }
    public void setStatus(TechSupportRequestStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getHandledById() { return handledById; }
    public void setHandledById(Long handledById) { this.handledById = handledById; }

    @Override
    public String toString() {
        return "TechSupportRequest{id=" + id
                + ", title='" + title + '\''
                + ", status=" + status
                + ", requester=" + (requester != null ? requester.getFullName() : "?")
                + '}';
    }
}
