import java.util.Scanner;
/**
 * Начало всего приложения
 * Создает AppContext и передает все остальное в интерфейс MainMenu
 */
public class MainFinal {
    public static void main(String[] args) {
        ApplicationContext ctx = new ApplicationContext();
        Scanner scanner = new Scanner(System.in);
        try {
            new MainMenuUI(ctx, scanner).run();
        } finally {
            // Сохраняем в finally, чтобы даже при обычном выходе через 0 база не потерялась.
            ctx.saveData();
            scanner.close();
        }
    }
}
