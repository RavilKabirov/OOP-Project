public class AdminMenuStrategy implements RoleMenuStrategy {
    private final AdminUI adminUI;

    public AdminMenuStrategy(AdminUI adminUI) {
        this.adminUI = adminUI;
    }

    @Override
    public void run() {
        adminUI.run();
    }
}
