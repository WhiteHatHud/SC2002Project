package Login;

import Admins.Admin;
import Doctors.Doctor;
import Pharmacists.PharmaController;
import Pharmacists.Pharmacist;
import Users.Staff;
import Utilities.Masking;
import Utilities.UserInputHandler;
import java.util.Map;

public class StaffLoginManager implements LoginInt {
    private DisplayManager displayManager;
    private UserInputHandler inputHandler;
    private AuthStaff authStaff;
    private boolean returnToMenu = false;

    public StaffLoginManager(DisplayManager displayManager, UserInputHandler inputHandler, Map<String, UserRegistry> registries) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
        this.authStaff = new AuthStaff("Staff_List.csv");
    }

    @Override
    public boolean start() {
        returnToMenu = false;

        while (true) {
            displayManager.showStaffLoginID();
            String userID = inputHandler.getInput();

            // Check if the user wants to go back
            if (userID.equals("~")) {
                System.out.println("Returning to the previous menu...");
                returnToMenu = true;
                return false;
            }

            displayManager.showEnterPW();
            Masking mask = new Masking();
            String password = mask.readPasswordWithMasking();

            Staff staffMember = authStaff.authenticateAndRetrieve(userID, password);

            if (staffMember != null) {
                DisplayFormat.clearScreen();
                System.out.println("Authentication successful!");

                // Determine the role and start the appropriate controller
                if (staffMember instanceof Pharmacist) {
                    PharmaController pharmaController = new PharmaController((Pharmacist) staffMember);
                    pharmaController.start();
                } else if (staffMember instanceof Doctor) {
                    //DoctorController doctorController = new DoctorController((Doctor) staffMember);
                    //doctorController.start();
                } else if (staffMember instanceof Admin) {
                    //AdminController adminController = new AdminController((Admin) staffMember);
                    //adminController.start();
                } else {
                    System.out.println("Unknown role type. Cannot start controller.");
                }
                break; 
            } else {
                DisplayFormat.clearScreen();
                DisplayManager.authFail();
            }
        }
        return false;
    }

    public boolean shouldReturnToMenu() {
        return returnToMenu;
    }
}
