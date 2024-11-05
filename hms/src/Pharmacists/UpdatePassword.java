package Pharmacists;

import Login.DisplayManager;

public class UpdatePassword {
    private Pharmacist pharma;

    public void start(Pharmacist pharma){
        this.pharma = pharma;
        displayMenu();
    }

    private void displayMenu() {
        boolean isRunning = true;
        
        while (isRunning) {
            System.out.println("\n--- Account Management ---");
            System.out.println("1. Change Password");
            System.out.println("2. Return to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = PharmaShared.getUserInputHandler().getUserChoice();
            DisplayManager.clearScreen();
            
            switch (choice) {
                case 1:
                    setPW();
                    break;
                case 2:
                    System.out.println("Returning to the main menu...");
                    isRunning = false; // Exit loop
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private void setPW() {
        System.out.print("Enter new password: ");
        String newPassword = PharmaShared.getUserInputHandler().getInput();
        
        pharma.setPassword(newPassword);
        PharmaShared.getCSVUpdater().updateField(pharma.getUserID(), "password", newPassword);
        
        DisplayManager.clearScreen();
        System.out.println("Password updated successfully!");
    }
}
