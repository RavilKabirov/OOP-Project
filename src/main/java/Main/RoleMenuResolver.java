import java.util.Objects;

public class RoleMenuResolver {
    private final RoleMenuStrategy deanStrategy;
    private final RoleMenuStrategy hrManagerStrategy;
    private final RoleMenuStrategy prManagerStrategy;
    private final RoleMenuStrategy teacherStrategy;
    private final RoleMenuStrategy studentStrategy;
    private final RoleMenuStrategy adminStrategy;
    private final RoleMenuStrategy fallbackStrategy;

    public RoleMenuResolver(DeanUI deanUI,
                            HRManagerUI hrManagerUI,
                            PRManagerUI prManagerUI,
                            TeacherUI teacherUI,
                            StudentUI studentUI,
                            AdminUI adminUI,
                            Runnable fallbackMenu) {
        this.deanStrategy = new DeanMenuStrategy(deanUI);
        this.hrManagerStrategy = new HRManagerMenuStrategy(hrManagerUI);
        this.prManagerStrategy = new PRManagerMenuStrategy(prManagerUI);
        this.teacherStrategy = new TeacherMenuStrategy(teacherUI);
        this.studentStrategy = new StudentMenuStrategy(studentUI);
        this.adminStrategy = new AdminMenuStrategy(adminUI);
        this.fallbackStrategy = () -> fallbackMenu.run();
        Objects.requireNonNull(fallbackMenu, "fallbackMenu");
    }

    public RoleMenuStrategy resolve(User user) {
        Objects.requireNonNull(user, "user");

        // Dean extends Teacher, so check the more specific role first.
        if (user instanceof Dean) return deanStrategy;
        if (user instanceof HRManager) return hrManagerStrategy;
        if (user instanceof PRManager) return prManagerStrategy;
        if (user instanceof Teacher) return teacherStrategy;
        if (user instanceof Student) return studentStrategy;
        if (user instanceof Admin) return adminStrategy;
        return fallbackStrategy;
    }
}
