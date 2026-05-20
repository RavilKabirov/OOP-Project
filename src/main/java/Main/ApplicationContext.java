/**
 * Централизованный класс дял хранения всех зависимостей. 
 *
 * Здесь созданы все репозитории и сервисы используемые в приложении. Все они в интерфейсах вызываются строго через гетер этого класса
 *
 * Если нужно добавить новый сервис:
 * 1. Добавить private final field у класса
 * 2. Добавить его в конструктор класса
 * 3. Добавить геттер в класс
 */
public class ApplicationContext {

    // Репозитории
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final MarkRepository markRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final ComplaintRepository complaintRepository;
    private final MessageRepository messageRepository;
    private final NewsRepository newsRepository;
    private final ManagerRepository managerRepository;
    private final OrganizationRepository organizationRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipApplicationRepository membershipApplicationRepository;
    private final ResearchPaperRepository researchPaperRepository;
    private final AuthorshipRepository authorshipRepository;
    private final LessonRepository lessonRepository;
    private final TechSupportRequestRepository techSupportRequestRepository;

    // Сервисы
    private final UserService userService;
    private final CourseService courseService;
    private final DepartmentService departmentService;
    private final TeacherService teacherService;
    private final EnrollmentService enrollmentService;
    private final MarkService markService;
    private final TranscriptService transcriptService;
    private final FacultyService facultyService;
    private final ComplaintService complaintService;
    private final MessageService messageService;
    private final NewsService newsService;
    private final OrganizationService organizationService;
    private final WorkloadService workloadService;
    private final ReportService reportService;
    private final ResearchService researchService;
    private final LessonService lessonService;
    private final PhDSupervisorService phdSupervisorService;
    private final TechSupportService techSupportService;

    // Конструктор

    public ApplicationContext() {

        // Между репо нету зависимостей
        // Сначала пробуем загрузить сохраненые данные. Если их нет, создаем пустые репозитории.
        UniversityDataStore.State data = UniversityDataStore.load();
        boolean loadedFromDisk = data != null;

        if (loadedFromDisk) {
            userRepository = data.userRepository;
            studentRepository = data.studentRepository;
            teacherRepository = data.teacherRepository;
            courseRepository = data.courseRepository;
            enrollmentRepository = data.enrollmentRepository;
            markRepository = data.markRepository;
            facultyRepository = data.facultyRepository;
            departmentRepository = data.departmentRepository;
            complaintRepository = data.complaintRepository;
            messageRepository = data.messageRepository;
            newsRepository = data.newsRepository;
            managerRepository = data.managerRepository;
            organizationRepository = data.organizationRepository;
            membershipRepository = data.membershipRepository;
            membershipApplicationRepository = data.membershipApplicationRepository;
            researchPaperRepository = data.researchPaperRepository;
            authorshipRepository = data.authorshipRepository;
            lessonRepository = data.lessonRepository != null ? data.lessonRepository : new LessonRepository();
            techSupportRequestRepository = data.techSupportRequestRepository != null ? data.techSupportRequestRepository : new TechSupportRequestRepository();
        } else {
            userRepository = new UserRepository();
            studentRepository = new StudentRepository();
            teacherRepository = new TeacherRepository();
            courseRepository = new CourseRepository();
            enrollmentRepository = new EnrollmentRepository();
            markRepository = new MarkRepository();
            facultyRepository = new FacultyRepository();
            departmentRepository = new DepartmentRepository();
            complaintRepository = new ComplaintRepository();
            messageRepository = new MessageRepository();
            newsRepository = new NewsRepository();
            managerRepository = new ManagerRepository();
            organizationRepository = new OrganizationRepository();
            membershipRepository = new MembershipRepository();
            membershipApplicationRepository = new MembershipApplicationRepository();
            researchPaperRepository = new ResearchPaperRepository();
            authorshipRepository = new AuthorshipRepository();
            lessonRepository = new LessonRepository();
            techSupportRequestRepository = new TechSupportRequestRepository();
        }

        // У сервисов зависимость с нужными репозиториями
        userService = new UserService(userRepository);

        courseService = new CourseService(courseRepository, departmentRepository, teacherRepository);

        departmentService = new DepartmentService(departmentRepository, facultyRepository, teacherRepository, courseRepository);

        teacherService = new TeacherService(teacherRepository, departmentRepository);

        enrollmentService = new EnrollmentService(enrollmentRepository, studentRepository, courseRepository);

        markService = new MarkService(markRepository, enrollmentRepository, teacherRepository);

        transcriptService = new TranscriptService(enrollmentRepository, markRepository, studentRepository);

        facultyService = new FacultyService(facultyRepository, departmentRepository, teacherRepository);

        complaintService = new ComplaintService(complaintRepository, teacherRepository, studentRepository);

        messageService = new MessageService(messageRepository, userRepository);

        newsService = new NewsService(newsRepository, userRepository);

        organizationService = new OrganizationService(organizationRepository, membershipRepository, membershipApplicationRepository, studentRepository);

        workloadService = new WorkloadService(courseRepository, teacherRepository, departmentRepository);

        reportService = new ReportService(enrollmentRepository, markRepository, teacherRepository,studentRepository, courseRepository, departmentRepository, facultyRepository);

        researchService = new ResearchService(researchPaperRepository, authorshipRepository, userRepository);

        lessonService = new LessonService(lessonRepository, courseRepository, teacherRepository, studentRepository);

        phdSupervisorService = new PhDSupervisorService(studentRepository, teacherRepository);

        techSupportService = new TechSupportService(techSupportRequestRepository, userRepository);

        if (!loadedFromDisk) {
            // Первый запуск приложения, поэтому заполняем чуть-чуть данных для теста.
            seedDemoData();
        }
        ensureDemoTechSupportSpecialist();
    }

