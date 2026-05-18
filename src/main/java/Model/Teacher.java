import java.util.ArrayList;
import java.util.List;

public class Teacher extends Employee implements IResearcher {

    private List<Course>       courses;
    private TeacherPosition    teacherPosition;
    private List<ResearchPaper> publications = new ArrayList<>();

    public Teacher(String email, String firstName, String lastName) {
    		super(email, firstName, lastName);
        this.courses = new ArrayList<>();
    }


    public List<Course> getCourses()              { return courses; }
    public void addCourse(Course value)           { courses.add(value); }
    public void removeCourse(Course value)        { courses.remove(value); }

    public TeacherPosition getTeacherPosition()           { return teacherPosition; }
    public void setTeacherPosition(TeacherPosition value) { this.teacherPosition = value; }


    @Override
    public String getResearcherId() {
        return getId() != null ? getId().toString() : getEmployeeId();
    }

    @Override
    public List<ResearchPaper> getPublications() {
        return publications;
    }

    @Override
    public void addPublication(ResearchPaper paper) {
        if (paper != null && !publications.contains(paper)) {
            publications.add(paper);
        }
    }

    @Override
    public void removePublication(Long paperId) {
        publications.removeIf(p -> p.getId() != null && p.getId().equals(paperId));
    }
}