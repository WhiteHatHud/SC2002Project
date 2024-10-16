package SC2002Project.hms.login;
import java.util.Scanner;




class LoginManager {
    public void handleChoice(int choice) {
        switch(choice) {
            case 1: 
                // login logic
                break;
            case 2:
                // reset password logic
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice");
        }
    }
}
