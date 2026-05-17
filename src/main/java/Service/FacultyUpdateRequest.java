
public class FacultyUpdateRequest {

    private final String name;
    public FacultyUpdateRequest(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public String toString() {
        return "FacultyUpdateRequest{name='" + name + "'}";
    }
}