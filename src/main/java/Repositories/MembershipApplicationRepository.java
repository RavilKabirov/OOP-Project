import java.io.*;
import java.util.*;

public class MembershipApplicationRepository{

    private List<MembershipApplication> applications;

    public MembershipApplicationRepository(){
        applications=new ArrayList<>();
    }

    public MembershipApplication save(MembershipApplication a){
        if(a.getId()==null){
            a.setId(generateId());
            applications.add(a);
        }else{
            Optional<MembershipApplication> old=findById(a.getId());

            if(old.isPresent()){
                applications.remove(old.get());
            }

            applications.add(a);
        }

        return a;
    }

    public Optional<MembershipApplication> findById(Long a){
        for(MembershipApplication app:applications){
            if(app.getId()!=null&&app.getId().equals(a)){
                return Optional.of(app);
            }
        }

        return Optional.empty();
    }

    public List<MembershipApplication> findByOrganizationId(Long a){
        List<MembershipApplication> result=new ArrayList<>();

        for(MembershipApplication app:applications){
            if(app.getOrganization()!=null&&app.getOrganization().getId()!=null&&app.getOrganization().getId().equals(a)){
                result.add(app);
            }
        }

        return result;
    }

    public List<MembershipApplication> findByStudentId(Long a){
        List<MembershipApplication> result=new ArrayList<>();

        for(MembershipApplication app:applications){
            if(app.getStudent()!=null&&app.getStudent().getId()!=null&&app.getStudent().getId().equals(a)){
                result.add(app);
            }
        }

        return result;
    }

    public List<MembershipApplication> findAll(){
        return applications;
    }

    public void deleteById(Long a){
        Optional<MembershipApplication> app=findById(a);

        if(app.isPresent()){
            applications.remove(app.get());
        }
    }

    private Long generateId(){
        Long max=0L;

        for(MembershipApplication app:applications){
            if(app.getId()!=null&&app.getId()>max){
                max=app.getId();
            }
        }

        return max+1;
    }
}