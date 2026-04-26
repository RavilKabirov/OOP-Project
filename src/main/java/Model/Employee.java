import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class Employee extends User {
    private String employeeId;
    private Department department;
    private MessageRepository messages;
    
    public Employee() {
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

    /**
     * @return
     */
    public List<Message> getSentMessages() {
        // TODO implement here
        return messages.getSentMessages();
    }

    /**
     * @return
     */
    public List<Message> getInboxMessages() {
        // TODO implement here
        return messages.getInboxMessages();
    }

}