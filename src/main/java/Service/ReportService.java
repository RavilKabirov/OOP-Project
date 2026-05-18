import java.time.LocalDate;
import java.util.*;

public class ReportService {

    private final EnrollmentRepository  enrollmentRepository;
    private final MarkRepository        markRepository;
    private final TeacherRepository     teacherRepository;
    private final StudentRepository     studentRepository;
    private final CourseRepository      courseRepository;
    private final DepartmentRepository  departmentRepository;
    private final FacultyRepository     facultyRepository;   // School = Faculty

    public ReportService(EnrollmentRepository enrollmentRepository,
                         MarkRepository markRepository,
                         TeacherRepository teacherRepository,
                         StudentRepository studentRepository,
                         CourseRepository courseRepository,
                         DepartmentRepository departmentRepository,
                         FacultyRepository facultyRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.markRepository       = markRepository;
        this.teacherRepository    = teacherRepository;
        this.studentRepository    = studentRepository;
        this.courseRepository     = courseRepository;
        this.departmentRepository = departmentRepository;
        this.facultyRepository    = facultyRepository;
    }


    public FacultyReport generateFacultyReport(Long facultyId, ReportPeriod period) {
        School school = requireSchool(facultyId);

        List<Department> depts = facultyRepository.findDepartmentsBySchoolId(facultyId);

        Set<Long> deptIds = new HashSet<>();
        for (Department d : depts) deptIds.add(d.getId());

        int teacherCount = 0;
        for (Teacher t : teacherRepository.findAll()) {
            if (t.getDepartment() != null && deptIds.contains(t.getDepartment().getId())) {
                teacherCount++;
            }
        }

        List<Enrollment> periodEnrollments = enrollmentsInPeriod(
                enrollmentRepository.findAll(), period);

        Set<String> studentIds = new HashSet<>();
        int completed = 0;
        for (Enrollment e : periodEnrollments) {
            if (e.getStudent() != null) studentIds.add(e.getStudent().getStudentId());
            if (e.getStatus() == EnrollmentStatus.COMPLETED) completed++;
        }

        double avgGpa = averageGpa(periodEnrollments);

        System.out.println("[ReportService] Faculty report generated for '"
                + school.getName() + "' period=" + period.getLabel());

        return new FacultyReport(facultyId, school.getName(), period,
                depts.size(), teacherCount, studentIds.size(),
                periodEnrollments.size(), completed, avgGpa);
    }

    public DepartmentReport generateDepartmentReport(Long deptId, ReportPeriod period) {
        Department dept = requireDepartment(deptId);

        List<Teacher> teachers = teacherRepository.findByDepartment(deptId);

        Set<String> courseIds = new HashSet<>();
        for (Teacher t : teachers) {
            for (Course c : t.getCourses()) courseIds.add(c.getCourseId());
        }

        List<Enrollment> all = enrollmentRepository.findAll();
        List<Enrollment> relevant = new ArrayList<>();
        for (Enrollment e : enrollmentsInPeriod(all, period)) {
            if (e.getCourse() != null && courseIds.contains(e.getCourse().getCourseId())) {
                relevant.add(e);
            }
        }

        int completed = 0;
        for (Enrollment e : relevant) {
            if (e.getStatus() == EnrollmentStatus.COMPLETED) completed++;
        }

        double avgGpa = averageGpa(relevant);

        System.out.println("[ReportService] Department report generated for '"
                + dept.getName() + "' period=" + period.getLabel());

        return new DepartmentReport(deptId, dept.getName(), period,
                teachers.size(), courseIds.size(),
                relevant.size(), completed, avgGpa);
    }

    public TeacherWorkloadReport generateTeacherWorkloadReport(Long teacherId,
                                                               Semester semester) {
        Teacher teacher = requireTeacher(teacherId);
        ReportPeriod period = semester.toReportPeriod();

        List<Course>  courses     = teacher.getCourses();
        List<String>  courseNames = new ArrayList<>();
        Set<String>   studentIds  = new HashSet<>();
        int           totalEnroll = 0;

        for (Course c : courses) {
            courseNames.add(c.getName());
            for (Enrollment e : enrollmentsInPeriod(
                    enrollmentRepository.findAll(), period)) {
                if (e.getCourse() != null
                        && e.getCourse().getCourseId().equals(c.getCourseId())) {
                    totalEnroll++;
                    if (e.getStudent() != null) {
                        studentIds.add(e.getStudent().getStudentId());
                    }
                }
            }
        }

        System.out.println("[ReportService] Workload report for '"
                + teacher.getFullName() + "' semester=" + semester);

        return new TeacherWorkloadReport(teacherId, teacher.getFullName(),
                semester, courses.size(), studentIds.size(),
                totalEnroll, courseNames);
    }

    public StudentProgressReport generateStudentProgressReport(String studentId) {
        Student student = requireStudent(studentId);

        List<Enrollment> all = enrollmentRepository.findByStudent(studentId);

        int completed = 0, active = 0, dropped = 0, credits = 0;
        List<String> completedNames = new ArrayList<>();
        double totalScore = 0;
        int    scoredCount = 0;

        for (Enrollment e : all) {
            switch (e.getStatus()) {
                case COMPLETED:
                    completed++;
                    if (e.getCourse() != null) {
                        completedNames.add(e.getCourse().getName());
                        credits += e.getCourse().getCredits();
                    }
                    if (e.getMark() != null) {
                        Mark m = e.getMark() != null ? e.getMark()
                                : markRepository.findByEnrollment(e.getId());
                        if (m != null) { totalScore += m.calculateTotal(); scoredCount++; }
                    }
                    break;
                case ACTIVE:    active++;  break;
                case REJECTED:   dropped++; break;
                default:        break;
            }
        }

        for (Enrollment e : all) {
            if (e.getStatus() == EnrollmentStatus.ACTIVE) {
                Mark m = e.getMark() != null ? e.getMark()
                        : markRepository.findByEnrollment(e.getId());
                if (m != null) { totalScore += m.calculateTotal(); scoredCount++; }
            }
        }

        double gpa = scoredCount > 0 ? totalScore / scoredCount : 0.0;

        System.out.println("[ReportService] Progress report for student " + studentId);

        return new StudentProgressReport(studentId, student.getFullName(),
                student.getYearOfStudy(), gpa, credits,
                completed, active, dropped, completedNames);
    }

