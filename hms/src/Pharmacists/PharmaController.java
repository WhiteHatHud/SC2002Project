package Pharmacists;

import Login.ControllerInt;
import Login.DisplayFormat;
import Utilities.LogoutTimer;

public class PharmaController implements ControllerInt {
    private Pharmacist pharma;

    public PharmaController(Pharmacist pharma) {
        this.pharma = pharma;
    }

    public boolean start() {
        DisplayFormat.clearScreen();
        System.out.println("Welcome, " + pharma.getName());

        boolean isActive = true;
        while (isActive) {
            PharmaShared.getDisplayManager().getDisplayMenu();
            int choice = PharmaShared.getUserInputHandler().getUserChoice();
            isActive = handleChoice(choice);
        }

        return false;
    }

    @Override
    public boolean handleChoice(int choice) {
        DisplayFormat.clearScreen();
        switch (choice) {
            case 1: // update password ONLY
            UpdatePassword update = new UpdatePassword();
            update.start(pharma);
            
            break;

            case 2: // View pending prescriptions
                ViewPendingPrescriptions viewPrescriptions = new ViewPendingPrescriptions();
                viewPrescriptions.displayPrescriptions();
                break;
                
            case 3: // Update prescription status
                UpdatePrescriptionStatus updateStatus = new UpdatePrescriptionStatus(pharma);
                updateStatus.promptUpdateStatus();
                break;
                
            case 4: // Check inventory
                CheckMedicineInventory inventoryChecker = new CheckMedicineInventory();
                inventoryChecker.displayInventory();
                break;
                
            case 5: // request for stock
                SubmitRequest request = new SubmitRequest();
                request.request(pharma.getUserID(),pharma.getName());
                break;
                
            case 6: //logout
                LogoutTimer.confirmLogout();
                return false; 
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }

        return true; // Continue session if choice is not logout
    }
}
