import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TeacherLessonUI {

    private final ApplicationContext ctx;
    private final Scanner sc;

    public TeacherLessonUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
    }

    public void run(Teacher teacher) {
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Lessons");
            System.out.println("  1. My lessons");
            System.out.println("  2. Create lesson");
            System.out.println("  3. Mark attendance");
            System.out.println("  4. View lesson attendance");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 4);
            switch (choice) {
                case 1: showMyLessons(teacher); break;
                case 2: createLesson(teacher); break;
                case 3: markAttendance(teacher); break;
                case 4: showAttendance(); break;
                case 0: running = false; break;
            }
        }
    }

    private void showMyLessons(Teacher teacher) {
        ConsoleUtils.printHeader("My Lessons");
        List<Lesson> lessons = ctx.getLessonService().getTeacherLessons(teacher.getId());
        if (lessons.isEmpty()) {
            System.out.println("  No lessons yet.");
        } else {
            lessons.forEach(this::printLesson);
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void createLesson(Teacher teacher) {
        ConsoleUtils.printHeader("Create Lesson");
        try {
            if (teacher.getCourses().isEmpty()) {
                System.out.println("  You have no courses.");
                ConsoleUtils.pressEnterToContinue(sc);
                return;
            }

            Course course = selectCourse(teacher);
            String topic = ConsoleUtils.readNonBlank(sc, "  Topic: ");
            LessonType lessonType = readLessonType();
            LocalDateTime startsAt = readDateTime("  Starts at (yyyy-MM-ddTHH:mm): ");
            LocalDateTime endsAt = readDateTime("  Ends at   (yyyy-MM-ddTHH:mm): ");

            Lesson lesson = ctx.getLessonService().createLesson(
                    teacher.getId(), course.getCourseId(), topic, lessonType, startsAt, endsAt);
            ConsoleUtils.printSuccess("Lesson #" + lesson.getId() + " created.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void markAttendance(Teacher teacher) {
        ConsoleUtils.printHeader("Mark Attendance");
        try {
            Long lessonId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Lesson ID: "));
            String studentId = ConsoleUtils.readNonBlank(sc, "  Student ID: ");
            AttendanceStatus status = readAttendanceStatus();
            ctx.getLessonService().markAttendance(teacher.getId(), lessonId, studentId, status);
            ConsoleUtils.printSuccess("Attendance marked.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showAttendance() {
        ConsoleUtils.printHeader("Lesson Attendance");
        try {
            Long lessonId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Lesson ID: "));
            Map<String, AttendanceStatus> attendance = ctx.getLessonService().getAttendance(lessonId);
            if (attendance.isEmpty()) {
                System.out.println("  No attendance records yet.");
            } else {
                attendance.forEach((studentId, status) ->
                        System.out.printf("  %-10s %s%n", studentId, status));
            }
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private Course selectCourse(Teacher teacher) {
        List<Course> courses = teacher.getCourses();
        for (int i = 0; i < courses.size(); i++) {
            System.out.printf("  %d. [%s] %s%n", i + 1, courses.get(i).getCourseId(), courses.get(i).getName());
        }
        int idx = ConsoleUtils.readIntInRange(sc, "  Course: ", 1, courses.size()) - 1;
        return courses.get(idx);
    }

    private AttendanceStatus readAttendanceStatus() {
        AttendanceStatus[] values = AttendanceStatus.values();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = ConsoleUtils.readIntInRange(sc, "  Status: ", 1, values.length);
        return values[choice - 1];
    }

    private LessonType readLessonType() {
        LessonType[] values = LessonType.values();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = ConsoleUtils.readIntInRange(sc, "  Lesson type: ", 1, values.length);
        return values[choice - 1];
    }

    private LocalDateTime readDateTime(String prompt) {
        while (true) {
            try {
                return LocalDateTime.parse(ConsoleUtils.readNonBlank(sc, prompt));
            } catch (DateTimeParseException ex) {
                ConsoleUtils.printError("Use format yyyy-MM-ddTHH:mm, example: 2026-05-20T10:30");
            }
        }
    }

    private void printLesson(Lesson lesson) {
        System.out.printf("  #%-3d [%s] %-25s %-8s %s - %s  attendance=%d%n",
                lesson.getId(),
                lesson.getCourse() != null ? lesson.getCourse().getCourseId() : "?",
                lesson.getTopic(),
                lesson.getLessonType(),
                lesson.getStartsAt(),
                lesson.getEndsAt(),
                lesson.getAttendance().size());
    }
}
