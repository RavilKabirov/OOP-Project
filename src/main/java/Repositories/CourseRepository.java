import java.util.*;

public class CourseRepository {

    private List<Course> courses;

    public CourseRepository() {
        this.courses = new ArrayList<>();
    }

    public Course save(Course course) {
        Optional<Course> existing = findByCourseId(course.getCourseId());
        if (existing.isPresent()) {
            courses.remove(existing.get());
        }
        courses.add(course);
        return course;
    }

    public Optional<Course> findByCourseId(String courseId) {
        return courses.stream()
                .filter(c -> c.getCourseId().equals(courseId))
                .findFirst();
    }

    public List<Course> findByTeacher(String teacherId) {
        List<Course> result = new ArrayList<>();
        for (Course c : courses) {
            for (Teacher t : c.getTeachers()) {
                if (t.getId() != null && t.getId().toString().equals(teacherId)) {
                    result.add(c);
                    break;
                }
            }
        }
        return result;
    }

    public List<Course> findByCourseType(CourseType type) {
        List<Course> result = new ArrayList<>();
        for (Course c : courses) {
            if (type.equals(c.getCourseType())) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Course> searchCourses(String titleKeyword) {
        List<Course> result = new ArrayList<>();
        String lower = titleKeyword.toLowerCase();
        for (Course c : courses) {
            if (c.getName() != null && c.getName().toLowerCase().contains(lower)) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Course> findAll() {
        return new ArrayList<>(courses);
    }

    public void deleteById(String id) {
        if (id != null) {
        		Optional<Course> course = findByCourseId(id);
            course.ifPresent(courses::remove);
        }
    }
}
