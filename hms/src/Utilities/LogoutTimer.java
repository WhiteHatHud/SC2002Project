/**
 * Utility class for handling logout confirmation and countdown with interruption support.
 * Provides a mechanism for users to confirm logout with an option to cancel during a countdown.
 */
package Utilities;

import Login.DisplayFormat;
import java.io.IOException;
import java.util.Scanner;

public class LogoutTimer {

    private static final Scanner scanner = new Scanner(System.in);
    private static volatile boolean interrupted = false; // Shared flag for detecting interruptions

    /**
     * Prompts the user to confirm logout. If the user confirms, starts a countdown 
     * that can be interrupted by entering any character.
     *
     * @return true if the logout is confirmed and the countdown completes, false if the 
     *         logout is canceled or interrupted.
     */
    public static boolean confirmLogout() {
        System.out.print("Logging out, confirm? Enter (yes/no): ");
        String input = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(input)) {
            interrupted = false; // Reset the flag for a fresh countdown

            if (countdown()) {
                return true;  // Confirm logout if countdown completes
            } else {
                System.out.println("Logout interrupted. Returning to the previous page...");
                return false; // Logout interrupted, return to the previous page
            }
        } else {
            System.out.println("Logout canceled. Returning to the previous page...");
            return false; // Cancel logout and return to the previous page
        }
    }

    /**
     * Initiates a countdown timer that counts down from 3 to 1. 
     * The countdown can be interrupted if the user enters any character.
     *
     * @return true if the countdown completes without interruption, false if interrupted.
     */
    private static boolean countdown() {
        System.out.println("Logging out in...(Enter any character to cancel logout)");

        // Create a new thread to detect key press during the countdown
        Thread inputThread = new Thread(() -> {
            try {
                while (System.in.available() == 0) {
                    // Wait for input to be available without blocking
                    Thread.sleep(100); // Small delay to avoid busy-waiting
                }
                interrupted = true; // Set flag if any key is detected
            } catch (IOException | InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Error or interruption during input detection.");
            }
        });

        inputThread.start();

        for (int i = 3; i > 0; i--) {
            if (interrupted) {
                inputThread.interrupt(); // Stop the input thread
                return false; // Indicate that the countdown was interrupted
            }

            System.out.println(i);
            try {
                Thread.sleep(1000); // Wait for 1 second
            } catch (InterruptedException e) {
                System.out.println("Logout interrupted");
                return false; // Indicate that the countdown was interrupted
            }
        }

        DisplayFormat.clearScreen();
        return true; // Indicate that the countdown completed successfully
    }
}
