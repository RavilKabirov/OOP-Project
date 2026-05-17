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
        
        System.out.println("\n========== Faculty Service Tests ============\n");
        runFacultyTests();
        
        System.out.println("\n========== User Service Tests ============\n");
        runUserServiceTests();
        
        System.out.println("\n========== Teacher Service Tests ============\n");
        runTeacherTests();
        
        System.out.println("\n========== Complaint Service Tests ============\n");
        runComplaintTests();
        
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

    private static void runFacultyTests() {

        // ── Setup ─────────────────────────────────────────────────────────────
        FacultyRepository    facultyRepo  = new FacultyRepository();
        DepartmentRepository deptRepo     = new DepartmentRepository();
        TeacherRepository    teacherRepo  = new TeacherRepository();
        FacultyService       facultyService =
                new FacultyService(facultyRepo, deptRepo, teacherRepo);

        // ── TEST 1: createFaculty ─────────────────────────────────────────────
        System.out.println("--- TEST 1: createFaculty ---");
        School engineering = facultyService.createFaculty(
                new FacultyCreationRequest("School of Engineering"));
        School science = facultyService.createFaculty(
                new FacultyCreationRequest("School of Science"));
        School arts = facultyService.createFaculty(
                new FacultyCreationRequest("School of Arts"));
        System.out.println("  Created: " + engineering.getName() + " (id=" + engineering.getId() + ")");
        System.out.println("  Created: " + science.getName()     + " (id=" + science.getId()     + ")");
        System.out.println("  Created: " + arts.getName()        + " (id=" + arts.getId()        + ")");

        // ── TEST 2: getAllFaculties ────────────────────────────────────────────
        System.out.println("\n--- TEST 2: getAllFaculties ---");
        List<School> all = facultyService.getAllFaculties();
        System.out.println("  Total schools: " + all.size() + " (expect 3)");
        all.forEach(s -> System.out.println("  - " + s.getName()));

        // ── TEST 3: getFacultyById ────────────────────────────────────────────
        System.out.println("\n--- TEST 3: getFacultyById ---");
        facultyService.getFacultyById(engineering.getId())
                .ifPresent(s -> System.out.println("  Found: " + s.getName()));
        System.out.println("  Missing id returns empty: "
                + facultyService.getFacultyById(999L).isEmpty() + " (expect true)");

        // ── TEST 4: updateFaculty ─────────────────────────────────────────────
        System.out.println("\n--- TEST 4: updateFaculty ---");
        School updated = facultyService.updateFaculty(
                arts.getId(), new FacultyUpdateRequest("School of Arts & Humanities"));
        System.out.println("  Updated name: " + updated.getName()
                + " (expect 'School of Arts & Humanities')");

        // ── TEST 5: searchFaculties ───────────────────────────────────────────
        System.out.println("\n--- TEST 5: searchFaculties ---");
        List<School> hits = facultyService.searchFaculties("science");
        System.out.println("  Search 'science' hits: " + hits.size() + " (expect 1)");
        hits.forEach(s -> System.out.println("  - " + s.getName()));

        List<School> allHits = facultyService.searchFaculties("school");
        System.out.println("  Search 'school' hits: " + allHits.size() + " (expect 3)");

        // ── TEST 6: addDepartmentToFaculty ────────────────────────────────────
        System.out.println("\n--- TEST 6: addDepartmentToFaculty ---");
        Department cs = new Department();
        cs.setName("Computer Science");
        cs.setCode("CS");
        facultyService.addDepartmentToFaculty(engineering.getId(), cs);

        Department ee = new Department();
        ee.setName("Electrical Engineering");
        ee.setCode("EE");
        facultyService.addDepartmentToFaculty(engineering.getId(), ee);

        Department math = new Department();
        math.setName("Mathematics");
        math.setCode("MATH");
        facultyService.addDepartmentToFaculty(science.getId(), math);

        System.out.println("  Engineering departments saved.");

        // ── TEST 7: getFacultyDepartments ─────────────────────────────────────
        System.out.println("\n--- TEST 7: getFacultyDepartments ---");
        List<Department> engDepts = facultyService.getFacultyDepartments(engineering.getId());
        System.out.println("  Engineering dept count: " + engDepts.size() + " (expect 2)");
        engDepts.forEach(d -> System.out.println("  - [" + d.getCode() + "] " + d.getName()));

        List<Department> sciDepts = facultyService.getFacultyDepartments(science.getId());
        System.out.println("  Science dept count: " + sciDepts.size() + " (expect 1)");

        // ── TEST 8: assignDean ────────────────────────────────────────────────
        System.out.println("\n--- TEST 8: assignDean ---");
        Teacher prof = new Teacher("prof.dean@uni.edu", "Eleanor", "Vance") {};
        prof.setId(10L);
        prof.setEmployeeId("T010");
        teacherRepo.save(prof);

        facultyService.assignDean(engineering.getId(), 10L);
        School afterAssign = facultyService.getFacultyById(engineering.getId()).get();
        System.out.println("  Dean of Engineering: "
                + afterAssign.getDean().getFullName() + " (expect 'Eleanor Vance')");

        // ── TEST 9: assignDean — teacher not found guard ──────────────────────
        System.out.println("\n--- TEST 9: assignDean with unknown teacher ---");
        try {
            facultyService.assignDean(engineering.getId(), 999L);
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 10: createFaculty — duplicate name guard ─────────────────────
        System.out.println("\n--- TEST 10: createFaculty duplicate name guard ---");
        try {
            facultyService.createFaculty(new FacultyCreationRequest("School of Engineering"));
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 11: deleteFaculty ────────────────────────────────────────────
        System.out.println("\n--- TEST 11: deleteFaculty ---");
        facultyService.deleteFaculty(arts.getId());
        System.out.println("  Schools after delete: "
                + facultyService.getAllFaculties().size() + " (expect 2)");
        System.out.println("  Arts gone: "
                + facultyService.getFacultyById(arts.getId()).isEmpty() + " (expect true)");

        // ── TEST 12: deleteFaculty also removes departments ───────────────────
        System.out.println("\n--- TEST 12: deleteFaculty removes linked departments ---");
        facultyService.deleteFaculty(engineering.getId());
        System.out.println("  Schools remaining: "
                + facultyService.getAllFaculties().size() + " (expect 1)");
        try {
            facultyService.getFacultyDepartments(engineering.getId());
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }
    }
    
    
    private static void runUserServiceTests() {

        UserRepository userRepo = new UserRepository();
        UserService userService = new UserService(userRepo);

        System.out.println("--- TEST 1: Create users ---");

        User u1 = userService.createUser(
                new UserCreationRequest("alice@mail.com", "Alice", "Smith", "123")
        );

        User u2 = userService.createUser(
                new UserCreationRequest("bob@mail.com", "Bob", "Jones", "456")
        );

        System.out.println("  u1: " + u1);
        System.out.println("  u2: " + u2);

        System.out.println("\n--- TEST 2: Get by ID ---");
        System.out.println("  Found: " + userService.getUserById(u1.getId()));

        System.out.println("\n--- TEST 3: Get by Email ---");
        System.out.println("  Found: " + userService.getUserByEmail("bob@mail.com"));

        System.out.println("\n--- TEST 4: Block / Unblock ---");
        userService.blockUser(u1.getId());
        System.out.println("  Alice active: " + userService.getUserById(u1.getId()).isActive());

        userService.unblockUser(u1.getId());
        System.out.println("  Alice active: " + userService.getUserById(u1.getId()).isActive());

        System.out.println("\n--- TEST 5: Update user ---");
        userService.updateUser(u2.getId(),
                new UserUpdateRequest("newbob@mail.com", "Bobby", "Jones", "999"));

        System.out.println("  Updated: " + userService.getUserById(u2.getId()));

        System.out.println("\n--- TEST 6: Search users ---");
        SearchFilters filters = new SearchFilters(true);
        userService.blockUser(u2.getId()); // чтобы проверить фильтр

        userService.searchUsers("alice", filters)
                .forEach(u -> System.out.println("  " + u));

        System.out.println("\n--- TEST 7: Delete user ---");
        userService.deleteUser(u1.getId());

        try {
            userService.getUserById(u1.getId());
            System.out.println("  ERROR: should have thrown!");
        } catch (RuntimeException ex) {
            System.out.println("  Correctly deleted: " + ex.getMessage());
        }

        System.out.println("\n--- TEST 8: Duplicate email guard ---");
        try {
            userService.createUser(
                    new UserCreationRequest("newbob@mail.com", "Test", "User", "111")
            );
            System.out.println("  ERROR: should have thrown!");
        } catch (RuntimeException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

    }
    private static void runTeacherTests() {
        TeacherRepository    teacherRepo    = new TeacherRepository();
        DepartmentRepository deptRepo       = new DepartmentRepository();
        TeacherService       teacherService = new TeacherService(teacherRepo, deptRepo);

        Department csDept = new Department();
        csDept.setName("Computer Science");
        csDept.setCode("CS");
        csDept.setEmail("cs@uni.edu");
        deptRepo.save(csDept);  

        Department mathDept = new Department();
        mathDept.setName("Mathematics");
        mathDept.setCode("MATH");
        mathDept.setEmail("math@uni.edu");
        deptRepo.save(mathDept);

 
        Teacher alice = new Teacher("alice@uni.edu", "Alice", "Johnson");
        alice.setEmployeeId("T001");
        alice.setTeacherPosition(TeacherPosition.PROFESSOR);
        teacherRepo.save(alice);

        Teacher bob = new Teacher("bob@uni.edu", "Bob", "Smith");
        bob.setEmployeeId("T002");
        bob.setTeacherPosition(TeacherPosition.LECTOR);
        teacherRepo.save(bob);

        Teacher carol = new Teacher("carol@uni.edu", "Carol", "White");
        carol.setEmployeeId("T003");
        carol.setTeacherPosition(TeacherPosition.SENIOR_LECTOR);
        teacherRepo.save(carol);



        teacherRepo.save(alice);
        teacherRepo.save(bob);
        teacherRepo.save(carol);


        System.out.println("=== TEACHER SERVICE TESTS ===\n");

        // ----- TEST 1: getTeacherByEmployeeId -----
        
        Teacher foundByEmpId = teacherService.getTeacherByEmployeeId("T002");
        System.out.println("  Found by employeeId: " + foundByEmpId.getFullName());

        try {
            teacherService.getTeacherByEmployeeId("UNKNOWN");
            System.out.println("  ERROR: should have thrown NoSuchElementException");
        } catch (NoSuchElementException e) {
            System.out.println("  Correctly threw NoSuchElementException: " + e.getMessage());
        }

        // ----- TEST 2: updateTeacherProfile -----
        System.out.println("\n--- TEST 2: Update teacher profile ---");
        TeacherProfileUpdateRequest updateReq = new TeacherProfileUpdateRequest("Alice", "Johnson-Brown",
                        "alice.new@uni.edu", TeacherPosition.PROFESSOR);
        Teacher updatedAlice = teacherService.updateTeacherProfile(alice.getEmployeeId(), updateReq);
        System.out.println("  Updated teacher: " + updatedAlice.getFullName() + ", email: " +
                updatedAlice.getEmail() + ", position: " + updatedAlice.getTeacherPosition());

        try {
            teacherService.updateTeacherProfile("UNKNOWN", updateReq);
            System.out.println("  ERROR: should have thrown NoSuchElementException");
        } catch (NoSuchElementException e) {
            System.out.println("  Correctly threw NoSuchElementException (teacher not found)");
        }

        // ----- TEST 3: getTeacherCourses -----
        System.out.println("\n--- TEST 3: Get courses taught by teacher ---");
        List<Course> aliceCourses = teacherService.getTeacherCourses(alice.getEmployeeId());
        System.out.println("  Alice's courses:");
        aliceCourses.forEach(c -> System.out.println("    " + c.getName() + " (" + c.getCourseId() + ")"));

        List<Course> bobCourses = teacherService.getTeacherCourses(bob.getEmployeeId());
        System.out.println("  Bob's courses:");
        bobCourses.forEach(c -> System.out.println("    " + c.getName()));

        // ----- TEST 4: assignTeacherToDepartment & getTeachersByDepartment -----
        System.out.println("\n--- TEST 4: Assign teachers to departments ---");
        teacherService.assignTeacherToDepartment(alice.getEmployeeId(), csDept.getId());
        teacherService.assignTeacherToDepartment(bob.getEmployeeId(), mathDept.getId());
        teacherService.assignTeacherToDepartment(carol.getEmployeeId(), csDept.getId());

        List<Teacher> csTeachers = teacherService.getTeachersByDepartment(csDept.getId());
        System.out.println("  CS department teachers:");
        csTeachers.forEach(t -> System.out.println("    " + t.getFullName()));

        List<Teacher> mathTeachers = teacherService.getTeachersByDepartment(mathDept.getId());
        System.out.println("  Math department teachers:");
        mathTeachers.forEach(t -> System.out.println("    " + t.getFullName()));

        try {
            teacherService.getTeachersByDepartment(999L);
            System.out.println("  ERROR: should have thrown NoSuchElementException");
        } catch (NoSuchElementException e) {
            System.out.println("  Correctly threw NoSuchElementException for unknown department");
        }

        // ----- TEST 5: searchTeachers -----
        System.out.println("\n--- TEST 5: Search teachers by keyword ---");
        List<Teacher> searchByAlice = teacherService.searchTeachers("Alice");
        System.out.println("  Search 'Alice':");
        searchByAlice.forEach(t -> System.out.println("    " + t.getFullName()));

        List<Teacher> searchByEmail = teacherService.searchTeachers("uni.edu");
        System.out.println("  Search 'uni.edu':");
        searchByEmail.forEach(t -> System.out.println("    " + t.getFullName() + " (" + t.getEmail() + ")"));

        List<Teacher> searchByPosition = teacherService.searchTeachers("LECTOR");
        System.out.println("  Search 'LECTOR':");
        searchByPosition.forEach(t -> System.out.println("    " + t.getFullName() + " - " + t.getTeacherPosition()));

        // ----- TEST 6: Null / invalid argument checks -----
        System.out.println("\n--- TEST 6: Invalid argument exceptions ---");
        

        try {
            teacherService.getTeacherByEmployeeId(null);
            System.out.println("  ERROR: should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("  Correctly threw: " + e.getMessage());
        }

        try {
            teacherService.updateTeacherProfile(alice.getEmployeeId(), null);
            System.out.println("  ERROR: should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("  Correctly threw: " + e.getMessage());
        }

        try {
            teacherService.assignTeacherToDepartment(alice.getEmployeeId(), null);
            System.out.println("  ERROR: should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("  Correctly threw: " + e.getMessage());
        }

        System.out.println("\n=== All TeacherService tests completed ===");
    }
    
    private static void runComplaintTests() {
    	 
        // ── Setup ─────────────────────────────────────────────────────────────
        ComplaintRepository complaintRepo = new ComplaintRepository();
        TeacherRepository   teacherRepo   = new TeacherRepository();
        StudentRepository   studentRepo   = new StudentRepository();
        ComplaintService    complaintService =
                new ComplaintService(complaintRepo, teacherRepo, studentRepo);
 
        // ── Teacher ───────────────────────────────────────────────────────────
        Teacher prof = new Teacher("asd@asd.dsa", "ASD", "DSA") {};
        prof.setId(1L);
        prof.setEmployeeId("T001");
        
        teacherRepo.save(prof);
 
        Teacher prof2 = new Teacher("ffhdjd@ds.ds", "EW", "WE") {};
        prof2.setId(2L);
        prof2.setEmployeeId("T002");
        teacherRepo.save(prof2);
 
        // ── Students ──────────────────────────────────────────────────────────
        Student alice = new Student() {};
        alice.setId(10L);
        alice.setStudentId("S001");
        alice.setFullName("Alice", "Smith");
        studentRepo.save(alice);
 
        Student bob = new Student() {};
        bob.setId(11L);
        bob.setStudentId("S002");
        bob.setFullName("Bob", "Jones");
        studentRepo.save(bob);
 
        // ── TEST 1: submitComplaint ───────────────────────────────────────────
        System.out.println("--- TEST 1: submitComplaint ---");
        Complaint c1 = complaintService.submitComplaint(
                1L, "S001", ManagerForComplaint.DEAN, UrgencyLevel.HIGH);
        Complaint c2 = complaintService.submitComplaint(
                1L, "S002", ManagerForComplaint.DEPARTMENT_HEAD, UrgencyLevel.LOW);
        Complaint c3 = complaintService.submitComplaint(
                2L, "S001", ManagerForComplaint.DEAN, UrgencyLevel.MEDIUM);
        System.out.println("  c1: " + c1 + " status=" + c1.getComplaintStatus());
        System.out.println("  c2: " + c2 + " status=" + c2.getComplaintStatus());
        System.out.println("  c3: " + c3 + " status=" + c3.getComplaintStatus());
 
        // ── TEST 2: getAllComplaints ───────────────────────────────────────────
        System.out.println("\n--- TEST 2: getAllComplaints ---");
        System.out.println("  Total: " + complaintService.getAllComplaints().size()
                + " (expect 3)");
 
        // ── TEST 3: getComplaintsByTeacher ────────────────────────────────────
        System.out.println("\n--- TEST 3: getComplaintsByTeacher ---");
        List<Complaint> byTuring = complaintService.getComplaintsByTeacher(1L);
        System.out.println("  Turing's complaints: " + byTuring.size() + " (expect 2)");
        List<Complaint> byAda = complaintService.getComplaintsByTeacher(2L);
        System.out.println("  Lovelace's complaints: " + byAda.size() + " (expect 1)");
 
        // ── TEST 4: getComplaintsByStudent ────────────────────────────────────
        System.out.println("\n--- TEST 4: getComplaintsByStudent ---");
        List<Complaint> aboutAlice = complaintService.getComplaintsByStudent("S001");
        System.out.println("  About Alice: " + aboutAlice.size() + " (expect 2)");
        List<Complaint> aboutBob = complaintService.getComplaintsByStudent("S002");
        System.out.println("  About Bob: " + aboutBob.size() + " (expect 1)");
 
        // ── TEST 5: getComplaintsByRecipient ──────────────────────────────────
        System.out.println("\n--- TEST 5: getComplaintsByRecipient ---");
        List<Complaint> toDean = complaintService.getComplaintsByRecipient(
                ManagerForComplaint.DEAN);
        System.out.println("  To DEAN: " + toDean.size() + " (expect 2)");
        List<Complaint> toHead = complaintService.getComplaintsByRecipient(
                ManagerForComplaint.DEPARTMENT_HEAD);
        System.out.println("  To DEPARTMENT_HEAD: " + toHead.size() + " (expect 1)");
 
        // ── TEST 6: getComplaintsByUrgency ────────────────────────────────────
        System.out.println("\n--- TEST 6: getComplaintsByUrgency ---");
        System.out.println("  HIGH: " + complaintService.getComplaintsByUrgency(
                UrgencyLevel.HIGH).size() + " (expect 1)");
        System.out.println("  MEDIUM: " + complaintService.getComplaintsByUrgency(
                UrgencyLevel.MEDIUM).size() + " (expect 1)");
        System.out.println("  LOW: " + complaintService.getComplaintsByUrgency(
                UrgencyLevel.LOW).size() + " (expect 1)");
 
        // ── TEST 7: reviewComplaint ───────────────────────────────────────────
        System.out.println("\n--- TEST 7: reviewComplaint ---");
        complaintService.reviewComplaint(c1.getId());
        System.out.println("  c1 status: " + c1.getComplaintStatus()
                + " (expect UNDER_REVIEW)");
        System.out.println("  UNDER_REVIEW count: " + complaintService
                .getComplaintsByStatus(ComplaintStatus.UNDER_REVIEW).size()
                + " (expect 1)");
 
        // ── TEST 8: resolveComplaint ──────────────────────────────────────────
        System.out.println("\n--- TEST 8: resolveComplaint ---");
        complaintService.resolveComplaint(c1.getId());
        System.out.println("  c1 resolved: " + c1.isResolved() + " (expect true)");
        System.out.println("  RESOLVED count: " + complaintService
                .getComplaintsByStatus(ComplaintStatus.RESOLVED).size()
                + " (expect 1)");
 
        // ── TEST 9: rejectComplaint ───────────────────────────────────────────
        System.out.println("\n--- TEST 9: rejectComplaint ---");
        complaintService.rejectComplaint(c2.getId());
        System.out.println("  c2 status: " + c2.getComplaintStatus()
                + " (expect REJECTED)");
 
        // ── TEST 10: lifecycle guard — resolve without review ─────────────────
        System.out.println("\n--- TEST 10: resolve without review guard ---");
        try {
            complaintService.resolveComplaint(c3.getId()); // still SUBMITTED
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalStateException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }
 
        // ── TEST 11: lifecycle guard — reject already resolved ────────────────
        System.out.println("\n--- TEST 11: reject already resolved guard ---");
        try {
            complaintService.rejectComplaint(c1.getId()); // already RESOLVED
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalStateException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }
 
        // ── TEST 12: deleteComplaint — active complaint guard ─────────────────
        System.out.println("\n--- TEST 12: deleteComplaint on active complaint guard ---");
        try {
            complaintService.deleteComplaint(c3.getId()); // still SUBMITTED
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalStateException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }
 
        // ── TEST 13: deleteComplaint — success on resolved ────────────────────
        System.out.println("\n--- TEST 13: deleteComplaint resolved complaint ---");
        complaintService.deleteComplaint(c1.getId());
        System.out.println("  Total after delete: " + complaintService
                .getAllComplaints().size() + " (expect 2)");
        try {
            complaintService.getComplaintById(c1.getId());
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly not found: " + ex.getMessage());
        }
 
        // ── TEST 14: submit with unknown teacher guard ─────────────────────────
        System.out.println("\n--- TEST 14: submit with unknown teacher ---");
        try {
            complaintService.submitComplaint(
                    999L, "S001", ManagerForComplaint.DEAN, UrgencyLevel.LOW);
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }
 
        // ── TEST 15: submit with unknown student guard ─────────────────────────
        System.out.println("\n--- TEST 15: submit with unknown student ---");
        try {
            complaintService.submitComplaint(
                    1L, "S999", ManagerForComplaint.DEAN, UrgencyLevel.LOW);
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
