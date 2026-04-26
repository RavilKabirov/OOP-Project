import java.io.*;
import java.util.*;

public class MarkRepository{
    private List<Mark> marks;

    public MarkRepository(){
        this.marks=new ArrayList<>();
    }

    public Mark save(Mark a){
        marks.add(a);
        return a;
    }

    public Optional<Mark> findById(Long a){
        return findByEnrollment(a);
    }

    public Mark findByEnrollment(Long a){
        for(Mark mark:marks){
            if(mark.getEnrollmentId()!=null&&mark.getEnrollmentId().equals(a)){
                return mark;
            }
        }
        return null;
    }

    public List<Mark> findAll(){
        return marks;
    }

    public void deleteByEnrollmentId(Long a){
        marks.removeIf(mark->mark.getEnrollmentId()!=null&&mark.getEnrollmentId().equals(a));
    }
}    
