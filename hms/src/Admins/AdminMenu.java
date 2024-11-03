package Admins;

import Utilities.LogoutTimer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class AdminMenu {
    private final Map<Integer, MenuAction> actions = new LinkedHashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    public AdminMenu() {
        // Register all menu actions
        actions.put(1, new ViewAndManageStaff());
        //actions.put(2, new ViewAppointmentDetailsAction());
        actions.put(3, new ManageMedicationInventoryAction());
        //actions.put(4, new ApproveReplenishmentRequestsAction());
        //actions.put(5, new SystemInitializationAction());
        actions.put(6, new LogoutAction());
        //Add new functions in future (one line)
    }

    public void displayMenu() {
        boolean running = true;

        while (running) {

            System.out.println("\n=== Administrator Menu ===\n");

            // Dynamically display menu options
            for (Map.Entry<Integer, MenuAction> entry : actions.entrySet()) {
                int optionNumber = entry.getKey();
                MenuAction action = entry.getValue();
                System.out.println(optionNumber + ". " + action.getDescription());
            }

            System.out.print("Please enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            MenuAction action = actions.get(choice);
            if (action != null) {
                action.execute();
                if (action instanceof LogoutAction) {
                    LogoutTimer.confirmLogout();
                    running = false; // Exit the loop if logout is chosen
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
