public class AuthContext {
        private static User currentUser;

        public static void setCurrentUser(User user) { currentUser = user; }
        public static User getCurrentUser() {
            if (currentUser == null) throw new IllegalStateException("User is not authenticated");
            return currentUser;
        }
        public static boolean isAdmin() { return currentUser instanceof Admin; }
        public static void logout() { currentUser = null; }
    }