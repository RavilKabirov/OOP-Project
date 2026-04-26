import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    private List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(generateId());
        } else {
            Optional<User> existing = findById(user.getId());
            existing.ifPresent(users::remove);
        }
        users.add(user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public void setUsers(List<User> value) {
        this.users = new ArrayList<>(value);
    }

    public void deleteById(Long id) {
        users.removeIf(u -> u.getId().equals(id));
    }

    public boolean existsByEmail(String email) {
        return users.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }
    private Long generateId() {
        return users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0L) + 1;
    }
}