import java.util.List;
import java.util.Scanner;

/**
 * Кабинет HR manager.
 * Тут собраны teacher profile, department и workload, потому что это ближе к HR части.
 */
public class HRManagerUI {

    private final ApplicationContext ctx;
    private final Scanner sc;
    private final DepartmentManagementUI departmentUI;
    private final WorkloadUI workloadUI;
    private final MessageCenterUI messageCenterUI;

    public HRManagerUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
        this.departmentUI = new DepartmentManagementUI(ctx, sc);
        this.workloadUI = new WorkloadUI(ctx, sc);
        this.messageCenterUI = new MessageCenterUI(ctx, sc);
    }

    public void run() {
        boolean running = true;
        while (running) {
            HRManager manager = AuthContext.asHRManager();
            ConsoleUtils.printHeader("HR Manager Portal - " + manager.getFullName());
            System.out.println("  1. HR report");
            System.out.println("  2. Teacher search");
            System.out.println("  3. Assign teacher to department");
            System.out.println("  4. Update teacher profile");
            System.out.println("  5. Department management");
            System.out.println("  6. Workload management");
            System.out.println("  7. Messages");
            System.out.println("  8. Profile");
            System.out.println("  0. Log out");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 8);
            switch (choice) {
                case 1: showHRReport(); break;
                case 2: searchTeachers(); break;
                case 3: assignTeacherToDepartment(); break;
                case 4: updateTeacherProfile(); break;
                case 5: departmentUI.run(); break;
                case 6: workloadUI.runManagement(); break;
                case 7: messageCenterUI.runForEmployee(manager); break;
                case 8: showProfile(manager); break;
                case 0: running = false; break;
            }
        }
    }

    private void showHRReport() {
        ConsoleUtils.printHeader("HR Report");
        System.out.println("  " + ctx.getReportService().generateHRReport());
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void searchTeachers() {
        ConsoleUtils.printHeader("Teacher Search");
        String keyword = ConsoleUtils.readLine(sc, "  Keyword: ");
        List<Teacher> teachers = ctx.getTeacherService().searchTeachers(keyword);
        if (teachers.isEmpty()) {
            System.out.println("  No teachers found.");
        } else {
            teachers.forEach(t -> System.out.printf("  #%-3d %-25s employee=%s department=%s position=%s%n",
                    t.getId(),
                    t.getFullName(),
                    t.getEmployeeId(),
                    t.getDepartment() != null ? t.getDepartment().getName() : "-",
                    t.getTeacherPosition()));
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void assignTeacherToDepartment() {
        ConsoleUtils.printHeader("Assign Teacher To Department");
        try {
            String employeeId = ConsoleUtils.readNonBlank(sc, "  Teacher employee ID: ");
            Long deptId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Department ID: "));
            ctx.getTeacherService().assignTeacherToDepartment(employeeId, deptId);
            ConsoleUtils.printSuccess("Teacher assigned to department.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void updateTeacherProfile() {
        ConsoleUtils.printHeader("Update Teacher Profile");
        try {
            String employeeId = ConsoleUtils.readNonBlank(sc, "  Teacher employee ID: ");
            String firstName = ConsoleUtils.readLine(sc, "  First name (optional): ");
            String lastName = ConsoleUtils.readLine(sc, "  Last name (optional): ");
            String email = ConsoleUtils.readLine(sc, "  Email (optional): ");
            TeacherPosition position = readOptionalTeacherPosition();
            ctx.getTeacherService().updateTeacherProfile(employeeId,
                    new TeacherProfileUpdateRequest(
                            firstName.isBlank() ? null : firstName,
                            lastName.isBlank() ? null : lastName,
                            email.isBlank() ? null : email,
                            position));
            ConsoleUtils.printSuccess("Teacher profile updated.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private TeacherPosition readOptionalTeacherPosition() {
        TeacherPosition[] values = TeacherPosition.values();
        System.out.println("  Position: 0=UNCHANGED");
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = ConsoleUtils.readIntInRange(sc, "  Choose: ", 0, values.length);
        return choice == 0 ? null : values[choice - 1];
    }

    private void showProfile(HRManager manager) {
        ConsoleUtils.printHeader("HR Manager Profile");
        System.out.println("  Name:          " + manager.getFullName());
        System.out.println("  Email:         " + manager.getEmail());
        System.out.println("  Employee ID:   " + manager.getEmployeeId());
        System.out.println("  Level:         " + manager.getLevel());
        System.out.println("  Specialization:" + manager.getSpecialization());
        System.out.println("  Hiring quota:  " + manager.getHiringQuota());
        System.out.println("  Vacancies:     " + manager.getOpenVacancies());
        ConsoleUtils.pressEnterToContinue(sc);
    }
}
