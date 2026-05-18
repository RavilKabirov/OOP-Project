
public class PaperSearchFilters {

    private PaperType      type;
    private ResearchStatus status;
    private String         keyword;       
    private Long           researcherId;  

    public PaperSearchFilters() {}

    public PaperSearchFilters withType(PaperType type) {
        this.type = type; return this;
    }

    public PaperSearchFilters withStatus(ResearchStatus status) {
        this.status = status; return this;
    }

    public PaperSearchFilters withKeyword(String keyword) {
        this.keyword = keyword; return this;
    }

    public PaperSearchFilters withResearcherId(Long researcherId) {
        this.researcherId = researcherId; return this;
    }


    public PaperType      getType()         { return type; }
    public ResearchStatus getStatus()       { return status; }
    public String         getKeyword()      { return keyword; }
    public Long           getResearcherId() { return researcherId; }

    @Override
    public String toString() {
        return "PaperSearchFilters{type=" + type + ", status=" + status
                + ", keyword='" + keyword + "', researcherId=" + researcherId + "}";
    }
}