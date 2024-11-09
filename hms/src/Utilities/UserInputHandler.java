package Utilities;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInputHandler {

    private Scanner sc = new Scanner(System.in);

    public int getUserChoice() {
        int choice = -1; // Default invalid choice
        try {
            choice = sc.nextInt();
            sc.nextLine(); 
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
        return choice;
    }
    public int getUserChoice(int i) {
        int choice = -1; // Default invalid choice
        try {
            choice = sc.nextInt();
            if(choice > i || choice <= 0) throw new InputMismatchException("Invalid input. Please enter a valid integer.");
            sc.nextLine(); 
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
        return choice;
    }

    public String getInput() {
        String input = "";
        try {
            input = sc.next(); 
        } catch (Exception e) {
            System.out.println("An error occurred while reading input: " + e.getMessage());
        }
        return input;
    }

    public String getNextLine() {
        String line = "";
        try {
            line = sc.nextLine();
        } catch (Exception e) {
            System.out.println("An error occurred while reading input: " + e.getMessage());
        }
        return line;
    }
}
