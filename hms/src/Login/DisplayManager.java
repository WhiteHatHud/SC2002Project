package Login;

import java.util.Scanner;

/**
 * Manages the display of various screens and messages for the Hospital Management System,
 * including welcome, login, and error screens. This class provides formatted output using
 * the inherited display utilities from {@link DisplayFormat}.
 */
public class DisplayManager extends DisplayFormat {

    /**
     * Displays the welcome screen, including the hospital management system logo and main menu options.
     */
    public void showWelcomeScreen() {
        String[] hmsLogo = {"H   H  M   M  SSSSS", "H   H  MM MM  S     ", "HHHHH  M M M  SSSSS", "H   H  M   M      S", "H   H  M   M  SSSSS"};
        String welcomeMessage = "Welcome to the Hospital Management System";
        String[] menu = {"1. Login", "2. Reset password", "3. Quit \n", "Please enter your choice (1~3): "};
        
        for (String line : hmsLogo) {
            printCentered(line, 80);
        }
        printCentered(welcomeMessage, 80);
        for (String line : menu) {
            printCentered(line, 80);
        }
        divider();
    }

    /**
     * Displays the login screen with options to choose user type (Patient or Staff).
     */
    public void showLoginScreen() {
        String[] loginMenu = {"I am a...\n", "1. Patient", "2. Staff\n", "Please enter your choice (1-2, ~ to return to main menu): "};

        for (String line : loginMenu) {
            printCentered(line, 80);
        }
        divider();
    }

    /**
     * Displays a prompt for staff to enter their staff ID.
     */
    public void showStaffLoginID() {
        String[] msg = {"Please enter your staff ID (Enter ~ to return to main menu): "};
        for (String line : msg) {
            printCentered(line, 80);
        }
    }

    /**
     * Displays a prompt for patients to enter their patient ID.
     */
    public void showPatientLoginID() {
        String[] msg = {"Please enter your patient ID (Enter ~ to return to main menu): "};
        for (String line : msg) {
            printCentered(line, 80);
        }
    }

    /**
     * Displays a prompt for the user to enter their password.
     */
    public void showEnterPW() {
        String[] msg = {"Please enter your password : "};
        for (String line : msg) {
            printCentered(line, 80);
        }
    }

    /**
     * Displays a message instructing the user to enter '~' to return to the main menu.
     */
    public static void retMainMenu() {
        String[] msg = {"Enter ~ to go back to the main menu."};
        for (String line : msg) {
            printCentered(line, 80);
        }
    }

    /**
     * Displays a message instructing the user to enter any key to return to the main menu.
     */
    public static void retMainMenuAny() {
        String[] msg = {"Enter any key to go back to the main menu."};
        for (String line : msg) {
            printCentered(line, 80);
        }
    }

    /**
     * Displays an authentication failure message.
     */
    public static void authFail() {
        String[] msg = {"Authentication failed. Please try again."};
        for (String line : msg) {
            printCentered(line, 80);
        }
    }

    /**
     * Displays a prompt for the user to enter a new password.
     */
    public static void passwordUpdate() {
        String[] msg = {"Please enter your new password"};
        for (String line : msg) {
            printCentered(line, 80);
        }
    }

    /**
     * Displays a message indicating an invalid choice.
     */
    public static void invalidChoice() {
        String[] msg = {"Invalid choice! Try again"};
        for (String line : msg) {
            printCentered(line, 80);
        }
    }

    /**
     * Displays an error message centered on the screen if the error message is not empty.
     *
     * @param errorMessage The error message to display.
     * @return An empty string, as this method only displays the message.
     */
    public static String loadErrorMessage(String errorMessage) {
        if (!errorMessage.isEmpty()) {
            DisplayFormat.printCentered(errorMessage, 80);
        }
        return "";
    }

    /**
     * Pauses the console until the user presses a key, then clears the screen.
     */
    public static void pauseContinue() {
        System.out.print("Press any key to continue...");
        Scanner scanner = new Scanner(System.in);    
        scanner.nextLine(); // Wait for user to press any key
        DisplayManager.clearScreen();
    }
}
