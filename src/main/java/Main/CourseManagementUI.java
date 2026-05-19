import java.util.List;
import java.util.Scanner;

/**
 * Интерфейс для управления курсами.
 * В основном используется админом, но вынесен отдельно чтобы AdminUI не стал совсем огромным.
 */
public class CourseManagementUI {

    private final ApplicationContext ctx;
    private final Scanner sc;

    public CourseManagementUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
    }

    public void run() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Course Management");
            System.out.println("  1. List / search courses");
            System.out.println("  2. Create course");
            System.out.println("  3. Update course");
            System.out.println("  4. Delete course");
            System.out.println("  5. Assign instructor");
            System.out.println("  6. Course roster");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 6);
            switch (choice) {
                case 1: searchCourses(); break;
                case 2: createCourse(); break;
                case 3: updateCourse(); break;
                case 4: deleteCourse(); break;
                case 5: assignInstructor(); break;
                case 6: showRoster(); break;
                case 0: running = false; break;
            }
        }
    }

    private void searchCourses() {
        ConsoleUtils.printHeader("Courses");
        String keyword = ConsoleUtils.readLine(sc, "  Name contains (optional): ");
        String maxRaw = ConsoleUtils.readLine(sc, "  Max credits (optional): ");
        CourseType type = readOptionalCourseType();
        Integer maxCredits = null;
        if (!maxRaw.isBlank()) {
            try {
                maxCredits = Integer.parseInt(maxRaw);
            } catch (NumberFormatException ex) {
                ConsoleUtils.printError("Max credits ignored: not a number.");
            }
        }

        List<Course> courses = ctx.getCourseService().searchCourses(
                new CourseSearchFilters(type, keyword, maxCredits));
        printCourses(courses);
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void createCourse() {
        ConsoleUtils.printHeader("Create Course");
        try {
            String courseId = ConsoleUtils.readNonBlank(sc, "  Course ID: ");
            String name = ConsoleUtils.readNonBlank(sc, "  Name: ");
            int credits = ConsoleUtils.readIntInRange(sc, "  Credits: ", 1, 10);
            CourseType type = readCourseType();
            Course course = ctx.getCourseService().createCourse(
                    new CourseCreationRequest(courseId, name, credits, type));
            ConsoleUtils.printSuccess("Course " + course.getCourseId() + " created.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void updateCourse() {
        ConsoleUtils.printHeader("Update Course");
        try {
            String courseId = ConsoleUtils.readNonBlank(sc, "  Course ID: ");
            String name = ConsoleUtils.readLine(sc, "  New name (optional): ");
            String creditsRaw = ConsoleUtils.readLine(sc, "  New credits (optional): ");
            CourseType type = readOptionalCourseType();
            Integer credits = creditsRaw.isBlank() ? null : Integer.parseInt(creditsRaw);
            ctx.getCourseService().updateCourse(
                    courseId,
                    new CourseUpdateRequest(name.isBlank() ? null : name, credits, type));
            ConsoleUtils.printSuccess("Course updated.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void deleteCourse() {
        ConsoleUtils.printHeader("Delete Course");
        try {
            String courseId = ConsoleUtils.readNonBlank(sc, "  Course ID: ");
            if (!ConsoleUtils.confirm(sc, "  Delete course " + courseId + "?")) {
                System.out.println("  Cancelled.");
                ConsoleUtils.pressEnterToContinue(sc);
                return;
            }
            ctx.getCourseService().deleteCourse(courseId);
            ConsoleUtils.printSuccess("Course deleted.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void assignInstructor() {
        ConsoleUtils.printHeader("Assign Instructor");
        try {
            String courseId = ConsoleUtils.readNonBlank(sc, "  Course ID: ");
            Long teacherId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Teacher user ID: "));
            ctx.getCourseService().assignInstructor(courseId, teacherId);
            ConsoleUtils.printSuccess("Instructor assigned.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showRoster() {
        ConsoleUtils.printHeader("Course Roster");
        try {
            String courseId = ConsoleUtils.readNonBlank(sc, "  Course ID: ");
            List<Enrollment> roster = ctx.getEnrollmentService().getCourseRoster(courseId);
            if (roster.isEmpty()) {
                System.out.println("  No enrolled students.");
            } else {
                roster.forEach(e -> System.out.printf("  #%-3d %-20s status=%s%n",
                        e.getId(),
                        e.getStudent() != null ? e.getStudent().getFullName() : "?",
                        e.getStatus()));
            }
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    public void printCourses(List<Course> courses) {
        if (courses.isEmpty()) {
            System.out.println("  No courses found.");
            return;
        }
        System.out.printf("  %-10s %-30s %-8s %-10s %-8s%n",
                "ID", "Name", "Credits", "Type", "Teachers");
        ConsoleUtils.printDivider();
        for (Course c : courses) {
            System.out.printf("  %-10s %-30s %-8d %-10s %-8d%n",
                    c.getCourseId(), c.getName(), c.getCredits(),
                    c.getCourseType(), c.getTeachers().size());
        }
    }

    private CourseType readCourseType() {
        CourseType[] values = CourseType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = ConsoleUtils.readIntInRange(sc, "  Course type: ", 1, values.length);
        return values[choice - 1];
    }

    private CourseType readOptionalCourseType() {
        // 0 значит что фильтр по типу не ставим
        System.out.println("  Course type: 0=ANY");
        CourseType[] values = CourseType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d=%s", i + 1, values[i]);
        }
        System.out.println();
        int choice = ConsoleUtils.readIntInRange(sc, "  Choose: ", 0, values.length);
        return choice == 0 ? null : values[choice - 1];
    }
}
