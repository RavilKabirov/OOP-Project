import java.io.*;
import java.util.*;
import java.time.*;

public class OrganizationService{

    private OrganizationRepository organizationRepository;
    private MembershipRepository membershipRepository;
    private MembershipApplicationRepository applicationRepository;
    private StudentRepository studentRepository;

    public OrganizationService(){
    }

    public OrganizationService(OrganizationRepository a,MembershipRepository b,MembershipApplicationRepository c,StudentRepository d){
        organizationRepository=a;
        membershipRepository=b;
        applicationRepository=c;
        studentRepository=d;
    }

    public Organization createOrganization(OrganizationCreationRequest a,Long b){
        Student leader=studentRepository.findById(b)
                .orElseThrow(()->new IllegalArgumentException("Leader not found"));

        if(organizationRepository.findByName(a.getName()).isPresent()){
            throw new IllegalStateException("Organization with this name already exists");
        }

        Organization org=new Organization();
        org.setName(a.getName());
        org.setDescription(a.getDescription());
        org.setLeader(leader);
        org.setFoundedAt(LocalDateTime.now());
        org.setIsActive(true);

        Organization saved=organizationRepository.save(org);

        Membership membership=new Membership();
        membership.setStudent(leader);
        membership.setOrganization(saved);
        membership.setRole(MembershipRole.LEADER);
        membership.activate();

        Membership savedMembership=membershipRepository.save(membership);
        saved.addMember(savedMembership);

        return saved;
    }

    public Organization updateOrganization(Long a,OrganizationUpdateRequest b){
        Organization org=organizationRepository.findById(a)
                .orElseThrow(()->new IllegalArgumentException("Organization not found"));

        if(b.getName()!=null){
            org.setName(b.getName());
        }

        if(b.getDescription()!=null){
            org.setDescription(b.getDescription());
        }

        return organizationRepository.save(org);
    }

    public void deleteOrganization(Long a){
        Organization org=organizationRepository.findById(a)
                .orElseThrow(()->new IllegalArgumentException("Organization not found"));

        List<Membership> members=membershipRepository.findByOrganizationId(a);

        for(Membership m:members){
            if(m.getStatus()==MembershipStatus.ACTIVE){
                throw new IllegalStateException("Cannot delete organization with active members");
            }
        }

        List<MembershipApplication> apps=applicationRepository.findByOrganizationId(a);

        for(MembershipApplication app:apps){
            if(app.getApplicationStatus()==ApplicationStatus.PENDING){
                throw new IllegalStateException("Cannot delete organization with pending applications");
            }
        }

        org.setIsActive(false);
        organizationRepository.save(org);
    }

    public Optional<Organization> getOrganizationById(Long a){
        return organizationRepository.findById(a);
    }

    public List<Organization> getAllActiveOrganizations(){
        return organizationRepository.findAllActive();
    }

    public List<Organization> searchOrganizations(String a){
        List<Organization> result=new ArrayList<>();

        for(Organization o:organizationRepository.findAllActive()){
            if(o.getName()!=null&&o.getName().toLowerCase().contains(a.toLowerCase())){
                result.add(o);
            }
        }

        return result;
    }

    public MembershipApplication applyForMembership(Long a,Long b,String c){
        Student student=studentRepository.findById(a)
                .orElseThrow(()->new IllegalArgumentException("Student not found"));

        Organization org=organizationRepository.findById(b)
                .orElseThrow(()->new IllegalArgumentException("Organization not found"));

        if(org.getIsActive()==null||!org.getIsActive()){
            throw new IllegalStateException("Organization is not active");
        }

        if(membershipRepository.existsByStudentIdAndOrganizationId(a,b)){
            throw new IllegalStateException("Student is already member");
        }

        MembershipApplication app=new MembershipApplication();
        app.setStudent(student);
        app.setOrganization(org);
        app.setBody(c);
        app.setApplicationStatus(ApplicationStatus.PENDING);
        app.setSubmittedAt(LocalDateTime.now());

        return applicationRepository.save(app);
    }

