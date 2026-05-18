import java.util.ArrayList;
import java.util.List;


public class PHDStudent extends Student implements IResearcher {

    private final List<ResearchPaper> publications = new ArrayList<>();
    private String dissertationTopic;
    private Teacher supervisor;

    public PHDStudent() {}

    public String  getDissertationTopic()          { return dissertationTopic; }
    public void    setDissertationTopic(String v)  { this.dissertationTopic = v; }

    public Teacher getSupervisor()                 { return supervisor; }
    public void    setSupervisor(Teacher t)        { this.supervisor = t; }


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
        return "PhDStudent{id=" + getId() + ", name=" + getFullName()
                + ", topic='" + dissertationTopic
                + "', publications=" + publications.size() + "}";
    }
}