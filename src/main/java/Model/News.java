import java.time.LocalDate;
import java.time.LocalDateTime;

public class News {

    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private LocalDateTime publishedAt;
    private boolean isArchived;

    public News() {}

    public News(Long id, String title, String content, Long authorId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.publishedAt = LocalDateTime.now();
        this.isArchived = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }

    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean archived) { this.isArchived = archived; }

    @Override
    public String toString() {
        return "[" + id + "] " + title + " | " + publishedAt;
    }
}