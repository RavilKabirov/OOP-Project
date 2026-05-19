import java.util.List;
import java.util.Scanner;

/**
 * Меню для департаментов: создание, поиск, head и список преподавателей.
 * Тут оставлен общий Scanner, как и в других интерфейсах, чтобы не было проблем с nextLine.
 */
public class DepartmentManagementUI {

    private final ApplicationContext ctx;
    private final Scanner sc;

    public DepartmentManagementUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
    }

    public void run() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Department Management");
            System.out.println("  1. Search departments");
            System.out.println("  2. Create department");
            System.out.println("  3. Update department");
            System.out.println("  4. Delete department");
            System.out.println("  5. Assign head");
            System.out.println("  6. Department teachers");
            System.out.println("  7. Departments by school");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 7);
            switch (choice) {
                case 1: searchDepartments(); break;
                case 2: createDepartment(); break;
                case 3: updateDepartment(); break;
                case 4: deleteDepartment(); break;
                case 5: assignHead(); break;
                case 6: showTeachers(); break;
                case 7: showBySchool(); break;
                case 0: running = false; break;
            }
        }
    }

    private void searchDepartments() {
        ConsoleUtils.printHeader("Departments");
        String keyword = ConsoleUtils.readLine(sc, "  Name contains: ");
        printDepartments(ctx.getDepartmentService().searchDepartments(keyword));
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void createDepartment() {
        ConsoleUtils.printHeader("Create Department");
        try {
            String name = ConsoleUtils.readNonBlank(sc, "  Name: ");
            Long schoolId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  School ID: "));
            Department dept = ctx.getDepartmentService().createDepartment(
                    new DepartmentCreationRequest(name, schoolId));
            ConsoleUtils.printSuccess("Department #" + dept.getId() + " created.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void updateDepartment() {
        ConsoleUtils.printHeader("Update Department");
        try {
            Long deptId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Department ID: "));
            String name = ConsoleUtils.readLine(sc, "  New name (optional): ");
            String schoolRaw = ConsoleUtils.readLine(sc, "  New school ID (optional): ");
            Long schoolId = schoolRaw.isBlank() ? null : Long.parseLong(schoolRaw);
            ctx.getDepartmentService().updateDepartment(
                    deptId,
                    new DepartmentUpdateRequest(name.isBlank() ? null : name, schoolId));
            ConsoleUtils.printSuccess("Department updated.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void deleteDepartment() {
        ConsoleUtils.printHeader("Delete Department");
        try {
            Long deptId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Department ID: "));
            if (!ConsoleUtils.confirm(sc, "  Delete department #" + deptId + "?")) {
                System.out.println("  Cancelled.");
                ConsoleUtils.pressEnterToContinue(sc);
                return;
            }
            ctx.getDepartmentService().deleteDepartment(deptId);
            ConsoleUtils.printSuccess("Department deleted.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void assignHead() {
        ConsoleUtils.printHeader("Assign Department Head");
        try {
            Long deptId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Department ID: "));
            Long teacherId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Teacher user ID: "));
            ctx.getDepartmentService().assignHead(deptId, teacherId);
            ConsoleUtils.printSuccess("Department head assigned.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showTeachers() {
        ConsoleUtils.printHeader("Department Teachers");
        try {
            Long deptId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Department ID: "));
            List<Teacher> teachers = ctx.getDepartmentService().getDepartmentTeachers(deptId);
            if (teachers.isEmpty()) {
                System.out.println("  No teachers in this department.");
            } else {
                teachers.forEach(t -> System.out.printf("  #%-3d %-25s employee=%s%n",
                        t.getId(), t.getFullName(), t.getEmployeeId()));
            }
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showBySchool() {
        ConsoleUtils.printHeader("Departments By School");
        try {
            Long schoolId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  School ID: "));
            printDepartments(ctx.getDepartmentService().getDepartmentsByFaculty(schoolId));
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    public void printDepartments(List<Department> departments) {
        if (departments.isEmpty()) {
            System.out.println("  No departments found.");
            return;
        }
        departments.forEach(d -> System.out.printf("  #%-3d [%s] %-30s head=%s%n",
                d.getId(),
                d.getCode(),
                d.getName(),
                d.getHead() != null ? d.getHead().getFullName() : "-"));
    }
}
