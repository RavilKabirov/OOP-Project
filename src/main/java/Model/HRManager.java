
import java.io.*;
import java.util.*;

/**
 * 
 */
public class HRManager extends Manager {
    private List<School> departments = new ArrayList<>();
    private String specialization;
    
    public HRManager() {
    }

    /**
     * @return
     */
    public List<School> getDepartments() {
        return departments;
    }

    /**
     * @param value
     */
    public void addDepartment(School value) {
        departments.add(value);
    }

    /**
     * @return
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * @param value
     */
    public void setSpecialization(String value) {
        this.specialization=value;
    }

    /**
     * @param value
     */
    public void removeDepartment(School value) {
        departments.remove(value);
    }

}