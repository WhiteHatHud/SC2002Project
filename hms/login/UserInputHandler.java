package SC2002Project.hms.login;
import java.util.Scanner;


public class UserInputHandler {

        public int getUserChoice() {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

}
