import java.util.List;
import java.util.Scanner;

/**
 *
 * Секции:
 *   1. Faculty overview    — FacultyService
 *   2. Complaints          — ComplaintService
 *   3. Reports             — ReportService
 *   4. Messages            — MessageService
 *   5. Profile
 *   0. Log out
 */
public class DeanUI {

    private final ApplicationContext ctx;
    private final Scanner sc;
    private final MessageCenterUI messageCenterUI;

    public DeanUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
        this.messageCenterUI = new MessageCenterUI(ctx, sc);
    }

    public void run() {
        boolean running = true;
        while (running) {
            Dean dean = AuthContext.asDean();
            String schoolName = dean.getSchool() != null ? dean.getSchool().getName() : "No school assigned";
            ConsoleUtils.printHeader("Dean Portal — " + dean.getFullName() + "  [" + schoolName + "]");

            System.out.println("  1. Faculty overview");
            System.out.println("  2. Complaints");
            System.out.println("  3. Reports");
            System.out.println("  4. Messages");
            System.out.println("  5. Profile");
            System.out.println("  0. Log out");
            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 5);
            switch (choice) {
                case 1: showFacultyOverview(dean); break;
                case 2: showComplaints();          break;
                case 3: showReports(dean);         break;
                case 4: messageCenterUI.runForEmployee(dean); break;
                case 5: showProfile(dean);         break;
                case 0: running = false;           break;
            }
        }
    }

    private void showFacultyOverview(Dean dean) {
        ConsoleUtils.printHeader("Faculty Overview");
        if (dean.getSchool() == null) {
            System.out.println("  No school assigned to your account.");
            ConsoleUtils.pressEnterToContinue(sc);
            return;
        }

        School school = dean.getSchool();
        System.out.println("  School: " + school.getName());

        List<Department> depts = ctx.getFacultyService().getFacultyDepartments(school.getId());
        System.out.println("  Departments: " + depts.size());
        depts.forEach(d -> System.out.println("    - [" + d.getCode() + "] " + d.getName()));

        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showComplaints() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Complaints — DEAN queue");
            System.out.println("  1. View pending complaints");
            System.out.println("  2. Review complaint (SUBMITTED → UNDER_REVIEW)");
            System.out.println("  3. Resolve complaint");
            System.out.println("  4. Reject complaint");
            System.out.println("  0. Back");
            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 4);
            switch (choice) {
                case 1: listPendingComplaints(); break;
                case 2: changeComplaintStatus("review");  break;
                case 3: changeComplaintStatus("resolve"); break;
                case 4: changeComplaintStatus("reject");  break;
                case 0: running = false;                  break;
            }
        }
    }

    private void listPendingComplaints() {
        ConsoleUtils.printHeader("Pending Complaints (DEAN)");
        var list = ctx.getComplaintService().getComplaintsByRecipient(ManagerForComplaint.DEAN);
        if (list.isEmpty()) {
            System.out.println("  No complaints in the Dean queue.");
        } else {
            list.forEach(c -> System.out.printf( "  #%-3d  %-20s → %-20s  status=%-12s  urgency=%s%n", c.getId(), c.getSender() != null ? c.getSender().getFullName() : "?", c.getTargetStudent() != null ? c.getTargetStudent().getFullName() : "?", c.getComplaintStatus(), c.getUrgencyLevel()));
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }
    private void changeComplaintStatus(String action) {
        ConsoleUtils.printHeader("Complaint — " + action.toUpperCase());
        try {
            Long id = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Complaint ID: "));
            switch (action) {
                case "review":  ctx.getComplaintService().reviewComplaint(id);  break;
                case "resolve": ctx.getComplaintService().resolveComplaint(id); break;
                case "reject":  ctx.getComplaintService().rejectComplaint(id);  break;
            }
            ConsoleUtils.printSuccess("Complaint #" + id + " updated.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showReports(Dean dean) {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Reports");
            System.out.println("  1. Faculty report");
            System.out.println("  2. Department report");
            System.out.println("  3. Enrollment statistics");
            System.out.println("  0. Back");
            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 3);
            switch (choice) {
                case 1: printFacultyReport(dean);    break;
                case 2: printDepartmentReport();     break;
                case 3: printEnrollmentStats();      break;
                case 0: running = false;             break;
            }
        }
    }

    private void printFacultyReport(Dean dean) {
        ConsoleUtils.printHeader("Faculty Report");
        if (dean.getSchool() == null) {
            ConsoleUtils.printError("No school assigned.");
            ConsoleUtils.pressEnterToContinue(sc);
            return;
        }
        FacultyReport r = ctx.getReportService().generateFacultyReport(dean.getSchool().getId(), ReportPeriod.all());
        System.out.println("  " + r);
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void printDepartmentReport() {
        ConsoleUtils.printHeader("Department Report");
        try {
            Long deptId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Department ID: "));
            DepartmentReport r = ctx.getReportService().generateDepartmentReport(deptId, ReportPeriod.all());
            System.out.println("  " + r);
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void printEnrollmentStats() {
        ConsoleUtils.printHeader("Enrollment Statistics");
        EnrollmentStatistics s = ctx.getReportService().generateEnrollmentStatistics(ReportPeriod.all());
        System.out.println("  " + s);
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showMessages(Dean dean) {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Messages");
            System.out.println("  1. Inbox (" + ctx.getMessageService().getInbox(dean.getId()).size() + ")");
            System.out.println("  2. Send message");
            System.out.println("  0. Back");
            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 2);
            switch (choice) {
                case 1: showInbox(dean);    break;
                case 2: sendMessage(dean);  break;
                case 0: running = false;    break;
            }
        }
    }

    private void showInbox(Dean dean) {
        ConsoleUtils.printHeader("Inbox");
        var msgs = ctx.getMessageService().getInbox(dean.getId());
        if (msgs.isEmpty()) { System.out.println("  No messages."); }
        else msgs.forEach(m -> System.out.printf( "  #%-3d  from=%-5d  [%s]  %s%n", m.getId(), m.getSenderId(), m.getSentAt().toLocalDate(), m.getSubject()));
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void sendMessage(Dean dean) {
        ConsoleUtils.printHeader("Send Message");
        try {
            Long recipientId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Recipient user ID: "));
            String subject = ConsoleUtils.readNonBlank(sc, "  Subject: ");
            String body = ConsoleUtils.readNonBlank(sc, "  Body:    ");
            ctx.getMessageService().sendMessage(dean.getId(), recipientId, subject, body);
            ConsoleUtils.printSuccess("Message sent.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showProfile(Dean dean) {
        ConsoleUtils.printHeader("Profile");
        System.out.println("  Name:       " + dean.getFullName());
        System.out.println("  Email:      " + dean.getEmail());
        System.out.println("  Employee ID:" + dean.getEmployeeId());
        System.out.println("  School:     " + (dean.getSchool() != null ? dean.getSchool().getName() : "—"));
        ConsoleUtils.pressEnterToContinue(sc);
    }
}
