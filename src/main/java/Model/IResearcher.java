import java.util.List;


public interface IResearcher {

    String getResearcherId();

    List<ResearchPaper> getPublications();

    void addPublication(ResearchPaper paper);

    void removePublication(Long paperId);

    default int getTotalCitations() {
        int total = 0;
        for (ResearchPaper p : getPublications()) {
            total += p.getCitationCount();
        }
        return total;
    }
}