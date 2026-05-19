import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Сохранение всей "базы" на компьютер.
 * Сериализация репозиториев в один файл university.ser.
 */
public class UniversityDataStore {

    private static final String DATA_DIR = "data";
    private static final String DATA_FILE = DATA_DIR + File.separator + "university.ser";

    private UniversityDataStore() {}

    public static State load() {
        // Если файла нет, ApplicationContext потом сам создаст демо данные.
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object data = in.readObject();
            if (data instanceof State) {
                System.out.println("[DataStore] Loaded data from " + file.getAbsolutePath());
                return (State) data;
            }
        } catch (Exception ex) {
            System.out.println("[DataStore] Could not load saved data: " + ex.getMessage());
        }
        return null;
    }

    public static void save(ApplicationContext ctx) {

        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(DATA_FILE);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(new State(ctx));
            System.out.println("[DataStore] Saved data to " + file.getAbsolutePath());
        } catch (Exception ex) {
            System.out.println("[DataStore] Could not save data: " + ex.getMessage());
        }
    }

    public static class State implements Serializable {
        // В State кладем именно репозитории, потому что в них лежит вся текущая инфа.
        private static final long serialVersionUID = 1L;

        final UserRepository userRepository;
        final StudentRepository studentRepository;
        final TeacherRepository teacherRepository;
        final CourseRepository courseRepository;
        final EnrollmentRepository enrollmentRepository;
        final MarkRepository markRepository;
        final FacultyRepository facultyRepository;
        final DepartmentRepository departmentRepository;
        final ComplaintRepository complaintRepository;
        final MessageRepository messageRepository;
        final NewsRepository newsRepository;
        final ManagerRepository managerRepository;
        final OrganizationRepository organizationRepository;
        final MembershipRepository membershipRepository;
        final MembershipApplicationRepository membershipApplicationRepository;
        final ResearchPaperRepository researchPaperRepository;
        final AuthorshipRepository authorshipRepository;

        State(ApplicationContext ctx) {
            userRepository = ctx.getUserRepository();
            studentRepository = ctx.getStudentRepository();
            teacherRepository = ctx.getTeacherRepository();
            courseRepository = ctx.getCourseRepository();
            enrollmentRepository = ctx.getEnrollmentRepository();
            markRepository = ctx.getMarkRepository();
            facultyRepository = ctx.getFacultyRepository();
            departmentRepository = ctx.getDepartmentRepository();
            complaintRepository = ctx.getComplaintRepository();
            messageRepository = ctx.getMessageRepository();
            newsRepository = ctx.getNewsRepository();
            managerRepository = ctx.getManagerRepository();
            organizationRepository = ctx.getOrganizationRepository();
            membershipRepository = ctx.getMembershipRepository();
            membershipApplicationRepository = ctx.getMembershipApplicationRepository();
            researchPaperRepository = ctx.getResearchPaperRepository();
            authorshipRepository = ctx.getAuthorshipRepository();
        }
    }
}
