import java.util.List;
import java.util.Scanner;

public class NewsFeedUI {

    private final ApplicationContext ctx;
    private final Scanner sc;

    public NewsFeedUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
    }

    public void run() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("News and Research");
            System.out.println("  1. News feed");
            System.out.println("  2. Research catalog");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 2);
            switch (choice) {
                case 1: showFeed(); break;
                case 2: showResearchCatalog(); break;
                case 0: running = false; break;
            }
        }
    }

    private void showFeed() {
        ConsoleUtils.printHeader("News Feed");
        System.out.println("  Published research:");
        List<ResearchPaper> papers = ctx.getResearchService().searchPapers(
                null, new PaperSearchFilters().withStatus(ResearchStatus.PUBLISHED));
        if (papers.isEmpty()) {
            System.out.println("    No published research yet.");
        } else {
            papers.forEach(p -> System.out.printf("    #%-3d %-35s citations=%d%n",
                    p.getId(), p.getTitle(), p.getCitationCount()));
        }

        System.out.println("\n  News:");
        List<News> news = ctx.getNewsService().getLatestNews(10);
        if (news.isEmpty()) {
            System.out.println("    No news yet.");
        } else {
            news.forEach(n -> System.out.printf("    #%-3d %-30s [%s]%n",
                    n.getId(), n.getTitle(), n.getPublishedAt().toLocalDate()));
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showResearchCatalog() {
        ConsoleUtils.printHeader("Research Catalog");
        String title = ConsoleUtils.readLine(sc, "  Title contains (optional): ");
        String keyword = ConsoleUtils.readLine(sc, "  Keyword (optional): ");
        ResearchStatus status = readOptionalResearchStatus();
        PaperType type = readOptionalPaperType();
        String researcherRaw = ConsoleUtils.readLine(sc, "  Researcher user ID (optional): ");

        PaperSearchFilters filters = new PaperSearchFilters();
        if (!keyword.isBlank()) filters.withKeyword(keyword);
        if (status != null) filters.withStatus(status);
        if (type != null) filters.withType(type);
        if (!researcherRaw.isBlank()) filters.withResearcherId(Long.parseLong(researcherRaw));

        List<ResearchPaper> papers = ctx.getResearchService().searchPapers(title, filters);
        if (papers.isEmpty()) {
            System.out.println("  No research papers found.");
        } else {
            papers.forEach(p -> System.out.printf("  #%-3d %-35s type=%s status=%s citations=%d%n",
                    p.getId(), p.getTitle(), p.getType(), p.getStatus(), p.getCitationCount()));
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private ResearchStatus readOptionalResearchStatus() {
        System.out.println("  Status: 0=ANY");
        ResearchStatus[] values = ResearchStatus.values();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = ConsoleUtils.readIntInRange(sc, "  Choose status: ", 0, values.length);
        return choice == 0 ? null : values[choice - 1];
    }

    private PaperType readOptionalPaperType() {
        System.out.println("  Type: 0=ANY");
        PaperType[] values = PaperType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = ConsoleUtils.readIntInRange(sc, "  Choose type: ", 0, values.length);
        return choice == 0 ? null : values[choice - 1];
    }
}
