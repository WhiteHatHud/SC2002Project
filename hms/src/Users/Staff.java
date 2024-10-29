package Users;

public class Staff extends Users{
    protected String role;
    protected String gender;
    protected int age;
    public Staff(){}
    public Staff(String staffID, String name, String role, String gender, int age){
        super(staffID, name);
        this.role = role;
        this.gender = gender;
        this.age = age;
    }
    public String getRole(){
        return role;
    }
    public String getGender(){
        return gender;
    }
    public int getAge(){
        return age;
    }
    public void setRole(String role){
        this.role = role;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public void setAge(int age){
        this.age = age;
    }
}
