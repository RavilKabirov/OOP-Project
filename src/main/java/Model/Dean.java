
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Dean extends Manager {
    private School school;
    

    public Dean(String email, String firstName, String lastName) {
		super(email, firstName, lastName);
	}

	/**
     * @return
     */
    public School getSchool() {
        return school;
    }

    /**
     * @param value
     */
    public void setSchool(School value) {
        this.school = value;
    }

}