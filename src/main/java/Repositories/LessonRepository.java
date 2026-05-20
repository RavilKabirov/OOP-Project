import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LessonRepository implements java.io.Serializable {

    private final List<Lesson> lessons = new ArrayList<>();
    private long nextId = 1;

    public Lesson save(Lesson lesson) {
        if (lesson.getId() == null) {
            lesson.setId(nextId++);
            lessons.add(lesson);
        } else {
            for (int i = 0; i < lessons.size(); i++) {
                if (lessons.get(i).getId().equals(lesson.getId())) {
                    lessons.set(i, lesson);
                    return lesson;
                }
            }
            lessons.add(lesson);
        }
        return lesson;
    }

    public Optional<Lesson> findById(Long id) {
        return lessons.stream()
                .filter(l -> l.getId() != null && l.getId().equals(id))
                .findFirst();
    }

    public List<Lesson> findByTeacher(Long teacherId) {
        List<Lesson> result = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.getTeacher() != null
                    && lesson.getTeacher().getId() != null
                    && lesson.getTeacher().getId().equals(teacherId)) {
                result.add(lesson);
            }
        }
        return result;
    }

    public List<Lesson> findByCourse(String courseId) {
        List<Lesson> result = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.getCourse() != null
                    && lesson.getCourse().getCourseId() != null
                    && lesson.getCourse().getCourseId().equals(courseId)) {
                result.add(lesson);
            }
        }
        return result;
    }

    public List<Lesson> findAll() {
        return new ArrayList<>(lessons);
    }

    public void deleteById(Long id) {
        lessons.removeIf(l -> l.getId() != null && l.getId().equals(id));
    }
}
