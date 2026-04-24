
import java.io.*;
import java.util.*;

/**
 * 
 */
public class ComplaintRepository {

    /**
     * Default constructor
     */
    public ComplaintRepository() {
    }

    /**
     * 
     */
    public List<Complaint> complaints;

    /**
     * @param complaint 
     * @return
     */
    public Complaint save(Complaint complaint) {
        // TODO implement here
        return null;
    }

    /**
     * @param id 
     * @return
     */
    public Optional<Complaint> findById(Long id) {
        // TODO implement here
        return null;
    }

    /**
     * @param teacherId 
     * @return
     */
    public List<Complaint> findBySender(Long teacherId) {
        // TODO implement here
        return null;
    }

    /**
     * @param managerId 
     * @return
     */
    public List<Complaint> findByRecipient(Long managerId) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentID 
     * @return
     */
    public List<Complaint> findByTargetStudent(Long studentID) {
        // TODO implement here
        return null;
    }

    /**
     * @param status 
     * @return
     */
    public List<Complaint> findByStatus(ComplaintStatus status) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Complaint> findAll() {
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