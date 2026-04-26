
import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class Manager extends Employee {
    private ManagerLevel level;
    private LocalDateTime appointedAt;
    
    
    public Manager() {
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