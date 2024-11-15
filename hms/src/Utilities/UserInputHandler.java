package Utilities;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A utility class for handling user input from the console.
 * Provides methods for obtaining user choices, reading strings, and managing input errors.
 * This class is designed to facilitate user interaction in console applications.
 * 
 * @version 1.0
 * @since 2024-11-15
 */
public class UserInputHandler {

    /** The Scanner object used to read input from the console. */
    private Scanner sc = new Scanner(System.in);

    /**
     * Retrieves an integer input from the user.
     * If the input is not an integer, an error message is displayed.
     * 
     * @return the integer input from the user, or -1 if input is invalid.
     */
    public int getUserChoice() {
        int choice = -1; // Default invalid choice
        try {
            choice = sc.nextInt();
            sc.nextLine(); // Consume the newline character
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
        return choice;
    }

    /**
     * Retrieves an integer input from the user within a given range.
     * Ensures the input is positive and less than or equal to the specified limit.
     * 
     * @param i the maximum acceptable value for the input.
     * @return the integer input from the user, or -1 if input is invalid or out of range.
     */
    public int getUserChoice(int i) {
        int choice = -1; // Default invalid choice
        try {
            choice = sc.nextInt();
            if (choice > i || choice <= 0) 
                throw new InputMismatchException("Invalid input. Please enter a valid integer.");
            sc.nextLine(); // Consume the newline character
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
        return choice;
    }

    /**
     * Reads a single word input from the user.
     * 
     * @return the string input from the user.
     */
    public String getInput() {
        String input = "";
        try {
            input = sc.next(); 
        } catch (Exception e) {
            System.out.println("An error occurred while reading input: " + e.getMessage());
        }
        return input;
    }

    /**
     * Reads an entire line of input from the user.
     * 
     * @return the line input from the user.
     */
    public String getNextLine() {
        String line = "";
        try {
            line = sc.nextLine();
        } catch (Exception e) {
            System.out.println("An error occurred while reading input: " + e.getMessage());
        }
        return line;
    }

    /**
     * Prompts the user for a positive integer input.
     * Repeatedly requests input until a positive integer is provided.
     * 
     * @return a positive integer input from the user.
     */
    public int getPositiveInt() {
        int choice = -1;
        while (choice < 1) {
            choice = getUserChoice();
        }
        return choice;
    }

    /**
     * Prompts the user for a positive integer input within a given range.
     * Repeatedly requests input until a valid number is provided.
     * 
     * @param i the maximum acceptable value for the input.
     * @return a positive integer input within the specified range.
     */
    public int getPositiveInt(int i) {
        int choice = -1;
        while (choice < 1) {
            choice = getUserChoice(i);
        }
        return choice;
    }

    /**
     * Prompts the user for an integer input that is not zero.
     * Repeatedly requests input until a non-zero integer is provided.
     * 
     * @return a non-zero integer input from the user.
     */
    public int getNonZero() {
        int choice = 0;
        while (choice == 0) {
            choice = getUserChoice();
        }
        return choice;
    }
}
