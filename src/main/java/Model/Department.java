
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Department {
    private Long id;
    private String name;
    private String code;
    private Employee head;
    private String email;
    
    public Department() {
    }


    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param value
     */
    public void setName(String value) {
        this.name=value;
    }

    /**
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * @param value
     */
    public void setCode(String value) {
    	this.code=value;
    }

    /**
     * @return
     */
    public Employee getHead() {
        return head;
    }

    /**
     * @param value
     */
    public void setHead(Employee value) {
        this.head=value;
    }

    /**
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param value
     */
    public void setEmail(String value) {
        this.email=value;
    }

    public Long getId(){
        return this.id;
    }
}