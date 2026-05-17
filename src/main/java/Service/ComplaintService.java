import java.time.LocalDateTime;
import java.util.List;

public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final TeacherRepository   teacherRepository;
    private final StudentRepository   studentRepository;

    public ComplaintService(ComplaintRepository complaintRepository,
                            TeacherRepository teacherRepository,
                            StudentRepository studentRepository) {
        this.complaintRepository = complaintRepository;
        this.teacherRepository   = teacherRepository;
        this.studentRepository   = studentRepository;
    }

    public Complaint submitComplaint(Long teacherId, String studentId,
                                     ManagerForComplaint recipient,
                                     UrgencyLevel urgency) {
        Teacher teacher = requireTeacher(teacherId);
        Student student = requireStudent(studentId);

        Complaint complaint = new Complaint();
        complaint.setSender(teacher);
        complaint.setTargetStudent(student);
        complaint.setRecipient(recipient);
        complaint.setUrgencyLevel(urgency);
        complaint.setComplaintStatus(ComplaintStatus.SUBMITTED);
        complaint.setCreatedAt(LocalDateTime.now());
        complaint.setUpdatedAt(LocalDateTime.now());

        complaintRepository.save(complaint);

        System.out.printf("[ComplaintService] Complaint #%d submitted by %s about %s → %s [%s]%n",
                complaint.getId(), teacher.getFullName(),
                student.getFullName(), recipient, urgency);
        return complaint;
    }

    public Complaint reviewComplaint(Long complaintId) {
        Complaint complaint = requireComplaint(complaintId);

        if (complaint.getComplaintStatus() != ComplaintStatus.SUBMITTED) {
            throw new IllegalStateException(
                    "Complaint #" + complaintId + " is not SUBMITTED (current: "
                    + complaint.getComplaintStatus() + ").");
        }

        complaint.updateStatus(ComplaintStatus.UNDER_REVIEW);
        complaintRepository.save(complaint);

        System.out.println("[ComplaintService] Complaint #" + complaintId + " is now UNDER_REVIEW.");
        return complaint;
    }

    public Complaint resolveComplaint(Long complaintId) {
        Complaint complaint = requireComplaint(complaintId);

        if (complaint.getComplaintStatus() != ComplaintStatus.UNDER_REVIEW) {
            throw new IllegalStateException(
                    "Complaint #" + complaintId + " must be UNDER_REVIEW to resolve "
                    + "(current: " + complaint.getComplaintStatus() + ").");
        }

        complaint.updateStatus(ComplaintStatus.RESOLVED);
        complaintRepository.save(complaint);

        System.out.println("[ComplaintService] Complaint #" + complaintId + " RESOLVED.");
        return complaint;
    }

    public Complaint rejectComplaint(Long complaintId) {
        Complaint complaint = requireComplaint(complaintId);

        ComplaintStatus current = complaint.getComplaintStatus();
        if (current == ComplaintStatus.RESOLVED || current == ComplaintStatus.REJECTED) {
            throw new IllegalStateException(
                    "Complaint #" + complaintId + " is already " + current + ".");
        }

        complaint.updateStatus(ComplaintStatus.REJECTED);
        complaintRepository.save(complaint);

        System.out.println("[ComplaintService] Complaint #" + complaintId + " REJECTED.");
        return complaint;
    }

    public void deleteComplaint(Long complaintId) {
        Complaint complaint = requireComplaint(complaintId);

        ComplaintStatus status = complaint.getComplaintStatus();
        if (status != ComplaintStatus.RESOLVED && status != ComplaintStatus.REJECTED) {
            throw new IllegalStateException(
                    "Cannot delete complaint #" + complaintId
                    + " while it is still " + status + ".");
        }

        complaintRepository.deleteById(complaintId);
        System.out.println("[ComplaintService] Deleted complaint #" + complaintId + ".");
    }

    public Complaint getComplaintById(Long complaintId) {
        return requireComplaint(complaintId);
    }

    public List<Complaint> getComplaintsByTeacher(Long teacherId) {
        requireTeacher(teacherId);
        return complaintRepository.findBySender(teacherId);
    }

    public List<Complaint> getComplaintsByStudent(String studentId) {
        requireStudent(studentId);
        return complaintRepository.findByTargetStudentId(studentId);
    }

    public List<Complaint> getComplaintsByRecipient(ManagerForComplaint recipient) {
        return complaintRepository.findByRecipient(recipient);
    }

    public List<Complaint> getComplaintsByStatus(ComplaintStatus status) {
        return complaintRepository.findByStatus(status);
    }

    public List<Complaint> getComplaintsByUrgency(UrgencyLevel urgency) {
        return complaintRepository.findByUrgency(urgency);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }


    private Complaint requireComplaint(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Complaint not found with id: " + id));
    }

    private Teacher requireTeacher(Long teacherId) {
        return teacherRepository.findAll().stream()
                .filter(t -> t.getId() != null && t.getId().equals(teacherId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Teacher not found with id: " + teacherId));
    }

    private Student requireStudent(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found with studentId: " + studentId));
    }
}