import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    private UserRepository users;
    private AuthService auth;

    @BeforeEach
    void setUp() {
        users = new UserRepository();
        auth = new AuthService(users, new PasswordEncoder());
    }

    @Test
    void registersAndAuthenticatesUserCaseInsensitively() {
        User registered = auth.register("ada@uni.edu", "secret", "Ada", "Lovelace", UserRole.USER);

        assertNotEquals("secret", registered.getPasswordHash());
        assertSame(registered, auth.login("ADA@UNI.EDU", "secret"));
    }

    @Test
    void rejectsIncorrectPasswordAndDuplicateEmail() {
        auth.register("ada@uni.edu", "secret", "Ada", "Lovelace", UserRole.USER);

        assertThrows(IllegalArgumentException.class, () -> auth.login("ada@uni.edu", "wrong"));
        assertThrows(IllegalArgumentException.class,
                () -> auth.register("ADA@uni.edu", "other", "A", "B", UserRole.USER));
    }

    @Test
    void blocksInactiveAccount() {
        User user = auth.register("ada@uni.edu", "secret", "Ada", "Lovelace", UserRole.USER);
        user.setActive(false);

        assertThrows(IllegalStateException.class, () -> auth.login("ada@uni.edu", "secret"));
    }
}
