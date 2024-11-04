package Utilities;

import java.util.Scanner;


public class UserInputHandler {

        Scanner sc = new Scanner(System.in);


        public int getUserChoice() {
            int choice = sc.nextInt(); 
            sc.nextLine(); // Consume the newline character left in the buffer
            return choice;
        }

    public String getInput() {
        return sc.next();  // Gets string inputs like ID or password
    }
    
    public String getNextLine() {
        return sc.nextLine();
    }

}
