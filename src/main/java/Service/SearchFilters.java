public class SearchFilters {

    private boolean activeOnly;

    public SearchFilters() {
    }

    public SearchFilters(boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    public boolean isActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(boolean activeOnly) {
        this.activeOnly = activeOnly;
    }
}