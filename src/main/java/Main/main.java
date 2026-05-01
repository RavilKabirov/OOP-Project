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
