package Utilities;
import java.util.Scanner;
import Login.DisplayFormat;


public class LogoutTimer {

    private static final Scanner scanner = new Scanner(System.in);

    public static boolean confirmLogout() {
        System.out.print("Logging out, confirm? Enter (yes/no): ");
        String input = scanner.nextLine().trim().toLowerCase();
        if ("yes".equals(input)) {
            countdown();
            return true;
        }
        return false;
    }

    private static void countdown() {
        System.out.println("Logging out in...");
        for (int i = 3; i > 0; i--) {
            System.out.println(i);
            try {
                Thread.sleep(1000); // wait for 1 second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Logout interrupted");
                return;
            }
        }
        DisplayFormat.clearScreen();
    }
}
