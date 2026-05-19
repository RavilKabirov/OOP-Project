
import java.io.*;
import java.util.*;

/**
 * 
 */
public class HRManager extends Manager {
    private List<School> departments = new ArrayList<>();
    private String specialization;
    private int hiringQuota;
    private int openVacancies;
    
    public HRManager() {
    }

    public HRManager(String email, String firstName, String lastName) {
        super(email, firstName, lastName);
    }

    /**
     * @return
     */
    public List<School> getDepartments() {
        return departments;
    }

    public List<School> getManagedSchools() {
        return departments;
    }

    /**
     * @param value
     */
    public void addDepartment(School value) {
        if (value != null && !departments.contains(value)) {
            departments.add(value);
        }
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

    public int getHiringQuota() {
        return hiringQuota;
    }

    public void setHiringQuota(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Hiring quota cannot be negative.");
        }
        this.hiringQuota = value;
    }

    public int getOpenVacancies() {
        return openVacancies;
    }

    public void setOpenVacancies(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Open vacancies cannot be negative.");
        }
        this.openVacancies = value;
    }

    /**
     * @param value
     */
    public void removeDepartment(School value) {
        departments.remove(value);
    }

    public boolean managesSchool(School school) {
        return school != null && departments.contains(school);
    }

}
