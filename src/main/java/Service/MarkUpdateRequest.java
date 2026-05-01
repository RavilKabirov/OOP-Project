
public class MarkUpdateRequest {

    private final MarkType type;
    private final double newValue;
    private final String comment;

    public MarkUpdateRequest(MarkType type, double newValue, String comment) {
        if (type == null) {
            throw new IllegalArgumentException("MarkType must not be null.");
        }
        if ((type == MarkType.FIRST_ATTESTATION || type == MarkType.SECOND_ATTESTATION) && (newValue < 0.0 || newValue > 30.0)) {
            throw new IllegalArgumentException(
                "Score must be between 0 and 30, got: " + newValue);
        }
        if ((type == MarkType.FINAL_EXAM) && (newValue < 0.0 || newValue > 40.0)) {
            throw new IllegalArgumentException(
                "Score must be between 0 and 40, got: " + newValue);
        }
        this.type     = type;
        this.newValue = newValue;
        this.comment  = comment;
    }

    public MarkUpdateRequest(MarkType type, double newValue) {
        this(type, newValue, null);
    }

    public MarkType getType()     { return type; }
    public double   getNewValue() { return newValue; }
    public String   getComment()  { return comment; }

    @Override
    public String toString() {
        return "MarkUpdateRequest{type=" + type +
               ", newValue=" + newValue +
               ", comment='" + (comment != null ? comment : "") + "'}";
    }
}