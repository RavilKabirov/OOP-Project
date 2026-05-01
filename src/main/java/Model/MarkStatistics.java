
public class MarkStatistics {

    private final String courseId;
    private final int totalStudents;
    private final double averageTotal;
    private final double highestTotal;
    private final double lowestTotal;
    private final double averageFirstAttestation;
    private final double averageSecondAttestation;
    private final double averageFinalExam;
    private final int passCount;  
    private final int failCount;  

    public MarkStatistics(String courseId,
                          int totalStudents,
                          double averageTotal,
                          double highestTotal,
                          double lowestTotal,
                          double averageFirstAttestation,
                          double averageSecondAttestation,
                          double averageFinalExam,
                          int passCount,
                          int failCount) {
        this.courseId = courseId;
        this.totalStudents = totalStudents;
        this.averageTotal = averageTotal;
        this.highestTotal = highestTotal;
        this.lowestTotal = lowestTotal;
        this.averageFirstAttestation = averageFirstAttestation;
        this.averageSecondAttestation = averageSecondAttestation;
        this.averageFinalExam = averageFinalExam;
        this.passCount = passCount;
        this.failCount = failCount;
    }

    public String getCourseId()                 { return courseId; }
    public int    getTotalStudents()            { return totalStudents; }
    public double getAverageTotal()             { return averageTotal; }
    public double getHighestTotal()             { return highestTotal; }
    public double getLowestTotal()              { return lowestTotal; }
    public double getAverageFirstAttestation()  { return averageFirstAttestation; }
    public double getAverageSecondAttestation() { return averageSecondAttestation; }
    public double getAverageFinalExam()         { return averageFinalExam; }
    public int    getPassCount()                { return passCount; }
    public int    getFailCount()                { return failCount; }

    @Override
    public String toString() {
        return String.format(
            "MarkStatistics{course='%s', students=%d, avg=%.2f, high=%.2f, low=%.2f, " +
            "avgFirst=%.2f, avgSecond=%.2f, avgFinal=%.2f, pass=%d, fail=%d}",
            courseId, totalStudents, averageTotal, highestTotal, lowestTotal,
            averageFirstAttestation, averageSecondAttestation, averageFinalExam,
            passCount, failCount);
    }
}