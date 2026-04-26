import java.io.*;
import java.util.*;

public class Major {

    private Long id;
    private String majorId;
    private String name;
    private String description;
    private School school;

    public Major(){
    }
    public Major(Long id,String majorId,String name){
        this.id=id;
        this.majorId=majorId;
        this.name=name;
    }
    public Long getId(){
        return id;
    }
    public String getMajorId(){
        return majorId;
    }
    public String getName(){
        return name;
    }

    public void setName(String v) {
        this.name=v;
    }
    public String getDescription(){
        return description;
    }

    public void setDescription(String v){
        this.description=v;
    }
    public School getSchool(){
        return school;
    }
    public void setSchool(School v){
        this.school=v;
    }

    public String toString() {
        return name+" "+majorId+" ";
    }
} 
