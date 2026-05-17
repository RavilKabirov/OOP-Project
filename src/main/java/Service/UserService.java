import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void blockUser(Long userId) {
        User user = getUserById(userId);
        user.setActive(false);
        userRepository.save(user);
    }

    public void unblockUser(Long userId) {
        User user = getUserById(userId);
        user.setActive(true);
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<User> searchUsers(String query, SearchFilters filters) {
        List<User> result = new ArrayList<>();
        List<User> allUsers = userRepository.getUsers();

        for (User user : allUsers) {
            boolean matches = true;
            if (query != null && !query.isEmpty()) {
                String lower = query.toLowerCase();
                if (!(user.getFullName().toLowerCase().contains(lower) ||
                        user.getEmail().toLowerCase().contains(lower))) {
                    matches = false;
                }
            }

            if (filters != null) {
                if (filters.isActiveOnly() && !user.isActive()) {
                    matches = false;
                }
            }

            if (matches) {
                result.add(user);
            }
        }

        return result;
    }

    public User createUser(UserCreationRequest userData) {
        if (userRepository.existsByEmail(userData.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User(
                userData.getEmail(),
                userData.getFirstName(),
                userData.getLastName()
        );
        PasswordEncoder pe = new PasswordEncoder();

        user.setPasswordHash(pe.encode(userData.getPassword()));

        return userRepository.save(user);
    }

    public User updateUser(Long userId, UserUpdateRequest userData) {
        User user = getUserById(userId);

        if (userData.getFirstName() != null && userData.getLastName() != null) {
            user.setFullName(userData.getFirstName(), userData.getLastName());
        }

        if (userData.getEmail() != null) {
            user.setEmail(userData.getEmail());
        }

        if (userData.getPassword() != null) {
        		PasswordEncoder pe = new PasswordEncoder();
            user.setPasswordHash(pe.encode(userData.getPassword()));
        }

        return userRepository.save(user);
    }
}