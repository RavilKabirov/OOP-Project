
public class FacultyCreationRequest {

    private final String name;

    public FacultyCreationRequest(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("School name must not be blank.");
        }
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public String toString() {
        return "FacultyCreationRequest{name='" + name + "'}";
    }
}