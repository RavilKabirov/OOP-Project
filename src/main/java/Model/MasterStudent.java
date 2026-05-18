import java.util.ArrayList;
import java.util.List;

public class MasterStudent extends Student implements IResearcher {

    private final List<ResearchPaper> publications = new ArrayList<>();
    private String thesisTopic;

    public MasterStudent() {}

    public String getThesisTopic()           { return thesisTopic; }
    public void   setThesisTopic(String v)   { this.thesisTopic = v; }


    @Override
    public String getResearcherId() {
        return getId() != null ? getId().toString() : getStudentId();
    }

    @Override
    public List<ResearchPaper> getPublications() {
        return publications;
    }

    @Override
    public void addPublication(ResearchPaper paper) {
        if (paper != null && !publications.contains(paper)) {
            publications.add(paper);
        }
    }

    @Override
    public void removePublication(Long paperId) {
        publications.removeIf(p -> p.getId() != null && p.getId().equals(paperId));
    }

    @Override
    public String toString() {
        return "MasterStudent{id=" + getId() + ", name=" + getFullName()
                + ", publications=" + publications.size() + "}";
    }
}