public class DepartmentUpdateRequest{

    private String name;
    private Long facultyId;

    public DepartmentUpdateRequest(String name, Long facultyId){
        this.name = name;
        this.facultyId = facultyId;
    }

    public String getName(){
        return name;
    }

    public Long getFacultyId(){
        return facultyId;
    }
}