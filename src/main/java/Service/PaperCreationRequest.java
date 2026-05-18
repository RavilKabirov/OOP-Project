import java.util.ArrayList;
import java.util.List;

public class PaperCreationRequest {

    private final String       title;
    private final String       text;
    private final PaperType    type;
    private final List<String> keywords;

    public PaperCreationRequest(String title, String text,
                                PaperType type, List<String> keywords) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Paper title must not be blank.");
        }
        if (type == null) {
            throw new IllegalArgumentException("PaperType must not be null.");
        }
        this.title    = title;
        this.text     = text != null ? text : "";
        this.type     = type;
        this.keywords = keywords != null ? keywords : new ArrayList<>();
    }

    public String       getTitle()    { return title; }
    public String       getText()     { return text; }
    public PaperType    getType()     { return type; }
    public List<String> getKeywords() { return keywords; }

    @Override
    public String toString() {
        return "PaperCreationRequest{title='" + title + "', type=" + type + "}";
    }
}