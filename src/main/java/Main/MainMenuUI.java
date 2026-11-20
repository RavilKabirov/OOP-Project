import java.util.Scanner;

/**
 * 
 * Обязанности:
 * - Экран логина если юзер не аутентифицирован
 * - После логина ижем на нужный нам UI.
 * - Логаут и выход из приложения
 *
 */
public class MainMenuUI {
    private final ApplicationContext ctx;
    private final Scanner sc;
    

    // Подменю. Создаются всего один раз, а потом реюзаются.
    private final StudentUI studentUI;
    private final TeacherUI teacherUI;
    private final DeanUI deanUI;
    private final AdminUI adminUI;
    private final ResearcherUI researcherUI;
    private final HRManagerUI hrManagerUI;
    private final PRManagerUI prManagerUI;
    private final RoleMenuResolver roleMenuResolver;
    private final PasswordEncoder pe;
    private final AuthService authService;

    public MainMenuUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
        this.pe = new PasswordEncoder();
        this.authService = new AuthService(ctx.getUserRepository(), pe);
        this.studentUI = new StudentUI(ctx, sc);
        this.teacherUI = new TeacherUI(ctx, sc);
        this.deanUI = new DeanUI(ctx, sc);
        this.adminUI = new AdminUI(ctx, sc);
        this.researcherUI = new ResearcherUI(ctx, sc);
        this.hrManagerUI = new HRManagerUI(ctx, sc);
        this.prManagerUI = new PRManagerUI(ctx, sc);
        this.roleMenuResolver = new RoleMenuResolver(
                deanUI, hrManagerUI, prManagerUI, teacherUI, studentUI, adminUI,
                this::showGenericMenu);
    }


    public void run() {
        ConsoleUtils.printHeader("Welcome to UniSystem");

        boolean running = true;
        while (running) {
            if (!AuthContext.isLoggedIn()) {
                running = showGuestMenu();
            } else {
                showRoleMenu();
            }
        }

        System.out.println("\nGoodbye!");
    }

    // Гостевое меню. Возвращает false если выходим из приложения

    private boolean showGuestMenu() {
        ConsoleUtils.printHeader("Main Menu");
        System.out.println("  1. Log in");
        System.out.println("  0. Exit");

        int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 1);
        switch (choice) {
            case 1: handleLogin();  break;
            case 0: return false;
        }
        return true;
    }

    private void handleLogin() {
        ConsoleUtils.printHeader("Log In");
        String email    = ConsoleUtils.readNonBlank(sc, "  Email:    ");
        String password = ConsoleUtils.readPassword(sc,  "  Password: ");

        try {
            User user = authService.login(email, password);
            if (user == null) {
                ConsoleUtils.printError("Login failed.");
                return;
            }
            AuthContext.setCurrentUser(user);
            ConsoleUtils.printSuccess("Welcome, " + user.getFullName() + "!");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
    }


    private void showRoleMenu() {
        roleMenuResolver.resolve(AuthContext.getCurrentUser()).run();
        // Логаут когда UI возвращает значение
        if (AuthContext.isLoggedIn()) {
            AuthContext.logout();
        }
    }

    // Generic на всякий случай с минимальными действиями 

    private void showGenericMenu() {
        boolean running = true;
        while (running) {
            User user = AuthContext.getCurrentUser();
            ConsoleUtils.printHeader("Account - " + user.getFullName() + "  [" + user.getClass().getSimpleName() + "]");
            System.out.println("  1. View profile");
            System.out.println("  2. Change password");
            if (AuthContext.isResearcher()) {
                System.out.println("  3. Researcher portal");
            }
            System.out.println("  0. Log out");
            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, AuthContext.isResearcher() ? 3 : 2);
            switch (choice) {
                case 1: showProfile(user);          break;
                case 2: handleChangePassword(user); break;
                case 3:
                    if (AuthContext.isResearcher()) researcherUI.run();
                    else ConsoleUtils.printError("Invalid option.");
                    break;
                case 0: running = false;            break;
            }
        }
    }

    // Хелперы для generic UI

    private void showProfile(User user) {
        ConsoleUtils.printHeader("Profile");
        System.out.println("  Name:   " + user.getFullName());
        System.out.println("  Email:  " + user.getEmail());
        System.out.println("  Role:   " + user.getClass().getSimpleName());
        System.out.println("  Active: " + user.isActive());
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void handleChangePassword(User user) {
        ConsoleUtils.printHeader("Change Password");
        String current = ConsoleUtils.readPassword(sc, "  Current password: ");
        if (!pe.matches(current, user.getPasswordHash())) {
            ConsoleUtils.printError("Incorrect password.");
            return;
        }

        String newPass  = ConsoleUtils.readNonBlank(sc, "  New password:     ");
        String confirm  = ConsoleUtils.readNonBlank(sc, "  Confirm password: ");
        if (!newPass.equals(confirm)) {
            ConsoleUtils.printError("Passwords do not match.");
            return;
        }
        user.setPasswordHash(pe.encode(newPass));
        ctx.getUserRepository().save(user);
        ConsoleUtils.printSuccess("Password updated.");
        ConsoleUtils.pressEnterToContinue(sc);
    }
}
