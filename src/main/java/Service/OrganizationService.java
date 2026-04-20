
import java.io.*;
import java.util.*;

/**
 * 
 */
public class OrganizationService {

    /**
     * Default constructor
     */
    public OrganizationService() {
    }

    /**
     * 
     */
    private OrganizationRepository organizationRepository;

    /**
     * 
     */
    private MembershipRepository membershipRepository;

    /**
     * 
     */
    private MembershipApplicationRepository applicationRepository;

    /**
     * 
     */
    private StudentRepository studentRepository;

    /**
     * @param data 
     * @param leaderId 
     * @return
     */
    public Organization createOrganization(OrganizationCreationRequest data, Long leaderId) {
        // TODO implement here
        return null;
    }

    /**
     * @param orgId 
     * @param data 
     * @return
     */
    public Organization updateOrganization(Long orgId, OrganizationUpdateRequest data) {
        // TODO implement here
        return null;
    }

    /**
     * @param orgId 
     * @return
     */
    public void deleteOrganization(Long orgId) {
        // TODO implement here
        return null;
    }

    /**
     * @param orgId 
     * @return
     */
    public Optional<Organization> getOrganizationById(Long orgId) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Organization> getAllActiveOrganizations() {
        // TODO implement here
        return null;
    }

    /**
     * @param keyword 
     * @return
     */
    public List<Organization> searchOrganizations(String keyword) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @param orgId 
     * @param message 
     * @return
     */
    public MembershipApplication applyForMembership(Long studentId, Long orgId, String message) {
        // TODO implement here
        return null;
    }

    /**
     * @param appId 
     * @param leaderId 
     * @return
     */
    public Membership approveApplication(Long appId, Long leaderId) {
        // TODO implement here
        return null;
    }

    /**
     * @param appId 
     * @param leaderId 
     * @return
     */
    public void rejectApplication(Long appId, Long leaderId) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @param orgId 
     * @param role 
     * @return
     */
    public Membership addMember(Long studentId, Long orgId, MembershipRole role) {
        // TODO implement here
        return null;
    }

    /**
     * @param membershipId 
     * @return
     */
    public void removeMember(Long membershipId) {
        // TODO implement here
        return null;
    }

    /**
     * @param membershipId 
     * @param newRole 
     * @return
     */
    public void changeMemberRole(Long membershipId, MembershipRole newRole) {
        // TODO implement here
        return null;
    }

    /**
     * @param orgId 
     * @return
     */
    public List<Membership> getOrganizationMembers(Long orgId) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @return
     */
    public List<Organization> getStudentOrganizations(Long studentId) {
        // TODO implement here
        return null;
    }

    /**
     * @param orgId 
     * @return
     */
    public List<MembershipApplication> getPendingApplications(Long orgId) {
        // TODO implement here
        return null;
    }

}