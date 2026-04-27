
import java.io.*;
import java.util.*;

public class FacultyRepository {
    public List<Faculty> faculties;
    
    public FacultyRepository() {
    	this.faculties = new ArrayList<>();
    }

    public Faculty save(Faculty faculty) {
        Optional<Faculty> existing = findById(faculty.getId());
        if(existing.isPresent()) {
        	faculties.remove(existing.get());
        }
        faculties.add(faculty);
        return faculty;
    }

    public Optional<Faculty> findById(Long id) {
        return faculties.stream()
        		.filter(f -> f.getName().equals(name))
        		.findFirst();
    }

    public Optional<Manager> findByName(String name) {
        for (Manager m : managers) {
            if (m.getFullName().equalsIgnoreCase(name)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }
    
    public List<Faculty> findAll() {
        return new ArrayList<>();
    }

    public void deleteByEmployeeId(String employeeId) {
        managers.removeIf(m -> m.getEmployeeId().equals(employeeId));
    }

}