    // Гетеры репо

    public UserRepository getUserRepository() { return userRepository; }
    public StudentRepository getStudentRepository() { return studentRepository; }
    public TeacherRepository getTeacherRepository() { return teacherRepository; }
    public CourseRepository getCourseRepository() { return courseRepository; }
    public EnrollmentRepository getEnrollmentRepository() { return enrollmentRepository; }
    public MarkRepository getMarkRepository() { return markRepository; }
    public FacultyRepository getFacultyRepository() { return facultyRepository; }
    public DepartmentRepository getDepartmentRepository(){ return departmentRepository; }
    public ComplaintRepository getComplaintRepository(){ return complaintRepository; }
    public MessageRepository getMessageRepository() { return messageRepository; }
    public NewsRepository getNewsRepository() { return newsRepository; }
    public ManagerRepository getManagerRepository() { return managerRepository; }
    public OrganizationRepository getOrganizationRepository() { return organizationRepository; }
    public MembershipRepository getMembershipRepository() { return membershipRepository; }
    public MembershipApplicationRepository getMembershipApplicationRepository() { return membershipApplicationRepository; }
    public ResearchPaperRepository getResearchPaperRepository(){ return researchPaperRepository; }
    public AuthorshipRepository getAuthorshipRepository() { return authorshipRepository; }
    public LessonRepository getLessonRepository() { return lessonRepository; }
    public TechSupportRequestRepository getTechSupportRequestRepository() { return techSupportRequestRepository; }

    // Гетеры сервисов

    public UserService getUserService() { return userService; }
    public CourseService getCourseService() { return courseService; }
    public DepartmentService getDepartmentService() { return departmentService; }
    public TeacherService getTeacherService() { return teacherService; }
    public EnrollmentService getEnrollmentService(){ return enrollmentService; }
    public MarkService  getMarkService() { return markService; }
    public TranscriptService getTranscriptService(){ return transcriptService; }
    public FacultyService  getFacultyService() { return facultyService; }
    public ComplaintService  getComplaintService(){ return complaintService; }
    public MessageService  getMessageService() { return messageService; }
    public NewsService getNewsService() { return newsService; }
    public OrganizationService getOrganizationService() { return organizationService; }
    public WorkloadService getWorkloadService() { return workloadService; }
    public ReportService getReportService(){ return reportService; }
    public ResearchService getResearchService(){ return researchService; }
    public LessonService getLessonService() { return lessonService; }
    public PhDSupervisorService getPhdSupervisorService() { return phdSupervisorService; }
    public TechSupportService getTechSupportService() { return techSupportService; }

