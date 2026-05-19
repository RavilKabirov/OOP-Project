/**
 * Здесь мы проверяем instance нашего юзера для вывода нужного UI для него. Также здесь есть удобные методы для проверки каста c exceptions, чтобы не повторять один и тот же код проверки юзера на каст
 */
public class AuthContext {

    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
        System.out.println("[AuthContext] User logged out.");
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    // Проверка на роли

    public static boolean isAdmin() {
        return currentUser instanceof Admin;
    }

    public static boolean isDean() {
        return currentUser instanceof Dean;
    }

    public static boolean isTeacher() {
        // Dean расширяет учителя, но для него у нас отдельный UI, поэтому здесь он не считается за учителя
        return currentUser instanceof Teacher && !(currentUser instanceof Dean);
    }

    public static boolean isStudent() {
        return currentUser instanceof Student;
    }

    public static boolean isMasterStudent() {
        return currentUser instanceof MasterStudent;
    }

    public static boolean isPhDStudent() {
        return currentUser instanceof PHDStudent;
    }

    public static boolean isManager() {
        return currentUser instanceof Manager && !(currentUser instanceof Dean);
    }

    public static boolean isHRManager() {
        return currentUser instanceof HRManager;
    }

    public static boolean isPRManager() {
        return currentUser instanceof PRManager;
    }

    public static boolean isResearcher() {
        return currentUser instanceof IResearcher;
    }

    // Проверка каста с прокидкой exceptions

    public static Student asStudent() {
        if (!(currentUser instanceof Student))
            throw new IllegalStateException("Current user is not a Student.");
        return (Student) currentUser;
    }

    public static Teacher asTeacher() {
        if (!(currentUser instanceof Teacher))
            throw new IllegalStateException("Current user is not a Teacher.");
        return (Teacher) currentUser;
    }

    public static Dean asDean() {
        if (!(currentUser instanceof Dean))
            throw new IllegalStateException("Current user is not a Dean.");
        return (Dean) currentUser;
    }

    public static IResearcher asResearcher() {
        if (!(currentUser instanceof IResearcher))
            throw new IllegalStateException("Current user is not a Researcher.");
        return (IResearcher) currentUser;
    }

    public static HRManager asHRManager() {
        if (!(currentUser instanceof HRManager))
            throw new IllegalStateException("Current user is not an HR Manager.");
        return (HRManager) currentUser;
    }

    public static PRManager asPRManager() {
        if (!(currentUser instanceof PRManager))
            throw new IllegalStateException("Current user is not a PR Manager.");
        return (PRManager) currentUser;
    }
}
