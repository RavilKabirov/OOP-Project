import java.util.*;


public class ComplaintRepository {

    public List<Complaint> complaints;
    private long nextId = 1;

    public ComplaintRepository() {
        this.complaints = new ArrayList<>();
    }

    public Complaint save(Complaint complaint) {
        if (complaint.getId() == null) {
            complaint.setId(nextId++);
            complaints.add(complaint);
        } else {
            for (int i = 0; i < complaints.size(); i++) {
                if (complaints.get(i).getId().equals(complaint.getId())) {
                    complaints.set(i, complaint);
                    return complaint;
                }
            }
            complaints.add(complaint); 
        }
        return complaint;
    }

    public Optional<Complaint> findById(Long id) {
        return complaints.stream()
                .filter(c -> c.getId() != null && c.getId().equals(id))
                .findFirst();
    }


    public List<Complaint> findBySender(Long teacherId) {
        List<Complaint> result = new ArrayList<>();
        for (Complaint c : complaints) {
            if (c.getSender() != null
                    && c.getSender().getId() != null
                    && c.getSender().getId().equals(teacherId)) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Complaint> findByRecipient(ManagerForComplaint role) {
        List<Complaint> result = new ArrayList<>();
        for (Complaint c : complaints) {
            if (role.equals(c.getRecipient())) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Complaint> findByTargetStudentId(String studentId) {
        List<Complaint> result = new ArrayList<>();
        for (Complaint c : complaints) {
            if (c.getTargetStudent() != null
                    && studentId.equals(c.getTargetStudent().getStudentId())) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Complaint> findByStatus(ComplaintStatus status) {
        List<Complaint> result = new ArrayList<>();
        for (Complaint c : complaints) {
            if (status.equals(c.getComplaintStatus())) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Complaint> findByUrgency(UrgencyLevel level) {
        List<Complaint> result = new ArrayList<>();
        for (Complaint c : complaints) {
            if (level.equals(c.getUrgencyLevel())) {
                result.add(c);
            }
        }
        return result;
    }

    public List<Complaint> findAll() {
        return new ArrayList<>(complaints);
    }

    public void deleteById(Long id) {
        complaints.removeIf(c -> c.getId() != null && c.getId().equals(id));
    }
}