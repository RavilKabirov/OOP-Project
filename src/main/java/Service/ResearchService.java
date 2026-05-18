import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResearchService {

    private final ResearchPaperRepository researchPaperRepository;
    private final AuthorshipRepository    authorshipRepository;
    private final UserRepository          researcherRepository;   

    public ResearchService(ResearchPaperRepository researchPaperRepository,
                           AuthorshipRepository authorshipRepository,
                           UserRepository researcherRepository) {
        this.researchPaperRepository = researchPaperRepository;
        this.authorshipRepository    = authorshipRepository;
        this.researcherRepository    = researcherRepository;
    }


    public ResearchPaper createPaper(Long researcherId,
                                     PaperCreationRequest paperData) {
        IResearcher researcher = requireResearcher(researcherId);

        ResearchPaper paper = new ResearchPaper();
        paper.setTitle(paperData.getTitle());
        paper.setText(paperData.getText());
        paper.setType(paperData.getType());
        paper.setKeywords(paperData.getKeywords());
        paper.setStatus(ResearchStatus.DRAFT);
        paper.setUpdatedAt(LocalDateTime.now());

        researchPaperRepository.save(paper);

        Authorship authorship = new Authorship(
                paper.getId(), researcherId, 1, "Creator");
        authorshipRepository.save(authorship);
        researcher.addPublication(paper);

        System.out.printf("[ResearchService] Paper #%d '%s' created by researcher %d%n",
                paper.getId(), paper.getTitle(), researcherId);
        return paper;
    }

    public void addAuthor(Long paperId, Long researcherId,
                          int order, String contribution) {
        ResearchPaper paper     = requirePaper(paperId);
        IResearcher   researcher = requireResearcher(researcherId);

        requireEditable(paper);

        if (authorshipRepository.findByPaperAndResearcher(
                paperId, researcherId).isPresent()) {
            throw new IllegalArgumentException(
                    "Researcher " + researcherId
                    + " is already an author of paper #" + paperId);
        }

        Authorship authorship = new Authorship(
                paperId, researcherId, order, contribution);
        authorshipRepository.save(authorship);
        researcher.addPublication(paper);

        System.out.printf("[ResearchService] Researcher %d added as author #%d of paper #%d%n",
                researcherId, order, paperId);
    }

    public void removeAuthor(Long paperId, Long researcherId) {
        requirePaper(paperId);
        IResearcher researcher = requireResearcher(researcherId);

        authorshipRepository.findByPaperAndResearcher(paperId, researcherId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Researcher " + researcherId
                        + " is not an author of paper #" + paperId));

        List<Authorship> current = authorshipRepository.findByPaperId(paperId);
        if (current.size() <= 1) {
            throw new IllegalStateException(
                    "Cannot remove the only author from paper #" + paperId);
        }

        authorshipRepository.deleteByPaperAndResearcher(paperId, researcherId);
        researcher.removePublication(paperId);

        System.out.printf("[ResearchService] Researcher %d removed from paper #%d%n",
                researcherId, paperId);
    }

    public void submitPaperForReview(Long paperId) {
        ResearchPaper paper = requirePaper(paperId);

        if (paper.getStatus() != ResearchStatus.DRAFT
                && paper.getStatus() != ResearchStatus.REJECTED) {
            throw new IllegalStateException(
                    "Paper #" + paperId + " cannot be submitted from status "
                    + paper.getStatus() + ". Must be DRAFT or REJECTED.");
        }

        if (paper.getTitle() == null || paper.getTitle().isBlank()) {
            throw new IllegalStateException(
                    "Paper #" + paperId + " must have a title before submission.");
        }

        if (authorshipRepository.findByPaperId(paperId).isEmpty()) {
            throw new IllegalStateException(
                    "Paper #" + paperId + " must have at least one author.");
        }

        paper.setStatus(ResearchStatus.UNDER_REVIEW);
        paper.setUpdatedAt(LocalDateTime.now());
        researchPaperRepository.save(paper);

        System.out.println("[ResearchService] Paper #" + paperId + " submitted for review.");
    }

    public void publishPaper(Long paperId, String doi, String fileUrl) {
        ResearchPaper paper = requirePaper(paperId);

        if (paper.getStatus() != ResearchStatus.UNDER_REVIEW) {
            throw new IllegalStateException(
                    "Paper #" + paperId + " must be UNDER_REVIEW to publish "
                    + "(current: " + paper.getStatus() + ").");
        }
        if (doi == null || doi.isBlank()) {
            throw new IllegalArgumentException("DOI must not be blank.");
        }
        if (fileUrl == null || fileUrl.isBlank()) {
            throw new IllegalArgumentException("File URL must not be blank.");
        }

        paper.setStatus(ResearchStatus.PUBLISHED);
        paper.setDoi(doi);
        paper.setFileUrl(fileUrl);
        paper.setPublicationDate(LocalDateTime.now());
        paper.setUpdatedAt(LocalDateTime.now());
        researchPaperRepository.save(paper);

        System.out.printf("[ResearchService] Paper #%d '%s' published. DOI=%s%n",
                paperId, paper.getTitle(), doi);
    }

    public void rejectPaper(Long paperId, String reason) {
        ResearchPaper paper = requirePaper(paperId);

        if (paper.getStatus() != ResearchStatus.UNDER_REVIEW) {
            throw new IllegalStateException(
                    "Paper #" + paperId + " must be UNDER_REVIEW to reject "
                    + "(current: " + paper.getStatus() + ").");
        }

        paper.setStatus(ResearchStatus.REJECTED);
        paper.setUpdatedAt(LocalDateTime.now());
        researchPaperRepository.save(paper);

        System.out.printf("[ResearchService] Paper #%d REJECTED. Reason: %s%n",
                paperId, reason);
    }

    public void citePaper(Long paperId) {
        ResearchPaper paper = requirePaper(paperId);

        if (paper.getStatus() != ResearchStatus.PUBLISHED) {
            throw new IllegalStateException(
                    "Only PUBLISHED papers can be cited (paper #"
                    + paperId + " is " + paper.getStatus() + ").");
        }

        paper.incrementCitations();
        researchPaperRepository.save(paper);

        System.out.println("[ResearchService] Paper #" + paperId
                + " cited. Total citations: " + paper.getCitationCount());
    }

    public List<ResearchPaper> getResearcherPapers(Long researcherId) {
        requireResearcher(researcherId);

        List<ResearchPaper> result = new ArrayList<>();
        for (Authorship a : authorshipRepository.findByResearcherId(researcherId)) {
            researchPaperRepository.findById(a.getPaperId())
                    .ifPresent(result::add);
        }
        return result;
    }

    public List<ResearchPaper> searchPapers(String query,
                                            PaperSearchFilters filters) {
        List<ResearchPaper> candidates;
        if (query != null && !query.isBlank()) {
            candidates = researchPaperRepository.findByTitleContaining(query);
        } else {
            candidates = researchPaperRepository.findAll();
        }

        if (filters == null) return candidates;

        List<ResearchPaper> result = new ArrayList<>();
        for (ResearchPaper p : candidates) {

            if (filters.getType() != null
                    && !filters.getType().equals(p.getType())) continue;

            if (filters.getStatus() != null
                    && !filters.getStatus().equals(p.getStatus())) continue;

            if (filters.getKeyword() != null && !filters.getKeyword().isBlank()) {
                String kw = filters.getKeyword().toLowerCase();
                boolean found = false;
                for (String k : p.getKeywords()) {
                    if (k.toLowerCase().contains(kw)) { found = true; break; }
                }
                if (!found) continue;
            }

            if (filters.getResearcherId() != null) {
                boolean isAuthor = authorshipRepository
                        .findByPaperAndResearcher(p.getId(),
                                filters.getResearcherId())
                        .isPresent();
                if (!isAuthor) continue;
            }

            result.add(p);
        }
        return result;
    }

    public List<Authorship> getPaperAuthors(Long paperId) {
        requirePaper(paperId);
        return authorshipRepository.findByPaperId(paperId);
    }

    private IResearcher requireResearcher(Long userId) {
        User user = researcherRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found with id: " + userId));
        if (!(user instanceof IResearcher)) {
            throw new IllegalArgumentException(
                    "User " + userId + " (" + user.getClass().getSimpleName()
                    + ") is not a researcher. "
                    + "Only Teacher, MasterStudent, and PhDStudent may author papers.");
        }
        return (IResearcher) user;
    }

    private ResearchPaper requirePaper(Long paperId) {
        return researchPaperRepository.findById(paperId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "ResearchPaper not found with id: " + paperId));
    }

    private void requireEditable(ResearchPaper paper) {
        if (paper.getStatus() == ResearchStatus.UNDER_REVIEW
                || paper.getStatus() == ResearchStatus.PUBLISHED) {
            throw new IllegalStateException(
                    "Paper #" + paper.getId()
                    + " cannot be modified while " + paper.getStatus() + ".");
        }
    }
}