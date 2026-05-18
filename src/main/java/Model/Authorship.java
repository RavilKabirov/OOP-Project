/**
 * Represents the authorship relationship between a researcher and a paper.
 *
 * Stores the researcher's role (order of authorship, contribution description)
 * on a specific paper.
 */
public class Authorship {

    private Long   paperId;
    private Long   researcherId;   // User.getId() of the researcher
    private int    authorOrder;    // 1 = first author, 2 = second, etc.
    private String contribution;   // free-text description of role

    public Authorship() {}

    public Authorship(Long paperId, Long researcherId,
                      int authorOrder, String contribution) {
        this.paperId      = paperId;
        this.researcherId = researcherId;
        this.authorOrder  = authorOrder;
        this.contribution = contribution;
    }

    public Long   getPaperId()          { return paperId; }
    public void   setPaperId(Long v)    { this.paperId = v; }

    public Long   getResearcherId()      { return researcherId; }
    public void   setResearcherId(Long v){ this.researcherId = v; }

    public int    getAuthorOrder()       { return authorOrder; }
    public void   setAuthorOrder(int v)  { this.authorOrder = v; }

    public String getContribution()      { return contribution; }
    public void   setContribution(String v){ this.contribution = v; }

    @Override
    public String toString() {
        return "Authorship{paper=" + paperId + ", researcher=" + researcherId
                + ", order=" + authorOrder + ", contribution='" + contribution + "'}";
    }
}