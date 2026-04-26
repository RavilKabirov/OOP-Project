
import java.io.*;
import java.util.*;


public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Incorrect email or password"));
        if (!user.isActive()) throw new IllegalStateException("Account blocked");
        if (!passwordEncoder.matches(password, user.getPasswordHash()))
            throw new IllegalArgumentException("Incorrect email or password");
        return user;
    }


    public User register(String email, String password, String firstName, String lastName, UserRole role) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already used");
        }
        User user;
        if (role == UserRole.ADMIN) {
            user = new Admin(email, firstName, lastName);
        } else {
            user = new User(email, firstName, lastName);
        }
        user.setPassword(passwordEncoder.encode(password));
        user.switchActive();

        return userRepository.save(user);
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User is not found"));
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash()))
            throw new IllegalArgumentException("Old password is incorrect");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


}