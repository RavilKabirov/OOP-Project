import java.util.Scanner;

/**
 * Интерфейс для нагрузки преподавателей.
 * Нюанс: courseId у нас строковый, поэтому тут читаем его как String, а не как Long.
 */
public class WorkloadUI {

    private final ApplicationContext ctx;
    private final Scanner sc;

    public WorkloadUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
    }

    public void runManagement() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Workload Management");
            System.out.println("  1. Assign teacher to course");
            System.out.println("  2. Remove teacher from course");
            System.out.println("  3. Teacher workload");
            System.out.println("  4. Department workload");
            System.out.println("  5. Faculty workload");
            System.out.println("  6. Approve faculty workload");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 6);
            switch (choice) {
                case 1: assignTeacher(); break;
                case 2: removeTeacher(); break;
                case 3: showTeacherWorkload(); break;
                case 4: showDepartmentWorkload(); break;
                case 5: showFacultyWorkload(); break;
                case 6: approveWorkload(); break;
                case 0: running = false; break;
            }
        }
    }

    public void showCurrentTeacherWorkload(Teacher teacher) {
        ConsoleUtils.printHeader("My Workload");
        try {
            System.out.println("  " + ctx.getWorkloadService().getTeacherWorkload(
                    teacher.getId(), readSemester()));
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void assignTeacher() {
        ConsoleUtils.printHeader("Assign Teacher To Course");
        try {
            Long teacherId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Teacher user ID: "));
            String courseId = ConsoleUtils.readNonBlank(sc, "  Course ID: ");
            int hours = ConsoleUtils.readIntInRange(sc, "  Hours: ", 1, 300);
            ctx.getWorkloadService().assignTeacherToCourse(teacherId, courseId, hours);
            ConsoleUtils.printSuccess("Teacher assigned.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void removeTeacher() {
        ConsoleUtils.printHeader("Remove Teacher From Course");
        try {
            Long teacherId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Teacher user ID: "));
            String courseId = ConsoleUtils.readNonBlank(sc, "  Course ID: ");
            ctx.getWorkloadService().removeTeacherFromCourse(teacherId, courseId);
            ConsoleUtils.printSuccess("Teacher removed.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showTeacherWorkload() {
        ConsoleUtils.printHeader("Teacher Workload");
        try {
            Long teacherId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Teacher user ID: "));
            System.out.println("  " + ctx.getWorkloadService().getTeacherWorkload(teacherId, readSemester()));
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showDepartmentWorkload() {
        ConsoleUtils.printHeader("Department Workload");
        try {
            Long deptId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Department ID: "));
            System.out.println("  " + ctx.getWorkloadService().getDepartmentWorkload(deptId));
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showFacultyWorkload() {
        ConsoleUtils.printHeader("Faculty Workload");
        try {
            Long facultyId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  School ID: "));
            System.out.println("  " + ctx.getWorkloadService().getFacultyWorkload(facultyId));
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void approveWorkload() {
        ConsoleUtils.printHeader("Approve Workload");
        try {
            Long facultyId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  School ID: "));
            Long approvedBy = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Approver teacher user ID: "));
            ctx.getWorkloadService().approveWorkload(facultyId, approvedBy);
            ConsoleUtils.printSuccess("Workload approved.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private Semester readSemester() {
        int year = ConsoleUtils.readIntInRange(sc, "  Year: ", 2000, 2100);
        System.out.println("  1. SPRING");
        System.out.println("  2. SUMMER");
        System.out.println("  3. FALL");
        int choice = ConsoleUtils.readIntInRange(sc, "  Term: ", 1, 3);
        Semester.SemesterTerm term = choice == 1
                ? Semester.SemesterTerm.SPRING
                : choice == 2 ? Semester.SemesterTerm.SUMMER : Semester.SemesterTerm.FALL;
        return new Semester(year, term);
    }
}
