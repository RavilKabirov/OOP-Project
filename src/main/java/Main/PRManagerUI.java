import java.util.List;
import java.util.Scanner;

/**
 * Кабинет PR manager.
 * Главная работа тут с news и каналами, плюс обычные сообщения как у сотрудников.
 */
public class PRManagerUI {

    private final ApplicationContext ctx;
    private final Scanner sc;
    private final MessageCenterUI messageCenterUI;

    public PRManagerUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
        this.messageCenterUI = new MessageCenterUI(ctx, sc);
    }

    public void run() {
        boolean running = true;
        while (running) {
            PRManager manager = AuthContext.asPRManager();
            ConsoleUtils.printHeader("PR Manager Portal - " + manager.getFullName());

            System.out.println("  1. Latest news");
            System.out.println("  2. Publish news");
            System.out.println("  3. My news");
            System.out.println("  4. Edit news");
            System.out.println("  5. Archive news");
            System.out.println("  6. Managed channels");
            System.out.println("  7. Messages");
            System.out.println("  8. Profile");
            System.out.println("  0. Log out");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 8);
            switch (choice) {
                case 1: showLatestNews(); break;
                case 2: publishNews(manager); break;
                case 3: showMyNews(manager); break;
                case 4: editNews(manager); break;
                case 5: archiveNews(manager); break;
                case 6: showChannels(manager); break;
                case 7: messageCenterUI.runForEmployee(manager); break;
                case 8: showProfile(manager); break;
                case 0: running = false; break;
            }
        }
    }

    private void showLatestNews() {
        ConsoleUtils.printHeader("Latest News");
        List<News> news = ctx.getNewsService().getLatestNews(10);
        if (news.isEmpty()) {
            System.out.println("  No news published yet.");
        } else {
            news.forEach(this::printNews);
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void publishNews(PRManager manager) {
        ConsoleUtils.printHeader("Publish News");
        try {
            String title = ConsoleUtils.readNonBlank(sc, "  Title: ");
            String content = ConsoleUtils.readNonBlank(sc, "  Content: ");
            News news = ctx.getNewsService().publishNews(manager.getId(), title, content);
            ConsoleUtils.printSuccess("News #" + news.getId() + " published.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showMyNews(PRManager manager) {
        ConsoleUtils.printHeader("My News");
        List<News> news = ctx.getNewsService().getNewsByAuthor(manager.getId());
        if (news.isEmpty()) {
            System.out.println("  You have not published news yet.");
        } else {
            news.forEach(this::printNews);
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void editNews(PRManager manager) {
        ConsoleUtils.printHeader("Edit News");
        try {
            Long newsId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  News ID: "));
            String content = ConsoleUtils.readNonBlank(sc, "  New content: ");
            ctx.getNewsService().editNews(newsId, manager.getId(), content);
            ConsoleUtils.printSuccess("News #" + newsId + " updated.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void archiveNews(PRManager manager) {
        ConsoleUtils.printHeader("Archive News");
        try {
            Long newsId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  News ID: "));
            ctx.getNewsService().archiveNews(newsId, manager.getId());
            ConsoleUtils.printSuccess("News #" + newsId + " archived.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showChannels(PRManager manager) {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Managed Channels");
            if (manager.getManagedChannels().isEmpty()) {
                System.out.println("  No channels assigned.");
            } else {
                manager.getManagedChannels().forEach(ch -> System.out.println("  - " + ch));
            }
            System.out.println("\n  1. Add channel");
            System.out.println("  2. Remove channel");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 2);
            switch (choice) {
                case 1:
                    manager.addManagedChannel(ConsoleUtils.readNonBlank(sc, "  Channel: "));
                    ctx.getUserRepository().save(manager);
                    ConsoleUtils.printSuccess("Channel added.");
                    break;
                case 2:
                    manager.removeManagedChannel(ConsoleUtils.readNonBlank(sc, "  Channel: "));
                    ctx.getUserRepository().save(manager);
                    ConsoleUtils.printSuccess("Channel removed.");
                    break;
                case 0:
                    running = false;
                    break;
            }
        }
    }

    private void showProfile(PRManager manager) {
        ConsoleUtils.printHeader("PR Manager Profile");
        System.out.println("  Name:        " + manager.getFullName());
        System.out.println("  Email:       " + manager.getEmail());
        System.out.println("  Employee ID: " + manager.getEmployeeId());
        System.out.println("  Level:       " + manager.getLevel());
        System.out.println("  Channels:    " + manager.getManagedChannels().size());
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void printNews(News news) {
        System.out.printf("  #%-3d %-30s  author=%d  archived=%s%n", news.getId(), news.getTitle(), news.getAuthorId(), news.isArchived());
    }
}
