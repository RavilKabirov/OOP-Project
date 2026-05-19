import java.util.Scanner;

/**
 * Класс для вывода метода для чтения инпута и его валидации с try/catch Очень удобно чтобы не писать каждый раз одно и тоже.
 *
 * Сканер принимается в методах чтобы у нас был один общий сканер на все методы, без двойного буферинга
 */
public class ConsoleUtils {

    private ConsoleUtils() {}

    // Инпуты для чисел

    /**
     * Для одного int. При неправильном вводе репромптит
     */
    public static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (sc.hasNextInt()) {
                int value = sc.nextInt();
                sc.nextLine(); // Скидывает линия для сканера
                return value;
            }
            sc.nextLine(); 
            System.out.println("  Please enter a valid number.");
        }
    }

    /**
     * Для чтения в диапазоне чисел
     */
    public static int readIntInRange(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int value = readInt(sc, prompt);
            if (value >= min && value <= max) return value;
            System.out.println("  Please enter a number between " + min + " and " + max + ".");
        }
    }

    // Инпуты для стрингов

    /**
     * Просто линия с тримом. Пустая строка если просто нажали enter
     */
    public static String readLine(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    /**
     * То же самое, но нельзя пустую строку
     */
    public static String readNonBlank(Scanner sc, String prompt) {
        while (true) {
            String value = readLine(sc, prompt);
            if (!value.isBlank()) return value;
            System.out.println("  This field cannot be empty.");
        }
    }

    /**
     * Читает пароль
     */
    public static String readPassword(Scanner sc, String prompt) {
        return readNonBlank(sc, prompt);
    }

    // Инпут для подтверждения

    /**
     * Принимает y или yes case insensitive для true
     */
    public static boolean confirm(Scanner sc, String prompt) {
        String answer = readLine(sc, prompt + " (y/n): ");
        return answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes");
    }

    // Для визуала

    public static void printDivider() {
        System.out.println("─────────────────────────────────────────");
    }

    public static void printHeader(String title) {
        printDivider();
        System.out.println("  " + title);
        printDivider();
    }

    public static void printSuccess(String message) {
        System.out.println("  ✓ " + message);
    }

    public static void printError(String message) {
        System.out.println("  ✗ Error: " + message);
    }

    public static void pressEnterToContinue(Scanner sc) {
        System.out.print("\n  Press Enter to continue...");
        sc.nextLine();
    }
}