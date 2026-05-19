import java.util.*;

public class WorkloadService {

    private CourseRepository courseRepository;
    private TeacherRepository teacherRepository;
    private DepartmentRepository departmentRepository;

    public WorkloadService(CourseRepository courseRepository,
                           TeacherRepository teacherRepository,
                           DepartmentRepository departmentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.departmentRepository = departmentRepository;
    }

    public void assignTeacherToCourse(Long teacherId, Long courseId, int hours) {
        if (hours <= 0) {
            throw new IllegalArgumentException("Hours must be positive");
        }

        String teacherEmpId = String.valueOf(teacherId);
        Teacher teacher = teacherRepository.findByEmployeeId(teacherEmpId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Teacher not found with id: " + teacherId
                ));

        Course course = courseRepository.findByCourseId(String.valueOf(courseId))
                .orElseThrow(() -> new IllegalArgumentException(
                    "Course not found with id: " + courseId
                ));

        for (Teacher t : course.getTeachers()) {
            if (teacherEmpId.equals(t.getEmployeeId())) {
                throw new IllegalStateException(
                    "Teacher is already assigned to this course"
                );
            }
        }

        course.addTeacher(teacher);
        teacher.addCourse(course);

        System.out.println("Assigned " + teacher.getFullName() + 
        	    " to " + course.getName() + " for " + hours + " hours");
        	courseRepository.save(course);courseRepository.save(course);
    }

    public void removeTeacherFromCourse(Long teacherId, Long courseId) {
        String teacherEmpId = String.valueOf(teacherId);
        Teacher teacher = teacherRepository.findByEmployeeId(teacherEmpId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Teacher not found with id: " + teacherId
                ));

        Course course = courseRepository.findByCourseId(String.valueOf(courseId))
                .orElseThrow(() -> new IllegalArgumentException(
                    "Course not found with id: " + courseId
                ));

        boolean assigned = false;
        for (Teacher t : course.getTeachers()) {
            if (teacherEmpId.equals(t.getEmployeeId())) {
                assigned = true;
                break;
            }
        }
        if (!assigned) {
            throw new IllegalStateException(
                "Teacher is not assigned to this course"
            );
        }

        course.removeTeacher(teacher);
        teacher.removeCourse(course);

        courseRepository.save(course);
    }

    public WorkloadReport getDepartmentWorkload(Long deptId) {
        Department dept = departmentRepository.findById(deptId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Department not found with id: " + deptId
                ));

        List<Teacher> teachers = teacherRepository.findByDepartment(deptId);

        WorkloadReport report = new WorkloadReport(
            "Workload Report - Department: " + dept.getName()
        );

        for (Teacher teacher : teachers) {
        	TeacherWorkload workload = new TeacherWorkload(teacher, 
        		    new Semester(2024, Semester.SemesterTerm.FALL));
            for (Course course : teacher.getCourses()) {
                workload.addCourse(course, 3);
            }
            report.addWorkload(workload);
        }

        return report;
    }

    public WorkloadReport getFacultyWorkload(Long facultyId) {
        List<Teacher> allTeachers = teacherRepository.findAll();

        WorkloadReport report = new WorkloadReport(
            "Workload Report - Faculty id: " + facultyId
        );

        for (Teacher teacher : allTeachers) {
        	TeacherWorkload workload = new TeacherWorkload(teacher, 
        		    new Semester(2024, Semester.SemesterTerm.FALL));
            for (Course course : teacher.getCourses()) {
                workload.addCourse(course, 3);
            }
            report.addWorkload(workload);
        }

        return report;
    }

    public void approveWorkload(Long facultyId, Long approvedBy) {
        teacherRepository.findByEmployeeId(String.valueOf(approvedBy))
                .orElseThrow(() -> new IllegalArgumentException(
                    "Approver not found with id: " + approvedBy
                ));

        WorkloadReport report = getFacultyWorkload(facultyId);
        report.approve(approvedBy);

        System.out.println("Workload approved by: " + approvedBy);
        System.out.println(report);
    }

    public TeacherWorkload getTeacherWorkload(Long teacherId, Semester semester) {
        String teacherEmpId = String.valueOf(teacherId);
        Teacher teacher = teacherRepository.findByEmployeeId(teacherEmpId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Teacher not found with id: " + teacherId
                ));

        TeacherWorkload workload = new TeacherWorkload(teacher, semester);

        for (Course course : teacher.getCourses()) {
            workload.addCourse(course, course.getCredits() * 15);
        }

        return workload;
    }
}