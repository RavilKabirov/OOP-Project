import java.io.*;
import java.util.*;
import java.time.*;

public class Organization{

    private Long id;
    private String name;
    private String description;
    private LocalDateTime foundedAt;
    private Boolean isActive;
    private Student leader;
    private List<Membership>members;

    public Organization(){
        members=new ArrayList<>();
    }
    public Long getId(){
        return id;
    }
    public void setId(Long a){
        id=a;
    }
    public String getName(){
        return name;
    }

    public void setName(String a){
        name=a;
    }
    public String getDescription(){
        return description;
    }

    public void setDescription(String a){
        description=a;
    }

    public LocalDateTime getFoundedAt(){
        return foundedAt;
    }
    public void setFoundedAt(LocalDateTime a){
        foundedAt=a;
    }
    public Boolean getIsActive(){
        return isActive;
    }
    public void setIsActive(Boolean a){
        isActive=a;
    }

    public Student getLeader(){
        return leader;
    }

    public void setLeader(Student a){
        leader=a;
    }
    public List<Membership>getMembers(){
        return members;
    }

    public void addMember(Membership a){
        if(a!=null){
            members.add(a);
        }
    }

    @Override
    public String toString(){
        return name+' '+leader;
    }
}
