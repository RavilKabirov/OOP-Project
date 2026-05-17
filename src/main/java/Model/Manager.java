
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 
 */
public abstract class Manager extends Employee {
    private ManagerLevel level = ManagerLevel.DEPARTMENT;
    private LocalDateTime appointedAt;
    
    
    public Manager(String email, String firstName, String lastName) {
    	super(email, firstName, lastName);
    }

    /**
     * @return
     */
    public ManagerLevel getLevel() {
        return level;
    }

    /**
     * @param value
     */
    public void setLevel(ManagerLevel value) {
        this.level=value;
    }

    /**
     * @return
     */
    public LocalDateTime getAppointedAt() {
        return appointedAt;
    }

    /**
     * @param value
     */
    public void setAppointedAt(LocalDateTime value) {
        this.appointedAt=value;
    }

}