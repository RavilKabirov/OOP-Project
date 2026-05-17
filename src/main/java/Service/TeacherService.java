import java.util.*;
import java.util.stream.Collectors;


public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    public TeacherService(TeacherRepository teacherRepository,
                          DepartmentRepository departmentRepository) {
        this.teacherRepository = teacherRepository;
        this.departmentRepository = departmentRepository;
    }


    public Teacher getTeacherByEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("Employee ID cannot be null or blank");
        }
        return teacherRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Teacher not found with employeeId: " + employeeId));
    }

    public Teacher updateTeacherProfile(String employeeId, TeacherProfileUpdateRequest profileData) {
        if (employeeId == null) {
            throw new IllegalArgumentException("Teacher ID cannot be null");
        }
        if (profileData == null) {
            throw new IllegalArgumentException("Profile update data cannot be null");
        }

        Teacher teacher = getTeacherByEmployeeId(employeeId);

        if (profileData.getFirstName() != null && profileData.getLastName() != null) {
            teacher.setFullName(profileData.getFirstName(), profileData.getLastName());
        } else {
            if (profileData.getFirstName() != null) {
                teacher.setFullName(profileData.getFirstName(), teacher.getLastName());
            }
            if (profileData.getLastName() != null) {
                teacher.setFullName(teacher.getFirstName(), profileData.getLastName());
            }
        }

        if (profileData.getEmail() != null) {
            teacher.setEmail(profileData.getEmail());
        }

        if (profileData.getTeacherPosition() != null) {
            teacher.setTeacherPosition(profileData.getTeacherPosition());
        }

        return teacherRepository.save(teacher);
    }


    public List<Course> getTeacherCourses(String teacherId) {
        Teacher teacher = getTeacherByEmployeeId(teacherId);
        return teacher.getCourses();
    }

    public List<Teacher> getTeachersByDepartment(Long deptId) {
        if (deptId == null) {
            throw new IllegalArgumentException("Department ID cannot be null");
        }
        departmentRepository.findById(deptId)
                .orElseThrow(() -> new NoSuchElementException("Department not found with ID: " + deptId));
        return teacherRepository.findByDepartment(deptId);
    }

    public void assignTeacherToDepartment(String teacherId, Long deptId) {
        if (teacherId == null) {
            throw new IllegalArgumentException("Teacher ID cannot be null");
        }
        if (deptId == null) {
            throw new IllegalArgumentException("Department ID cannot be null");
        }

        Teacher teacher = getTeacherByEmployeeId(teacherId);
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new NoSuchElementException("Department not found with ID: " + deptId));

        teacher.setDepartment(department);
        teacherRepository.save(teacher);
    }

    public List<Teacher> searchTeachers(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return teacherRepository.findAll();
        }
        String lowerKeyword = keyword.toLowerCase();
        return teacherRepository.findAll().stream()
                .filter(t -> matchesKeyword(t, lowerKeyword))
                .collect(Collectors.toList());
    }

    private boolean matchesKeyword(Teacher teacher, String lowerKeyword) {
        return (teacher.getFirstName() != null && teacher.getFirstName().toLowerCase().contains(lowerKeyword))
                || (teacher.getLastName() != null && teacher.getLastName().toLowerCase().contains(lowerKeyword))
                || (teacher.getEmail() != null && teacher.getEmail().toLowerCase().contains(lowerKeyword))
                || (teacher.getEmployeeId() != null && teacher.getEmployeeId().toLowerCase().contains(lowerKeyword));
    }



}