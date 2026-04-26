
import java.io.*;
import java.util.*;

/**
 * 
 */
public class PHDStudent extends Student {
    private String dissertationTopic;
    private Teacher supervisor;

    public PHDStudent() {}
    
    public String getDissertationTopic() {
    	return  dissertationTopic;
    }
    
    public void setDissertationTopic(String value) {
    	this.dissertationTopic=value;
    }
    
    public Teacher getSupervisor() {
    	return supervisor;
    }
    
    public void setSupervisor(Teacher value) {
    	this.supervisor=value;
    }
}