public class OrganizationCreationRequest{
    private String name;
    private String description;

    public OrganizationCreationRequest(String a,String b){
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