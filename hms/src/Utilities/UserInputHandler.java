package Utilities;

import java.util.Scanner;


public class UserInputHandler {

        Scanner sc = new Scanner(System.in);


    public int getUserChoice() {
        return sc.nextInt(); // Ints only
    }

    public String getInput() {
        return sc.next();  // Gets string inputs like ID or password
    }
    
    public String getNextLine() {
        return sc.next();
    }

}
