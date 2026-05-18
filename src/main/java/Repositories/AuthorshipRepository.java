import java.util.*;

public class AuthorshipRepository {

    private final List<Authorship> authorships;

    public AuthorshipRepository() {
        this.authorships = new ArrayList<>();
    }

    public Authorship save(Authorship authorship) {
        for (int i = 0; i < authorships.size(); i++) {
            Authorship a = authorships.get(i);
            if (a.getPaperId().equals(authorship.getPaperId())
                    && a.getResearcherId().equals(authorship.getResearcherId())) {
                authorships.set(i, authorship);
                return authorship;
            }
        }
        authorships.add(authorship);
        return authorship;
    }

    public List<Authorship> findByPaperId(Long paperId) {
        List<Authorship> result = new ArrayList<>();
        for (Authorship a : authorships) {
            if (a.getPaperId().equals(paperId)) result.add(a);
        }
        result.sort(Comparator.comparingInt(Authorship::getAuthorOrder));
        return result;
    }

    public List<Authorship> findByResearcherId(Long researcherId) {
        List<Authorship> result = new ArrayList<>();
        for (Authorship a : authorships) {
            if (a.getResearcherId().equals(researcherId)) result.add(a);
        }
        return result;
    }

    public Optional<Authorship> findByPaperAndResearcher(Long paperId, Long researcherId) {
        return authorships.stream()
                .filter(a -> a.getPaperId().equals(paperId)
                          && a.getResearcherId().equals(researcherId))
                .findFirst();
    }

    public void deleteByPaperAndResearcher(Long paperId, Long researcherId) {
        authorships.removeIf(a -> a.getPaperId().equals(paperId)
                                && a.getResearcherId().equals(researcherId));
    }

    public void deleteAllByPaperId(Long paperId) {
        authorships.removeIf(a -> a.getPaperId().equals(paperId));
    }

    public List<Authorship> findAll() {
        return new ArrayList<>(authorships);
    }
}