    public CoursePerformanceReport generateCoursePerformanceReport(String courseId) {
        Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Course not found: " + courseId));

        List<Enrollment> all = enrollmentRepository.findAll();
        List<Enrollment> forCourse = new ArrayList<>();
        for (Enrollment e : all) {
            if (e.getCourse() != null
                    && e.getCourse().getCourseId().equals(courseId)) {
                forCourse.add(e);
            }
        }

        int completed = 0, active = 0, dropped = 0;
        int pass = 0, fail = 0;
        double sum = 0, highest = Double.MIN_VALUE, lowest = Double.MAX_VALUE;
        int graded = 0;

        for (Enrollment e : forCourse) {
            switch (e.getStatus()) {
                case COMPLETED: completed++; break;
                case ACTIVE:    active++;    break;
                case REJECTED:   dropped++;   break;
                default:        break;
            }
            Mark m = e.getMark() != null ? e.getMark()
                    : markRepository.findByEnrollment(e.getId());
            if (m != null) {
                double total = m.calculateTotal();
                sum += total;
                if (total > highest) highest = total;
                if (total < lowest)  lowest  = total;
                if (total >= 50) pass++; else fail++;
                graded++;
            }
        }

        double avg = graded > 0 ? sum / graded : 0.0;
        if (graded == 0) { highest = 0; lowest = 0; }

        System.out.println("[ReportService] Performance report for course " + courseId);

        return new CoursePerformanceReport(courseId, course.getName(),
                course.getCredits(), forCourse.size(),
                completed, active, dropped,
                avg, highest, lowest, pass, fail);
    }

    public EnrollmentStatistics generateEnrollmentStatistics(ReportPeriod period) {
        List<Enrollment> inPeriod = enrollmentsInPeriod(
                enrollmentRepository.findAll(), period);

        int active = 0, completed = 0, dropped = 0, pending = 0;
        Set<String> students = new HashSet<>();
        Set<String> courses  = new HashSet<>();

        for (Enrollment e : inPeriod) {
            switch (e.getStatus()) {
                case ACTIVE:    active++;    break;
                case COMPLETED: completed++; break;
                case REJECTED:   dropped++;   break;
                case PENDING:   pending++;   break;
            }
            if (e.getStudent() != null) students.add(e.getStudent().getStudentId());
            if (e.getCourse()  != null) courses.add(e.getCourse().getCourseId());
        }

        System.out.println("[ReportService] Enrollment statistics for period "
                + period.getLabel());

        return new EnrollmentStatistics(period, inPeriod.size(),
                active, completed, dropped, pending,
                students.size(), courses.size());
    }

    public HRReport generateHRReport() {
        List<Teacher> teachers = teacherRepository.findAll();

        Map<String, Integer> byPosition   = new LinkedHashMap<>();
        Map<String, Integer> byDepartment = new LinkedHashMap<>();
        int noCoursesCount = 0;
        int totalCourses   = 0;

        for (Teacher t : teachers) {
            String pos = t.getTeacherPosition() != null
                    ? t.getTeacherPosition().name() : "UNASSIGNED";
            byPosition.merge(pos, 1, Integer::sum);

            String dept = t.getDepartment() != null
                    ? t.getDepartment().getName() : "UNASSIGNED";
            byDepartment.merge(dept, 1, Integer::sum);

            int courseCount = t.getCourses().size();
            totalCourses += courseCount;
            if (courseCount == 0) noCoursesCount++;
        }

        double avg = teachers.isEmpty() ? 0.0
                : (double) totalCourses / teachers.size();

        int deptCount = departmentRepository.findAll().size();

        System.out.println("[ReportService] HR report generated.");

        return new HRReport(teachers.size(), deptCount,
                byPosition, byDepartment, avg, noCoursesCount);
    }

    private List<Enrollment> enrollmentsInPeriod(List<Enrollment> all,
                                                  ReportPeriod period) {
        List<Enrollment> result = new ArrayList<>();
        for (Enrollment e : all) {
            if (e.getEnrollmentDate() != null) {
                LocalDate d = e.getEnrollmentDate().toLocalDate();
                if (period.contains(d)) result.add(e);
            } else {
                // If date is unknown, include it (conservative)
                result.add(e);
            }
        }
        return result;
    }

    private double averageGpa(List<Enrollment> enrollments) {
        double sum = 0;
        int    n   = 0;
        for (Enrollment e : enrollments) {
            Mark m = e.getMark() != null ? e.getMark()
                    : markRepository.findByEnrollment(e.getId());
            if (m != null) { sum += m.calculateTotal(); n++; }
        }
        return n > 0 ? sum / n : 0.0;
    }

    private School requireSchool(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "School not found with id: " + id));
    }

    private Department requireDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Department not found with id: " + id));
    }

    private Teacher requireTeacher(Long id) {
        return teacherRepository.findAll().stream()
                .filter(t -> t.getId() != null && t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Teacher not found with id: " + id));
    }

    private Student requireStudent(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found: " + studentId));
    }
}