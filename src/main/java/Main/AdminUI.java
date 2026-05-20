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
    private final CourseManagementUI courseUI;
    private final DepartmentManagementUI departmentUI;
    private final OrganizationUI organizationUI;
    private final WorkloadUI workloadUI;
    private final ComplaintManagementUI complaintUI;
    private final NewsFeedUI newsFeedUI;
    private final SupportRequestUI supportRequestUI;

    public AdminUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc  = sc;
        this.courseUI = new CourseManagementUI(ctx, sc);
        this.departmentUI = new DepartmentManagementUI(ctx, sc);
        this.organizationUI = new OrganizationUI(ctx, sc);
        this.workloadUI = new WorkloadUI(ctx, sc);
        this.complaintUI = new ComplaintManagementUI(ctx, sc);
        this.newsFeedUI = new NewsFeedUI(ctx, sc);
        this.supportRequestUI = new SupportRequestUI(ctx, sc);
    }

    public void run() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Admin Portal");
            System.out.println("  1. User management");
            System.out.println("  2. Faculty management");
            System.out.println("  3. Department management");
            System.out.println("  4. Course management");
            System.out.println("  5. Teacher management");
            System.out.println("  6. Organization management");
            System.out.println("  7. Complaint management");
            System.out.println("  8. Workload management");
            System.out.println("  9. Research review");
            System.out.println("  10. Reports");
            System.out.println("  11. News and research");
            System.out.println("  12. Tech support requests");
            System.out.println("  13. Profile");
            System.out.println("  0. Log out");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 13);
            switch (choice) {
                case 1: showUserManagement();    break;
                case 2: showFacultyManagement(); break;
                case 3: departmentUI.run();       break;
                case 4: courseUI.run();           break;
                case 5: showTeacherManagement();  break;
                case 6: organizationUI.runForAdmin(); break;
                case 7: complaintUI.run();        break;
                case 8: workloadUI.runManagement(); break;
                case 9: showResearchReview();     break;
                case 10: showReports();           break;
                case 11: newsFeedUI.run();         break;
                case 12: supportRequestUI.runForEmployee((Employee) AuthContext.getCurrentUser()); break;
                case 13: showProfile();           break;
                case 0: running = false;         break;
            }
        }
    }

    private void showUserManagement() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("User Management");
            System.out.println("  1. List all users");
            System.out.println("  2. Create user");
            System.out.println("  3. Update user");
            System.out.println("  4. Block user");
            System.out.println("  5. Unblock user");
            System.out.println("  6. Delete user");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 6);
            switch (choice) {
                case 1: listAllUsers();       break;
                case 2: createUser();         break;
                case 3: updateUser();         break;
                case 4: setUserActive(false); break;
                case 5: setUserActive(true);  break;
                case 6: deleteUser();         break;
                case 0: running = false;      break;
            }
        }
    }

    private void createUser() {
        ConsoleUtils.printHeader("Create User");
        try {
            String email = ConsoleUtils.readNonBlank(sc, "  Email: ");
            if (ctx.getUserRepository().existsByEmail(email)) {
                throw new IllegalArgumentException("Email already exists.");
            }
            String firstName = ConsoleUtils.readNonBlank(sc, "  First name: ");
            String lastName = ConsoleUtils.readNonBlank(sc, "  Last name: ");
            String password = ConsoleUtils.readPassword(sc, "  Password: ");
            User user = buildUserByType(email, firstName, lastName);
            user.setPasswordHash(new PasswordEncoder().encode(password));
            ctx.getUserRepository().save(user);
            saveToRoleRepository(user);
            ConsoleUtils.printSuccess("User #" + user.getId() + " created.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private User buildUserByType(String email, String firstName, String lastName) {
        // Создание разных типов пользователей оставил тут, потому что это чисто админская штука.
        System.out.println("  Type:");
        System.out.println("  1. Basic User");
        System.out.println("  2. Admin");
        System.out.println("  3. Teacher");
        System.out.println("  4. Bachelor Student");
        System.out.println("  5. Master Student");
        System.out.println("  6. PhD Student");
        System.out.println("  7. HR Manager");
        System.out.println("  8. PR Manager");
        System.out.println("  9. Tech Support Specialist");
        int type = ConsoleUtils.readIntInRange(sc, "  Choose: ", 1, 9);

        switch (type) {
            case 2: {
                Admin admin = new Admin(email, firstName, lastName);
                admin.setEmployeeId(ConsoleUtils.readNonBlank(sc, "  Employee ID: "));
                admin.setAccessLevel(AdminLevel.MODERATOR);
                return admin;
            }
            case 3: {
                Teacher teacher = new Teacher(email, firstName, lastName);
                teacher.setEmployeeId(ConsoleUtils.readNonBlank(sc, "  Employee ID: "));
                teacher.setTeacherPosition(readOptionalTeacherPosition());
                return teacher;
            }
            case 4: {
                BachelorStudent student = new BachelorStudent();
                fillStudent(student, email, firstName, lastName);
                return student;
            }
            case 5: {
                MasterStudent student = new MasterStudent();
                fillStudent(student, email, firstName, lastName);
                student.setThesisTopic(ConsoleUtils.readLine(sc, "  Thesis topic (optional): "));
                return student;
            }
            case 6: {
                PHDStudent student = new PHDStudent();
                fillStudent(student, email, firstName, lastName);
                student.setDissertationTopic(ConsoleUtils.readLine(sc, "  Dissertation topic (optional): "));
                return student;
            }
            case 7: {
                HRManager manager = new HRManager(email, firstName, lastName);
                manager.setEmployeeId(ConsoleUtils.readNonBlank(sc, "  Employee ID: "));
                manager.setSpecialization(ConsoleUtils.readLine(sc, "  Specialization (optional): "));
                manager.setLevel(readManagerLevel());
                return manager;
            }
            case 8: {
                PRManager manager = new PRManager(email, firstName, lastName);
                manager.setEmployeeId(ConsoleUtils.readNonBlank(sc, "  Employee ID: "));
                manager.setCampaignFocus(ConsoleUtils.readLine(sc, "  Campaign focus (optional): "));
                manager.setLevel(readManagerLevel());
                return manager;
            }
            case 9: {
                TechSupportSpecialist specialist = new TechSupportSpecialist(email, firstName, lastName);
                specialist.setEmployeeId(ConsoleUtils.readNonBlank(sc, "  Employee ID: "));
                specialist.setSupportArea(ConsoleUtils.readLine(sc, "  Support area (optional): "));
                return specialist;
            }
            default:
                return new User(email, firstName, lastName);
        }
    }

    private void fillStudent(Student student, String email, String firstName, String lastName) {
        student.setEmail(email);
        student.setFullName(firstName, lastName);
        student.setStudentId(ConsoleUtils.readNonBlank(sc, "  Student ID: "));
        student.setYearOfStudy(ConsoleUtils.readIntInRange(sc, "  Year of study: ", 1, 8));
    }

    private ManagerLevel readManagerLevel() {
        ManagerLevel[] values = ManagerLevel.values();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = ConsoleUtils.readIntInRange(sc, "  Manager level: ", 1, values.length);
        return values[choice - 1];
    }

    private void saveToRoleRepository(User user) {
        if (user instanceof Teacher) {
            ctx.getTeacherRepository().save((Teacher) user);
        }
        if (user instanceof Student) {
            ctx.getStudentRepository().save((Student) user);
        }
        if (user instanceof Manager && !(user instanceof Dean)) {
            ctx.getManagerRepository().save((Manager) user);
        }
    }

    private void updateUser() {
        ConsoleUtils.printHeader("Update User");
        try {
            Long userId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  User ID: "));
            String email = ConsoleUtils.readLine(sc, "  New email (optional): ");
            String firstName = ConsoleUtils.readLine(sc, "  New first name (optional): ");
            String lastName = ConsoleUtils.readLine(sc, "  New last name (optional): ");
            String password = ConsoleUtils.readLine(sc, "  New password (optional): ");
            ctx.getUserService().updateUser(userId, new UserUpdateRequest(
                    email.isBlank() ? null : email,
                    firstName.isBlank() ? null : firstName,
                    lastName.isBlank() ? null : lastName,
                    password.isBlank() ? null : password));
            ConsoleUtils.printSuccess("User updated.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
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

    private void showTeacherManagement() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Teacher Management");
            System.out.println("  1. Search teachers");
            System.out.println("  2. Teachers by department");
            System.out.println("  3. Assign teacher to department");
            System.out.println("  4. Update teacher profile");
            System.out.println("  5. Assign PhD supervisor");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 5);
            switch (choice) {
                case 1: searchTeachers(); break;
                case 2: teachersByDepartment(); break;
                case 3: assignTeacherToDepartment(); break;
                case 4: updateTeacherProfile(); break;
                case 5: assignPhDSupervisor(); break;
                case 0: running = false; break;
            }
        }
    }

    private void searchTeachers() {
        ConsoleUtils.printHeader("Search Teachers");
        String keyword = ConsoleUtils.readLine(sc, "  Keyword: ");
        var teachers = ctx.getTeacherService().searchTeachers(keyword);
        if (teachers.isEmpty()) {
            System.out.println("  No teachers found.");
        } else {
            teachers.forEach(this::printTeacher);
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void teachersByDepartment() {
        ConsoleUtils.printHeader("Teachers By Department");
        try {
            Long deptId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Department ID: "));
            var teachers = ctx.getTeacherService().getTeachersByDepartment(deptId);
            if (teachers.isEmpty()) {
                System.out.println("  No teachers found.");
            } else {
                teachers.forEach(this::printTeacher);
            }
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void assignTeacherToDepartment() {
        ConsoleUtils.printHeader("Assign Teacher To Department");
        try {
            String employeeId = ConsoleUtils.readNonBlank(sc, "  Teacher employee ID: ");
            Long deptId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Department ID: "));
            ctx.getTeacherService().assignTeacherToDepartment(employeeId, deptId);
            ConsoleUtils.printSuccess("Teacher assigned.");
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

    private void assignPhDSupervisor() {
        ConsoleUtils.printHeader("Assign PhD Supervisor");
        try {
            String phdStudentId = ConsoleUtils.readNonBlank(sc, "  PhD Student ID: ");
            Long teacherId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Supervisor teacher user ID: "));
            ctx.getPhdSupervisorService().assignSupervisor(phdStudentId, teacherId);
            ConsoleUtils.printSuccess("Supervisor assigned.");
        } catch (SupervisorHIndexException ex) {
            ConsoleUtils.printError("Supervisor h-index check failed: " + ex.getMessage());
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

    private void printTeacher(Teacher teacher) {
        System.out.printf("  #%-3d %-25s employee=%s department=%s position=%s%n",
                teacher.getId(),
                teacher.getFullName(),
                teacher.getEmployeeId(),
                teacher.getDepartment() != null ? teacher.getDepartment().getName() : "-",
                teacher.getTeacherPosition());
    }

    private void showResearchReview() {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Research Review");
            System.out.println("  1. List papers by status");
            System.out.println("  2. Publish paper");
            System.out.println("  3. Reject paper");
            System.out.println("  4. Cite paper");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 4);
            switch (choice) {
                case 1: listPapersByStatus(); break;
                case 2: publishPaper(); break;
                case 3: rejectPaper(); break;
                case 4: citePaper(); break;
                case 0: running = false; break;
            }
        }
    }

    private void listPapersByStatus() {
        ConsoleUtils.printHeader("Papers By Status");
        ResearchStatus status = readResearchStatus();
        var papers = ctx.getResearchService().searchPapers(
                null,
                new PaperSearchFilters().withStatus(status));
        if (papers.isEmpty()) {
            System.out.println("  No papers found.");
        } else {
            papers.forEach(p -> System.out.printf("  #%-3d %-35s status=%s citations=%d%n",
                    p.getId(), p.getTitle(), p.getStatus(), p.getCitationCount()));
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void publishPaper() {
        ConsoleUtils.printHeader("Publish Paper");
        try {
            Long paperId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Paper ID: "));
            String doi = ConsoleUtils.readNonBlank(sc, "  DOI: ");
            String fileUrl = ConsoleUtils.readNonBlank(sc, "  File URL: ");
            ctx.getResearchService().publishPaper(paperId, doi, fileUrl);
            ConsoleUtils.printSuccess("Paper published.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void rejectPaper() {
        ConsoleUtils.printHeader("Reject Paper");
        try {
            Long paperId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Paper ID: "));
            String reason = ConsoleUtils.readNonBlank(sc, "  Reason: ");
            ctx.getResearchService().rejectPaper(paperId, reason);
            ConsoleUtils.printSuccess("Paper rejected.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void citePaper() {
        ConsoleUtils.printHeader("Cite Paper");
        try {
            Long paperId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Paper ID: "));
            ctx.getResearchService().citePaper(paperId);
            ConsoleUtils.printSuccess("Citation added.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private ResearchStatus readResearchStatus() {
        ResearchStatus[] values = ResearchStatus.values();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = ConsoleUtils.readIntInRange(sc, "  Status: ", 1, values.length);
        return values[choice - 1];
    }

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
