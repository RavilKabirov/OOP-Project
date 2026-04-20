
import java.io.*;
import java.util.*;

/**
 * 
 */
public class AuthService {

    /**
     * Default constructor
     */
    public AuthService() {
    }

    /**
     * 
     */
    public void Attribute1;

    /**
     * 
     */
    private UserRepository userRepository;

    /**
     * 
     */
    private SessionRepository sessionRepository;

    /**
     * 
     */
    private EmailService emailService;

    /**
     * 
     */
    private PasswordEncoder passwordEncoder;

    /**
     * @param email 
     * @param password 
     * @return
     */
    public UserSession login(String email, String password) {
        // TODO implement here
        return null;
    }

    /**
     * @param sessionId 
     * @return
     */
    public void logout(String sessionId) {
        // TODO implement here
        return null;
    }

    /**
     * @param email 
     * @param password 
     * @param firstName 
     * @param lastName 
     * @param role 
     * @return
     */
    public User register(String email, String password, String firstName, String lastName, UserRole role) {
        // TODO implement here
        return null;
    }

    /**
     * @param token 
     * @return
     */
    public void confirmRegistration(String token) {
        // TODO implement here
        return null;
    }

    /**
     * @param email 
     * @return
     */
    public void initiatePasswordReset(String email) {
        // TODO implement here
        return null;
    }

    /**
     * @param oldPass 
     * @param newPassword 
     * @return
     */
    public void resetPassword(String oldPass, String newPassword) {
        // TODO implement here
        return null;
    }

    /**
     * @param userId 
     * @param oldPassword 
     * @param newPassword 
     * @return
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        // TODO implement here
        return null;
    }

    /**
     * @param sessionId 
     * @return
     */
    public boolean validateSession(String sessionId) {
        // TODO implement here
        return false;
    }

}