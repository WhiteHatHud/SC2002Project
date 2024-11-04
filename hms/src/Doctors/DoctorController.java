package Doctors;
import Login.ControllerInt;
import Login.DisplayFormat;
import Pharmacists.CheckMedicineInventory;
import Pharmacists.SubmitRequest;
import Pharmacists.UpdatePassword;
import Pharmacists.UpdatePrescriptionStatus;
import Pharmacists.ViewPendingPrescriptions;
import Utilities.LogoutTimer;

public class DoctorController implements ControllerInt{

    private Doctor doctor;

    public DoctorController(Doctor doctor){
        this.doctor=doctor;
    }

    public boolean start() {
        DisplayFormat.clearScreen();
        System.out.println("Welcome, " + doctor.getName());

        boolean isActive = true;
        while (isActive) {
            DoctorShared.getDisplayManager().getDisplayMenu();
            int choice = DoctorShared.getUserInputHandler().getUserChoice();
            isActive = handleChoice(choice);
        }

        return false;
    }

    
    @Override
    public boolean handleChoice(int choice) {
        DisplayFormat.clearScreen();
        switch (choice) {
            case 1: 
            break;

            case 2: // View pending prescriptions
                ViewPendingPrescriptions viewPrescriptions = new ViewPendingPrescriptions();
                viewPrescriptions.displayPrescriptions();
                break;
                
            case 3: // Update prescription status
                UpdatePrescriptionStatus updateStatus = new UpdatePrescriptionStatus();
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
