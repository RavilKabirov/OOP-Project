
public class CourseSearchFilters {
    private CourseType courseType;
    private String nameKeyword;
    private Integer maxCredits;

    public CourseSearchFilters(CourseType courseType,
                                String nameKeyword, Integer maxCredits) {
        this.courseType = courseType;
        this.nameKeyword = nameKeyword;
        this.maxCredits = maxCredits;
    }

    public CourseType getCourseType() { return courseType; }
    public String getNameKeyword() { return nameKeyword; }
    public Integer getMaxCredits() { return maxCredits; }
}