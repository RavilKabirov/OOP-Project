import java.util.*;
import java.time.*;

public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    public OrganizationService(OrganizationRepository organizationRepository, MembershipRepository membershipRepository, MembershipApplicationRepository applicationRepository, StudentRepository studentRepository) {
        this.organizationRepository = organizationRepository;
        this.membershipRepository = membershipRepository;
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
    }

    public Organization createOrganization(OrganizationCreationRequest data, String leaderStudentId) {
        Student leader = studentRepository.findByStudentId(leaderStudentId).orElseThrow(() -> new IllegalArgumentException("Leader not found"));

        if (organizationRepository.findByName(data.getName()).isPresent()) {
            throw new IllegalStateException(
                    "Organization with this name already exists.");
        }

        Organization org = new Organization();
        org.setName(data.getName());
        org.setDescription(data.getDescription());
        org.setLeader(leader);
        org.setFoundedAt(LocalDateTime.now());
        org.setIsActive(true);

        Organization saved = organizationRepository.save(org);

        Membership membership = new Membership();
        membership.setStudent(leader);
        membership.setOrganization(saved);
        membership.setRole(MembershipRole.LEADER);
        membership.activate();

        Membership savedMembership = membershipRepository.save(membership);
        saved.addMember(savedMembership);

        return saved;
    }

    public Organization updateOrganization(Long orgId, OrganizationUpdateRequest data) {
        Organization org = organizationRepository.findById(orgId).orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        if (data.getName() != null) org.setName(data.getName());
        if (data.getDescription() != null) org.setDescription(data.getDescription());

        return organizationRepository.save(org);
    }

    public void deleteOrganization(Long orgId) {
        Organization org = organizationRepository.findById(orgId).orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        for (Membership m : membershipRepository.findByOrganizationId(orgId)) {
            // Перед удалением организации нужно убрать всех активных участников.
            if (m.getStatus() == MembershipStatus.ACTIVE) {
                throw new IllegalStateException("Cannot delete organization with active members.");
            }
        }

        for (MembershipApplication app : applicationRepository.findByOrganizationId(orgId)) {
            // Pending заявки тоже блокируют удаление, их сначала надо approve/reject.
            if (app.getApplicationStatus() == ApplicationStatus.PENDING) {
                throw new IllegalStateException(
                        "Cannot delete organization with pending applications.");
            }
        }

        org.setIsActive(false);
        organizationRepository.save(org);
    }

    public Optional<Organization> getOrganizationById(Long orgId) {
        return organizationRepository.findById(orgId);
    }

    public List<Organization> getAllActiveOrganizations() {
        return organizationRepository.findAllActive();
    }

    public List<Organization> searchOrganizations(String keyword) {
        if (keyword == null) return organizationRepository.findAllActive();
        List<Organization> result = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (Organization o : organizationRepository.findAllActive()) {
            if (o.getName() != null && o.getName().toLowerCase().contains(lower)) {
                result.add(o);
            }
        }
        return result;
    }

    public MembershipApplication applyForMembership(String studentId, Long orgId, String applicationBody) {
        Student student = studentRepository.findByStudentId(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Organization org = organizationRepository.findById(orgId).orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        if (!Boolean.TRUE.equals(org.getIsActive())) {
            // В неактивную организацию нельзя подаваться, она как бы уже закрыта.
            throw new IllegalStateException("Organization is not active.");
        }

        if (membershipRepository.existsByStudentIdAndOrganizationId(studentId, orgId)) {
            // Проверка от дублей: один student не может быть участником одной organization дважды.
            throw new IllegalStateException("Student is already a member.");
        }

        MembershipApplication app = new MembershipApplication();
        app.setStudent(student);
        app.setOrganization(org);
        app.setBody(applicationBody);
        app.setApplicationStatus(ApplicationStatus.PENDING);
        app.setSubmittedAt(LocalDateTime.now());

        return applicationRepository.save(app);
    }

    public Membership approveApplication(Long applicationId, Long leaderId) {
        MembershipApplication app = applicationRepository.findById(applicationId).orElseThrow(() -> new IllegalArgumentException("Application not found"));

        Organization org = app.getOrganization();

        // Approve/reject может делать только leader этой организации.
        if (!org.getLeader().getId().equals(leaderId)) {
            throw new IllegalStateException("Only the leader can approve applications.");
        }
        if (app.getApplicationStatus() != ApplicationStatus.PENDING) {
            // Нельзя повторно обработать заявку, если она уже была закрыта.
            throw new IllegalStateException("Application is not pending.");
        }

        app.approve();
        applicationRepository.save(app);

        Membership membership = new Membership();
        membership.setStudent(app.getStudent());
        membership.setOrganization(org);
        membership.setRole(MembershipRole.MEMBER);
        membership.activate();

        Membership saved = membershipRepository.save(membership);
        org.addMember(saved);

        return saved;
    }

    public void rejectApplication(Long applicationId, Long leaderId) {
        MembershipApplication app = applicationRepository.findById(applicationId).orElseThrow(() -> new IllegalArgumentException("Application not found"));

        Organization org = app.getOrganization();

        // Тут такая же проверка прав как и в approve.
        if (!org.getLeader().getId().equals(leaderId)) {
            throw new IllegalStateException("Only the leader can reject applications.");
        }
        if (app.getApplicationStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Application is not pending.");
        }

        app.reject();
        applicationRepository.save(app);
    }

    public Membership addMember(String studentId, Long orgId, MembershipRole role) {
        Student student = studentRepository.findByStudentId(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Organization org = organizationRepository.findById(orgId).orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        if (!Boolean.TRUE.equals(org.getIsActive())) {
            throw new IllegalStateException("Organization is not active.");
        }
        if (membershipRepository.existsByStudentIdAndOrganizationId(studentId, orgId)) {
            throw new IllegalStateException("Student is already a member.");
        }

        Membership membership = new Membership();
        membership.setStudent(student);
        membership.setOrganization(org);
        membership.setRole(role);
        membership.activate();

        Membership saved = membershipRepository.save(membership);
        org.addMember(saved);

        return saved;
    }

    public void removeMember(Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId).orElseThrow(() -> new IllegalArgumentException("Membership not found"));
        membership.deactivate();
        membershipRepository.save(membership);
    }

    public void changeMemberRole(Long membershipId, MembershipRole newRole) {
        Membership membership = membershipRepository.findById(membershipId).orElseThrow(() -> new IllegalArgumentException("Membership not found"));
        membership.setRole(newRole);
        membershipRepository.save(membership);
    }

    public List<Membership> getOrganizationMembers(Long orgId) {
        List<Membership> result = new ArrayList<>();
        for (Membership m : membershipRepository.findByOrganizationId(orgId)) {
            if (m.getStatus() == MembershipStatus.ACTIVE) result.add(m);
        }
        return result;
    }

    public List<Organization> getStudentOrganizations(Long userId) {
        List<Organization> result = new ArrayList<>();
        for (Membership m : membershipRepository.findByStudentId(userId)) {
            if (m.getStatus() == MembershipStatus.ACTIVE && m.getOrganization() != null && Boolean.TRUE.equals(m.getOrganization().getIsActive())) {
                result.add(m.getOrganization());
            }
        }
        return result;
    }

    public List<MembershipApplication> getPendingApplications(Long orgId) {
        List<MembershipApplication> result = new ArrayList<>();
        for (MembershipApplication app : applicationRepository.findByOrganizationId(orgId)) {
            if (app.getApplicationStatus() == ApplicationStatus.PENDING) {
                result.add(app);
            }
        }
        return result;
    }
}
