import java.util.*;

public class main {



    public static void main(String[] args) {
        UserRepository userRepo = new UserRepository();
        PasswordEncoder encoder = new PasswordEncoder();
        AuthService authService = new AuthService(userRepo, encoder);
        if (userRepo.getUsers().isEmpty()) {
            User admin = authService.register("admin@admin.com", "admin", "Admin", "Default", UserRole.ADMIN);
            System.out.println("Created admin: " + admin.getEmail() + " / password: admin");
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== University system ===");

        while (running) {
            if (AuthContext.getCurrentUser() == null) {
                System.out.println("\n--- Main menu ---");
                System.out.println("1. Sing in");
                System.out.println("0. Exit");
                System.out.print("Choose option: ");

                int choice = readInt(scanner);
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        handleLogin(scanner, authService);
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                         System.out.println("Incorrect sign.");
                }
            } else {
                User current = AuthContext.getCurrentUser();
                System.out.println("\n--- Kabinet (" + current.getEmail() + ") ---");
                System.out.println("1. Change password");
                System.out.println("2. View profile");
                if (AuthContext.isAdmin()) {
                    System.out.println("3. Register new user");
                }
                System.out.println("0. Exit from account");
                System.out.print("Choose Option: ");

                int choice = readInt(scanner);
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        handleChangePassword(scanner, authService, current);
                        break;
                    case 2:
                        showProfile(current);
                        break;
                    case 3: 
                        if (AuthContext.isAdmin()) {
                            handleRegistration(scanner, authService);
                        } else {
                            System.out.println("incorrect.");
                        }
                        break;
                    
                    case 0: 
                        AuthContext.logout();
                        System.out.println("You are signed out.");
                        break;
                    
                    default:
                        System.out.println("Incorrect input.");
                }
            }
        }

        System.out.println("Program ended.");
        scanner.close();
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Type number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void handleLogin(Scanner scanner, AuthService authService) {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            User user = authService.login(email, password);
            AuthContext.setCurrentUser(user);
            System.out.println("Welcome, " + user.getFullName() + "!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleRegistration(Scanner scanner, AuthService authService) {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Second Name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Role (USER or ADMIN): ");
        String roleStr = scanner.nextLine().trim().toUpperCase();
        UserRole role = roleStr.equals("ADMIN") ? UserRole.ADMIN : UserRole.USER;

        try {
            User newUser = authService.register(email, password, firstName, lastName, role);
            System.out.println("User created: " + newUser.getEmail() + " (ID=" + newUser.getId() + ")");
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }

    private static void handleChangePassword(Scanner scanner, AuthService authService, User user) {
        System.out.print("Old password: ");
        String oldPass = scanner.nextLine().trim();
        System.out.print("New password: ");
        String newPass = scanner.nextLine().trim();

        try {
            authService.changePassword(user.getId(), oldPass, newPass);
            System.out.println("Password changed successfuly.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void showProfile(User user) {
        System.out.println("\n--- Profile ---");
        System.out.println("Email: " + user.getEmail());
        System.out.println("Name: " + user.getFullName());
        System.out.println("Role: " + (user instanceof Admin ? "Admin" : "User"));
    }
}