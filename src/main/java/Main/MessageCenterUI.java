import java.util.List;
import java.util.Scanner;

/**
 * Общее меню сообщений для всех Employee.
 * Сделал один класс, потому что у teacher/dean/hr/pr логика сообщений одинаковая.
 */
public class MessageCenterUI {

    private final ApplicationContext ctx;
    private final Scanner sc;

    public MessageCenterUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
    }

    public void runForEmployee(Employee employee) {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Messages");
            System.out.println("  1. Inbox");
            System.out.println("  2. Sent");
            System.out.println("  3. Send message");
            System.out.println("  4. Mark as read");
            System.out.println("  5. Delete message");
            System.out.println("  6. Conversation");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 6);
            switch (choice) {
                case 1: showInbox(employee); break;
                case 2: showSent(employee); break;
                case 3: sendMessage(employee); break;
                case 4: markAsRead(employee); break;
                case 5: deleteMessage(employee); break;
                case 6: showConversation(employee); break;
                case 0: running = false; break;
            }
        }
    }

    private void showInbox(Employee employee) {
        ConsoleUtils.printHeader("Inbox");
        printMessages(ctx.getMessageService().getInbox(employee.getId()), true);
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showSent(Employee employee) {
        ConsoleUtils.printHeader("Sent Messages");
        printMessages(ctx.getMessageService().getSentMessages(employee.getId()), false);
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void sendMessage(Employee employee) {
        ConsoleUtils.printHeader("Send Message");
        try {
            Long recipientId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Recipient user ID: "));
            String subject = ConsoleUtils.readNonBlank(sc, "  Subject: ");
            String body = ConsoleUtils.readNonBlank(sc, "  Body: ");
            ctx.getMessageService().sendMessage(employee.getId(), recipientId, subject, body);
            ConsoleUtils.printSuccess("Message sent.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void markAsRead(Employee employee) {
        ConsoleUtils.printHeader("Mark Message As Read");
        try {
            Long messageId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Message ID: "));
            ctx.getMessageService().markAsRead(messageId, employee.getId());
            ConsoleUtils.printSuccess("Message marked as read.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void deleteMessage(Employee employee) {
        // Сообщение удаляется только "для себя", физически из репозитория оно не пропадает.
        ConsoleUtils.printHeader("Delete Message");
        try {
            Long messageId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Message ID: "));
            ctx.getMessageService().deleteMessage(messageId, employee.getId());
            ConsoleUtils.printSuccess("Message deleted for you.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showConversation(Employee employee) {
        ConsoleUtils.printHeader("Conversation");
        try {
            Long otherId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Other employee user ID: "));
            printMessages(ctx.getMessageService().getConversation(employee.getId(), otherId), true);
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void printMessages(List<Message> messages, boolean showRead) {
        if (messages.isEmpty()) {
            System.out.println("  No messages.");
            return;
        }
        for (Message m : messages) {
            System.out.printf("  #%-3d from=%-4d to=%-4d [%s] %s%s%n",
                    m.getId(),
                    m.getSenderId(),
                    m.getRecipientId(),
                    m.getSentAt().toLocalDate(),
                    showRead ? "read=" + m.isRead() + " " : "",
                    m.getSubject());
        }
    }
}
