import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class Lesson implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Course course;
    private Teacher teacher;
    private String topic;
    private LessonType lessonType;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private Map<String, AttendanceStatus> attendance = new LinkedHashMap<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public LessonType getLessonType() { return lessonType; }
    public void setLessonType(LessonType lessonType) { this.lessonType = lessonType; }

    public LocalDateTime getStartsAt() { return startsAt; }
    public void setStartsAt(LocalDateTime startsAt) { this.startsAt = startsAt; }

    public LocalDateTime getEndsAt() { return endsAt; }
    public void setEndsAt(LocalDateTime endsAt) { this.endsAt = endsAt; }

    public Map<String, AttendanceStatus> getAttendance() { return attendance; }

    public void markAttendance(String studentId, AttendanceStatus status) {
        attendance.put(studentId, status);
    }

    @Override
    public String toString() {
        return "Lesson{id=" + id + ", course=" + (course != null ? course.getCourseId() : "?")  + ", teacher=" + (teacher != null ? teacher.getFullName() : "?")  + ", topic='" + topic + "'" + ", lessonType=" + lessonType + ", startsAt=" + startsAt  + ", endsAt=" + endsAt + "}";
    }
}
