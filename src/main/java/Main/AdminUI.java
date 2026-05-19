import java.util.List;
import java.util.Scanner;

/**
 * Секции:
 *   1. User management — UserRepository (list / block / unblock / delete)
 *   2. Faculty management — FacultyService
 *   3. Reports — ReportService (HR, enrollment, faculty)
 *   4. Profile
 *   0. Log out
 */
public class AdminUI {

    private final ApplicationContext ctx;
    private final Scanner            sc;

    public AdminUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc  = sc;
    }

    public void run() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Admin Portal");
            System.out.println("  1. User management");
            System.out.println("  2. Faculty management");
            System.out.println("  3. Reports");
            System.out.println("  4. Profile");
            System.out.println("  0. Log out");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 4);
            switch (choice) {
                case 1: showUserManagement();    break;
                case 2: showFacultyManagement(); break;
                case 3: showReports();           break;
                case 4: showProfile();           break;
                case 0: running = false;         break;
            }
        }
    }

    private void showUserManagement() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("User Management");
            System.out.println("  1. List all users");
            System.out.println("  2. Block user");
            System.out.println("  3. Unblock user");
            System.out.println("  4. Delete user");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 4);
            switch (choice) {
                case 1: listAllUsers();    break;
                case 2: setUserActive(false); break;
                case 3: setUserActive(true);  break;
                case 4: deleteUser();      break;
                case 0: running = false;   break;
            }
        }
    }

    private void listAllUsers() {
        ConsoleUtils.printHeader("All Users");
        List<User> users = ctx.getUserRepository().getUsers();
        if (users.isEmpty()) {
            System.out.println("  No users registered.");
        } else {
            System.out.printf("  %-5s  %-25s  %-30s  %-15s  %s%n",
                    "ID", "Name", "Email", "Type", "Active");
            ConsoleUtils.printDivider();
            for (User u : users) {
                System.out.printf("  %-5d  %-25s  %-30s  %-15s  %s%n",
                        u.getId(), u.getFullName(), u.getEmail(),
                        u.getClass().getSimpleName(), u.isActive() ? "✓" : "✗");
            }
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void setUserActive(boolean active) {
        ConsoleUtils.printHeader(active ? "Unblock User" : "Block User");
        try {
            Long userId = Long.parseLong(
                    ConsoleUtils.readNonBlank(sc, "  User ID: "));
            User user = ctx.getUserRepository().findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "User not found: " + userId));
            user.setActive(active);
            ctx.getUserRepository().save(user);
            ConsoleUtils.printSuccess(user.getFullName()
                    + (active ? " unblocked." : " blocked."));
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void deleteUser() {
        ConsoleUtils.printHeader("Delete User");
        try {
            Long userId = Long.parseLong(
                    ConsoleUtils.readNonBlank(sc, "  User ID: "));
            User user = ctx.getUserRepository().findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "User not found: " + userId));
            if (!ConsoleUtils.confirm(sc,
                    "  Delete " + user.getFullName() + "? This cannot be undone.")) {
                System.out.println("  Cancelled.");
                ConsoleUtils.pressEnterToContinue(sc);
                return;
            }
            ctx.getUserRepository().deleteById(userId);
            ConsoleUtils.printSuccess("User deleted.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showFacultyManagement() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Faculty Management");
            System.out.println("  1. List all schools");
            System.out.println("  2. Create school");
            System.out.println("  3. Rename school");
            System.out.println("  4. Delete school");
            System.out.println("  5. Assign dean");
            System.out.println("  6. Add department to school");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 6);
            switch (choice) {
                case 1: listSchools(); break;
                case 2: createSchool(); break;
                case 3: renameSchool(); break;
                case 4: deleteSchool(); break;
                case 5: assignDean(); break;
                case 6: addDepartment(); break;
                case 0: running = false; break;
            }
        }
    }

    private void listSchools() {
        ConsoleUtils.printHeader("All Schools");
        List<School> schools = ctx.getFacultyService().getAllFaculties();
        if (schools.isEmpty()) { System.out.println("  No schools yet."); }
        else schools.forEach(s -> System.out.printf("  #%-3d  %s  (dean: %s)%n", s.getId(), s.getName(), s.getDean() != null ? s.getDean().getFullName() : "—"));
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void createSchool() {
        ConsoleUtils.printHeader("Create School");
        try {
            String name = ConsoleUtils.readNonBlank(sc, "  School name: ");
            School s = ctx.getFacultyService().createFaculty(new FacultyCreationRequest(name));
            ConsoleUtils.printSuccess("School '" + s.getName() + "' created (id=" + s.getId() + ").");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void renameSchool() {
        ConsoleUtils.printHeader("Rename School");
        try {
            Long id = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  School ID: "));
            String newName = ConsoleUtils.readNonBlank(sc, "  New name: ");
            ctx.getFacultyService().updateFaculty(id, new FacultyUpdateRequest(newName));
            ConsoleUtils.printSuccess("School renamed.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void deleteSchool() {
        ConsoleUtils.printHeader("Delete School");
        try {
            Long id = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  School ID: "));
            if (!ConsoleUtils.confirm(sc, "  Delete school and all its departments?")) {
                System.out.println("  Cancelled.");
                ConsoleUtils.pressEnterToContinue(sc);
                return;
            }
            ctx.getFacultyService().deleteFaculty(id);
            ConsoleUtils.printSuccess("School deleted.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void assignDean() {
        ConsoleUtils.printHeader("Assign Dean");
        try {
            Long schoolId  = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  School ID:  "));
            Long teacherId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Teacher ID: "));
            ctx.getFacultyService().assignDean(schoolId, teacherId);
            ConsoleUtils.printSuccess("Dean assigned.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void addDepartment() {
        ConsoleUtils.printHeader("Add Department");
        try {
            Long schoolId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  School ID: "));
            String name   = ConsoleUtils.readNonBlank(sc, "  Dept name: ");
            String code   = ConsoleUtils.readNonBlank(sc, "  Dept code: ");
            Department dept = new Department();
            dept.setName(name);
            dept.setCode(code);
            ctx.getFacultyService().addDepartmentToFaculty(schoolId, dept);
            ConsoleUtils.printSuccess("Department '" + name + "' added.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    // ── Reports ───────────────────────────────────────────────────────────────

    private void showReports() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Reports");
            System.out.println("  1. HR report");
            System.out.println("  2. Enrollment statistics");
            System.out.println("  3. Student progress report");
            System.out.println("  4. Course performance report");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 4);
            switch (choice) {
                case 1: printHR();                    break;
                case 2: printEnrollmentStats();       break;
                case 3: printStudentProgress();       break;
                case 4: printCoursePerformance();     break;
                case 0: running = false;              break;
            }
        }
    }

    private void printHR() {
        ConsoleUtils.printHeader("HR Report");
        System.out.println("  " + ctx.getReportService().generateHRReport());
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void printEnrollmentStats() {
        ConsoleUtils.printHeader("Enrollment Statistics");
        System.out.println("  " + ctx.getReportService()
                .generateEnrollmentStatistics(ReportPeriod.all()));
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void printStudentProgress() {
        ConsoleUtils.printHeader("Student Progress Report");
        try {
            String sid = ConsoleUtils.readNonBlank(sc, "  Student ID: ");
            System.out.println("  " + ctx.getReportService()
                    .generateStudentProgressReport(sid));
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void printCoursePerformance() {
        ConsoleUtils.printHeader("Course Performance Report");
        try {
            String cid = ConsoleUtils.readNonBlank(sc, "  Course ID: ");
            System.out.println("  " + ctx.getReportService()
                    .generateCoursePerformanceReport(cid));
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    // ── Profile ───────────────────────────────────────────────────────────────

    private void showProfile() {
        User user = AuthContext.getCurrentUser();
        ConsoleUtils.printHeader("Admin Profile");
        System.out.println("  Name:  " + user.getFullName());
        System.out.println("  Email: " + user.getEmail());
        System.out.println("  ID:    " + user.getId());
        ConsoleUtils.pressEnterToContinue(sc);
    }
}