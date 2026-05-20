import java.util.List;
import java.util.Scanner;

/**
 *
 * Секции:
 *   1. My courses      — CourseRepository 
 *   2. Assign grades   — MarkService
 *   3. Complaints      — ComplaintService 
 *   4. Messages        — MessageService
 *   5. Research        — ResearchService  
 *   6. Profile
 *   0. Log out
 */
public class TeacherUI {

    private final ApplicationContext ctx;
    private final Scanner sc;
    private final ResearcherUI researcherUI;
    private final WorkloadUI workloadUI;
    private final MessageCenterUI messageCenterUI;
    private final TeacherLessonUI teacherLessonUI;
    private final NewsFeedUI newsFeedUI;
    private final SupportRequestUI supportRequestUI;

    public TeacherUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
        this.researcherUI = new ResearcherUI(ctx, sc);
        this.workloadUI = new WorkloadUI(ctx, sc);
        this.messageCenterUI = new MessageCenterUI(ctx, sc);
        this.teacherLessonUI = new TeacherLessonUI(ctx, sc);
        this.newsFeedUI = new NewsFeedUI(ctx, sc);
        this.supportRequestUI = new SupportRequestUI(ctx, sc);
    }

    public void run() {
        boolean running = true;
        while (running) {
            Teacher teacher = AuthContext.asTeacher();
            ConsoleUtils.printHeader("Teacher Portal — " + teacher.getFullName() + "  [" + (teacher.getTeacherPosition() != null ? teacher.getTeacherPosition() : "No position") + "]");

            System.out.println("  1. My courses");
            System.out.println("  2. Assign / update grade");
            System.out.println("  3. Complaints");
            System.out.println("  4. Messages");
            System.out.println("  5. Research");
            System.out.println("  6. Lessons");
            System.out.println("  7. Workload");
            System.out.println("  8. News and research");
            System.out.println("  9. Tech support requests");
            System.out.println("  10. Profile");
            System.out.println("  0. Log out");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 10);
            switch (choice) {
                case 1: showMyCourses(teacher); break;
                case 2: showAssignGrade(teacher);break;
                case 3: showComplaints(teacher); break;
                case 4: messageCenterUI.runForEmployee(teacher); break;
                case 5: researcherUI.run(); break;
                case 6: teacherLessonUI.run(teacher); break;
                case 7: workloadUI.showCurrentTeacherWorkload(teacher); break;
                case 8: newsFeedUI.run(); break;
                case 9: supportRequestUI.runForEmployee(teacher); break;
                case 10: showProfileMenu(teacher); break;
                case 0: running = false; break;
            }
        }
    }


    private void showMyCourses(Teacher teacher) {
        ConsoleUtils.printHeader("My Courses");
        List<Course> courses = teacher.getCourses();
        if (courses.isEmpty()) {
            System.out.println("  No courses assigned.");
        } else {
            for (Course c : courses) {
                System.out.printf("  [%s] %s  (%d credits, %d enrolled)%n",
                        c.getCourseId(), c.getName(), c.getCredits(),
                        c.getEnrollments().size());
            }
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }


    private void showAssignGrade(Teacher teacher) {
        ConsoleUtils.printHeader("Assign / Update Grade");

        if (teacher.getCourses().isEmpty()) {
            System.out.println("  You have no courses.");
            ConsoleUtils.pressEnterToContinue(sc);
            return;
        }

        System.out.println("  Your courses:");
        List<Course> courses = teacher.getCourses();
        for (int i = 0; i < courses.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, courses.get(i).getName());
        }
        int courseIdx = ConsoleUtils.readIntInRange(
                sc, "  Select course (number): ", 1, courses.size()) - 1;
        String courseId = courses.get(courseIdx).getCourseId();
        String studentId = ConsoleUtils.readNonBlank(sc, "  Student ID: ");
        System.out.println("  Component:  1=FIRST_ATTESTATION  2=SECOND_ATTESTATION  3=FINAL_EXAM");
        int comp = ConsoleUtils.readIntInRange(sc, "  Choose component: ", 1, 3);
        MarkType type = comp == 1 ? MarkType.FIRST_ATTESTATION  : comp == 2 ? MarkType.SECOND_ATTESTATION  :  MarkType.FINAL_EXAM;
        double score;
        while (true) {
            try {
                String raw = ConsoleUtils.readNonBlank(sc, "  Score (0-100): ");
                score = Double.parseDouble(raw);
                if (score < 0 || score > 100) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                ConsoleUtils.printError("Enter a number between 0 and 100.");
            }
        }

        String comment = ConsoleUtils.readLine(sc, "  Comment (optional): ");

        try {
            ctx.getMarkService().assignMark(
                    teacher.getId(), studentId, courseId, score, type, comment);
            ConsoleUtils.printSuccess("Grade assigned.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showComplaints(Teacher teacher) {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Complaints");
            System.out.println("  1. View my submitted complaints");
            System.out.println("  2. Submit new complaint");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 2);
            switch (choice) {
                case 1: listMyComplaints(teacher); break;
                case 2: submitComplaint(teacher);  break;
                case 0: running = false;           break;
            }
        }
    }

    private void listMyComplaints(Teacher teacher) {
        ConsoleUtils.printHeader("My Complaints");
        var list = ctx.getComplaintService().getComplaintsByTeacher(teacher.getId());
        if (list.isEmpty()) {
            System.out.println("  No complaints submitted.");
        } else {
            list.forEach(c -> System.out.printf( "  #%d  about %-20s  status=%-12s  urgency=%s%n", c.getId(), c.getTargetStudent() != null ? c.getTargetStudent().getFullName() : "?", c.getComplaintStatus(), c.getUrgencyLevel()));
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void submitComplaint(Teacher teacher) {
        ConsoleUtils.printHeader("Submit Complaint");
        String studentId = ConsoleUtils.readNonBlank(sc, "  Target student ID: ");

        System.out.println("  Recipient:  1=DEAN  2=DEPARTMENT_HEAD");
        int r = ConsoleUtils.readIntInRange(sc, "  Choose: ", 1, 2);
        ManagerForComplaint recipient = r == 1 ? ManagerForComplaint.DEAN : ManagerForComplaint.DEPARTMENT_HEAD;

        System.out.println("  Urgency:  1=LOW  2=MEDIUM  3=HIGH");
        int u = ConsoleUtils.readIntInRange(sc, "  Choose: ", 1, 3);
        UrgencyLevel urgency = u == 1 ? UrgencyLevel.LOW : u == 2 ? UrgencyLevel.MEDIUM : UrgencyLevel.HIGH;

        try {
            Complaint c = ctx.getComplaintService().submitComplaint(teacher.getId(), studentId, recipient, urgency);
            ConsoleUtils.printSuccess("Complaint #" + c.getId() + " submitted.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showMessages(Teacher teacher) {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Messages");
            System.out.println("  1. Inbox");
            System.out.println("  2. Sent");
            System.out.println("  3. Send new message");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 3);
            switch (choice) {
                case 1: showInbox(teacher);   break;
                case 2: showSent(teacher);    break;
                case 3: sendMessage(teacher); break;
                case 0: running = false;      break;
            }
        }
    }

    private void showInbox(Teacher teacher) {
        ConsoleUtils.printHeader("Inbox");
        var msgs = ctx.getMessageService().getInbox(teacher.getId());
        if (msgs.isEmpty()) { System.out.println("  No messages."); }
        else msgs.forEach(m -> System.out.printf( "  #%-3d  from=%-5d  [%s]  read=%-5s  %s%n", m.getId(), m.getSenderId(), m.getSentAt().toLocalDate(), m.isRead(), m.getSubject()));
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showSent(Teacher teacher) {
        ConsoleUtils.printHeader("Sent Messages");
        var msgs = ctx.getMessageService().getSentMessages(teacher.getId());
        if (msgs.isEmpty()) { System.out.println("  No sent messages."); }
        else msgs.forEach(m -> System.out.printf( "  #%-3d  to=%-5d  [%s]  %s%n", m.getId(), m.getRecipientId(), m.getSentAt().toLocalDate(), m.getSubject()));
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void sendMessage(Teacher teacher) {
        ConsoleUtils.printHeader("Send Message");
        try {
            Long recipientId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Recipient user ID: "));
            String subject = ConsoleUtils.readNonBlank(sc, "  Subject: ");
            String body = ConsoleUtils.readNonBlank(sc, "  Body:    ");
            ctx.getMessageService().sendMessage(teacher.getId(), recipientId, subject, body);
            ConsoleUtils.printSuccess("Message sent.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showProfileMenu(Teacher teacher) {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Profile");
            System.out.println("  1. View profile");
            System.out.println("  2. Update profile");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 2);
            switch (choice) {
                case 1: showProfile(teacher); break;
                case 2: updateProfile(teacher); break;
                case 0: running = false; break;
            }
        }
    }

    private void showProfile(Teacher teacher) {
        ConsoleUtils.printHeader("Profile");
        System.out.println("  Name:       " + teacher.getFullName());
        System.out.println("  Email:      " + teacher.getEmail());
        System.out.println("  Employee ID:" + teacher.getEmployeeId());
        System.out.println("  Position:   " + teacher.getTeacherPosition());
        System.out.println("  Department: " + (teacher.getDepartment() != null ? teacher.getDepartment().getName() : "—"));
        System.out.println("  Courses:    " + teacher.getCourses().size());
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void updateProfile(Teacher teacher) {
        ConsoleUtils.printHeader("Update Profile");
        try {
            String firstName = ConsoleUtils.readLine(sc, "  First name (optional): ");
            String lastName = ConsoleUtils.readLine(sc, "  Last name (optional): ");
            String email = ConsoleUtils.readLine(sc, "  Email (optional): ");
            ctx.getTeacherService().updateTeacherProfile(
                    teacher.getEmployeeId(),
                    new TeacherProfileUpdateRequest(
                            firstName.isBlank() ? null : firstName,
                            lastName.isBlank() ? null : lastName,
                            email.isBlank() ? null : email,
                            null));
            ConsoleUtils.printSuccess("Profile updated.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }
}