    public void saveData() {
        // Вызывается при закрытии приложения из MainFinal.
        UniversityDataStore.save(this);
    }

    private void seedDemoData() {
        // Демо база нужна только чтобы сразу можно было зайти разными ролями и потыкать меню.
        PasswordEncoder encoder = new PasswordEncoder();

        Admin admin = new Admin("admin@admin.com", "Admin", "Default");
        admin.setEmployeeId("ADMIN001");
        admin.setAccessLevel(AdminLevel.SUPERADMIN);
        admin.setPasswordHash(encoder.encode("admin"));
        userRepository.save(admin);

        School site = facultyService.createFaculty(new FacultyCreationRequest("School of Information Technology"));
        School bs = facultyService.createFaculty(new FacultyCreationRequest("Business School"));

        Department se = new Department();
        se.setName("Software Engineering");
        se.setCode("SE");
        facultyService.addDepartmentToFaculty(site.getId(), se);

        Department is = new Department();
        is.setName("Information Systems");
        is.setCode("IS");
        facultyService.addDepartmentToFaculty(site.getId(), is);

        Department management = new Department();
        management.setName("Management");
        management.setCode("MGT");
        facultyService.addDepartmentToFaculty(bs.getId(), management);

        Dean dean = new Dean("dean@uni.edu", "Dana", "Dean");
        dean.setEmployeeId("D001");
        dean.setSchool(site);
        dean.setPasswordHash(encoder.encode("password"));
        userRepository.save(dean);
        managerRepository.save(dean);
        site.setDean(dean);
        facultyRepository.save(site);

        HRManager hr = new HRManager("hr@uni.edu", "Helen", "Recruiter");
        hr.setEmployeeId("HR001");
        hr.setLevel(ManagerLevel.UNIVERSITY);
        hr.setSpecialization("Academic hiring");
        hr.setHiringQuota(5);
        hr.setOpenVacancies(2);
        hr.setPasswordHash(encoder.encode("password"));
        userRepository.save(hr);
        managerRepository.save(hr);

        PRManager pr = new PRManager("pr@uni.edu", "Peter", "Relations");
        pr.setEmployeeId("PR001");
        pr.setLevel(ManagerLevel.UNIVERSITY);
        pr.setCampaignFocus("Admissions and student life");
        pr.addManagedChannel("Website");
        pr.addManagedChannel("Instagram");
        pr.setPasswordHash(encoder.encode("password"));
        userRepository.save(pr);
        managerRepository.save(pr);

        Teacher turing = new Teacher("turing@uni.edu", "Alan", "Turing");
        turing.setEmployeeId("T001");
        turing.setDepartment(se);
        turing.setTeacherPosition(TeacherPosition.PROFESSOR);
        turing.setPasswordHash(encoder.encode("password"));
        userRepository.save(turing);
        teacherRepository.save(turing);

        Teacher hopper = new Teacher("hopper.teacher@uni.edu", "Grace", "Hopper");
        hopper.setEmployeeId("T002");
        hopper.setDepartment(is);
        hopper.setTeacherPosition(TeacherPosition.SENIOR_LECTOR);
        hopper.setPasswordHash(encoder.encode("password"));
        userRepository.save(hopper);
        teacherRepository.save(hopper);

        se.setHead(turing);
        departmentRepository.save(se);

        BachelorStudent alice = new BachelorStudent();
        alice.setEmail("alice@uni.edu");
        alice.setFullName("Alice", "Student");
        alice.setStudentId("S001");
        alice.setYearOfStudy(2);
        alice.setGpa(3.4);
        alice.setTotalCredits(45);
        alice.setPasswordHash(encoder.encode("password"));
        userRepository.save(alice);
        studentRepository.save(alice);

        MasterStudent bob = new MasterStudent();
        bob.setEmail("bob@uni.edu");
        bob.setFullName("Bob", "Researcher");
        bob.setStudentId("M001");
        bob.setYearOfStudy(1);
        bob.setThesisTopic("Distributed systems testing");
        bob.setPasswordHash(encoder.encode("password"));
        userRepository.save(bob);
        studentRepository.save(bob);

        PHDStudent clara = new PHDStudent();
        clara.setEmail("clara@uni.edu");
        clara.setFullName("Clara", "Scientist");
        clara.setStudentId("P001");
        clara.setYearOfStudy(3);
        clara.setDissertationTopic("Explainable AI in education");
        clara.setSupervisor(turing);
        clara.setPasswordHash(encoder.encode("password"));
        userRepository.save(clara);
        studentRepository.save(clara);

        courseService.createCourse(new CourseCreationRequest("CS101", "Object-Oriented Programming", 5, CourseType.MAJOR));
        courseService.createCourse(new CourseCreationRequest("CS201", "Data Structures", 5, CourseType.MAJOR));
        courseService.createCourse(new CourseCreationRequest("BUS101", "Project Management", 3, CourseType.ELECTIVE));
        courseService.assignInstructor("CS101", turing.getId());
        courseService.assignInstructor("CS201", hopper.getId());

        enrollmentService.enrollStudent("S001", "CS101");
        enrollmentService.enrollStudent("M001", "CS101");
        enrollmentService.enrollStudent("P001", "CS201");
        markService.assignMark(turing.getId(), "S001", "CS101", 27, MarkType.FIRST_ATTESTATION, "Demo first attestation");
        markService.assignMark(turing.getId(), "S001", "CS101", 25, MarkType.SECOND_ATTESTATION, "Demo second attestation");
        markService.assignMark(turing.getId(), "S001", "CS101", 36, MarkType.FINAL_EXAM, "Demo final");

        complaintService.submitComplaint(turing.getId(), "S001", ManagerForComplaint.DEAN, UrgencyLevel.MEDIUM);

        ResearchPaper paper = researchService.createPaper(turing.getId(), new PaperCreationRequest( "Teaching OOP With Console Systems", "A small demo paper for testing the research workflow.", PaperType.CONFERENCE_PAPER, java.util.Arrays.asList("oop", "education", "console-ui")));
        researchService.addAuthor(paper.getId(), bob.getId(), 2, "Case study");
        researchService.submitPaperForReview(paper.getId());
        researchService.publishPaper(paper.getId(), "10.0000/demo-oop", "https://uni.example/papers/demo-oop.pdf");
        researchService.citePaper(paper.getId());

        Organization acm = organizationService.createOrganization( new OrganizationCreationRequest("ACM Student Chapter", "Programming contests and tech talks"), "S001");
        organizationService.applyForMembership("M001", acm.getId(), "I want to help with workshops.");

        newsService.publishNews(pr.getId(), "Welcome Week", "Welcome week starts on Monday. Check your schedule.");
        messageService.sendMessage(hr.getId(), turing.getId(), "Workload review", "Please review your assigned courses.");
        messageService.sendMessage(pr.getId(), dean.getId(), "News published", "Welcome Week news is now live.");

        System.out.println("[ApplicationContext] Demo database created. Admin: admin@admin.com/admin, demo password: password");
    }

    private void ensureDemoTechSupportSpecialist() {
        // Для старых сохраненок добавляем саппорта, иначе тестить роль будет нечем.
        if (userRepository.findByEmail("support@uni.edu").isPresent()) {
            return;
        }
        PasswordEncoder encoder = new PasswordEncoder();
        TechSupportSpecialist specialist = new TechSupportSpecialist("support@uni.edu", "Sam", "Support");
        specialist.setEmployeeId("SUP001");
        specialist.setSupportArea("Accounts and internal systems");
        specialist.setPasswordHash(encoder.encode("password"));
        userRepository.save(specialist);
    }
}
