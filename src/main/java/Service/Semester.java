/**
 * Represents an academic semester used for teacher workload reporting.
 */
public class Semester {

    public enum SemesterTerm { SPRING, SUMMER, FALL }

    private final int          year;
    private final SemesterTerm term;

    public Semester(int year, SemesterTerm term) {
        this.year = year;
        this.term = term;
    }

    public int          getYear() { return year; }
    public SemesterTerm getTerm() { return term; }

    /** Converts this semester to the equivalent ReportPeriod for date filtering. */
    public ReportPeriod toReportPeriod() {
        switch (term) {
            case FALL:   return ReportPeriod.fallSemester(year);
            case SPRING: return ReportPeriod.springSemester(year);
            case SUMMER: return ReportPeriod.summerSession(year);
            default: throw new IllegalStateException("Unknown term: " + term);
        }
    }

    @Override
    public String toString() {
        return term + " " + year;
    }
}