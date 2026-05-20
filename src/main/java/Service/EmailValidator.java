import java.util.regex.Pattern;

public class EmailValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private EmailValidator() {}

    public static void validate(String email) {
        if (!isValid(email)) {
            throw new IllegalArgumentException("Invalid email address.");
        }
    }

    public static boolean isValid(String email) {
        return email != null
                && !email.isBlank()
                && !email.contains(" ")
                && EMAIL_PATTERN.matcher(email).matches();
    }
}
