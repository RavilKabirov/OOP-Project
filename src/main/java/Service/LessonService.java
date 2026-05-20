import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    public Lesson createLesson(Long teacherId, String courseId, String topic, LessonType lessonType, LocalDateTime startsAt, LocalDateTime endsAt) {
        Teacher teacher = requireTeacher(teacherId);
        Course course = courseRepository.findByCourseId(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        boolean teachesCourse = teacher.getCourses().stream().anyMatch(c -> c.getCourseId().equals(courseId));
        if (!teachesCourse) {
            throw new IllegalStateException("Teacher does not teach this course.");
        }

        if (startsAt == null || endsAt == null || !endsAt.isAfter(startsAt)) {
            throw new IllegalArgumentException("Lesson end time must be after start time.");
        }
        if (lessonType == null) {
            throw new IllegalArgumentException("Lesson type must be selected.");
        }

        Lesson lesson = new Lesson();
        lesson.setTeacher(teacher);
        lesson.setCourse(course);
        lesson.setTopic(topic);
        lesson.setLessonType(lessonType);
        lesson.setStartsAt(startsAt);
        lesson.setEndsAt(endsAt);

        return lessonRepository.save(lesson);
    }

    public void markAttendance(Long teacherId, Long lessonId, String studentId, AttendanceStatus status) {
        Lesson lesson = requireLesson(lessonId);

        if (lesson.getTeacher() == null || !teacherId.equals(lesson.getTeacher().getId())) {
            throw new IllegalStateException("Only lesson teacher can mark attendance.");
        }

        studentRepository.findByStudentId(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        boolean enrolled = false;
        if (lesson.getCourse() != null) {
            for (Enrollment e : lesson.getCourse().getEnrollments()) {
                if (e.getStudent() != null && studentId.equals(e.getStudent().getStudentId()) && e.getStatus() == EnrollmentStatus.ACTIVE) {
                    enrolled = true;
                    break;
                }
            }
        }
        if (!enrolled) {
            throw new IllegalStateException("Student is not actively enrolled in lesson course.");
        }

        lesson.markAttendance(studentId, status);
        lessonRepository.save(lesson);
    }

    public List<Lesson> getTeacherLessons(Long teacherId) {
        requireTeacher(teacherId);
        return lessonRepository.findByTeacher(teacherId);
    }

    public List<Lesson> getCourseLessons(String courseId) {
        courseRepository.findByCourseId(courseId).orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        return lessonRepository.findByCourse(courseId);
    }

    public Map<String, AttendanceStatus> getAttendance(Long lessonId) {
        return requireLesson(lessonId).getAttendance();
    }

    private Teacher requireTeacher(Long teacherId) {
        return teacherRepository.findAll().stream().filter(t -> t.getId() != null && t.getId().equals(teacherId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));
    }

    private Lesson requireLesson(Long lessonId) {
        return lessonRepository.findById(lessonId).orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + lessonId));
    }
}
