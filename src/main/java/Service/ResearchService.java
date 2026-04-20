
import java.io.*;
import java.util.*;

/**
 * 
 */
public class ResearchService {

    /**
     * Default constructor
     */
    public ResearchService() {
    }

    /**
     * 
     */
    private ResearchPaperRepository researchPaperRepository;

    /**
     * 
     */
    private AuthorshipRepository authorshipRepository;

    /**
     * 
     */
    private UserRepository researcherRepository;

    /**
     * @param researcherId 
     * @param paperData 
     * @return
     */
    public ResearchPaper createPaper(Long researcherId, PaperCreationRequest paperData) {
        // TODO implement here
        return null;
    }

    /**
     * @param paperId 
     * @param researcherId 
     * @param order 
     * @param contribution 
     * @return
     */
    public void addAuthor(Long paperId, Long researcherId, int order, String contribution) {
        // TODO implement here
        return null;
    }

    /**
     * @param paperId 
     * @param researcherId 
     * @return
     */
    public void removeAuthor(Long paperId, Long researcherId) {
        // TODO implement here
        return null;
    }

    /**
     * @param paperId 
     * @return
     */
    public void submitPaperForReview(Long paperId) {
        // TODO implement here
        return null;
    }

    /**
     * @param paperId 
     * @param doi 
     * @param fileUrl 
     * @return
     */
    public void publishPaper(Long paperId, String doi, String fileUrl) {
        // TODO implement here
        return null;
    }

    /**
     * @param researcherId 
     * @return
     */
    public List<ResearchPaper> getResearcherPapers(Long researcherId) {
        // TODO implement here
        return null;
    }

    /**
     * @param query 
     * @param filters 
     * @return
     */
    public List<ResearchPaper> searchPapers(String query, PaperSearchFilters filters) {
        // TODO implement here
        return null;
    }

}