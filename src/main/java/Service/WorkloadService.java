import java.util.*;

public class WorkloadService {

    private final CourseRepository     courseRepository;
    private final TeacherRepository    teacherRepository;
    private final DepartmentRepository departmentRepository;

    public WorkloadService(CourseRepository courseRepository, TeacherRepository teacherRepository, DepartmentRepository departmentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.departmentRepository = departmentRepository;
    }

    public void assignTeacherToCourse(Long teacherId, Long courseId, int hours) {
        assignTeacherToCourse(teacherId, String.valueOf(courseId), hours);
    }

    public void assignTeacherToCourse(Long teacherId, String courseId, int hours) {
        if (hours <= 0) throw new IllegalArgumentException("Hours must be positive.");


        Teacher teacher = teacherRepository.findAll().stream().filter(t -> t.getId() != null && t.getId().equals(teacherId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));

        Course course = courseRepository.findByCourseId(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));

        boolean alreadyAssigned = course.getTeachers().stream().anyMatch(t -> t.getId() != null && t.getId().equals(teacherId));
        if (alreadyAssigned) {
            // Не назначаем teacher на тот же course второй раз.
            throw new IllegalStateException("Teacher is already assigned to this course.");
        }

        course.addTeacher(teacher);
        teacher.addCourse(course);

        courseRepository.save(course);

        System.out.println("[WorkloadService] Assigned " + teacher.getFullName() + " to '" + course.getName() + "' for " + hours + " hours.");
    }

    public void removeTeacherFromCourse(Long teacherId, Long courseId) {
        removeTeacherFromCourse(teacherId, String.valueOf(courseId));
    }

    public void removeTeacherFromCourse(Long teacherId, String courseId) {
        Teacher teacher = teacherRepository.findAll().stream().filter(t -> t.getId() != null && t.getId().equals(teacherId)).findFirst().orElseThrow(() -> new IllegalArgumentException( "Teacher not found with id: " + teacherId));

        Course course = courseRepository.findByCourseId(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));

        boolean assigned = course.getTeachers().stream().anyMatch(t -> t.getId() != null && t.getId().equals(teacherId));
        if (!assigned) {
            // Если teacher не был назначен, remove делать нельзя.
            throw new IllegalStateException("Teacher is not assigned to this course.");
        }

        course.removeTeacher(teacher);
        teacher.removeCourse(course);
        courseRepository.save(course);

        System.out.println("[WorkloadService] Removed " + teacher.getFullName() + " from '" + course.getName() + "'.");
    }

    public WorkloadReport getDepartmentWorkload(Long deptId) {
        Department dept = departmentRepository.findById(deptId).orElseThrow(() -> new IllegalArgumentException( "Department not found with id: " + deptId));

        List<Teacher> teachers = teacherRepository.findByDepartment(deptId);

        WorkloadReport report = new WorkloadReport("Workload Report — Department: " + dept.getName());

        for (Teacher teacher : teachers) {
            TeacherWorkload workload = new TeacherWorkload(teacher, new Semester(2024, Semester.SemesterTerm.FALL));
            for (Course course : teacher.getCourses()) {
                workload.addCourse(course, course.getCredits() * 15);
            }
            report.addWorkload(workload);
        }

        return report;
    }

    public WorkloadReport getFacultyWorkload(Long facultyId) {

        List<Teacher> allTeachers = teacherRepository.findAll();

        WorkloadReport report = new WorkloadReport("Workload Report — Faculty id: " + facultyId);

        for (Teacher teacher : allTeachers) {
            TeacherWorkload workload = new TeacherWorkload(teacher, new Semester(2024, Semester.SemesterTerm.FALL));
            for (Course course : teacher.getCourses()) {
                workload.addCourse(course, course.getCredits() * 15);
            }
            report.addWorkload(workload);
        }

        return report;
    }

    public void approveWorkload(Long facultyId, Long approvedBy) {

        boolean approverExists = teacherRepository.findAll().stream().anyMatch(t -> t.getId() != null && t.getId().equals(approvedBy));
        if (!approverExists) {
            // Approver должен существовать в системе, иначе отчет будет утвержден непонятно кем.
            throw new IllegalArgumentException("Approver not found with id: " + approvedBy);
        }

        WorkloadReport report = getFacultyWorkload(facultyId);
        report.approve(approvedBy);

        System.out.println("[WorkloadService] Workload approved by id=" + approvedBy);
        System.out.println(report);
    }

    public TeacherWorkload getTeacherWorkload(Long teacherId, Semester semester) {
        Teacher teacher = teacherRepository.findAll().stream().filter(t -> t.getId() != null && t.getId().equals(teacherId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));

        TeacherWorkload workload = new TeacherWorkload(teacher, semester);
        for (Course course : teacher.getCourses()) {
            workload.addCourse(course, course.getCredits() * 15);
        }
        return workload;
    }
}
