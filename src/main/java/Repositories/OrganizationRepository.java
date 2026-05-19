import java.io.*;
import java.util.*;

public class OrganizationRepository{

    private List<Organization> organizations;

    public OrganizationRepository(){
        organizations=new ArrayList<>();
    }

    public Organization save(Organization a){
        if(a.getId()==null){
            a.setId(generateId());
            organizations.add(a);
        }else{
            Optional<Organization> old=findById(a.getId());

            if(old.isPresent()){
                organizations.remove(old.get());
            }

            organizations.add(a);
        }

        return a;
    }

    public Optional<Organization> findById(Long a){
        for(Organization o:organizations){
            if(o.getId()!=null&&o.getId().equals(a)){
                return Optional.of(o);
            }
        }

        return Optional.empty();
    }

    public Optional<Organization> findByName(String a){
        for(Organization o:organizations){
            if(o.getName()!=null&&o.getName().equalsIgnoreCase(a)){
                return Optional.of(o);
            }
        }

        return Optional.empty();
    }

    public List<Organization> findAllActive(){
        List<Organization> result=new ArrayList<>();

        for(Organization o:organizations){
            if(o.getIsActive()!=null&&o.getIsActive()){
                result.add(o);
            }
        }

        return result;
    }

    public List<Organization> findAll(){
        return organizations;
    }

    public void deleteById(Long a){
        Optional<Organization> org=findById(a);

        if(org.isPresent()){
            organizations.remove(org.get());
        }
    }

    private Long generateId(){
        Long max=0L;

        for(Organization o:organizations){
            if(o.getId()!=null&&o.getId()>max){
                max=o.getId();
            }
        }

        return max+1;
    }
}