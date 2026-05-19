import java.util.*;

public class ManagerRepository implements java.io.Serializable {

    private final List<Manager> managers;

    public ManagerRepository() {
        this.managers = new ArrayList<>();
    }

    public Manager save(Manager manager) {
        String empId = manager.getEmployeeId();
        Optional<Manager> existing = findByEmployeeId(empId);
        if (existing.isPresent()) {
            managers.remove(existing.get());
        }
        managers.add(manager);
        return manager;
    }

    public Optional<Manager> findByEmployeeId(String employeeId) {
        return managers.stream()
                .filter(m -> m.getEmployeeId() != null
                          && m.getEmployeeId().equals(employeeId))
                .findFirst();
    }

    public Optional<Manager> findById(Long id) {
        return managers.stream()
                .filter(m -> m.getId() != null && m.getId().equals(id))
                .findFirst();
    }

    public List<Manager> findByLevel(ManagerLevel level) {
        List<Manager> result = new ArrayList<>();
        for (Manager m : managers) {
            if (m.getLevel() != null && m.getLevel().equals(level)) {
                result.add(m);
            }
        }
        return result;
    }

    public List<Manager> findAll() {
        return new ArrayList<>(managers);
    }


    public void deleteById(Long id) {
        managers.removeIf(m -> m.getId() != null && m.getId().equals(id));
    }

    public void deleteByEmployeeId(String employeeId) {
        managers.removeIf(m -> employeeId.equals(m.getEmployeeId()));
    }
}
