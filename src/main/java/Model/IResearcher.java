import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


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

    default int getHIndex() {
        List<Integer> citations = new ArrayList<>();
        for (ResearchPaper p : getPublications()) {
            citations.add(p.getCitationCount());
        }
        citations.sort(Collections.reverseOrder());

        int h = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }
        return h;
    }
}
