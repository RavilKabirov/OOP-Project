import java.util.*;

public class CourseRepository implements java.io.Serializable {

    private final List<Course> courses;

    public CourseRepository() {
        this.courses = new ArrayList<>();
    }

    public Course save(Course course) {
        if (course.getCourseId() == null) {
            throw new IllegalArgumentException("Course must have a courseId before saving.");
        }
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseId().equals(course.getCourseId())) {
                courses.set(i, course);
                return course;
            }
        }
        courses.add(course);
        return course;
    }

    public Optional<Course> findByCourseId(String courseId) {
        return courses.stream().filter(c -> c.getCourseId() != null && c.getCourseId().equals(courseId)).findFirst();
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
            if (type.equals(c.getCourseType())) result.add(c);
        }
        return result;
    }

    public List<Course> findByDepartment(Long deptId) {
        List<Course> result = new ArrayList<>();
        for (Course c : courses) {
            for (Teacher t : c.getTeachers()) {
                if (t.getDepartment() != null && t.getDepartment().getId() != null && t.getDepartment().getId().equals(deptId)) {
                    result.add(c);
                    break;
                }
            }
        }
        return result;
    }

    public List<Course> searchCourses(String titleKeyword) {
        if (titleKeyword == null || titleKeyword.isBlank()) return findAll();
        String lower = titleKeyword.toLowerCase();
        List<Course> result = new ArrayList<>();
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

    public void deleteById(String courseId) {
        courses.removeIf(c -> c.getCourseId() != null && c.getCourseId().equals(courseId));
    }
}
