public class PRManagerMenuStrategy implements RoleMenuStrategy {
    private final PRManagerUI prManagerUI;

    public PRManagerMenuStrategy(PRManagerUI prManagerUI) {
        this.prManagerUI = prManagerUI;
    }

    @Override
    public void run() {
        prManagerUI.run();
    }
}
