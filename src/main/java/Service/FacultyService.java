import java.util.*;

public class FacultyService {

    private final FacultyRepository    facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository    teacherRepository;

    public FacultyService(FacultyRepository facultyRepository,
                          DepartmentRepository departmentRepository,
                          TeacherRepository teacherRepository) {
        this.facultyRepository    = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.teacherRepository    = teacherRepository;
    }

    public School createFaculty(FacultyCreationRequest data) {
        facultyRepository.findByName(data.getName()).ifPresent(existing -> {
            throw new IllegalArgumentException(
                    "A school named '" + data.getName() + "' already exists "
                    + "(id=" + existing.getId() + ").");
        });

        School school = new School();
        school.setName(data.getName());
        facultyRepository.save(school);

        System.out.println("[FacultyService] Created school '" + school.getName()
                + "' (id=" + school.getId() + ")");
        return school;
    }

    public School updateFaculty(Long facultyId, FacultyUpdateRequest data) {
        School school = requireSchool(facultyId);

        if (data.getName() != null && !data.getName().isBlank()) {
            school.setName(data.getName());
        }

        facultyRepository.save(school);
        System.out.println("[FacultyService] Updated school id=" + facultyId
                + " → name='" + school.getName() + "'");
        return school;
    }

    public void deleteFaculty(Long facultyId) {
        requireSchool(facultyId);

        List<Department> deps = facultyRepository.findDepartmentsBySchoolId(facultyId);
        for (Department d : deps) {
            departmentRepository.deleteById(d.getId());
        }

        facultyRepository.deleteById(facultyId);
        System.out.println("[FacultyService] Deleted school id=" + facultyId
                + " and " + deps.size() + " linked department(s).");
    }

    public Optional<School> getFacultyById(Long facultyId) {
        return facultyRepository.findById(facultyId);
    }

    public List<School> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public void assignDean(Long facultyId, Long teacherId) {
        School school = requireSchool(facultyId);

        Teacher teacher = teacherRepository.findAll().stream()
                .filter(t -> t.getId() != null && t.getId().equals(teacherId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Teacher not found with id: " + teacherId));

        Dean dean = new Dean(teacher.getEmail(),
                             teacher.getFirstName(),
                             teacher.getLastName());
        dean.setId(teacher.getId());
        dean.setEmployeeId(teacher.getEmployeeId());
        dean.setSchool(school);

        school.setDean(dean);
        facultyRepository.save(school);

        System.out.println("[FacultyService] Assigned " + dean.getFullName()
                + " as Dean of '" + school.getName() + "'");
    }


    public List<Department> getFacultyDepartments(Long facultyId) {
        requireSchool(facultyId);
        return facultyRepository.findDepartmentsBySchoolId(facultyId);
    }

    public List<School> searchFaculties(String nameKeyword) {
        if (nameKeyword == null || nameKeyword.isBlank()) {
            return facultyRepository.findAll();
        }
        return facultyRepository.searchByName(nameKeyword);
    }


    public Department addDepartmentToFaculty(Long facultyId, Department department) {
        requireSchool(facultyId);

        departmentRepository.save(department);
        facultyRepository.addDepartment(facultyId, department);

        System.out.println("[FacultyService] Added department '" + department.getName()
                + "' to school id=" + facultyId);
        return department;
    }

    private School requireSchool(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "School (Faculty) not found with id: " + facultyId));
    }
}