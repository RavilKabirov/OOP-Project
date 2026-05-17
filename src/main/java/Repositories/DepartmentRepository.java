import java.util.*;


public class DepartmentRepository {

    private final List<Department> departments;
    private long nextId = 1;

    public DepartmentRepository() {
        this.departments = new ArrayList<>();
    }

    public Department save(Department department) {
        if (department.getId() == null) {
            department.setId(nextId++);
            departments.add(department);
        } else {
            for (int i = 0; i < departments.size(); i++) {
                if (departments.get(i).getId().equals(department.getId())) {
                    departments.set(i, department);
                    return department;
                }
            }
            departments.add(department);
        }
        return department;
    }

    public Optional<Department> findById(Long id) {
        return departments.stream()
                .filter(d -> d.getId() != null && d.getId().equals(id))
                .findFirst();
    }

    public Optional<Department> findByCode(String code) {
        return departments.stream()
                .filter(d -> d.getCode() != null
                          && d.getCode().equalsIgnoreCase(code))
                .findFirst();
    }


    public Optional<Department> findByHead(String hId) {
        return departments.stream()
                .filter(d -> d.getHead() != null
                          && hId.equals(d.getHead().getEmployeeId()))
                .findFirst();
    }

    public List<Department> findAll() {
        return new ArrayList<>(departments);
    }

    public void deleteById(Long id) {
        departments.removeIf(d -> d.getId() != null && d.getId().equals(id));
    }
}