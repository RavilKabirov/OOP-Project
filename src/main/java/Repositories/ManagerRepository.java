
import java.io.*;
import java.util.*;

public class ManagerRepository {
    private List<Manager> managers;
    
    public ManagerRepository() {
    	this.managers=new ArrayList<>();
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
        		.filter(m -> m.getEmployeeId().equals(employeeId))
        		.findFirst();
    }

    public List<Manager> findByLevel(ManagerLevel level) {
        List<Manager> result = new ArrayList<>();
        for (Manager m : managers) {
        	if (m.getLevel().equals(level)) {
        		result.add(m);
        	}
        }
        return result;
    }

    public List<Manager> findAll() {
        return new ArrayList<>(managers);
    }

    public void deleteById(Long id) {
        managers.removeIf(m -> m.getEmployeeId().equals(employeeId));
    }

}