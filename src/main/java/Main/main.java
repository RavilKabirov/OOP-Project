import java.util.*;

public class main {

    public static void main(String[] args) {

        UserRepository  userRepo    = new UserRepository();
        PasswordEncoder encoder     = new PasswordEncoder();
        AuthService     authService = new AuthService(userRepo, encoder);
        if (userRepo.getUsers().isEmpty()) {
            User admin = authService.register(
                    "admin@admin.com", "admin", "Admin", "Default", UserRole.ADMIN);
            System.out.println("Created admin: " + admin.getEmail() + " / password: admin");
        }

        System.out.println("\n========== Enrollment Service Tests ==========\n");
        runEnrollmentTests();

        System.out.println("\n========== Mark Service Tests ==========\n");
        runMarkTests();

        System.out.println("\n========== Transcript Service Tests ==========\n");
        runTranscriptTests();

        System.out.println("\n========== All Tests Complete ==========\n");

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
                    case 1: handleLogin(scanner, authService); break;
                    case 0: running = false; break;
                    default: System.out.println("Incorrect input.");
                }
            } else {
                User current = AuthContext.getCurrentUser();
                System.out.println("\n--- Cabinet (" + current.getEmail() + ") ---");
                System.out.println("1. Change password");
                System.out.println("2. View profile");
                if (AuthContext.isAdmin()) System.out.println("3. Register new user");
                System.out.println("0. Sign out");
                System.out.print("Choose option: ");
                int choice = readInt(scanner);
                scanner.nextLine();
                switch (choice) {
                    case 1: handleChangePassword(scanner, authService, current); break;
                    case 2: showProfile(current); break;
                    case 3:
                        if (AuthContext.isAdmin()) handleRegistration(scanner, authService);
                        else System.out.println("Access denied.");
                        break;
                    case 0:
                        AuthContext.logout();
                        System.out.println("You are signed out.");
                        break;
                    default: System.out.println("Incorrect input.");
                }
            }
        }

        System.out.println("Program ended.");
        scanner.close();
    }


    private static void runEnrollmentTests() {
        StudentRepository    studentRepo   = new StudentRepository();
        CourseRepository     courseRepo    = new CourseRepository();
        EnrollmentRepository enrollRepo    = new EnrollmentRepository();
        EnrollmentService    enrollService =
                new EnrollmentService(enrollRepo, studentRepo, courseRepo);

        Student alice = new Student() {};
        alice.setStudentId("S001"); alice.setFullName("Alice", "Smith");
        alice.setEmail("alice@uni.edu");
        studentRepo.save(alice);

        Student bob = new Student() {};
        bob.setStudentId("S002"); bob.setFullName("Bob", "Jones");
        bob.setEmail("bob@uni.edu");
        studentRepo.save(bob);

        Course math = new Course();
        math.setCourseId("MATH101"); math.setName("Calculus I"); math.setCredits(4);
        courseRepo.save(math);

        Course cs = new Course();
        cs.setCourseId("CS101"); cs.setName("Intro to Programming"); cs.setCredits(3);
        courseRepo.save(cs);

        System.out.println("--- TEST 1: Enroll students ---");
        Enrollment e1 = enrollService.enrollStudent("S001", "MATH101");
        Enrollment e2 = enrollService.enrollStudent("S001", "CS101");
        Enrollment e3 = enrollService.enrollStudent("S002", "MATH101");
        System.out.println("  e1: " + e1);
        System.out.println("  e2: " + e2);
        System.out.println("  e3: " + e3);

        System.out.println("\n--- TEST 2: isStudentEnrolled ---");
        System.out.println("  Alice in MATH101: " + enrollService.isStudentEnrolled("S001", "MATH101"));
        System.out.println("  Bob   in CS101:   " + enrollService.isStudentEnrolled("S002", "CS101"));

        System.out.println("\n--- TEST 3: Alice's enrollments ---");
        enrollService.getStudentEnrollments("S001")
                     .forEach(e -> System.out.println("  " + e));

        System.out.println("\n--- TEST 4: MATH101 roster ---");
        enrollService.getCourseRoster("MATH101")
                     .forEach(e -> System.out.println("  " + e));

        System.out.println("\n--- TEST 5: Complete Alice's MATH101 ---");
        enrollService.completeCourse(e1.getId(), 30, 22, 35.5);
        System.out.println("  Status: " + enrollService.getEnrollmentStatus("S001", "MATH101"));

        System.out.println("\n--- TEST 6: Bob drops MATH101 ---");
        enrollService.dropCourse("S002", "MATH101");
        System.out.println("  Bob status: " + enrollService.getEnrollmentStatus("S002", "MATH101"));

        System.out.println("\n--- TEST 7: Duplicate enrollment guard ---");
        try {
            enrollService.enrollStudent("S001", "CS101");
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalStateException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        System.out.println("\n--- TEST 8: Invalid grade guard ---");
        try {
            enrollService.completeCourse(e2.getId(), 30, 30, 90);
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }
    }

 
    private static void runMarkTests() {
        StudentRepository    studentRepo   = new StudentRepository();
        CourseRepository     courseRepo    = new CourseRepository();
        EnrollmentRepository enrollRepo    = new EnrollmentRepository();
        TeacherRepository    teacherRepo   = new TeacherRepository();
        MarkRepository       markRepo      = new MarkRepository();

        EnrollmentService enrollService =
                new EnrollmentService(enrollRepo, studentRepo, courseRepo);
        MarkService markService =
                new MarkService(markRepo, enrollRepo, teacherRepo);

        Student alice = new Student() {};
        alice.setId(1L); alice.setStudentId("S001"); alice.setFullName("Alice", "Smith");
        studentRepo.save(alice);

        Student bob = new Student() {};
        bob.setId(2L); bob.setStudentId("S002"); bob.setFullName("Bob", "Jones");
        studentRepo.save(bob);

        Course math = new Course();
        math.setCourseId("MATH101"); math.setName("Calculus I"); math.setCredits(4);
        courseRepo.save(math);

        Teacher prof = new Teacher("asd@mail.com", "Dr.", "Newton") {};
        prof.setId(10L); prof.setEmployeeId("T001"); 
        prof.addCourse(math);
        teacherRepo.save(prof);

        Enrollment eAlice = enrollService.enrollStudent("S001", "MATH101");
        Enrollment eBob   = enrollService.enrollStudent("S002", "MATH101");

        System.out.println("--- TEST 1: Assign mark components for Alice ---");
        Mark aliceMark = markService.assignMark(10L, "S001", "MATH101",
                30.0, MarkType.FIRST_ATTESTATION, "Midterm 1");
        markService.assignMark(10L, "S001", "MATH101",
                28.5, MarkType.SECOND_ATTESTATION, "Midterm 2");
        markService.assignMark(10L, "S001", "MATH101",
                35.0, MarkType.FINAL_EXAM, "Final");
        System.out.println("  Alice's mark: " + aliceMark);

        System.out.println("\n--- TEST 2: Assign marks for Bob ---");
        Mark bobMark = markService.assignMark(10L, "S002", "MATH101",
                20.0, MarkType.FIRST_ATTESTATION, null);
        markService.assignMark(10L, "S002", "MATH101",
                22.0, MarkType.SECOND_ATTESTATION, null);
        markService.assignMark(10L, "S002", "MATH101",
                25.0, MarkType.FINAL_EXAM, null);
        System.out.println("  Bob's mark: " + bobMark);

        System.out.println("\n--- TEST 3: calculateFinalGrade ---");
        System.out.printf("  Alice: %.2f (expect 93.50)%n",
                markService.calculateFinalGrade("S001", "MATH101"));
        System.out.printf("  Bob:   %.2f (expect 67.00)%n",
                markService.calculateFinalGrade("S002", "MATH101"));

        System.out.println("\n--- TEST 4: getStudentMarks ---");
        markService.getStudentMarks("S001", "MATH101")
                   .forEach(m -> System.out.println("  " + m));

        System.out.println("\n--- TEST 5: getCourseMarks ---");
        markService.getCourseMarks("MATH101")
                   .forEach(m -> System.out.println("  " + m));

        System.out.println("\n--- TEST 6: getMarkStatistics ---");
        System.out.println("  " + markService.getMarkStatistics("MATH101"));

        System.out.println("\n--- TEST 7: updateMark via MarkUpdateRequest ---");
        markService.updateMark(eAlice.getId(),
                new MarkUpdateRequest(MarkType.FINAL_EXAM, 40.0, "Appeal correction"));
        System.out.printf("  Alice new total: %.2f (expect 98.50)%n",
                markService.calculateFinalGrade("S001", "MATH101"));

        System.out.println("\n--- TEST 8: deleteMark ---");
        markService.deleteMark(eBob.getId());
        System.out.println("  Bob's marks after delete (expect []): "
                + markService.getStudentMarks("S002", "MATH101"));

        System.out.println("\n--- TEST 9: Unauthorised teacher guard ---");
        Teacher impostor = new Teacher("fraud@gm.com", "Dr.", "Fraud") {};
        impostor.setId(99L); impostor.setEmployeeId("T999");
        teacherRepo.save(impostor);
        try {
            markService.assignMark(99L, "S001", "MATH101",
                    50.0, MarkType.FIRST_ATTESTATION, null);
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        System.out.println("\n--- TEST 10: MarkUpdateRequest score out of range ---");
        try {
            new MarkUpdateRequest(MarkType.FINAL_EXAM, 110.0);
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        System.out.println("\n--- TEST 11: updateMark on missing mark ---");
        try {
            markService.updateMark(9999L,
                    new MarkUpdateRequest(MarkType.FIRST_ATTESTATION, 10.0));
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }
    }

    private static void runTranscriptTests() {

        StudentRepository    studentRepo   = new StudentRepository();
        CourseRepository     courseRepo    = new CourseRepository();
        EnrollmentRepository enrollRepo    = new EnrollmentRepository();
        TeacherRepository    teacherRepo   = new TeacherRepository();
        MarkRepository       markRepo      = new MarkRepository();

        EnrollmentService  enrollService  =
                new EnrollmentService(enrollRepo, studentRepo, courseRepo);
        MarkService        markService    =
                new MarkService(markRepo, enrollRepo, teacherRepo);
        TranscriptService  transcriptService =
                new TranscriptService(enrollRepo, markRepo, studentRepo);

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

        Course physics = new Course();
        physics.setCourseId("PHYS101");
        physics.setName("Physics I");
        physics.setCredits(3);
        courseRepo.save(physics);

        Course cs = new Course();
        cs.setCourseId("CS101");
        cs.setName("Intro to Programming");
        cs.setCredits(3);
        courseRepo.save(cs);

        Teacher prof = new Teacher("asdds", "Dr.", "Pablo") {};
        prof.setId(10L);
        prof.setEmployeeId("T001");
        prof.addCourse(math);
        prof.addCourse(physics);
        prof.addCourse(cs);
        teacherRepo.save(prof);

        Enrollment eAliceMath    = enrollService.enrollStudent("S001", "MATH101");
        Enrollment eAlicePhysics = enrollService.enrollStudent("S001", "PHYS101");
        Enrollment eAliceCS      = enrollService.enrollStudent("S001", "CS101");
        Enrollment eBobMath      = enrollService.enrollStudent("S002", "MATH101");

        markService.assignMark(10L, "S001", "MATH101", 28.0, MarkType.FIRST_ATTESTATION, null);
        markService.assignMark(10L, "S001", "MATH101", 30.0, MarkType.SECOND_ATTESTATION, null);
        markService.assignMark(10L, "S001", "MATH101", 32.0, MarkType.FINAL_EXAM, null);
        enrollService.completeCourse(eAliceMath.getId(), 25, 25, 40);

        markService.assignMark(10L, "S001", "PHYS101", 20.0, MarkType.FIRST_ATTESTATION, null);
        markService.assignMark(10L, "S001", "PHYS101", 22.0, MarkType.SECOND_ATTESTATION, null);
        markService.assignMark(10L, "S001", "PHYS101", 28.0, MarkType.FINAL_EXAM, null);
        enrollService.completeCourse(eAlicePhysics.getId(), 15, 23, 32);

        markService.assignMark(10L, "S002", "MATH101", 15.0, MarkType.FIRST_ATTESTATION, null);
        markService.assignMark(10L, "S002", "MATH101", 18.0, MarkType.SECOND_ATTESTATION, null);
        markService.assignMark(10L, "S002", "MATH101", 20.0, MarkType.FINAL_EXAM, null);
        // ── TEST 1: generateTranscript ────────────────────────────────────────
        System.out.println("--- TEST 1: generateTranscript for Alice ---");
        Transcript aliceTranscript = transcriptService.generateTranscript("S001");
        System.out.println("  " + aliceTranscript);
        System.out.println("  Enrollments in transcript: "
                + aliceTranscript.getEnrollments().size() + " (expect 3)");

        // ── TEST 2: generateReport prints to stdout ───────────────────────────
        System.out.println("\n--- TEST 2: generateReport ---");
        aliceTranscript.generateReport();

        // ── TEST 3: getCompletedCourses ───────────────────────────────────────
        System.out.println("\n--- TEST 3: getCompletedCourses for Alice ---");
        List<Enrollment> aliceCompleted = transcriptService.getCompletedCourses("S001");
        System.out.println("  Completed count: " + aliceCompleted.size() + " (expect 2)");
        aliceCompleted.forEach(e ->
                System.out.println("  - " + e.getCourse().getName()
                        + " [" + e.getStatus() + "]"));

        // ── TEST 4: getCompletedCourses for Bob (none completed) ──────────────
        System.out.println("\n--- TEST 4: getCompletedCourses for Bob (none) ---");
        List<Enrollment> bobCompleted = transcriptService.getCompletedCourses("S002");
        System.out.println("  Bob completed count: " + bobCompleted.size() + " (expect 0)");

        // ── TEST 5: calculateGPA – Alice has two graded enrollments ───────────
        // GPA = average of mark.calculateTotal():  (90 + 70) / 2 = 80.0
        System.out.println("\n--- TEST 5: calculateGPA for Alice ---");
        double aliceGPA = transcriptService.calculateGPA("S001");
        System.out.printf("  Alice GPA: %.2f (expect 80.00)%n", aliceGPA);

        // ── TEST 6: calculateGPA – Bob active, not yet graded-as-complete ─────
        // Bob has a mark object (active enrollment), so GPA = average of totals = 53.0
        System.out.println("\n--- TEST 6: calculateGPA for Bob ---");
        double bobGPA = transcriptService.calculateGPA("S002");
        System.out.printf("  Bob GPA: %.2f (expect 53.00)%n", bobGPA);

        // ── TEST 7: getTotalCreditsEarned – Alice: MATH(4) + PHYS(3) = 7 ─────
        System.out.println("\n--- TEST 7: getTotalCreditsEarned for Alice ---");
        int aliceCredits = transcriptService.getTotalCreditsEarned("S001");
        System.out.println("  Alice credits: " + aliceCredits + " (expect 7)");

        // ── TEST 8: getTotalCreditsEarned – Bob has no COMPLETED enrollments ──
        System.out.println("\n--- TEST 8: getTotalCreditsEarned for Bob ---");
        int bobCredits = transcriptService.getTotalCreditsEarned("S002");
        System.out.println("  Bob credits: " + bobCredits + " (expect 0)");

        // ── TEST 9: generateTranscript for Bob ────────────────────────────────
        System.out.println("\n--- TEST 9: generateTranscript for Bob ---");
        Transcript bobTranscript = transcriptService.generateTranscript("S002");
        System.out.println("  " + bobTranscript);
        System.out.println("  Enrollments: " + bobTranscript.getEnrollments().size()
                + " (expect 1)");

        // ── TEST 10: student not found guard ──────────────────────────────────
        System.out.println("\n--- TEST 10: generateTranscript with unknown id ---");
        try {
            transcriptService.generateTranscript("S999");
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 11: getCompletedCourses with unknown id ───────────────────────
        System.out.println("\n--- TEST 11: getCompletedCourses with unknown id ---");
        try {
            transcriptService.getCompletedCourses("S999");
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 12: student with zero enrollments ────────────────────────────
        System.out.println("\n--- TEST 12: Transcript for student with no enrollments ---");
        Student newStudent = new Student() {};
        newStudent.setStudentId("S003");
        newStudent.setFullName("Charlie", "Brown");
        studentRepo.save(newStudent);

        Transcript emptyTranscript = transcriptService.generateTranscript("S003");
        System.out.println("  Enrollments: " + emptyTranscript.getEnrollments().size()
                + " (expect 0)");
        System.out.printf("  GPA: %.2f (expect 0.00)%n", emptyTranscript.calculateGPA());
        System.out.println("  Credits: " + transcriptService.getTotalCreditsEarned("S003")
                + " (expect 0)");
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
        System.out.print("Email: ");        String email     = scanner.nextLine().trim();
        System.out.print("Password: ");     String password  = scanner.nextLine().trim();
        System.out.print("First Name: ");   String firstName = scanner.nextLine().trim();
        System.out.print("Last Name: ");    String lastName  = scanner.nextLine().trim();
        System.out.print("Role (USER or ADMIN): ");
        String roleStr = scanner.nextLine().trim().toUpperCase();
        UserRole role = roleStr.equals("ADMIN") ? UserRole.ADMIN : UserRole.USER;
        try {
            User newUser = authService.register(
                    email, password, firstName, lastName, role);
            System.out.println("User created: " + newUser.getEmail()
                    + " (ID=" + newUser.getId() + ")");
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }

    private static void handleChangePassword(Scanner scanner,
                                              AuthService authService, User user) {
        System.out.print("Old password: "); String oldPass = scanner.nextLine().trim();
        System.out.print("New password: "); String newPass = scanner.nextLine().trim();
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
        System.out.println("Name:  " + user.getFullName());
        System.out.println("Role:  " + (user instanceof Admin ? "Admin" : "User"));
    }
}
