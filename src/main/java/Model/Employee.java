
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Employee extends User {

    /**
     * Default constructor
     */
    public Employee() {
    }

    /**
     * 
     */
    private String employeeId;

    /**
     * 
     */
    private Department department;

    /**
     * 
     */
    private MessageRepository messages;




    /**
     * @return
     */
    public String getEmployeeId() {
        // TODO implement here
        return "";
    }

    /**
     * @param value
     */
    public void setEmployeeId(String value) {
        // TODO implement here
    }

    /**
     * @return
     */
    public Department getDepartment() {
        // TODO implement here
        return null;
    }

    /**
     * @param value
     */
    public void setDepartment(Department value) {
        // TODO implement here
    }

    /**
     * @return
     */
    public List<Message> getSentMessages() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Message> getInboxMessages() {
        // TODO implement here
        return null;
    }

}