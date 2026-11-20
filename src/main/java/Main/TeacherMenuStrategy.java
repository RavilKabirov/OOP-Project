public class TeacherMenuStrategy implements RoleMenuStrategy {
    private final TeacherUI teacherUI;

    public TeacherMenuStrategy(TeacherUI teacherUI) {
        this.teacherUI = teacherUI;
    }

    @Override
    public void run() {
        teacherUI.run();
    }
}