    public Membership approveApplication(Long a,Long b){
        MembershipApplication app=applicationRepository.findById(a)
                .orElseThrow(()->new IllegalArgumentException("Application not found"));

        Organization org=app.getOrganization();

        if(!org.getLeader().getId().equals(b)){
            throw new IllegalStateException("Only leader can approve application");
        }

        if(app.getApplicationStatus()!=ApplicationStatus.PENDING){
            throw new IllegalStateException("Application is not pending");
        }

        app.approve();
        applicationRepository.save(app);

        Membership membership=new Membership();
        membership.setStudent(app.getStudent());
        membership.setOrganization(org);
        membership.setRole(MembershipRole.MEMBER);
        membership.activate();

        Membership saved=membershipRepository.save(membership);
        org.addMember(saved);

        return saved;
    }

    public void rejectApplication(Long a,Long b){
        MembershipApplication app=applicationRepository.findById(a)
                .orElseThrow(()->new IllegalArgumentException("Application not found"));

        Organization org=app.getOrganization();

        if(!org.getLeader().getId().equals(b)){
            throw new IllegalStateException("Only leader can reject application");
        }

        if(app.getApplicationStatus()!=ApplicationStatus.PENDING){
            throw new IllegalStateException("Application is not pending");
        }

        app.reject();
        applicationRepository.save(app);
    }

    public Membership addMember(Long a,Long b,MembershipRole c){
        Student student=studentRepository.findById(a)
                .orElseThrow(()->new IllegalArgumentException("Student not found"));

        Organization org=organizationRepository.findById(b)
                .orElseThrow(()->new IllegalArgumentException("Organization not found"));

        if(org.getIsActive()==null||!org.getIsActive()){
            throw new IllegalStateException("Organization is not active");
        }

        if(membershipRepository.existsByStudentIdAndOrganizationId(a,b)){
            throw new IllegalStateException("Student is already member");
        }

        Membership membership=new Membership();
        membership.setStudent(student);
        membership.setOrganization(org);
        membership.setRole(c);
        membership.activate();

        Membership saved=membershipRepository.save(membership);
        org.addMember(saved);

        return saved;
    }

    public void removeMember(Long a){
        Membership membership=membershipRepository.findById(a)
                .orElseThrow(()->new IllegalArgumentException("Membership not found"));

        membership.deactivate();
        membershipRepository.save(membership);
    }

    public void changeMemberRole(Long a,MembershipRole b){
        Membership membership=membershipRepository.findById(a)
                .orElseThrow(()->new IllegalArgumentException("Membership not found"));

        membership.setRole(b);
        membershipRepository.save(membership);
    }

    public List<Membership> getOrganizationMembers(Long a){
        List<Membership> result=new ArrayList<>();

        for(Membership m:membershipRepository.findByOrganizationId(a)){
            if(m.getStatus()==MembershipStatus.ACTIVE){
                result.add(m);
            }
        }

        return result;
    }

    public List<Organization> getStudentOrganizations(Long a){
        List<Organization> result=new ArrayList<>();

        for(Membership m:membershipRepository.findByStudentId(a)){
            if(m.getStatus()==MembershipStatus.ACTIVE&&m.getOrganization()!=null&&m.getOrganization().getIsActive()!=null&&m.getOrganization().getIsActive()){
                result.add(m.getOrganization());
            }
        }

        return result;
    }

    public List<MembershipApplication> getPendingApplications(Long a){
        List<MembershipApplication> result=new ArrayList<>();

        for(MembershipApplication app:applicationRepository.findByOrganizationId(a)){
            if(app.getApplicationStatus()==ApplicationStatus.PENDING){
                result.add(app);
            }
        }

        return result;
    }
}     * @return
     */
    public Optional<Organization> findById(Long id) {
        // TODO implement here
        return null;
    }

    /**
     * @param name 
     * @return
     */
    public Optional<Organization> findByName(String name) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Organization> findAllActive() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Organization> findAll() {
        // TODO implement here
        return null;
    }

    /**
     * @param id 
     * @return
     */
    public void deleteById(Long id) {
        // TODO implement here
        return null;
    }

}
