package Login;
import Admins.AdminController;
import Doctors.DoctorController;
import Pharmacists.PharmaController;
import Users.Staff;
import Utilities.Masking;
import Utilities.UserInputHandler;
import java.util.Map;

public class StaffLoginManager implements LoginInt {
    private DisplayManager displayManager;
    private UserInputHandler inputHandler;
    private AuthenticationService authenticationService;
    private AuthStaff authStaff;
    private boolean returnToMenu = false;
    private boolean exist = false;

    public StaffLoginManager(DisplayManager displayManager, UserInputHandler inputHandler, Map<String, UserRegistry> registries) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
        this.authenticationService = new AuthenticationService(registries);
        this.authStaff = new AuthStaff("Staff_List.csv");
    }

    @Override
    public boolean start() {
        returnToMenu = false;
        Staff authenticatedStaff = null;

        while (authenticatedStaff == null) {
            displayManager.showStaffLoginID();
            String userID = inputHandler.getInput();

            // Check if the user wants to go back
            if (userID.equals("~")) {
                System.out.println("Returning to the previous menu...");
                returnToMenu = true;
                return false;
            }

            exist = true;
            displayManager.showEnterPW();
            Masking mask = new Masking();
            String password = mask.readPasswordWithMasking();
            AuthStaff auth = new AuthStaff("Staff_List.csv");

            if (auth.authenticateUser(userID, password)) {
                ControllerInt generic ;

                DisplayFormat.clearScreen();
                char roleIndicator = Character.toUpperCase(userID.charAt(0));  // Get the first character to determine role
                switch (roleIndicator) {
                    case 'P':
                        generic = new PharmaController();
                        generic.handleChoice(1);
                        break;
                    case 'A':
                        generic = new AdminController();
                        generic.handleChoice(1);
                        break;
                    case 'D':
                        generic = new DoctorController();
                        generic.handleChoice(1);

                        break;
                    default:
                        System.out.println("Invalid role indicator. Please ensure the user ID or password is correct.");
                        //authenticatedStaff = null;  // Reset to re-prompt for login
                        break;
                }
            } else {
                System.out.println("Authentication failed. Please try again.");
            }
        }
        return false;
    }

    public boolean shouldReturnToMenu() {
        return returnToMenu;
    }
}
