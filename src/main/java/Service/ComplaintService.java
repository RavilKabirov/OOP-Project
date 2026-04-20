
import java.io.*;
import java.util.*;

/**
 * 
 */
public class ComplaintService {

    /**
     * Default constructor
     */
    public ComplaintService() {
    }

    /**
     * 
     */
    private ComplaintRepository complaintRepository;

    /**
     * 
     */
    private TeacherRepository teacherRepository;

    /**
     * 
     */
    private StudentRepository studentRepository;

    /**
     * 
     */
    private ManagerRepository managerRepository;

    /**
     * @param teacherId 
     * @param studentId 
     * @param courseId 
     * @param type 
     * @param description 
     * @return
     */
    public Complaint submitComplaint(Long teacherId, Long studentId, Long courseId, ComplaintType type, String description) {
        // TODO implement here
        return null;
    }

    /**
     * @param managerId 
     * @return
     */
    public List<Complaint> getComplaintsForManager(Long managerId) {
        // TODO implement here
        return null;
    }

    /**
     * @param teacherId 
     * @return
     */
    public List<Complaint> getComplaintsByTeacher(Long teacherId) {
        // TODO implement here
        return null;
    }

    /**
     * @param studentId 
     * @return
     */
    public List<Complaint> getComplaintsByStudent(Long studentId) {
        // TODO implement here
        return null;
    }

    /**
     * @param complaintId 
     * @return
     */
    public Optional<Complaint> getComplaintById(Long complaintId) {
        // TODO implement here
        return null;
    }

    /**
     * @param complaintId 
     * @param managerId 
     * @param status 
     * @param resolution 
     * @return
     */
    public void reviewComplaint(Long complaintId, Long managerId, ComplaintStatus status, String resolution) {
        // TODO implement here
        return null;
    }

    /**
     * @param complaintId 
     * @param managerId 
     * @param resolution 
     * @return
     */
    public void resolveComplaint(Long complaintId, Long managerId, String resolution) {
        // TODO implement here
        return null;
    }

    /**
     * @param complaintId 
     * @param managerId 
     * @param reason 
     * @return
     */
    public void rejectComplaint(Long complaintId, Long managerId, String reason) {
        // TODO implement here
        return null;
    }

}