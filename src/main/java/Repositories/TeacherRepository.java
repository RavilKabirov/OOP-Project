
import java.util.*;

public class TeacherRepository {


    
    private List<Teacher> teachers;


    public TeacherRepository() {
        this.teachers = new ArrayList<>();
    }

    public Teacher save(Teacher teacher) {
        String empId = teacher.getEmployeeId();
        Optional<Teacher> existing = findByEmployeeId(empId);
        if (existing.isPresent()) {
            teachers.remove(existing.get());
        }
        teachers.add(teacher);
        return teacher;
    }

    public Optional<Teacher> findByEmployeeId(String employeeId) {
                return teachers.stream().filter(t -> t.getEmployeeId().equals(employeeId)).findFirst();
    }

    public List<Teacher> findByDepartment(Long deptId) {
        List<Teacher> result = new ArrayList<>();
        for (Teacher t : teachers) {
            if (t.getDepartment() != null && t.getDepartment().getId().equals(deptId)) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Teacher> findByCourse(String courseId) {
        List<Teacher> result = new ArrayList<>();
        for (Teacher t : teachers) {
            boolean teachesCourse = t.getCourses().stream().anyMatch(c -> c.getCourseId().equals(courseId));
            if (teachesCourse) {
                result.add(t);
            }
        }
        return result;
    }

    public List<Teacher> findAll() {
        return new ArrayList<>(teachers);
    }

    public void deleteById(String employeeId) {
        teachers.removeIf(t -> t.getEmployeeId().equals(employeeId));
    }

}