package Pharmacists;

import Login.DisplayManager;
import Medicine.PrescriptionsUI;
import Medicine.PrescriptionsUI.Action;
import Utilities.LogoutTimer;
import Utilities.UserInputHandler;
import java.util.EnumSet;
import Medicine.MedicineUI;

public class PharmaController {
    private Pharmacist pharmacist;
    private PharmaDisplayManager displayManager;
    private PrescriptionsUI prescriptionsUI;
    private UserInputHandler inputHandler;
    private RequestData requestData;
    private MedicineUI medicineUI;

    public PharmaController(Pharmacist pharmacist) {
        this.pharmacist = pharmacist;
        this.displayManager = new PharmaDisplayManager();
        this.prescriptionsUI = new PrescriptionsUI(EnumSet.of(Action.VIEW_ALL, Action.VIEW_PATIENT, Action.UPDATE_STATUS));
        this.inputHandler = new UserInputHandler();
        this.requestData = new RequestData();
        this.medicineUI = new MedicineUI();
    }

    public void start() {
        boolean isActive = true;
        while (isActive) {
            displayManager.displayMainMenu();  // Call display manager to show the main menu
            int choice = inputHandler.getUserChoice();
            isActive = handleMainChoice(choice);
        }
    }

    private boolean handleMainChoice(int choice) {
        DisplayManager.clearScreen();

        switch (choice) {
            case 1:
                viewAppointmentOutcomeRecord();
                break;
            case 2:
                prescriptionsUI.displayPrescriptionsMenu();
                break;
            case 3:
                medicineUI.displayAllMedicines();
                break;
            case 4:
                requestData.request(pharmacist.getUserID(), pharmacist.getName());
                break;

            case 5: // Logout
            
                if (LogoutTimer.confirmLogout()) {
                    return false; // Ends the session only if logout is confirmed
                } else {
                    DisplayManager.clearScreen();
                    return true; // Continue the session without printing additional messages
                }

            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        return true;
    }

    private void viewAppointmentOutcomeRecord() {
        // appointmentoutcomerecord logic
    }
}
