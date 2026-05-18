import java.time.LocalDate;

public class ReportPeriod {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String    label;

    public ReportPeriod(LocalDate startDate, LocalDate endDate, String label) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("ReportPeriod dates must not be null.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "endDate must not be before startDate.");
        }
        this.startDate = startDate;
        this.endDate   = endDate;
        this.label     = label != null ? label : startDate + " – " + endDate;
    }

    public ReportPeriod(LocalDate startDate, LocalDate endDate) {
        this(startDate, endDate, null);
    }


    public static ReportPeriod ofYear(int year) {
        return new ReportPeriod(
                LocalDate.of(year, 1, 1),
                LocalDate.of(year, 12, 31),
                "Year " + year);
    }

    public static ReportPeriod fallSemester(int year) {
        return new ReportPeriod(
                LocalDate.of(year, 9, 1),
                LocalDate.of(year, 12, 31),
                "Fall " + year);
    }

    public static ReportPeriod springSemester(int year) {
        return new ReportPeriod(
                LocalDate.of(year, 1, 1),
                LocalDate.of(year, 5, 31),
                "Spring " + year);
    }

    public static ReportPeriod summerSession(int year) {
        return new ReportPeriod(
                LocalDate.of(year, 6, 1),
                LocalDate.of(year, 8, 31),
                "Summer " + year);
    }

    public static ReportPeriod all() {
        return new ReportPeriod(
                LocalDate.of(1970, 1, 1),
                LocalDate.of(9999, 12, 31),
                "All time");
    }


    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate()   { return endDate;   }
    public String    getLabel()     { return label;     }

    public boolean contains(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    @Override
    public String toString() {
        return "ReportPeriod{" + label + ": " + startDate + " – " + endDate + "}";
    }
}