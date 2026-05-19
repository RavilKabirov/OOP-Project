import java.io.*;
import java.util.*;

public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    public DepartmentService(DepartmentRepository departmentRepository, FacultyRepository facultyRepository, TeacherRepository teacherRepository, CourseRepository courseRepository) {
        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    public Department createDepartment(DepartmentCreationRequest data) {
        if (data == null) throw new IllegalArgumentException("Request is null");

        School faculty = facultyRepository.findById(data.getFacultyId()).orElseThrow(() -> new IllegalArgumentException("Faculty not found"));

        Department dept = new Department();
        dept.setName(data.getName());

        Department saved = departmentRepository.save(dept);
        facultyRepository.addDepartment(faculty.getId(), saved);

        return saved;
    }

    public Department updateDepartment(Long deptId, DepartmentUpdateRequest data) {
        Department dept = departmentRepository.findById(deptId).orElseThrow(() -> new IllegalArgumentException("Department not found"));

        if (data.getName() != null) {
            dept.setName(data.getName());
        }

        if (data.getFacultyId() != null) {
            School faculty = facultyRepository.findById(data.getFacultyId()).orElseThrow(() -> new IllegalArgumentException("Faculty not found"));
            facultyRepository.addDepartment(faculty.getId(), dept);
        }

        return departmentRepository.save(dept);
    }

    public void deleteDepartment(Long deptId) {
        departmentRepository.findById(deptId).orElseThrow(() -> new IllegalArgumentException("Department not found"));

        List<Teacher> teachers = teacherRepository.findByDepartment(deptId);
        // Нельзя удалять department пока в нем есть teachers, сначала их надо перевести в другой department.
        if (teachers != null && !teachers.isEmpty()) {
            throw new IllegalStateException("Cannot delete department — it still has teachers.");
        }

        for (Teacher t : teacherRepository.findAll()) {
            if (t.getDepartment() != null && t.getDepartment().getId().equals(deptId)) {
                // Доп проверка: если у teacher еще есть courses, удаление department может сломать связи.
                if (!t.getCourses().isEmpty()) {
                    throw new IllegalStateException( "Cannot delete department — teacher " + t.getFullName() + " still has courses.");
                }
            }
        }

        departmentRepository.deleteById(deptId);
    }

    public Optional<Department> getDepartmentById(Long deptId) {
        return departmentRepository.findById(deptId);
    }

    public List<Department> getDepartmentsByFaculty(Long facultyId) {
        return facultyRepository.findDepartmentsBySchoolId(facultyId);
    }

    public void assignHead(Long deptId, Long teacherId) {
        Department dept = departmentRepository.findById(deptId).orElseThrow(() -> new IllegalArgumentException("Department not found"));

        Teacher teacher = teacherRepository.findAll().stream().filter(t -> t.getId() != null && t.getId().equals(teacherId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        // Head должен быть из этого же department, иначе получится странная связь.
        if (teacher.getDepartment() == null || !teacher.getDepartment().getId().equals(deptId)) {
            throw new IllegalArgumentException( "Teacher " + teacher.getFullName() + " does not belong to this department.");
        }

        dept.setHead(teacher);
        departmentRepository.save(dept);
    }

    public List<Teacher> getDepartmentTeachers(Long deptId) {
        return teacherRepository.findByDepartment(deptId);
    }

    public List<Department> searchDepartments(String nameKeyword) {
        List<Department> result = new ArrayList<>();
        if (nameKeyword == null) return result;

        String lower = nameKeyword.toLowerCase();
        for (Department d : departmentRepository.findAll()) {
            if (d.getName() != null && d.getName().toLowerCase().contains(lower)) {
                result.add(d);
            }
        }
        return result;
    }
}
