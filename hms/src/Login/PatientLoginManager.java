package Login;

public class PatientLoginManager implements LoginInt {
    private DisplayManager displayManager;
    private UserInputHandler inputHandler;

    public PatientLoginManager(DisplayManager displayManager, UserInputHandler inputHandler) {
        this.displayManager = displayManager;
        this.inputHandler = inputHandler;
    }

    @Override
    public void start() { //need to implement nonfunc req - press x to exit while inputting incase select wrong option
        
        displayManager.showPatientLoginID();   // Show prompt to enter Patient ID
        String userID = inputHandler.getInput();  
        displayManager.showEnterPW();  // Show prompt to enter Password
        String password = inputHandler.getInput();  

        // auth user
        AuthenticationService authService = new AuthenticationService();
        boolean isAuthenticated = authService.authenticate(userID, password);

        if (isAuthenticated) {
            System.out.println("Login successful! Welcome, Patient.");
            // pass control to Patient.java
        } else {
            System.out.println("Invalid credentials. Please try again.");
            // Retry logic or terminate
        }
    }
}
