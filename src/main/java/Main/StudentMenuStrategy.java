public class StudentMenuStrategy implements RoleMenuStrategy {
    private final StudentUI studentUI;

    public StudentMenuStrategy(StudentUI studentUI) {
        this.studentUI = studentUI;
    }

    @Override
    public void run() {
        studentUI.run();
    }
}
