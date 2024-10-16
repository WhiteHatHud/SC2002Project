package SC2002Project.hms.login;
import java.util.Scanner;


public class UserInputHandler {

        Scanner sc = new Scanner(System.in);
        public int getUserChoice() {
        
        return sc.nextInt();
    }

    public String getInput() {
        return sc.next();  // Gets string inputs like ID or password
    }
    

}
