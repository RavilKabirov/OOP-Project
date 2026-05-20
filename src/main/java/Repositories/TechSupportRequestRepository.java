import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TechSupportRequestRepository implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private final List<TechSupportRequest> requests = new ArrayList<>();
    private long nextId = 1;

    public TechSupportRequest save(TechSupportRequest request) {
        if (request.getId() == null) {
            request.setId(nextId++);
            requests.add(request);
        } else {
            for (int i = 0; i < requests.size(); i++) {
                if (requests.get(i).getId().equals(request.getId())) {
                    requests.set(i, request);
                    return request;
                }
            }
            requests.add(request);
        }
        return request;
    }

    public Optional<TechSupportRequest> findById(Long id) {
        return requests.stream()
                .filter(r -> r.getId() != null && r.getId().equals(id))
                .findFirst();
    }

    public List<TechSupportRequest> findAll() {
        return new ArrayList<>(requests);
    }

    public List<TechSupportRequest> findByRequester(Long requesterId) {
        List<TechSupportRequest> result = new ArrayList<>();
        for (TechSupportRequest request : requests) {
            if (request.getRequester() != null
                    && request.getRequester().getId() != null
                    && request.getRequester().getId().equals(requesterId)) {
                result.add(request);
            }
        }
        return result;
    }

    public List<TechSupportRequest> findByStatus(TechSupportRequestStatus status) {
        List<TechSupportRequest> result = new ArrayList<>();
        for (TechSupportRequest request : requests) {
            if (request.getStatus() == status) {
                result.add(request);
            }
        }
        return result;
    }
}
