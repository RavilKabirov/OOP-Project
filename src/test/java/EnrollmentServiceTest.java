import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentServiceTest {
    private StudentRepository students;
    private CourseRepository courses;
    private EnrollmentRepository enrollments;
    private EnrollmentService service;

    @BeforeEach
    void setUp() {
        students = new StudentRepository();
        courses = new CourseRepository();
        enrollments = new EnrollmentRepository();
        service = new EnrollmentService(enrollments, students, courses);

        Student student = new BachelorStudent();
        student.setStudentId("S-100");
        student.setFullName("Ada", "Lovelace");
        students.save(student);

        Course course = new Course();
        course.setCourseId("CS-101");
        course.setName("Programming");
        courses.save(course);
    }

    @Test
    void enrollsStudentAndAddsEnrollmentToCourseRoster() {
        Enrollment enrollment = service.enrollStudent("S-100", "CS-101");

        assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());
        assertTrue(service.isStudentEnrolled("S-100", "CS-101"));
        assertEquals(1, service.getCourseRoster("CS-101").size());
    }

    @Test
    void rejectsDuplicateActiveEnrollment() {
        service.enrollStudent("S-100", "CS-101");

        assertThrows(IllegalStateException.class,
                () -> service.enrollStudent("S-100", "CS-101"));
        assertEquals(1, service.getStudentEnrollments("S-100").size());
    }

    @Test
    void rejectsEnrollmentWhenStudentOrCourseDoesNotExist() {
        assertThrows(IllegalArgumentException.class,
                () -> service.enrollStudent("missing", "CS-101"));
        assertThrows(IllegalArgumentException.class,
                () -> service.enrollStudent("S-100", "missing"));
    }

    @Test
    void rejectsCompletionWithTotalGradeAboveMaximum() {
        Enrollment enrollment = service.enrollStudent("S-100", "CS-101");

        assertThrows(IllegalArgumentException.class,
                () -> service.completeCourse(enrollment.getId(), 40, 30, 31));
        assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());
    }
}
