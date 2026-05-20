/**
 * Специалист техподдержки, который разбирает реквесты сотрудников.
 */
public class TechSupportSpecialist extends Employee {

    private static final long serialVersionUID = 1L;

    private String supportArea;

    public TechSupportSpecialist() {
        super();
    }

    public TechSupportSpecialist(String email, String firstName, String lastName) {
        super(email, firstName, lastName);
    }

    public String getSupportArea() {
        return supportArea;
    }

    public void setSupportArea(String supportArea) {
        this.supportArea = supportArea;
    }
}
