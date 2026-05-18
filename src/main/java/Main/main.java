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
        
        System.out.println("\n========== Course Service Tests ============\n");
        runCourseTests();
        
        System.out.println("\n========== User Service Tests ============\n");
        runUserServiceTests();
        
        System.out.println("\n========== Teacher Service Tests ============\n");
        runTeacherTests();
        
        System.out.println("\n========== Complaint Service Tests ============\n");
        runComplaintTests();
        
        System.out.println("\n========== Message Service Tests ============\n");
        runMessageTests();
        
        System.out.println("\n========== News Service Tests ============\n");
        runNewsTests();

        System.out.println("\n========== Report Service Tests ============\n");
        runReportTests();

        System.out.println("\n========== Research Service Tests ============\n");
        runResearchTests();
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

        Enrollment eAliceMath = enrollService.enrollStudent("S001", "MATH101");
        Enrollment eAlicePhysics = enrollService.enrollStudent("S001", "PHYS101");
        Enrollment eAliceCS = enrollService.enrollStudent("S001", "CS101");
        Enrollment eBobMath = enrollService.enrollStudent("S002", "MATH101");

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
    
    private static void runCourseTests() {

        CourseRepository     courseRepo  = new CourseRepository();
        DepartmentRepository deptRepo    = new DepartmentRepository();
        TeacherRepository    teacherRepo = new TeacherRepository();
        CourseService        courseService =
                new CourseService(courseRepo, deptRepo, teacherRepo);

        // --- TEST 1: Create course ---
        System.out.println("--- TEST 1: createCourse ---");
        Course math = courseService.createCourse(
                new CourseCreationRequest("MATH101", "Calculus I", 4, CourseType.MAJOR)
        );
        Course cs = courseService.createCourse(
                new CourseCreationRequest("CS101", "OOP and Design", 5, CourseType.MAJOR)
        );
        Course elective = courseService.createCourse(
                new CourseCreationRequest("ART101", "Art History", 2, CourseType.ELECTIVE)
        );
        System.out.println("  Created: " + math.getName());
        System.out.println("  Created: " + cs.getName());
        System.out.println("  Created: " + elective.getName());

        // --- TEST 2: Duplicate guard ---
        System.out.println("\n--- TEST 2: Duplicate courseId guard ---");
        try {
            courseService.createCourse(
                    new CourseCreationRequest("MATH101", "Calculus II", 4, CourseType.MAJOR)
            );
            System.out.println("  ERROR: should have thrown!");
        } catch (RuntimeException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // --- TEST 3: getCourseById ---
        System.out.println("\n--- TEST 3: getCourseById ---");
        Course found = courseService.getCourseById("MATH101");
        System.out.println("  Found: " + found.getName() + " (expect 'Calculus I')");

        // --- TEST 4: updateCourse ---
        System.out.println("\n--- TEST 4: updateCourse ---");
        courseService.updateCourse("MATH101",
                new CourseUpdateRequest("Calculus I Advanced", null, null)
        );
        System.out.println("  Updated: " + courseService.getCourseById("MATH101").getName()
                + " (expect 'Calculus I Advanced')");

        // --- TEST 5: searchCourses by type ---
        System.out.println("\n--- TEST 5: searchCourses by type MAJOR ---");
        List<Course> majors = courseService.searchCourses(
                new CourseSearchFilters(CourseType.MAJOR, null, null)
        );
        System.out.println("  MAJOR count: " + majors.size() + " (expect 2)");

        // --- TEST 6: searchCourses by keyword ---
        System.out.println("\n--- TEST 6: searchCourses by keyword ---");
        List<Course> hits = courseService.searchCourses(
                new CourseSearchFilters(null, "calculus", null)
        );
        System.out.println("  Keyword 'calculus' hits: " + hits.size() + " (expect 1)");

        // --- TEST 7: searchCourses by maxCredits ---
        System.out.println("\n--- TEST 7: searchCourses by maxCredits ---");
        List<Course> cheap = courseService.searchCourses(
                new CourseSearchFilters(null, null, 3)
        );
        System.out.println("  Max 3 credits count: " + cheap.size() + " (expect 1)");

        // --- TEST 8: assignInstructor ---
        System.out.println("\n--- TEST 8: assignInstructor ---");
        Teacher prof = new Teacher("prof@uni.edu", "John", "Doe") {};
        prof.setEmployeeId("1");
        teacherRepo.save(prof);
        courseService.assignInstructor("MATH101", 1L);
        System.out.println("  Teachers on course: "
                + courseService.getCourseById("MATH101").getTeachers().size() + " (expect 1)");

        // --- TEST 9: assignInstructor duplicate guard ---
        System.out.println("\n--- TEST 9: assignInstructor duplicate guard ---");
        try {
            courseService.assignInstructor("MATH101", 1L);
            System.out.println("  ERROR: should have thrown!");
        } catch (RuntimeException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // --- TEST 10: deleteCourse without enrollments ---
        System.out.println("\n--- TEST 10: deleteCourse (no enrollments) ---");
        courseService.deleteCourse("ART101"); // elective
        List<Course> remaining = courseService.searchCourses(null);
        System.out.println("  Courses remaining: " + remaining.size() + " (expect 2)");

        // --- TEST 11: deleteCourse with enrollments guard ---
        System.out.println("\n--- TEST 11: deleteCourse with enrollments guard ---");
        Enrollment fakeEnrollment = new Enrollment();
        courseService.getCourseById("MATH101").addEnrollment(fakeEnrollment);
        try {
            courseService.deleteCourse("MATH101");
            System.out.println("  ERROR: should have thrown!");
        } catch (RuntimeException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

    }
    private static void runMessageTests() {

        UserRepository    userRepo    = new UserRepository();
        MessageRepository messageRepo = new MessageRepository();
        MessageService    messageService = new MessageService(messageRepo, userRepo);

        Teacher alice = new Teacher("alice@uni.edu", "Alice", "Prof") {};
        alice.setId(1L);
        alice.setEmployeeId("E001");
        userRepo.save(alice);

        Teacher bob = new Teacher("bob@uni.edu", "Bob", "Prof");
        bob.setId(2L);
        bob.setEmployeeId("E002");

        userRepo.save(bob);

        Teacher carol = new Teacher("carol@uni.edu", "Carol", "Prof");
        carol.setId(3L);
        carol.setEmployeeId("E003");
        userRepo.save(carol);


        User studentUser = new User("student@uni.edu", "Dave", "Student");
        studentUser.setId(99L);
        userRepo.save(studentUser);

        // ── TEST 1: sendMessage ───────────────────────────────────────────────
        System.out.println("--- TEST 1: sendMessage ---");
        Message m1 = messageService.sendMessage(1L, 2L, "Meeting tomorrow", "Hi Bob, are you free?");
        Message m2 = messageService.sendMessage(2L, 1L, "Re: Meeting tomorrow", "Sure, 10am works.");
        Message m3 = messageService.sendMessage(1L, 3L, "Exam schedule", "Carol, please review the draft.");
        Message m4 = messageService.sendMessage(3L, 2L, "Grades", "Bob, have you submitted grades?");
        System.out.println("  m1: " + m1);
        System.out.println("  m2: " + m2);
        System.out.println("  m3: " + m3);
        System.out.println("  m4: " + m4);

        // ── TEST 2: getInbox ──────────────────────────────────────────────────
        System.out.println("\n--- TEST 2: getInbox ---");
        List<Message> aliceInbox = messageService.getInbox(1L);
        System.out.println("  Alice inbox: " + aliceInbox.size() + " (expect 1)");
        aliceInbox.forEach(m -> System.out.println("  - [" + m.getSubject() + "]"));

        List<Message> bobInbox = messageService.getInbox(2L);
        System.out.println("  Bob inbox: " + bobInbox.size() + " (expect 2)");
        bobInbox.forEach(m -> System.out.println("  - [" + m.getSubject() + "]"));

        // ── TEST 3: getSentMessages ───────────────────────────────────────────
        System.out.println("\n--- TEST 3: getSentMessages ---");
        List<Message> aliceSent = messageService.getSentMessages(1L);
        System.out.println("  Alice sent: " + aliceSent.size() + " (expect 2)");

        List<Message> carolSent = messageService.getSentMessages(3L);
        System.out.println("  Carol sent: " + carolSent.size() + " (expect 1)");

        // ── TEST 4: getMessageById ────────────────────────────────────────────
        System.out.println("\n--- TEST 4: getMessageById ---");
        messageService.getMessageById(m1.getId())
                .ifPresent(m -> System.out.println("  Found: [" + m.getSubject() + "]"));
        System.out.println("  Missing id empty: "
                + messageService.getMessageById(999L).isEmpty() + " (expect true)");

        // ── TEST 5: markAsRead ────────────────────────────────────────────────
        System.out.println("\n--- TEST 5: markAsRead ---");
        System.out.println("  m1 isRead before: " + m1.isRead() + " (expect false)");
        messageService.markAsRead(m1.getId(), 2L); 
        System.out.println("  m1 isRead after:  " + m1.isRead() + " (expect true)");

        // ── TEST 6: markAsRead — wrong recipient guard ────────────────────────
        System.out.println("\n--- TEST 6: markAsRead wrong recipient guard ---");
        try {
            messageService.markAsRead(m1.getId(), 3L);
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 7: getConversation ───────────────────────────────────────────
        System.out.println("\n--- TEST 7: getConversation Alice ↔ Bob ---");
        List<Message> convo = messageService.getConversation(1L, 2L);
        System.out.println("  Messages in thread: " + convo.size() + " (expect 2)");
        convo.forEach(m -> System.out.println("  [" + m.getSenderId() + "→" + m.getRecipientId() + "] " + m.getSubject()));

        // ── TEST 8: deleteMessage — soft delete from sender's view ────────────
        System.out.println("\n--- TEST 8: deleteMessage (sender deletes m3) ---");
        messageService.deleteMessage(m3.getId(), 1L); 
        List<Message> aliceSentAfter = messageService.getSentMessages(1L);
        System.out.println("  Alice sent after delete: " + aliceSentAfter.size() + " (expect 1)");
        List<Message> carolInbox = messageService.getInbox(3L);
        System.out.println("  Carol inbox still has m3: " + carolInbox.size() + " (expect 1)");

        // ── TEST 9: deleteMessage — unauthorized employee guard ───────────────
        System.out.println("\n--- TEST 9: deleteMessage unauthorized guard ---");
        try {
            messageService.deleteMessage(m4.getId(), 1L); 
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 10: non-employee cannot send guard ───────────────────────────
        System.out.println("\n--- TEST 10: non-employee send guard ---");
        try {
            messageService.sendMessage(99L, 1L, "Hello", "Can I message a teacher?");
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 11: non-employee cannot receive guard ────────────────────────
        System.out.println("\n--- TEST 11: non-employee receive guard ---");
        try {
            messageService.sendMessage(1L, 99L, "Hello", "Messaging a student?");
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 12: self-message guard ───────────────────────────────────────
        System.out.println("\n--- TEST 12: self-message guard ---");
        try {
            messageService.sendMessage(1L, 1L, "Note to self", "...");
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalStateException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 13: blank subject guard ─────────────────────────────────────
        System.out.println("\n--- TEST 13: blank subject guard ---");
        try {
            messageService.sendMessage(1L, 2L, "  ", "Body text");
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 14: unknown sender guard ─────────────────────────────────────
        System.out.println("\n--- TEST 14: unknown sender guard ---");
        try {
            messageService.sendMessage(999L, 1L, "Subject", "Body");
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 15: conversation excludes messages deleted by viewer ─────────
        System.out.println("\n--- TEST 15: conversation respects soft-delete ---");
        Message m5 = messageService.sendMessage(1L, 2L, "Temp note", "Ignore this.");
        messageService.deleteMessage(m5.getId(), 1L);
        List<Message> convoAfter = messageService.getConversation(1L, 2L);

        System.out.println("  Alice's convo size after her delete: "
                + convoAfter.size() + " (expect 2)");
        List<Message> bobConvo = messageService.getConversation(2L, 1L);
        System.out.println("  Bob's convo size (still sees m5): "
                + bobConvo.size() + " (expect 3)");
    }
    
    private static void runNewsTests() {

        UserRepository userRepo = new UserRepository();
        NewsRepository newsRepo = new NewsRepository();
        NewsService    newsService = new NewsService(newsRepo, userRepo);

        // Создаём пользователей
        User author = new User("author@uni.edu", "Aibek", "Seitkali");
        userRepo.save(author);
        User editor = new User("editor@uni.edu", "Dana", "Bekova");
        userRepo.save(editor);

        // --- TEST 1: publishNews ---
        System.out.println("--- TEST 1: publishNews ---");
        News n1 = newsService.publishNews(author.getId(), "AI Conference", "Details here...");
        News n2 = newsService.publishNews(author.getId(), "Research Week", "Join us...");
        News n3 = newsService.publishNews(editor.getId(), "Campus News", "Today at campus...");
        System.out.println("  Published: " + n1);
        System.out.println("  Published: " + n2);
        System.out.println("  Published: " + n3);

        // --- TEST 2: getLatestNews ---
        System.out.println("\n--- TEST 2: getLatestNews(2) ---");
        newsService.getLatestNews(2)
                   .forEach(n -> System.out.println("  " + n));

        // --- TEST 3: getNewsById ---
        System.out.println("\n--- TEST 3: getNewsById ---");
        newsService.getNewsById(n1.getId())
                   .ifPresent(n -> System.out.println("  Found: " + n.getTitle()));

        // --- TEST 4: editNews ---
        System.out.println("\n--- TEST 4: editNews ---");
        newsService.editNews(n1.getId(), editor.getId(), "Updated content about AI.");
        System.out.println("  Edited: " + newsService.getNewsById(n1.getId()).get().getContent());

        // --- TEST 5: searchNews by keyword ---
        System.out.println("\n--- TEST 5: searchNews keyword 'research' ---");
        newsService.searchNews("research", null, null)
                   .forEach(n -> System.out.println("  " + n.getTitle()));

        // --- TEST 6: getNewsByAuthor ---
        System.out.println("\n--- TEST 6: getNewsByAuthor ---");
        newsService.getNewsByAuthor(author.getId())
                   .forEach(n -> System.out.println("  " + n.getTitle()));
        System.out.println("  Count: (expect 2)");

        // --- TEST 7: archiveNews ---
        System.out.println("\n--- TEST 7: archiveNews ---");
        newsService.archiveNews(n3.getId(), editor.getId());
        System.out.println("  Archived: " + newsService.getNewsById(n3.getId()).get().isArchived()
                + " (expect true)");
        System.out.println("  Latest count after archive: "
                + newsService.getLatestNews(10).size() + " (expect 2)");

        // --- TEST 8: edit archived guard ---
        System.out.println("\n--- TEST 8: edit archived news guard ---");
        try {
            newsService.editNews(n3.getId(), editor.getId(), "Try edit");
            System.out.println("  ERROR: should have thrown!");
        } catch (RuntimeException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // --- TEST 9: deleteNews ---
        System.out.println("\n--- TEST 9: deleteNews ---");
        newsService.deleteNews(n2.getId());
        System.out.println("  After delete latest count: "
                + newsService.getLatestNews(10).size() + " (expect 1)");

        // --- TEST 10: author not found guard ---
        System.out.println("\n--- TEST 10: publishNews unknown author guard ---");
        try {
            newsService.publishNews(999L, "Ghost News", "content");
            System.out.println("  ERROR: should have thrown!");
        } catch (RuntimeException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }
    }
    

    private static void runReportTests() {

        StudentRepository    studentRepo   = new StudentRepository();
        CourseRepository     courseRepo    = new CourseRepository();
        EnrollmentRepository enrollRepo    = new EnrollmentRepository();
        TeacherRepository    teacherRepo   = new TeacherRepository();
        MarkRepository       markRepo      = new MarkRepository();
        DepartmentRepository deptRepo      = new DepartmentRepository();
        FacultyRepository    facultyRepo   = new FacultyRepository();

        EnrollmentService enrollService =
                new EnrollmentService(enrollRepo, studentRepo, courseRepo);
        MarkService markService =
                new MarkService(markRepo, enrollRepo, teacherRepo);
        FacultyService facultyService =
                new FacultyService(facultyRepo, deptRepo, teacherRepo);

        ReportService reportService = new ReportService(
                enrollRepo, markRepo, teacherRepo,
                studentRepo, courseRepo, deptRepo, facultyRepo);

        School engineering = facultyService.createFaculty(
                new FacultyCreationRequest("School of Engineering"));

        Department csDept = new Department();
        csDept.setName("Computer Science");
        csDept.setCode("CS");
        facultyService.addDepartmentToFaculty(engineering.getId(), csDept);

        Department eeDept = new Department();
        eeDept.setName("Electrical Engineering");
        eeDept.setCode("EE");
        facultyService.addDepartmentToFaculty(engineering.getId(), eeDept);

        Teacher profA = new Teacher("turing@uni.edu", "Alan", "Turing");
        profA.setId(1L);
        profA.setEmployeeId("T001");
        profA.setDepartment(csDept);
        profA.setTeacherPosition(TeacherPosition.PROFESSOR);
        teacherRepo.save(profA);

        Teacher profB = new Teacher("ada@uni.edu", "Ada", "Lovelace");
        profB.setId(2L);
        profB.setEmployeeId("T002");
        profB.setDepartment(csDept);
        profB.setTeacherPosition(TeacherPosition.SENIOR_LECTOR);
        teacherRepo.save(profB);

        Teacher profC = new Teacher("tesla@uni.edu", "Nikola", "Tesla") {};
        profC.setId(3L);
        profC.setEmployeeId("T003");
        profC.setDepartment(eeDept);
        profC.setTeacherPosition(TeacherPosition.LECTOR);
        teacherRepo.save(profC);

        Course algo = new Course();
        algo.setCourseId("CS101"); algo.setName("Algorithms"); algo.setCredits(4);
        algo.addTeacher(profA);
        profA.addCourse(algo);
        courseRepo.save(algo);

        Course oop = new Course();
        oop.setCourseId("CS102"); oop.setName("OOP"); oop.setCredits(3);
        oop.addTeacher(profB);
        profB.addCourse(oop);
        courseRepo.save(oop);

        Course circuits = new Course();
        circuits.setCourseId("EE101"); circuits.setName("Circuits"); circuits.setCredits(3);
        circuits.addTeacher(profC);
        profC.addCourse(circuits);
        courseRepo.save(circuits);

        Student alice = new Student() {};
        alice.setId(10L); alice.setStudentId("S001");
        alice.setFullName("Alice", "Smith"); alice.setYearOfStudy(2);
        studentRepo.save(alice);

        Student bob = new Student() {};
        bob.setId(11L); bob.setStudentId("S002");
        bob.setFullName("Bob", "Jones"); bob.setYearOfStudy(1);
        studentRepo.save(bob);

        Student carol = new Student() {};
        carol.setId(12L); carol.setStudentId("S003");
        carol.setFullName("Carol", "White"); carol.setYearOfStudy(3);
        studentRepo.save(carol);

        Enrollment eAliceAlgo = enrollService.enrollStudent("S001", "CS101");
        Enrollment eAliceOop  = enrollService.enrollStudent("S001", "CS102");
        Enrollment eBobAlgo   = enrollService.enrollStudent("S002", "CS101");
        Enrollment eBobCirc   = enrollService.enrollStudent("S002", "EE101");
        Enrollment eCarolOop  = enrollService.enrollStudent("S003", "CS102");
        Enrollment eCarolCirc = enrollService.enrollStudent("S003", "EE101");

        markService.assignMark(1L,"S001","CS101", 28,MarkType.FIRST_ATTESTATION,null);
        markService.assignMark(1L,"S001","CS101", 30,MarkType.SECOND_ATTESTATION,null);
        markService.assignMark(1L,"S001","CS101", 35,MarkType.FINAL_EXAM,null);   // 93

        markService.assignMark(2L,"S001","CS102", 20,MarkType.FIRST_ATTESTATION,null);
        markService.assignMark(2L,"S001","CS102", 18,MarkType.SECOND_ATTESTATION,null);
        markService.assignMark(2L,"S001","CS102", 22,MarkType.FINAL_EXAM,null);   // 60

        markService.assignMark(1L,"S002","CS101", 15,MarkType.FIRST_ATTESTATION,null);
        markService.assignMark(1L,"S002","CS101", 14,MarkType.SECOND_ATTESTATION,null);
        markService.assignMark(1L,"S002","CS101", 20,MarkType.FINAL_EXAM,null);   // 49

        markService.assignMark(3L,"S002","EE101", 25,MarkType.FIRST_ATTESTATION,null);
        markService.assignMark(3L,"S002","EE101", 25,MarkType.SECOND_ATTESTATION,null);
        markService.assignMark(3L,"S002","EE101", 25,MarkType.FINAL_EXAM,null);   // 75

        markService.assignMark(2L,"S003","CS102", 30,MarkType.FIRST_ATTESTATION,null);
        markService.assignMark(2L,"S003","CS102", 30,MarkType.SECOND_ATTESTATION,null);
        markService.assignMark(2L,"S003","CS102", 30,MarkType.FINAL_EXAM,null);   // 90

        enrollService.completeCourse(eAliceAlgo.getId(), 30, 25, 38);
        enrollService.completeCourse(eAliceOop.getId(),  10, 15, 35);
        enrollService.completeCourse(eCarolOop.getId(),  30, 30, 30);

        ReportPeriod allTime = ReportPeriod.all();

        // ── TEST 1: generateEnrollmentStatistics ──────────────────────────────
        System.out.println("--- TEST 1: generateEnrollmentStatistics ---");
        EnrollmentStatistics stats = reportService.generateEnrollmentStatistics(allTime);
        System.out.println("  " + stats);
        System.out.println("  total: "     + stats.getTotalEnrollments()     + " (expect 6)");
        System.out.println("  completed: " + stats.getCompletedEnrollments() + " (expect 3)");
        System.out.println("  active: "    + stats.getActiveEnrollments()    + " (expect 3)");
        System.out.println("  students: "  + stats.getUniqueStudents()       + " (expect 3)");
        System.out.println("  courses: "   + stats.getUniqueCourses()        + " (expect 3)");

        // ── TEST 2: generateCoursePerformanceReport ───────────────────────────
        System.out.println("\n--- TEST 2: generateCoursePerformanceReport CS101 ---");
        CoursePerformanceReport cpr = reportService.generateCoursePerformanceReport("CS101");
        System.out.println("  " + cpr);
        System.out.println("  enrollments: " + cpr.getTotalEnrollments() + " (expect 2)");
        System.out.println("  completed: "   + cpr.getCompletedEnrollments() + " (expect 1)");
        System.out.printf ("  avg: %.2f (expect 71.00 = (93+49)/2)%n", cpr.getAverageTotal());
        System.out.println("  pass: " + cpr.getPassCount() + " (expect 1)");
        System.out.println("  fail: " + cpr.getFailCount() + " (expect 1)");

        // ── TEST 3: generateStudentProgressReport ─────────────────────────────
        System.out.println("\n--- TEST 3: generateStudentProgressReport Alice ---");
        StudentProgressReport spr = reportService.generateStudentProgressReport("S001");
        System.out.println("  " + spr);
        System.out.println("  GPA: "     + String.format("%.2f", spr.getGpa())
                + " (expect 76.50 = (93+60)/2)");
        System.out.println("  credits: " + spr.getTotalCreditsEarned() + " (expect 7)");
        System.out.println("  completed: " + spr.getCompletedCourses() + " (expect 2)");
        System.out.println("  active: "    + spr.getActiveCourses()    + " (expect 0)");

        // ── TEST 4: generateStudentProgressReport Bob ──────────────────────────
        System.out.println("\n--- TEST 4: generateStudentProgressReport Bob ---");
        StudentProgressReport sprBob = reportService.generateStudentProgressReport("S002");
        System.out.println("  " + sprBob);
        System.out.println("  completed: " + sprBob.getCompletedCourses() + " (expect 0)");
        System.out.println("  active: "    + sprBob.getActiveCourses()    + " (expect 2)");
        System.out.printf ("  GPA: %.2f (expect 62.00 = (49+75)/2)%n", sprBob.getGpa());

        // ── TEST 5: generateTeacherWorkloadReport ─────────────────────────────
        System.out.println("\n--- TEST 5: generateTeacherWorkloadReport Turing ---");
        Semester fall2024 = new Semester(2024, Semester.SemesterTerm.FALL);
        // enrollments were created now (today), which falls in Fall 2024 if run in Sep-Dec 2024
        // Use allTime period via a wide semester to guarantee inclusion
        Semester wideSemester = new Semester(2025, Semester.SemesterTerm.SPRING);
        TeacherWorkloadReport twr = reportService.generateTeacherWorkloadReport(1L, wideSemester);
        System.out.println("  " + twr);
        System.out.println("  courses: " + twr.getTotalCourses() + " (expect 1 — CS101)");

        // ── TEST 6: generateDepartmentReport ─────────────────────────────────
        System.out.println("\n--- TEST 6: generateDepartmentReport CS dept ---");
        DepartmentReport dr = reportService.generateDepartmentReport(
                csDept.getId(), allTime);
        System.out.println("  " + dr);
        System.out.println("  teachers: " + dr.getTotalTeachers() + " (expect 2)");
        System.out.println("  courses: "  + dr.getTotalCourses()  + " (expect 2)");
        System.out.println("  enrollments: " + dr.getTotalEnrollments() + " (expect 4)");
        System.out.println("  completed: "   + dr.getCompletedEnrollments() + " (expect 3)");

        // ── TEST 7: generateFacultyReport ─────────────────────────────────────
        System.out.println("\n--- TEST 7: generateFacultyReport Engineering ---");
        FacultyReport fr = reportService.generateFacultyReport(
                engineering.getId(), allTime);
        System.out.println("  " + fr);
        System.out.println("  departments: " + fr.getTotalDepartments() + " (expect 2)");
        System.out.println("  enrollments: " + fr.getTotalEnrollments() + " (expect 6)");
        System.out.println("  completed: "   + fr.getCompletedEnrollments() + " (expect 3)");

        // ── TEST 8: generateHRReport ──────────────────────────────────────────
        System.out.println("\n--- TEST 8: generateHRReport ---");
        HRReport hr = reportService.generateHRReport();
        System.out.println("  " + hr);
        System.out.println("  total teachers: " + hr.getTotalTeachers() + " (expect 3)");
        System.out.println("  by position: "    + hr.getTeachersByPosition());
        System.out.printf ("  avg courses: %.2f (expect 1.00)%n", hr.getAvgCoursesPerTeacher());
        System.out.println("  no-course teachers: " + hr.getTeachersWithNoCourses() + " (expect 0)");

        // ── TEST 9: ReportPeriod factory methods ──────────────────────────────
        System.out.println("\n--- TEST 9: ReportPeriod factories ---");
        ReportPeriod spring = ReportPeriod.springSemester(2025);
        ReportPeriod fall   = ReportPeriod.fallSemester(2025);
        ReportPeriod year   = ReportPeriod.ofYear(2025);
        System.out.println("  Spring 2025: " + spring);
        System.out.println("  Fall   2025: " + fall);
        System.out.println("  Year   2025: " + year);
        System.out.println("  allTime contains 1990-01-01: "
                + allTime.contains(java.time.LocalDate.of(1990, 1, 1)) + " (expect true)");
        System.out.println("  spring contains 2025-03-15: "
                + spring.contains(java.time.LocalDate.of(2025, 3, 15)) + " (expect true)");
        System.out.println("  spring contains 2025-09-01: "
                + spring.contains(java.time.LocalDate.of(2025, 9, 1)) + " (expect false)");

        // ── TEST 10: coursePerformanceReport — unknown course guard ────────────
        System.out.println("\n--- TEST 10: coursePerformanceReport unknown course ---");
        try {
            reportService.generateCoursePerformanceReport("INVALID999");
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 11: studentProgressReport — unknown student guard ─────────────
        System.out.println("\n--- TEST 11: studentProgressReport unknown student ---");
        try {
            reportService.generateStudentProgressReport("S999");
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 12: ReportPeriod invalid range guard ──────────────────────────
        System.out.println("\n--- TEST 12: ReportPeriod invalid range ---");
        try {
            new ReportPeriod(
                    java.time.LocalDate.of(2025, 12, 31),
                    java.time.LocalDate.of(2025, 1, 1));
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }
    }

    private static void runResearchTests() {

        UserRepository            userRepo   = new UserRepository();
        ResearchPaperRepository   paperRepo  = new ResearchPaperRepository();
        AuthorshipRepository      authRepo   = new AuthorshipRepository();
        ResearchService           rs         =
                new ResearchService(paperRepo, authRepo, userRepo);

        Teacher prof = new Teacher("turing@uni.edu", "Alan", "Turing");
        prof.setId(1L);
        prof.setEmployeeId("T001");

        prof.setTeacherPosition(TeacherPosition.PROFESSOR);
        userRepo.save(prof);

        MasterStudent master = new MasterStudent();
        master.setId(2L);
        master.setStudentId("M001");
        master.setFullName("Grace", "Hopper");
        master.setEmail("hopper@uni.edu");
        master.setThesisTopic("Compilers");
        userRepo.save(master);

        PHDStudent phd = new PHDStudent();
        phd.setId(3L);
        phd.setStudentId("P001");
        phd.setDissertationTopic("Information Theory");
        phd.setSupervisor(prof);
        userRepo.save(phd);

        User plainUser = new User("plain@uni.edu", "Plain", "User");
        plainUser.setId(99L);
        userRepo.save(plainUser);

        // ── TEST 1: createPaper ───────────────────────────────────────────────
        System.out.println("--- TEST 1: createPaper ---");
        ResearchPaper p1 = rs.createPaper(1L, new PaperCreationRequest(
                "On Computable Numbers",
                "A foundational paper on computability theory.",
                PaperType.JOURNAL_ARTICLE,
                List.of("computability", "Turing machine", "decidability")));

        ResearchPaper p2 = rs.createPaper(2L, new PaperCreationRequest(
                "A Survey of Compiler Design",
                "Overview of compiler construction techniques.",
                PaperType.CONFERENCE_PAPER,
                List.of("compilers", "parsing", "code generation")));

        ResearchPaper p3 = rs.createPaper(3L, new PaperCreationRequest(
                "A Mathematical Theory of Communication",
                "Entropy, information channels, and error correction.",
                PaperType.JOURNAL_ARTICLE,
                List.of("information theory", "entropy", "channel capacity")));

        System.out.println("  p1: " + p1);
        System.out.println("  p2: " + p2);
        System.out.println("  p3: " + p3);

        // ── TEST 2: auto-registered as first author ───────────────────────────
        System.out.println("\n--- TEST 2: creator auto-registered as author ---");
        List<Authorship> p1Authors = rs.getPaperAuthors(p1.getId());
        System.out.println("  p1 authors: " + p1Authors.size() + " (expect 1)");
        System.out.println("  " + p1Authors.get(0));

        // ── TEST 3: addAuthor ─────────────────────────────────────────────────
        System.out.println("\n--- TEST 3: addAuthor ---");
        rs.addAuthor(p1.getId(), 2L, 2, "Literature review");
        rs.addAuthor(p1.getId(), 3L, 3, "Proof verification");
        List<Authorship> p1AuthorsNow = rs.getPaperAuthors(p1.getId());
        System.out.println("  p1 authors after adds: " + p1AuthorsNow.size() + " (expect 3)");
        p1AuthorsNow.forEach(a -> System.out.println("  " + a));

        // ── TEST 4: getResearcherPapers ───────────────────────────────────────
        System.out.println("\n--- TEST 4: getResearcherPapers ---");
        System.out.println("  Turing's papers: "
                + rs.getResearcherPapers(1L).size() + " (expect 1)");
        System.out.println("  Hopper's papers: "
                + rs.getResearcherPapers(2L).size() + " (expect 2)");
        System.out.println("  Shannon's papers: "
                + rs.getResearcherPapers(3L).size() + " (expect 2)");

        // ── TEST 5: submitPaperForReview ──────────────────────────────────────
        System.out.println("\n--- TEST 5: submitPaperForReview ---");
        rs.submitPaperForReview(p1.getId());
        System.out.println("  p1 status: " + p1.getStatus() + " (expect UNDER_REVIEW)");

        // ── TEST 6: publishPaper ──────────────────────────────────────────────
        System.out.println("\n--- TEST 6: publishPaper ---");
        rs.publishPaper(p1.getId(), "10.1112/plms/s2-42.1.230", "https://papers.uni.edu/p1.pdf");
        System.out.println("  p1 status: "  + p1.getStatus()  + " (expect PUBLISHED)");
        System.out.println("  p1 DOI: "     + p1.getDoi());
        System.out.println("  p1 fileUrl: " + p1.getFileUrl());

        // ── TEST 7: citePaper ─────────────────────────────────────────────────
        System.out.println("\n--- TEST 7: citePaper ---");
        rs.citePaper(p1.getId());
        rs.citePaper(p1.getId());
        rs.citePaper(p1.getId());
        System.out.println("  p1 citations: " + p1.getCitationCount() + " (expect 3)");

        // ── TEST 8: IResearcher.getTotalCitations ─────────────────────────────
        System.out.println("\n--- TEST 8: getTotalCitations ---");
        System.out.println("  Turing total citations: "
                + prof.getTotalCitations() + " (expect 3)");
        System.out.println("  Hopper total citations: "
                + master.getTotalCitations() + " (expect 3 — co-author of p1)");

        // ── TEST 9: rejectPaper and resubmit ──────────────────────────────────
        System.out.println("\n--- TEST 9: rejectPaper + resubmit ---");
        rs.submitPaperForReview(p2.getId());
        rs.rejectPaper(p2.getId(), "Insufficient novelty");
        System.out.println("  p2 status after reject: " + p2.getStatus()
                + " (expect REJECTED)");
        rs.submitPaperForReview(p2.getId()); // can re-submit from REJECTED
        System.out.println("  p2 status after resubmit: " + p2.getStatus()
                + " (expect UNDER_REVIEW)");

        // ── TEST 10: removeAuthor ─────────────────────────────────────────────
        System.out.println("\n--- TEST 10: removeAuthor ---");
        rs.removeAuthor(p1.getId(), 3L); // remove Shannon from p1
        System.out.println("  p1 authors after remove: "
                + rs.getPaperAuthors(p1.getId()).size() + " (expect 2)");
        System.out.println("  Shannon's papers now: "
                + rs.getResearcherPapers(3L).size() + " (expect 1 — only p3)");

        // ── TEST 11: searchPapers — title query ───────────────────────────────
        System.out.println("\n--- TEST 11: searchPapers by title ---");
        List<ResearchPaper> found = rs.searchPapers("theory", null);
        System.out.println("  'theory' hits: " + found.size() + " (expect 2)");
        found.forEach(p -> System.out.println("  - " + p.getTitle()));

        // ── TEST 12: searchPapers — filter by type ────────────────────────────
        System.out.println("\n--- TEST 12: searchPapers filter by JOURNAL_ARTICLE ---");
        PaperSearchFilters typeFilter = new PaperSearchFilters()
                .withType(PaperType.JOURNAL_ARTICLE);
        List<ResearchPaper> journals = rs.searchPapers(null, typeFilter);
        System.out.println("  Journal articles: " + journals.size() + " (expect 2)");

        // ── TEST 13: searchPapers — filter by status ──────────────────────────
        System.out.println("\n--- TEST 13: searchPapers filter by PUBLISHED ---");
        PaperSearchFilters statusFilter = new PaperSearchFilters()
                .withStatus(ResearchStatus.PUBLISHED);
        List<ResearchPaper> published = rs.searchPapers(null, statusFilter);
        System.out.println("  Published papers: " + published.size() + " (expect 1)");

        // ── TEST 14: searchPapers — filter by keyword ─────────────────────────
        System.out.println("\n--- TEST 14: searchPapers filter by keyword 'entropy' ---");
        PaperSearchFilters kwFilter = new PaperSearchFilters().withKeyword("entropy");
        List<ResearchPaper> kwHits = rs.searchPapers(null, kwFilter);
        System.out.println("  'entropy' keyword hits: " + kwHits.size() + " (expect 1)");

        // ── TEST 15: searchPapers — filter by researcher ──────────────────────
        System.out.println("\n--- TEST 15: searchPapers filter by researcherId ---");
        PaperSearchFilters rFilter = new PaperSearchFilters().withResearcherId(2L);
        List<ResearchPaper> hopperPapers = rs.searchPapers(null, rFilter);
        System.out.println("  Hopper's papers via filter: " + hopperPapers.size() + " (expect 2)");

        // ── TEST 16: guard — non-researcher cannot create paper ───────────────
        System.out.println("\n--- TEST 16: non-researcher create guard ---");
        try {
            rs.createPaper(99L, new PaperCreationRequest(
                    "Unauthorized", "", PaperType.PREPRINT, null));
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalArgumentException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 17: guard — add author to PUBLISHED paper ────────────────────
        System.out.println("\n--- TEST 17: addAuthor to PUBLISHED paper guard ---");
        try {
            rs.addAuthor(p1.getId(), 3L, 4, "Late addition");
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalStateException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 18: guard — publish without UNDER_REVIEW ─────────────────────
        System.out.println("\n--- TEST 18: publish DRAFT paper guard ---");
        try {
            rs.publishPaper(p3.getId(), "10.0000/test", "https://test.edu/p3.pdf");
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalStateException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 19: guard — cite non-published paper ─────────────────────────
        System.out.println("\n--- TEST 19: cite non-published paper guard ---");
        try {
            rs.citePaper(p3.getId()); // p3 is still DRAFT
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalStateException ex) {
            System.out.println("  Correctly rejected: " + ex.getMessage());
        }

        // ── TEST 20: guard — remove last author ───────────────────────────────
        System.out.println("\n--- TEST 20: remove last author guard ---");
        try {
            rs.removeAuthor(p3.getId(), 3L); // Shannon is the only author of p3
            System.out.println("  ERROR: should have thrown!");
        } catch (IllegalStateException ex) {
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
