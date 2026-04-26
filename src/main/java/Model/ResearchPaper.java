import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

public class ResearchPaper{
    private Long id;
    private String title;
    private String text;
    private List<String> keywords;
    private LocalDateTime publicationDate;
    private PaperType type;
    public ResearchStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ResearchPaper(){
        this.keywords=new ArrayList<>();
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
    }

    public Long getId(){
        return id;
    }
    public void setId(Long a){
        this.id=a;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String a){
        this.title=a;
    }
    public String getText(){
        return text;
    }
    public void setText(String a){
        this.text=a;
    }
    public List<String> getKeywords(){
        return keywords;
    }

    public void setKeywords(List<String> a){
        this.keywords=a;
    }
    public LocalDateTime getPublicationDate(){
        return publicationDate;
    }
    public void setPublicationDate(LocalDateTime a){
        this.publicationDate=a;
    }
    public PaperType getType(){
        return type;
    }
    public void setType(PaperType a){
        this.type=a;
    }
    public ResearchStatus getStatus(){
        return status;
    }
    public void setStatus(ResearchStatus a){
        this.status=a;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime a){
        this.createdAt=a;
    }

    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime a){
        this.updatedAt=a;
    }

    public String toString(){
        return id+" "+title;
    }
}
