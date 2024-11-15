package Login;

import Doctors.Doctor;
import Doctors.DoctorController;
import Pharmacists.PharmaController;
import Pharmacists.Pharmacist;
import Admins.AdminController;
import Admins.Admin;
import Users.Staff;
import Utilities.Masking;
import Utilities.UserInputHandler;
import java.util.Map;

/**
 * Manages the login process for staff members (Pharmacist, Doctor, Admin).
 * This class prompts staff for their user ID and password, authenticates them using {@link AuthStaff},
 * and, if successful, launches the appropriate controller based on their role.
 */
public class StaffLoginManager implements LoginInt {

    /** Manages display for login prompts and messages. */
    private DisplayManager displayManager;

    /** Handles user input during the login process. */
    private UserInputHandler inputHandler;

    /** Authenticates staff members by verifying their credentials. */
    private AuthStaff authStaff;

    /** Flag indicating if the user wants to return to the main menu. */
    private boolean returnToMenu = false;

    /**
     * Constructs a StaffLoginManager with the specified display manager, input handler,
     * and user registries. Initializes the authentication service for staff login.
     *
     * @param displayManager Manages display screens for login prompts and messages.
     * @param inputHandler   Handles user input during the login process.
     * @param registries     A map of user type to user registry (not directly used in this class).
     */
    public StaffLoginManager(DisplayManager displayManager, UserInputHandler inputHandler, Map<String, UserRegistry> registries) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
        this.authStaff = new AuthStaff("Staff_List.csv");
    }

    /**
     * Starts the login process for a staff member. This method prompts for the staff’s user ID
     * and password, authenticates the credentials, and initiates the appropriate controller
     * (PharmaController, DoctorController, or AdminController) based on the authenticated staff member’s role.
     *
     * @return {@code false} if login was successful or if the user chooses to return to the main menu.
     */
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
                    DoctorController doctorController = new DoctorController((Doctor) staffMember);
                    doctorController.start();
                } else if (staffMember instanceof Admin) {
                    AdminController adminController = new AdminController((Admin) staffMember);
                    adminController.start();
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

    /**
     * Checks if the user has chosen to return to the main menu.
     *
     * @return {@code true} if the user wants to return to the main menu, {@code false} otherwise.
     */
    public boolean shouldReturnToMenu() {
        return returnToMenu;
    }
}
