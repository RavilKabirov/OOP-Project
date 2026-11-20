public class DeanMenuStrategy implements RoleMenuStrategy {
    private final DeanUI deanUI;

    public DeanMenuStrategy(DeanUI deanUI) {
        this.deanUI = deanUI;
    }

    @Override
    public void run() {
        deanUI.run();
    }
}
