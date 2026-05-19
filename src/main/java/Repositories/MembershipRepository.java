import java.io.*;
import java.util.*;

public class MembershipRepository{

    private List<Membership> memberships;

    public MembershipRepository(){
        memberships=new ArrayList<>();
    }

    public Membership save(Membership a){
        if(a.getId()==null){
            a.setId(generateId());
            memberships.add(a);
        }else{
            Optional<Membership> old=findById(a.getId());

            if(old.isPresent()){
                memberships.remove(old.get());
            }

            memberships.add(a);
        }

        return a;
    }

    public Optional<Membership> findById(Long a){
        for(Membership m:memberships){
            if(m.getId()!=null&&m.getId().equals(a)){
                return Optional.of(m);
            }
        }

        return Optional.empty();
    }

    public List<Membership> findByOrganizationId(Long a){
        List<Membership> result=new ArrayList<>();

        for(Membership m:memberships){
            if(m.getOrganization()!=null&&m.getOrganization().getId()!=null&&m.getOrganization().getId().equals(a)){
                result.add(m);
            }
        }

        return result;
    }

    public List<Membership> findByStudentId(Long a){
        List<Membership> result=new ArrayList<>();

        for(Membership m:memberships){
            if(m.getStudent()!=null&&m.getStudent().getId()!=null&&m.getStudent().getId().equals(a)){
                result.add(m);
            }
        }

        return result;
    }

    public boolean existsByStudentIdAndOrganizationId(Long a,Long b){
        for(Membership m:memberships){
            if(m.getStudent()!=null&&m.getOrganization()!=null
                    &&m.getStudent().getId()!=null
                    &&m.getOrganization().getId()!=null
                    &&m.getStudent().getId().equals(a)
                    &&m.getOrganization().getId().equals(b)
                    &&m.getStatus()==MembershipStatus.ACTIVE){
                return true;
            }
        }

        return false;
    }

    public List<Membership> findAll(){
        return memberships;
    }

    public void deleteById(Long a){
        Optional<Membership> membership=findById(a);

        if(membership.isPresent()){
            memberships.remove(membership.get());
        }
    }

    private Long generateId(){
        Long max=0L;

        for(Membership m:memberships){
            if(m.getId()!=null&&m.getId()>max){
                max=m.getId();
            }
        }

        return max+1;
    }
}