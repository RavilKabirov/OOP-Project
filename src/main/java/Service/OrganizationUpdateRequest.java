public class OrganizationUpdateRequest{
    private String name;
    private String description;

    public OrganizationUpdateRequest(String a,String b){
        name=a;
        description=b;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }
}