
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Admin extends Employee {
    private Long adminId;
    private AdminLevel accessLevel;
    private List<Permission> permissions;
    
    public Admin() {}

    /**
     * @return
     */
    public Long getAdminId() {
        return adminId;
    }

    /**
     * @param value
     */
    public void setAdminId(Long value) {
        this.adminId=value;
    }

    /**
     * @return
     */
    public AdminLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * @param value
     */
    public void setAccessLevel(AdminLevel value) {
        this.accessLevel=value;
    }

    /**
     * @return
     */
    public List<Permission> getPermissions() {
        return permissions;
    }

    /**
     * @param value
     */
    public void setPermissions(List<Permission> value) {
        this.permissions=value;
    }

}