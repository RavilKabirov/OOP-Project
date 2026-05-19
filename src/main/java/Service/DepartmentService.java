import java.io.*;
import java.util.*;

public class DepartmentService{

    private DepartmentRepository departmentRepository;
    private FacultyRepository facultyRepository;
    private TeacherRepository teacherRepository;
    private CourseRepository courseRepository;

    public DepartmentService(DepartmentRepository departmentRepository,
                             FacultyRepository facultyRepository,
                             TeacherRepository teacherRepository,
                             CourseRepository courseRepository){
        this.departmentRepository = departmentRepository;
        this.facultyRepository = facultyRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    public Department createDepartment(DepartmentCreationRequest data){
        if(data == null){
            throw new IllegalArgumentException("Request is null");
        }

        School faculty = facultyRepository.findById(data.getFacultyId())
                .orElseThrow(() -> new IllegalArgumentException("Faculty not found"));

        Department a = new Department();

        a.setName(data.getName());
        a.setFaculty(faculty);

        Department saved = departmentRepository.save(a);
        facultyRepository.addDepartment(faculty.getId(), saved);

        return saved;
    }

    public Department updateDepartment(Long deptId, DepartmentUpdateRequest data){
        Department a = departmentRepository.findById(deptId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        if(data.getName() != null){
            a.setName(data.getName());
        }

        if(data.getFacultyId() != null){
            School faculty = facultyRepository.findById(data.getFacultyId())
                    .orElseThrow(() -> new IllegalArgumentException("Faculty not found"));

            a.setFaculty(faculty);
            facultyRepository.addDepartment(faculty.getId(), a);
        }

        return departmentRepository.save(a);
    }

    public void deleteDepartment(Long deptId){
        Department a = departmentRepository.findById(deptId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        List<Teacher> teachers = teacherRepository.findByDepartment(deptId);

        if(teachers != null && !teachers.isEmpty()){
            throw new IllegalStateException("Cannot delete department. There are teachers in this department");
        }

        List<Course> courses = courseRepository.findByDepartment(deptId);

        if(courses != null && !courses.isEmpty()){
            throw new IllegalStateException("Cannot delete department. There are courses in this department");
        }

        departmentRepository.deleteById(deptId);
    }

    public Optional<Department> getDepartmentById(Long deptId){
        return departmentRepository.findById(deptId);
    }

    public List<Department> getDepartmentsByFaculty(Long facultyId){
        return facultyRepository.findDepartmentsBySchoolId(facultyId);
    }

    public void assignHead(Long deptId, Long teacherId){
        Department a = departmentRepository.findById(deptId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        Teacher b = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));

        if(b.getDepartment() == null || !b.getDepartment().getId().equals(deptId)){
            throw new IllegalArgumentException("Teacher is not from this department");
        }

        a.setHead(b);
        departmentRepository.save(a);
    }

    public List<Teacher> getDepartmentTeachers(Long deptId){
        return teacherRepository.findByDepartment(deptId);
    }

    public List<Department> searchDepartments(String nameKeyword){
        List<Department> result = new ArrayList<>();

        if(nameKeyword == null){
            return result;
        }

        String lower = nameKeyword.toLowerCase();

        for(Department a : departmentRepository.findAll()){
            if(a.getName() != null && a.getName().toLowerCase().contains(lower)){
                result.add(a);
            }
        }

        return result;
    }
}