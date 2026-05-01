import java.util.*;

public class main {

    public static void main(String[] args) {
        UserRepository userRepo = new UserRepository();
        PasswordEncoder encoder = new PasswordEncoder();
        AuthService authService = new AuthService(userRepo, encoder);
        if (userRepo.getUsers().isEmpty()) {
            User admin = authService.register("admin@admin.com", "admin", "Admin", "Default", UserRole.ADMIN);
            System.out.println("Created admin: " + admin.getEmail() + " / password: admin");
        }

        System.out.println("\n========== Enrollment Service Tests ==========\n");
        runEnrollmentTests();
        System.out.println("\n========== End of Enrollment Tests ==========\n");

        System.out.println("\n========== Mark Service Tests ==========\n");
        runMarkTests();
        System.out.println("\n========== End of Mark Tests ==========\n");


        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        System.out.println("=== University system ===");

        while (running) {
            if (AuthContext.getCurrentUser() == null) {
                System.out.println("\n--- Main menu ---");
                System.out.println("1. Sign in");
                System.out.println("0. Exit");
                System.out.print("Choose option: ");

                int choice = readInt(scanner);
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        handleLogin(scanner, authService);
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Incorrect input.");
                }
            } else {
                User current = AuthContext.getCurrentUser();
                System.out.println("\n--- Cabinet (" + current.getEmail() + ") ---");
                System.out.println("1. Change password");
                System.out.println("2. View profile");
                if (AuthContext.isAdmin()) {
                    System.out.println("3. Register new user");
                }
                System.out.println("0. Sign out");
                System.out.print("Choose option: ");

                int choice = readInt(scanner);
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        handleChangePassword(scanner, authService, current);
                        break;
                    case 2:
                        showProfile(current);
                        break;
                    case 3:
                        if (AuthContext.isAdmin()) {
                            handleRegistration(scanner, authService);
                        } else {
                            System.out.println("Access denied.");
                        }
                        break;
                    case 0:
                        AuthContext.logout();
                        System.out.println("You are signed out.");
                        break;
                    default:
                        System.out.println("Incorrect input.");
                }
            }
        }

        System.out.println("Program ended.");
        scanner.close();
    }

  
    private static void runEnrollmentTests() {

        StudentRepository studentRepo = new StudentRepository();
        CourseRepository  courseRepo  = new CourseRepository();
        EnrollmentRepository enrollRepo = new EnrollmentRepository();
        EnrollmentService enrollService =
                new EnrollmentService(enrollRepo, studentRepo, courseRepo);

        Student alice = new Student() {};
        alice.setStudentId("S001");
        alice.setFullName("Alice", "Smith");
        alice.setEmail("alice@uni.edu");
        studentRepo.save(alice);

        Student bob = new Student() {};
        bob.setStudentId("S002");
        bob.setFullName("Bob", "Jones");
        bob.setEmail("bob@uni.edu");
        studentRepo.save(bob);

        Course math = new Course();
        math.setCourseId("MATH101");
        math.setName("Calculus I");
        math.setCredits(4);
        courseRepo.save(math);

        Course cs = new Course();
        cs.setCourseId("CS101");
        cs.setName("Intro to Programming");
        cs.setCredits(3);
        courseRepo.save(cs);

        // ── TEST 1: Enroll students ─────────────────────────────────────────
        System.out.println("--- TEST 1: Enroll students ---");
        Enrollment e1 = enrollService.enrollStudent("S001", "MATH101");
        Enrollment e2 = enrollService.enrollStudent("S001", "CS101");
        Enrollment e3 = enrollService.enrollStudent("S002", "MATH101");
        System.out.println("  e1: " + e1);
        System.out.println("  e2: " + e2);
        System.out.println("  e3: " + e3);

        // ── TEST 2: isStudentEnrolled ───────────────────────────────────────
        System.out.println("\n--- TEST 2: isStudentEnrolled ---");
        System.out.println("  Alice enrolled in MATH101: "
                + enrollService.isStudentEnrolled("S001", "MATH101")); // true
        System.out.println("  Bob enrolled in CS101:     "
                + enrollService.isStudentEnrolled("S002", "CS101"));   // false

        // ── TEST 3: getStudentEnrollments ───────────────────────────────────
        System.out.println("\n--- TEST 3: Alice's enrollments ---");
        List<Enrollment> aliceEnrollments = enrollService.getStudentEnrollments("S001");
        aliceEnrollments.forEach(e -> System.out.println("  " + e));

        // ── TEST 4: getCourseRoster ─────────────────────────────────────────
        System.out.println("\n--- TEST 4: MATH101 roster ---");
        List<Enrollment> roster = enrollService.getCourseRoster("MATH101");
        roster.forEach(e -> System.out.println("  " + e));

        // ── TEST 5: completeCourse ──────────────────────────────────────────
        System.out.println("\n--- TEST 5: Complete Alice's MATH101 with grade 87.5 ---");
        enrollService.completeCourse(e1.getId(), 30, 22, 32);
        System.out.println("  Status after completion: "
                + enrollService.getEnrollmentStatus("S001", "MATH101")); // COMPLETED

        // ── TEST 6: dropCourse ──────────────────────────────────────────────
        System.out.println("\n--- TEST 6: Bob drops MATH101 ---");
        enrollService.dropCourse("S002", "MATH101");
        System.out.println("  Bob's status in MATH101: "
                + enrollService.getEnrollmentStatus("S002", "MATH101")); // DROPPED
        System.out.println("  MATH101 roster size after drop: "
                + enrollService.getCourseRoster("MATH101").size());       // 0

        // ── TEST 7: Duplicate enrollment guard ──────────────────────────────
        System.out.println("\n--- TEST 7: Duplicate enrollment guard ---");
        try {
            enrollService.enrollStudent("S001", "CS101"); // already ACTIVE
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalStateException ex) {
            System.out.println("  Correctly rejected duplicate: " + ex.getMessage());
        }

        // ── TEST 8: Invalid grade guard ─────────────────────────────────────
        System.out.println("\n--- TEST 8: Invalid grade guard ---");
        try {
            enrollService.completeCourse(e2.getId(), 30, 30, 45);
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected grade: " + ex.getMessage());
        }
    }

    private static void runMarkTests() {

        // -- Repositories & services -----------------------------------------
        StudentRepository    studentRepo   = new StudentRepository();
        CourseRepository     courseRepo    = new CourseRepository();
        EnrollmentRepository enrollRepo    = new EnrollmentRepository();
        TeacherRepository    teacherRepo   = new TeacherRepository();
        MarkRepository       markRepo      = new MarkRepository();

        EnrollmentService enrollService =
                new EnrollmentService(enrollRepo, studentRepo, courseRepo);
        MarkService markService =
                new MarkService(markRepo, enrollRepo, teacherRepo);

        // -- Students --------------------------------------------------------
        Student alice = new Student() {};
        alice.setId(1L);
        alice.setStudentId("S001");
        alice.setFullName("Alice", "Smith");
        studentRepo.save(alice);

        Student bob = new Student() {};
        bob.setId(2L);
        bob.setStudentId("S002");
        bob.setFullName("Bob", "Jones");
        studentRepo.save(bob);

        // -- Course ----------------------------------------------------------
        Course math = new Course();
        math.setCourseId("MATH101");
        math.setName("Calculus I");
        math.setCredits(4);
        courseRepo.save(math);

        // -- Teacher (Teacher is abstract via Employee; use anonymous subclass)
     
        Teacher teacher = new Teacher("newton@university.edu", "Isaac", "Newton");
        teacher.setId(10L);                  
        teacher.setEmployeeId("T001");        
        teacherRepo.save(teacher);          

        
        Course math1 = courseRepo.findByCourseId("MATH101").orElseThrow();
        teacher.addCourse(math1);
        teacherRepo.save(teacher);            

        Enrollment eAlice = enrollService.enrollStudent("S001", "MATH101");
        Enrollment eBob   = enrollService.enrollStudent("S002", "MATH101");
        // ── TEST 1: assign all three components for Alice ────────────────────
        System.out.println("--- TEST 1: Assign mark components for Alice ---");
        Mark aliceMark = markService.assignMark(
                10L, "S001", "MATH101", 30.0, MarkType.FIRST_ATTESTATION, "Midterm 1");
        System.out.println("  After 1st attestation : " + aliceMark);

        markService.assignMark(
                10L, "S001", "MATH101", 28.5, MarkType.SECOND_ATTESTATION, "Midterm 2");
        System.out.println("  After 2nd attestation : " + aliceMark);

        markService.assignMark(
                10L, "S001", "MATH101", 35.0, MarkType.FINAL_EXAM, "Final exam");
        System.out.println("  After final exam      : " + aliceMark);

        // ── TEST 2: assign marks for Bob ─────────────────────────────────────
        System.out.println("\n--- TEST 2: Assign marks for Bob ---");
        Mark bobMark = markService.assignMark(
                10L, "S002", "MATH101", 20.0, MarkType.FIRST_ATTESTATION, null);
        markService.assignMark(
                10L, "S002", "MATH101", 22.0, MarkType.SECOND_ATTESTATION, null);
        markService.assignMark(
                10L, "S002", "MATH101", 25.0, MarkType.FINAL_EXAM, null);
        System.out.println("  Bob's mark: " + bobMark);

        // ── TEST 3: calculateFinalGrade ──────────────────────────────────────
        System.out.println("\n--- TEST 3: calculateFinalGrade ---");
        System.out.printf("  Alice total: %.2f  (expect 93.50)%n",
                markService.calculateFinalGrade("S001", "MATH101"));
        System.out.printf("  Bob   total: %.2f  (expect 67.00)%n",
                markService.calculateFinalGrade("S002", "MATH101"));

        // ── TEST 4: getStudentMarks ──────────────────────────────────────────
        System.out.println("\n--- TEST 4: getStudentMarks for Alice ---");
        markService.getStudentMarks("S001", "MATH101")
                   .forEach(m -> System.out.println("  " + m));

        // ── TEST 5: getCourseMarks ───────────────────────────────────────────
        System.out.println("\n--- TEST 5: getCourseMarks for MATH101 ---");
        markService.getCourseMarks("MATH101")
                   .forEach(m -> System.out.println("  " + m));

        // ── TEST 6: getMarkStatistics ────────────────────────────────────────
        System.out.println("\n--- TEST 6: getMarkStatistics ---");
        MarkStatistics stats = markService.getMarkStatistics("MATH101");
        System.out.println("  " + stats);

        // ── TEST 7: updateMark via MarkUpdateRequest ─────────────────────────
        System.out.println("\n--- TEST 7: updateMark with MarkUpdateRequest ---");
        MarkUpdateRequest req = new MarkUpdateRequest(
                MarkType.FINAL_EXAM, 40.0, "Grade corrected after appeal");
        markService.updateMark(eAlice.getId(), req);
        System.out.printf("  Alice new total: %.2f  (expect 98.50)%n",
                markService.calculateFinalGrade("S001", "MATH101"));

        // ── TEST 8: deleteMark ───────────────────────────────────────────────
        System.out.println("\n--- TEST 8: deleteMark ---");
        markService.deleteMark(eBob.getId());
        List<Mark> bobAfterDelete = markService.getStudentMarks("S002", "MATH101");
        System.out.println("  Bob's marks after delete (expect []): " + bobAfterDelete);

        // ── TEST 9: unauthorised teacher guard ───────────────────────────────
        System.out.println("\n--- TEST 9: Unauthorised teacher guard ---");
        Teacher impostor = new Teacher("asdd@mail.com", "Dr.", "Fraud") ;
        impostor.setId(99L);
        impostor.setEmployeeId("T999");
        teacherRepo.save(impostor);
        try {
            markService.assignMark(99L, "S001", "MATH101",
                    50.0, MarkType.FIRST_ATTESTATION, null);
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 10: MarkUpdateRequest rejects score > 100 ───────────────────
        System.out.println("\n--- TEST 10: MarkUpdateRequest score out of range ---");
        try {
            new MarkUpdateRequest(MarkType.FINAL_EXAM, 110.0);
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 11: updateMark on non-existent enrollment ───────────────────
        System.out.println("\n--- TEST 11: updateMark on missing mark ---");
        try {
            markService.updateMark(9999L,
                    new MarkUpdateRequest(MarkType.FIRST_ATTESTATION, 10.0));
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }
    }




    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Type a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void handleLogin(Scanner scanner, AuthService authService) {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        try {
            User user = authService.login(email, password);
            AuthContext.setCurrentUser(user);
            System.out.println("Welcome, " + user.getFullName() + "!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleRegistration(Scanner scanner, AuthService authService) {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Role (USER or ADMIN): ");
        String roleStr = scanner.nextLine().trim().toUpperCase();
        UserRole role = roleStr.equals("ADMIN") ? UserRole.ADMIN : UserRole.USER;
        try {
            User newUser = authService.register(email, password, firstName, lastName, role);
            System.out.println("User created: " + newUser.getEmail() + " (ID=" + newUser.getId() + ")");
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }

    private static void handleChangePassword(Scanner scanner, AuthService authService, User user) {
        System.out.print("Old password: ");
        String oldPass = scanner.nextLine().trim();
        System.out.print("New password: ");
        String newPass = scanner.nextLine().trim();
        try {
            authService.changePassword(user.getId(), oldPass, newPass);
            System.out.println("Password changed successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showProfile(User user) {
        System.out.println("\n--- Profile ---");
        System.out.println("Email: " + user.getEmail());
        System.out.println("Name: " + user.getFullName());
        System.out.println("Role: " + (user instanceof Admin ? "Admin" : "User"));
    }
}
