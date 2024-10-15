
import java.util.*;


public class LoginPageMain {
    
    public static void main(String[] args) {
    	
        Scanner sc = new Scanner(System.in);
        int terminalWidth = 80;


        String[] hmsLogo = {
            "H   H  M   M  SSSSS",
            "H   H  MM MM  S     ",
            "HHHHH  M M M  SSSSS ",
            "H   H  M   M      S ",
            "H   H  M   M  SSSSS "
        };

        String welcomeMessage = "Welcome to the Hospital Management System";

        String[] menu = {
            "1. Login",
            "2. Reset password",
            "3. Forget UserID",
            "4. Quit \n",
            "Please enter your choice: "
        };

        for (String line : hmsLogo) {
            printCentered(line, terminalWidth);
        }

        System.out.println();  
        printCentered(welcomeMessage, terminalWidth);
        System.out.println();  

        for (String line : menu) {
            printCentered(line, terminalWidth);
        }
        
        int choice = sc.nextInt();

    }


    public static void printCentered(String text, int width) {
        int padding = (width - text.length()) / 2;
        for (int i = 0; i < padding; i++) {
            System.out.print(" ");  
        }
        System.out.println(text);  
    }
}


