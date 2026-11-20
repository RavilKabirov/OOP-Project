public class HRManagerMenuStrategy implements RoleMenuStrategy {
    private final HRManagerUI hrManagerUI;

    public HRManagerMenuStrategy(HRManagerUI hrManagerUI) {
        this.hrManagerUI = hrManagerUI;
    }

    @Override
    public void run() {
        hrManagerUI.run();
    }
}
