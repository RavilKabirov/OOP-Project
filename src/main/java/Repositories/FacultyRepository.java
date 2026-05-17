import java.util.*;


public class FacultyRepository {

    private final List<School>                  schools;
    private final Map<Long, List<Department>>   schoolDepartments;
    private long nextId = 1;

    public FacultyRepository() {
        this.schools           = new ArrayList<>();
        this.schoolDepartments = new HashMap<>();
    }


    public School save(School school) {
        if (school.getId() == null) {
            school.setId(nextId++);
            schools.add(school);
            schoolDepartments.put(school.getId(), new ArrayList<>());
        } else {
            for (int i = 0; i < schools.size(); i++) {
                if (schools.get(i).getId().equals(school.getId())) {
                    schools.set(i, school);
                    return school;
                }
            }
            schools.add(school);
            schoolDepartments.putIfAbsent(school.getId(), new ArrayList<>());
        }
        return school;
    }

    public Optional<School> findById(Long id) {
        return schools.stream()
                .filter(s -> s.getId() != null && s.getId().equals(id))
                .findFirst();
    }

    public Optional<School> findByName(String name) {
        return schools.stream()
                .filter(s -> s.getName() != null
                          && s.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<School> searchByName(String keyword) {
        List<School> result = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (School s : schools) {
            if (s.getName() != null && s.getName().toLowerCase().contains(lower)) {
                result.add(s);
            }
        }
        return result;
    }

    public List<School> findAll() {
        return new ArrayList<>(schools);
    }

    public void deleteById(Long id) {
        schools.removeIf(s -> s.getId() != null && s.getId().equals(id));
        schoolDepartments.remove(id);
    }


    public void addDepartment(Long schoolId, Department department) {
        schoolDepartments.computeIfAbsent(schoolId, k -> new ArrayList<>())
                         .add(department);
    }

    public List<Department> findDepartmentsBySchoolId(Long schoolId) {
        return new ArrayList<>(
                schoolDepartments.getOrDefault(schoolId, Collections.emptyList()));
    }

    public void removeDepartment(Long schoolId, Long departmentId) {
        List<Department> deps = schoolDepartments.get(schoolId);
        if (deps != null) {
            deps.removeIf(d -> d.getId() != null && d.getId().equals(departmentId));
        }
    }
}