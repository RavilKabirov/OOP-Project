import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private Long Id;
    private String firstName;
    private String lastName;
    private String Email;
    private String passwordHash;
    private LocalDateTime createdAt;
    private boolean isActive;

    // Конструктор, используемый сервисом регистрации
    public User(String email, String firstName, String lastName) {
        this.Email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    // Пустой конструктор (для репозиториев или наследования)
    public User() {
    }

    // Геттеры и сеттеры для имени/фамилии
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Email
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    // Прямой доступ к хешу – используется сервисом, не содержит своего хеширования
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    // Активность
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void switchActive() {
        this.isActive = !this.isActive;
    }

    // ID и дата создания
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return getFullName() + " | " + Email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(Id, user.Id) && Objects.equals(Email, user.Email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, Email);
    }
}