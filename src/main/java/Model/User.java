
import java.io.*;
import java.util.*;

/**
 * 
 */
public class User {

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
    public void setFullName() {
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
    public String getEmail() {
        // TODO implement here
        return null;
    }

    /**
     * @param email 
     * @return
     */
    public void setEmail(String email) {
        // TODO implement here
        return;
    }

    /**
     * @param oldPass 
     * @param newPass 
     * @return
     */
    public void changePassword(String oldPass, String newPass) {
        // TODO implement here
        return;
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

    public void switchActive(){
        return;
    }
    public Long getId(){
        return null;
    }
    public void setId(long id){
        return;
    }
    public boolean isActive(){
        return this.isActive;
    }
}