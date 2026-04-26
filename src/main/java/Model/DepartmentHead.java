
import java.io.*;
import java.util.*;

/**
 * 
 */
public class DepartmentHead extends Manager {
    private School school;
    
    public DepartmentHead() {
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
        this.school=value;
    }

}