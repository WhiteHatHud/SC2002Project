package Utilities;

import java.util.Scanner;


public class UserInputHandler {

        Scanner sc = new Scanner(System.in);


        public int getUserChoice() {
            int choice = sc.nextInt(); 
            sc.nextLine(); // Consume the newline character left in the buffer
            return choice;
        }

        public String getInputString() {
            String input = "";
            boolean valid = false;
        
            while (!valid) {
                try {
                    input = sc.nextLine().trim(); // Read the next line and trim whitespace
                    if (input.isEmpty()) { // Check if input is empty
                        throw new IllegalArgumentException("Input cannot be empty.");
                    }
                    valid = true; // Set to true if no exception is thrown
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input. Please enter a valid string.");
                }
            }
            return input;
        }
        

    public int getInputInt() {
        int input = 0;
        boolean valid = false;
    
        while (!valid) {
            try {
                input = Integer.parseInt(sc.nextLine().trim()); // Parse input to integer
                valid = true; // Set to true if no exception is thrown
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return input;
    }
    
    public String getNextLine() {
        return sc.nextLine();
    }

}
