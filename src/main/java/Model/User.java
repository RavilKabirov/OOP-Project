
import java.io.*;
import java.util.*;

/**
 * 
 */
public abstract class User {

    /**
     * Default constructor
     */
    public User() {
    }

    /**
     * 
     */
    private Long Id;

    /**
     * 
     */
    private String firstName;

    /**
     * 
     */
    private String lastName;

    /**
     * 
     */
    private String Email;

    /**
     * 
     */
    private String passwordHash;

    /**
     * 
     */
    private LocalDateTime createdAt;

    /**
     * 
     */
    private boolean isActive;

    /**
     * 
     */
    public void getFullName() {
        // TODO implement here
    }

    /**
     * @return
     */
    public String getFullName() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public void getEmail() {
        // TODO implement here
        return null;
    }

    /**
     * @param email 
     * @return
     */
    public void setEmail(String email) {
        // TODO implement here
        return null;
    }

    /**
     * @param oldPass 
     * @param newPass 
     * @return
     */
    public void changePassword(String oldPass, String newPass) {
        // TODO implement here
        return null;
    }

    /**
     * @param rawPass 
     * @return
     */
    public boolean checkPassword(String rawPass) {
        // TODO implement here
        return false;
    }

    /**
     * @param raw 
     * @return
     */
    private String hashPassword(String raw) {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public String getPasswordHash() {
        // TODO implement here
        return "";
    }

    /**
     * @param value
     */
    public void setPassword(String value) {
        // TODO implement here
    }

}