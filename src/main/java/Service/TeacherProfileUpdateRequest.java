public class TeacherProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private TeacherPosition teacherPosition;

    public TeacherProfileUpdateRequest() {}

    public TeacherProfileUpdateRequest(String firstName, String lastName, String email, TeacherPosition teacherPosition) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.teacherPosition = teacherPosition;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public TeacherPosition getTeacherPosition() { return teacherPosition; }
    public void setTeacherPosition(TeacherPosition teacherPosition) { this.teacherPosition = teacherPosition; }
}