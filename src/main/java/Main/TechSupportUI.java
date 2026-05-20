import java.util.List;
import java.util.Scanner;

public class TechSupportUI {

    private final ApplicationContext ctx;
    private final Scanner sc;
    private final NewsFeedUI newsFeedUI;
    private final SupportRequestUI supportRequestUI;

    public TechSupportUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
        this.newsFeedUI = new NewsFeedUI(ctx, sc);
        this.supportRequestUI = new SupportRequestUI(ctx, sc);
    }

    public void run() {
        boolean running = true;
        while (running) {
            TechSupportSpecialist specialist = AuthContext.asTechSupportSpecialist();
            ConsoleUtils.printHeader("Tech Support Portal - " + specialist.getFullName());
            System.out.println("  1. All requests");
            System.out.println("  2. Filter by status");
            System.out.println("  3. Update request status");
            System.out.println("  4. News and research");
            System.out.println("  5. My support requests");
            System.out.println("  6. Profile");
            System.out.println("  0. Log out");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 6);
            switch (choice) {
                case 1: showAllRequests(); break;
                case 2: filterByStatus(); break;
                case 3: updateStatus(specialist); break;
                case 4: newsFeedUI.run(); break;
                case 5: supportRequestUI.runForEmployee(specialist); break;
                case 6: showProfile(specialist); break;
                case 0: running = false; break;
            }
        }
    }

    private void showAllRequests() {
        ConsoleUtils.printHeader("All Tech Support Requests");
        printRequests(ctx.getTechSupportService().getAllRequests());
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void filterByStatus() {
        ConsoleUtils.printHeader("Requests By Status");
        TechSupportRequestStatus status = readStatus(true);
        printRequests(ctx.getTechSupportService().getRequestsByStatus(status));
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void updateStatus(TechSupportSpecialist specialist) {
        ConsoleUtils.printHeader("Update Request Status");
        try {
            Long requestId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Request ID: "));
            TechSupportRequestStatus status = readStatus(false);
            ctx.getTechSupportService().updateStatus(specialist.getId(), requestId, status);
            ConsoleUtils.printSuccess("Request #" + requestId + " updated.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private TechSupportRequestStatus readStatus(boolean includePending) {
        TechSupportRequestStatus[] values = includePending
                ? TechSupportRequestStatus.values()
                : new TechSupportRequestStatus[] {
                        TechSupportRequestStatus.ACCEPTED,
                        TechSupportRequestStatus.REJECTED,
                        TechSupportRequestStatus.DONE
                };
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = ConsoleUtils.readIntInRange(sc, "  Status: ", 1, values.length);
        return values[choice - 1];
    }

    private void printRequests(List<TechSupportRequest> requests) {
        if (requests.isEmpty()) {
            System.out.println("  No requests found.");
            return;
        }
        for (TechSupportRequest r : requests) {
            System.out.printf("  #%-3d %-30s by=%-22s status=%-8s handledBy=%s%n",
                    r.getId(),
                    r.getTitle(),
                    r.getRequester() != null ? r.getRequester().getFullName() : "?",
                    r.getStatus(),
                    r.getHandledById() != null ? r.getHandledById() : "-");
        }
    }

    private void showProfile(TechSupportSpecialist specialist) {
        ConsoleUtils.printHeader("Tech Support Profile");
        System.out.println("  Name:        " + specialist.getFullName());
        System.out.println("  Email:       " + specialist.getEmail());
        System.out.println("  Employee ID: " + specialist.getEmployeeId());
        System.out.println("  Area:        " + specialist.getSupportArea());
        ConsoleUtils.pressEnterToContinue(sc);
    }
}
