import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Отдельный интерфейс для всех кто является researcher.
 * Сюда вынесено создание и просмотр research papers, чтобы не дублировать одно и тоже
 * в TeacherUI и StudentUI.
 */
public class ResearcherUI {

    private final ApplicationContext ctx;
    private final Scanner sc;

    public ResearcherUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
    }

    public void run() {
        boolean running = true;
        while (running) {
            User user = AuthContext.getCurrentUser();
            IResearcher researcher = AuthContext.asResearcher();
            ConsoleUtils.printHeader("Researcher Portal - " + user.getFullName());

            System.out.println("  1. My papers");
            System.out.println("  2. Add new research");
            System.out.println("  3. Submit paper for review");
            System.out.println("  4. Add co-author");
            System.out.println("  5. Search papers");
            System.out.println("  6. Paper authors");
            System.out.println("  7. h-index");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 7);
            switch (choice) {
                case 1: showMyPapers(user, researcher); break;
                case 2: addNewResearch(user); break;
                case 3: submitPaper(); break;
                case 4: addCoAuthor(); break;
                case 5: searchPapers(); break;
                case 6: showPaperAuthors(); break;
                case 7: showHIndex(researcher); break;
                case 0: running = false; break;
            }
        }
    }

    private void showMyPapers(User user, IResearcher researcher) {
        ConsoleUtils.printHeader("My Papers");
        List<ResearchPaper> papers = ctx.getResearchService().getResearcherPapers(user.getId());
        if (papers.isEmpty()) {
            System.out.println("  No publications yet.");
        } else {
            papers.forEach(this::printPaper);
        }
        System.out.printf("  Total citations: %d%n", researcher.getTotalCitations());
        System.out.printf("  h-index: %d%n", researcher.getHIndex());
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showHIndex(IResearcher researcher) {
        ConsoleUtils.printHeader("h-index");
        System.out.printf("  h-index: %d%n", researcher.getHIndex());
        System.out.printf("  Total citations: %d%n", researcher.getTotalCitations());
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void addNewResearch(User user) {
        ConsoleUtils.printHeader("Add New Research");
        try {
            String title = ConsoleUtils.readNonBlank(sc, "  Title: ");
            String text = ConsoleUtils.readLine(sc, "  Abstract / text: ");
            PaperType type = readPaperType();
            List<String> keywords = readKeywords();

            ResearchPaper paper = ctx.getResearchService().createPaper(
                    user.getId(),
                    new PaperCreationRequest(title, text, type, keywords));
            ConsoleUtils.printSuccess("Research paper #" + paper.getId() + " created as DRAFT.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void submitPaper() {
        ConsoleUtils.printHeader("Submit Paper For Review");
        try {
            Long paperId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Paper ID: "));
            ctx.getResearchService().submitPaperForReview(paperId);
            ConsoleUtils.printSuccess("Paper #" + paperId + " submitted for review.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void addCoAuthor() {
        ConsoleUtils.printHeader("Add Co-author");
        try {
            Long paperId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Paper ID: "));
            Long researcherId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Researcher user ID: "));
            int order = ConsoleUtils.readInt(sc, "  Author order: ");
            String contribution = ConsoleUtils.readNonBlank(sc, "  Contribution: ");
            ctx.getResearchService().addAuthor(paperId, researcherId, order, contribution);
            ConsoleUtils.printSuccess("Researcher " + researcherId + " added to paper #" + paperId + ".");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void searchPapers() {
        ConsoleUtils.printHeader("Search Papers");
        String query = ConsoleUtils.readLine(sc, "  Title contains (optional): ");
        String keyword = ConsoleUtils.readLine(sc, "  Keyword (optional): ");

        PaperSearchFilters filters = new PaperSearchFilters();
        if (!keyword.isBlank()) {
            filters.withKeyword(keyword);
        }

        List<ResearchPaper> papers = ctx.getResearchService().searchPapers(query, filters);
        if (papers.isEmpty()) {
            System.out.println("  No papers found.");
        } else {
            papers.forEach(this::printPaper);
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showPaperAuthors() {
        ConsoleUtils.printHeader("Paper Authors");
        try {
            Long paperId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Paper ID: "));
            List<Authorship> authors = ctx.getResearchService().getPaperAuthors(paperId);
            if (authors.isEmpty()) {
                System.out.println("  No authors found.");
            } else {
                authors.forEach(a -> System.out.printf(
                        "  researcher=%d  order=%d  contribution=%s%n",
                        a.getResearcherId(), a.getAuthorOrder(), a.getContribution()));
            }
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private PaperType readPaperType() {
        // enum читаем номером, так меньше шансов написать тип с ошибкой руками
        PaperType[] values = PaperType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = ConsoleUtils.readIntInRange(sc, "  Type: ", 1, values.length);
        return values[choice - 1];
    }

    private List<String> readKeywords() {
        String raw = ConsoleUtils.readLine(sc, "  Keywords, comma-separated (optional): ");
        if (raw.isBlank()) {
            return new ArrayList<>();
        }
        List<String> keywords = new ArrayList<>();
        for (String item : Arrays.asList(raw.split(","))) {
            String keyword = item.trim();
            if (!keyword.isBlank()) {
                keywords.add(keyword);
            }
        }
        return keywords;
    }

    private void printPaper(ResearchPaper paper) {
        System.out.printf("  #%-3d %-35s  %-15s  citations=%d%n",
                paper.getId(),
                paper.getTitle(),
                paper.getStatus(),
                paper.getCitationCount());
    }
}
