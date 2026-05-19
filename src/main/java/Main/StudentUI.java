import java.util.Scanner;

/**
 *
 * Секции:
 *   1. My courses — EnrollmentService
 *   2. My grades  — MarkService
 *   3. My transcript  — TranscriptService
 *   4. Research — ResearchService (для MasterStudent / PhDStudent)
 *   5. Profile
 *   0. Log out
 */
public class StudentUI {
    private final ApplicationContext ctx;
    private final Scanner sc;
    public StudentUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc  = sc;
    }

    public void run() {
        boolean running = true;
        while (running) {
            Student student = AuthContext.asStudent();
            ConsoleUtils.printHeader("Student Portal — " + student.getFullName());

            System.out.println("  1. My courses");
            System.out.println("  2. My grades");
            System.out.println("  3. My transcript");
            if (AuthContext.isResearcher()) {
                System.out.println("  4. Research");
            }
            System.out.println("  5. Profile");
            System.out.println("  0. Log out");

            int max = AuthContext.isResearcher() ? 5 : 5;
            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, max);

            switch (choice) {
                case 1: showMyCourses(student);    break;
                case 2: showMyGrades(student);     break;
                case 3: showMyTranscript(student); break;
                case 4:
                    if (AuthContext.isResearcher()) showResearch();
                    else ConsoleUtils.printError("Invalid option.");
                    break;
                case 5: showProfile(student);      break;
                case 0: running = false;           break;
            }
        }
    }

    private void showMyCourses(Student student) {
        ConsoleUtils.printHeader("My Courses");
        var enrollments = ctx.getEnrollmentService().getStudentEnrollments(student.getStudentId());
        if (enrollments.isEmpty()) {
            System.out.println("  You are not enrolled in any courses.");
        } else {
            for (Enrollment e : enrollments) {
                System.out.printf("  [%-10s] %s  (%d credits)%n", e.getStatus(), e.getCourse() != null ? e.getCourse().getName() : "—", e.getCourse() != null ? e.getCourse().getCredits() : 0); 
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }
    }

    private void showMyGrades(Student student) {
        ConsoleUtils.printHeader("My Grades");
        var enrollments = ctx.getEnrollmentService().getStudentEnrollments(student.getStudentId());
        if (enrollments.isEmpty()) {
            System.out.println("  No enrollment records found.");
        } else {
            for (Enrollment e : enrollments) {
                String courseName = e.getCourse() != null ? e.getCourse().getName() : "—";
                if (e.getMark() != null) {
                    System.out.printf("  %-30s  %s%n", courseName, e.getMark());
                } else {
                    System.out.printf("  %-30s  No grade yet%n", courseName);
                }
            }
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }


    private void showMyTranscript(Student student) {
        ConsoleUtils.printHeader("My Transcript");
        Transcript t = ctx.getTranscriptService().generateTranscript(student.getStudentId());
        t.generateReport();
        System.out.printf("%n  GPA:            %.2f%n", ctx.getTranscriptService().calculateGPA(student.getStudentId()));
        System.out.printf("  Credits earned: %d%n", ctx.getTranscriptService().getTotalCreditsEarned(student.getStudentId()));
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showResearch() {
        ConsoleUtils.printHeader("My Research");
        IResearcher researcher = AuthContext.asResearcher();
        var papers = ctx.getResearchService()
                .getResearcherPapers(AuthContext.getCurrentUser().getId());
        if (papers.isEmpty()) {
            System.out.println("  No publications yet.");
        } else {
            papers.forEach(p -> System.out.println("  " + p));
        }
        System.out.printf("  Total citations: %d%n", researcher.getTotalCitations());
        ConsoleUtils.pressEnterToContinue(sc);
    }


    private void showProfile(Student student) {
        ConsoleUtils.printHeader("Profile");
        System.out.println("  Name:        " + student.getFullName());
        System.out.println("  Email:       " + student.getEmail());
        System.out.println("  Student ID:  " + student.getStudentId());
        System.out.println("  Year:        " + student.getYearOfStudy());
        System.out.println("  GPA:         " + student.getGpa());
        System.out.println("  Credits:     " + student.getTotalCredits());
        System.out.println("  Type:        " + student.getClass().getSimpleName());
        ConsoleUtils.pressEnterToContinue(sc);
    }
}