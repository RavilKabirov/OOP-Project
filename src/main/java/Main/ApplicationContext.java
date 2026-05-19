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
    private final ResearchPaperRepository researchPaperRepository;
    private final AuthorshipRepository authorshipRepository;

    // Сервисы
    private final EnrollmentService enrollmentService;
    private final MarkService markService;
    private final TranscriptService transcriptService;
    private final FacultyService facultyService;
    private final ComplaintService complaintService;
    private final MessageService messageService;
    private final ReportService reportService;
    private final ResearchService researchService;

    // Конструктор

    public ApplicationContext() {

        // Между репо нету зависимостей
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
        researchPaperRepository = new ResearchPaperRepository();
        authorshipRepository = new AuthorshipRepository();

        // У сервисов зависимость с нужными репозиториями
        enrollmentService = new EnrollmentService(enrollmentRepository, studentRepository, courseRepository);

        markService = new MarkService(markRepository, enrollmentRepository, teacherRepository);

        transcriptService = new TranscriptService(enrollmentRepository, markRepository, studentRepository);

        facultyService = new FacultyService(facultyRepository, departmentRepository, teacherRepository);

        complaintService = new ComplaintService(complaintRepository, teacherRepository, studentRepository);

        messageService = new MessageService(messageRepository, userRepository);

        reportService = new ReportService(enrollmentRepository, markRepository, teacherRepository,studentRepository, courseRepository, departmentRepository, facultyRepository);

        researchService = new ResearchService(researchPaperRepository, authorshipRepository, userRepository);
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
    public ResearchPaperRepository getResearchPaperRepository(){ return researchPaperRepository; }
    public AuthorshipRepository getAuthorshipRepository() { return authorshipRepository; }

    // Гетеры сервисов

    public EnrollmentService getEnrollmentService(){ return enrollmentService; }
    public MarkService  getMarkService() { return markService; }
    public TranscriptService getTranscriptService(){ return transcriptService; }
    public FacultyService  getFacultyService() { return facultyService; }
    public ComplaintService  getComplaintService(){ return complaintService; }
    public MessageService  getMessageService() { return messageService; }
    public ReportService getReportService(){ return reportService; }
    public ResearchService getResearchService(){ return researchService; }
}