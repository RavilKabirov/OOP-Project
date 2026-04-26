import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository {
    private List<Student> students;
    public StudentRepository() {
        this.students = new ArrayList<>();
    }
    public Student save(Student student) {
        String sid = student.getStudentId();
        Optional<Student> existing = findByStudentId(sid);
        if (existing.isPresent()) {
            students.remove(existing.get());
        }
        students.add(student);
        return student;
    }

    public Optional<Student> findByStudentId(String studentId) {
        return students.stream().filter(s -> s.getStudentId().equals(studentId)).findFirst();
    }

    public List<Student> findByMajor(Long majorId) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getMajor() != null && s.getMajor().getId().equals(majorId)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<Student> findByYearOfStudy(int year) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.getYearOfStudy() == year) {
                result.add(s);
            }
        }
        return result;
    }

    public List<Student> findAll() {
        return new ArrayList<>(students);
    }


    public void deleteByStudentId(String studentId) {
        students.removeIf(s -> s.getStudentId().equals(studentId));
    }
}