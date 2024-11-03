package Admins;
import Login.ControllerInt;


public class AdminController implements ControllerInt {
    Admin admin;


    public Admin getAdmin(String adminID, String Name){
        return admin;
    }
    @Override
    public boolean handleChoice(int choice) {
        System.out.println("In admin now");
        return true;

    }

}