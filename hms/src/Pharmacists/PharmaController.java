package Pharmacists;

import Login.DisplayManager;
import Medicine.PrescriptionsUI;
import Medicine.PrescriptionsUI.Action;
import Utilities.UserInputHandler;
import java.util.EnumSet;

public class PharmaController {
    private Pharmacist pharmacist;
    private PharmaDisplayManager displayManager;
    private PrescriptionsUI prescriptionsUI;
    private UserInputHandler inputHandler;

    public PharmaController(Pharmacist pharmacist) {
        this.pharmacist = pharmacist;
        this.displayManager = new PharmaDisplayManager();
        this.prescriptionsUI = new PrescriptionsUI(EnumSet.of(Action.VIEW_ALL, Action.VIEW_PATIENT, Action.UPDATE_STATUS));
        this.inputHandler = new UserInputHandler();
    }

    public void start() {
        boolean isActive = true;
        while (isActive) {
            displayManager.displayMainMenu();  // Call display manager to show the main menu
            int choice = inputHandler.getUserChoice();
            isActive = handleMainChoice(choice);
        }
    }

    // Handle main menu choices from PharmaDisplayManager
    private boolean handleMainChoice(int choice) {
        switch (choice) {
            case 1:
                updateAccountInformation();
                break;
            case 2:
                handlePrescriptionsMenu();
                break;
            case 3:
                handleMedicinesMenu();
                break;
            case 4:
                System.out.println("Logging out...");
                return false;  // Exit the loop to log out
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        return true;
    }

    private void updateAccountInformation() {
        System.out.println("Updating account information...");
        // Implement account update logic here
    }

    private void handlePrescriptionsMenu() {
        // Delegate to PrescriptionsUI for prescription-related tasks
        prescriptionsUI.displayPrescriptionsMenu();
    }

    private void handleMedicinesMenu() {
        System.out.println("Opening Medicines Menu...");
        // Implement medicine management logic here or delegate to a MedicinesUI class if necessary
    }
}
