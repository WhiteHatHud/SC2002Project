package Users;

public class Staff extends Users{
    protected String role;
    protected String gender;
    protected int age;
    protected String officeNumber;

    public Staff(){}
    public Staff(String staffID, String name, String role, String gender, int age, String officeNumber){
        super(staffID, name);
        this.role = role;
        this.gender = gender;
        this.age = age;
        this.officeNumber = officeNumber;
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
    public String getOfficeNumber(){
        return officeNumber;
    }
    public void setOfficeNumber(String officeNumber){
        this.officeNumber = officeNumber;
    }
    public String toCSV(){
        String stringID = String.valueOf(userID);
        String stringAge = String.valueOf(age);
        return String.join(",", stringID, name, role, gender, stringAge);
    }
    @Override
    public String toString() {
        return "Staff ID: " + userID + ", Name: " + name + ", Role: " + role + ", Gender: " + gender + ", Age: " + age;
    }
}
