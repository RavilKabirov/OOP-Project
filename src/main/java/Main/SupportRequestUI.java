import java.util.List;
import java.util.Scanner;

/**
 * Общий экран для сотрудников, чтобы они могли кидать реквесты в тех поддержку.
 */
public class SupportRequestUI {

    private final ApplicationContext ctx;
    private final Scanner sc;

    public SupportRequestUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
    }

    public void runForEmployee(Employee employee) {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Tech Support Requests");
            System.out.println("  1. My requests");
            System.out.println("  2. Send request");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 2);
            switch (choice) {
                case 1: showMyRequests(employee); break;
                case 2: sendRequest(employee); break;
                case 0: running = false; break;
            }
        }
    }

    private void showMyRequests(Employee employee) {
        ConsoleUtils.printHeader("My Tech Support Requests");
        printRequests(ctx.getTechSupportService().getEmployeeRequests(employee.getId()));
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void sendRequest(Employee employee) {
        ConsoleUtils.printHeader("Send Tech Support Request");
        try {
            String title = ConsoleUtils.readNonBlank(sc, "  Title: ");
            String description = ConsoleUtils.readNonBlank(sc, "  Description: ");
            TechSupportRequest request = ctx.getTechSupportService()
                    .submitRequest(employee.getId(), title, description);
            ConsoleUtils.printSuccess("Request #" + request.getId() + " sent.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void printRequests(List<TechSupportRequest> requests) {
        if (requests.isEmpty()) {
            System.out.println("  No support requests.");
            return;
        }
        for (TechSupportRequest r : requests) {
            System.out.printf("  #%-3d %-30s status=%-8s updated=%s%n",
                    r.getId(), r.getTitle(), r.getStatus(), r.getUpdatedAt().toLocalDate());
        }
    }
}
