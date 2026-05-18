import java.util.*;

public class ResearchPaperRepository {

    private final List<ResearchPaper> papers;
    private long nextId = 1;

    public ResearchPaperRepository() {
        this.papers = new ArrayList<>();
    }

    public ResearchPaper save(ResearchPaper paper) {
        if (paper.getId() == null) {
            paper.setId(nextId++);
            papers.add(paper);
        } else {
            for (int i = 0; i < papers.size(); i++) {
                if (papers.get(i).getId().equals(paper.getId())) {
                    papers.set(i, paper);
                    return paper;
                }
            }
            papers.add(paper);
        }
        return paper;
    }

    public Optional<ResearchPaper> findById(Long id) {
        return papers.stream()
                .filter(p -> p.getId() != null && p.getId().equals(id))
                .findFirst();
    }

    public List<ResearchPaper> findByTitleContaining(String keyword) {
        List<ResearchPaper> result = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (ResearchPaper p : papers) {
            if (p.getTitle() != null && p.getTitle().toLowerCase().contains(lower)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<ResearchPaper> findByStatus(ResearchStatus status) {
        List<ResearchPaper> result = new ArrayList<>();
        for (ResearchPaper p : papers) {
            if (status.equals(p.getStatus())) result.add(p);
        }
        return result;
    }

    public List<ResearchPaper> findByType(PaperType type) {
        List<ResearchPaper> result = new ArrayList<>();
        for (ResearchPaper p : papers) {
            if (type.equals(p.getType())) result.add(p);
        }
        return result;
    }

    public List<ResearchPaper> findByKeyword(String keyword) {
        List<ResearchPaper> result = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (ResearchPaper p : papers) {
            for (String kw : p.getKeywords()) {
                if (kw.toLowerCase().contains(lower)) {
                    result.add(p);
                    break;
                }
            }
        }
        return result;
    }

    public List<ResearchPaper> findAll() {
        return new ArrayList<>(papers);
    }

    public void deleteById(Long id) {
        papers.removeIf(p -> p.getId() != null && p.getId().equals(id));
    }
}