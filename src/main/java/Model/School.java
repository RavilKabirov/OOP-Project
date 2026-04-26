
import java.io.*;
import java.util.*;

/**
 * 
 */
public class School {
    private Long id;
    private String name;
    private Dean dean;
    
    public School() {
    }



    /**
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * @param value
     */
    public void setId(Long value) {
        this.id=value;
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
    public Dean getDean() {
        return dean;
    }

    /**
     * @param value
     */
    public void setDean(Dean value) {
        this.dean=value;
    }

}