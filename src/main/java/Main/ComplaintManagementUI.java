import java.util.List;
import java.util.Scanner;

/**
 * Админский интерфейс для жалоб.
 * У Dean есть свой маленький queue, а тут уже общий просмотр и фильтры.
 */
public class ComplaintManagementUI {

    private final ApplicationContext ctx;
    private final Scanner sc;

    public ComplaintManagementUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
    }

    public void run() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Complaint Management");
            System.out.println("  1. List all complaints");
            System.out.println("  2. View complaint");
            System.out.println("  3. Filter by status");
            System.out.println("  4. Filter by urgency");
            System.out.println("  5. Filter by student");
            System.out.println("  6. Review complaint");
            System.out.println("  7. Resolve complaint");
            System.out.println("  8. Reject complaint");
            System.out.println("  9. Delete closed complaint");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 9);
            switch (choice) {
                case 1: printComplaints(ctx.getComplaintService().getAllComplaints()); break;
                case 2: viewComplaint(); break;
                case 3: printComplaints(ctx.getComplaintService().getComplaintsByStatus(readComplaintStatus())); break;
                case 4: printComplaints(ctx.getComplaintService().getComplaintsByUrgency(readUrgency())); break;
                case 5: filterByStudent(); break;
                case 6: changeStatus("review"); break;
                case 7: changeStatus("resolve"); break;
                case 8: changeStatus("reject"); break;
                case 9: deleteComplaint(); break;
                case 0: running = false; break;
            }
            if (choice >= 1 && choice <= 9) {
                ConsoleUtils.pressEnterToContinue(sc);
            }
        }
    }

    private void viewComplaint() {
        ConsoleUtils.printHeader("Complaint");
        try {
            Long id = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Complaint ID: "));
            printComplaint(ctx.getComplaintService().getComplaintById(id));
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
    }

    private void filterByStudent() {
        ConsoleUtils.printHeader("Complaints By Student");
        try {
            String studentId = ConsoleUtils.readNonBlank(sc, "  Student ID: ");
            printComplaints(ctx.getComplaintService().getComplaintsByStudent(studentId));
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
    }

    private void changeStatus(String action) {
        ConsoleUtils.printHeader("Complaint - " + action.toUpperCase());
        try {
            Long id = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Complaint ID: "));
            if ("review".equals(action)) {
                ctx.getComplaintService().reviewComplaint(id);
            } else if ("resolve".equals(action)) {
                ctx.getComplaintService().resolveComplaint(id);
            } else {
                ctx.getComplaintService().rejectComplaint(id);
            }
            ConsoleUtils.printSuccess("Complaint updated.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
    }

    private void deleteComplaint() {
        ConsoleUtils.printHeader("Delete Complaint");
        try {
            Long id = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Complaint ID: "));
            ctx.getComplaintService().deleteComplaint(id);
            ConsoleUtils.printSuccess("Complaint deleted.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
    }

    private ComplaintStatus readComplaintStatus() {
        ComplaintStatus[] values = ComplaintStatus.values();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = ConsoleUtils.readIntInRange(sc, "  Status: ", 1, values.length);
        return values[choice - 1];
    }

    private UrgencyLevel readUrgency() {
        UrgencyLevel[] values = UrgencyLevel.values();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = ConsoleUtils.readIntInRange(sc, "  Urgency: ", 1, values.length);
        return values[choice - 1];
    }

    private void printComplaints(List<Complaint> complaints) {
        if (complaints.isEmpty()) {
            System.out.println("  No complaints found.");
            return;
        }
        complaints.forEach(this::printComplaint);
    }

    private void printComplaint(Complaint complaint) {
        System.out.printf("  #%-3d from=%-20s target=%-20s recipient=%s status=%s urgency=%s%n",
                complaint.getId(),
                complaint.getSender() != null ? complaint.getSender().getFullName() : "?",
                complaint.getTargetStudent() != null ? complaint.getTargetStudent().getFullName() : "?",
                complaint.getRecipient(),
                complaint.getComplaintStatus(),
                complaint.getUrgencyLevel());
    }
}
