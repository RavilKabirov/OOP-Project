import java.util.*;

public class Membership{
    private Long id;
    private Student student;
    private Organization organization;
    private MembershipRole role;
    private MembershipStatus status;

    public Membership(){}

    public Long getId(){
        return id;
    }
    public void setId(Long a){
        id=a;
    }
    public Student getStudent(){
        return student;
    }
    public void setStudent(Student a){
        student=a;
    }
    public Organization getOrganization(){
        return organization;
    }

    public void setOrganization(Organization a){
        organization=a;
    }
    public MembershipRole getRole(){
        return role;
    }

    public void setRole(MembershipRole a){
        role=a;
    }
    public MembershipStatus getStatus(){
        return status;
    }

    public void setStatus(MembershipStatus a){
        status=a;
    }

    public void activate(){
        status=MembershipStatus.ACTIVE;
    }
    public void deactivate(){
        status=MembershipStatus.INACTIVE;
    }

    public String toString(){
        return student+" "+role;
    }
}
