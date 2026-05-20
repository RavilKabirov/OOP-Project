import java.util.List;

public class TechSupportService {

    private final TechSupportRequestRepository requestRepository;
    private final UserRepository userRepository;

    public TechSupportService(TechSupportRequestRepository requestRepository,  UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    public TechSupportRequest submitRequest(Long employeeId, String title, String description) {
        Employee requester = requireEmployee(employeeId);
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Request title must not be blank.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Request description must not be blank.");
        }

        TechSupportRequest request = new TechSupportRequest();
        request.setRequester(requester);
        request.setTitle(title);
        request.setDescription(description);
        return requestRepository.save(request);
    }

    public void updateStatus(Long specialistId, Long requestId, TechSupportRequestStatus status) {
        requireTechSupportSpecialist(specialistId);
        TechSupportRequest request = requireRequest(requestId);
        if (status == TechSupportRequestStatus.PENDING) {
            // PENDING ставится только при создании, саппорт уже двигает реквест дальше.
            throw new IllegalArgumentException("Tech support cannot set request back to PENDING.");
        }
        request.setStatus(status);
        request.setHandledById(specialistId);
        requestRepository.save(request);
    }

    public List<TechSupportRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    public List<TechSupportRequest> getRequestsByStatus(TechSupportRequestStatus status) {
        return requestRepository.findByStatus(status);
    }

    public List<TechSupportRequest> getEmployeeRequests(Long employeeId) {
        requireEmployee(employeeId);
        return requestRepository.findByRequester(employeeId);
    }

    private Employee requireEmployee(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + userId));
        if (!(user instanceof Employee)) {
            throw new IllegalArgumentException("Only employees can send tech support requests.");
        }
        return (Employee) user;
    }

    private TechSupportSpecialist requireTechSupportSpecialist(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Tech support specialist not found: " + userId));
        if (!(user instanceof TechSupportSpecialist)) {
            throw new IllegalArgumentException("Only TechSupportSpecialist can update support requests.");
        }
        return (TechSupportSpecialist) user;
    }

    private TechSupportRequest requireRequest(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new IllegalArgumentException("Tech support request not found: " + requestId));
    }
}
