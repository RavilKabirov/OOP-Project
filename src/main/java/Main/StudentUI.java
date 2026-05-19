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
    private final ResearcherUI researcherUI;
    private final OrganizationUI organizationUI;
    public StudentUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc  = sc;
        this.researcherUI = new ResearcherUI(ctx, sc);
        this.organizationUI = new OrganizationUI(ctx, sc);
    }

    public void run() {
        boolean running = true;
        while (running) {
            Student student = AuthContext.asStudent();
            ConsoleUtils.printHeader("Student Portal — " + student.getFullName());

            System.out.println("  1. My courses");
            System.out.println("  2. My grades");
            System.out.println("  3. My transcript");
            System.out.println("  4. Course registration");
            System.out.println("  5. Organizations");
            if (AuthContext.isResearcher()) {
                System.out.println("  6. Research");
            }
            System.out.println("  " + (AuthContext.isResearcher() ? "7" : "6") + ". Profile");
            System.out.println("  0. Log out");

            int max = AuthContext.isResearcher() ? 7 : 6;
            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, max);

            switch (choice) {
                case 1: showMyCourses(student);    break;
                case 2: showMyGrades(student);     break;
                case 3: showMyTranscript(student); break;
                case 4:
                    showCourseRegistration(student);
                    break;
                case 5:
                    organizationUI.runForStudent(student);
                    break;
                case 6:
                    // У обычного bachelor тут сразу Profile, а у Master/PhD сначала Research.
                    if (AuthContext.isResearcher()) researcherUI.run();
                    else showProfile(student);
                    break;
                case 7: showProfile(student);      break;
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

    private void showCourseRegistration(Student student) {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Course Registration");
            System.out.println("  1. Available courses");
            System.out.println("  2. Enroll in course");
            System.out.println("  3. Drop course");
            System.out.println("  4. Check enrollment status");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 4);
            switch (choice) {
                case 1: showAvailableCourses(student); break;
                case 2: enrollInCourse(student); break;
                case 3: dropCourse(student); break;
                case 4: showEnrollmentStatus(student); break;
                case 0: running = false; break;
            }
        }
    }

    private void showAvailableCourses(Student student) {
        ConsoleUtils.printHeader("Available Courses");
        var courses = ctx.getCourseService().getAvailableCoursesForStudent(student.getId());
        if (courses.isEmpty()) {
            System.out.println("  No available courses.");
        } else {
            courses.forEach(c -> System.out.printf("  [%s] %-30s credits=%d type=%s%n",
                    c.getCourseId(), c.getName(), c.getCredits(), c.getCourseType()));
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void enrollInCourse(Student student) {
        ConsoleUtils.printHeader("Enroll In Course");
        try {
            String courseId = ConsoleUtils.readNonBlank(sc, "  Course ID: ");
            Enrollment enrollment = ctx.getEnrollmentService().enrollStudent(student.getStudentId(), courseId);
            ConsoleUtils.printSuccess("Enrollment #" + enrollment.getId() + " created.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void dropCourse(Student student) {
        ConsoleUtils.printHeader("Drop Course");
        try {
            String courseId = ConsoleUtils.readNonBlank(sc, "  Course ID: ");
            ctx.getEnrollmentService().dropCourse(student.getStudentId(), courseId);
            ConsoleUtils.printSuccess("Course dropped.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showEnrollmentStatus(Student student) {
        ConsoleUtils.printHeader("Enrollment Status");
        String courseId = ConsoleUtils.readNonBlank(sc, "  Course ID: ");
        System.out.println("  Status: " + ctx.getEnrollmentService()
                .getEnrollmentStatus(student.getStudentId(), courseId));
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
