import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
/**
 * 
 */
public abstract class User {
    private Long Id;
    private String firstName;
    private String lastName;
    private String Email;
    private String passwordHash;
    private LocalDateTime createdAt;
    private boolean isActive;
    
    public User() {
    }

    public void setFullName(String firstName, String lastName) {
        this.firstName=firstName;
        this.lastName=lastName;
    }

    /**
     * @return
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * @return
     */
    public String getEmail() {
        return Email;
    }

    /**
     * @param email 
     * @return
     */
    public void setEmail(String email) {
        this.Email=email;
    }

    /**
     * @param oldPass 
     * @param newPass 
     * @return
     */
    public void changePassword(String oldPass, String newPass) {
        if(checkPassword(oldPass)) {
        	this.passwordHash=hashPassword(newPass);
        }
    }

    /**
     * @param rawPass 
     * @return
     */
    
    private String hashPassword(String raw) {
    	return Integer.toHexString(raw.hashCode());
    }
    
    public boolean checkPassword(String rawPass) {
        return passwordHash.equals(hashPassword(rawPass));
    }


    /**
     * @return
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * @param value
     */
    public void setPassword(String value) {
        this.passwordHash=hashPassword(value);
    }

    public void switchActive(){
        this.isActive=!isActive;;
    }
    
    public void setId(long id){
        this.Id=id;
    }
    public boolean isActive(){
        return this.isActive;
    }

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}
	
	@Override
	public String toString() {
		return getFullName() + " | " + Email;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this==o) return true;
		if(!(o instanceof User)) return false;
		User user = (User) o;
		return Objects.equals(Id, user.Id) && Objects.equals(Email, user.Email);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(Id,Email);
	}
}