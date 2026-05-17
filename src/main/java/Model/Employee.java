import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class Employee extends User {
    private String employeeId;
    private Department department;
    
    public Employee(String email, String firstName, String lastName) {
        super(email, firstName, lastName);
    }

    /**
     * @return
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @param value
     */
    public void setEmployeeId(String value) {
        this.employeeId=value;
    }

    /**
     * @return
     */
    public Department getDepartment() {
        return department;
    }

    /**
     * @param value
     */
    public void setDepartment(Department value) {
        this.department=value;
    }

    

}