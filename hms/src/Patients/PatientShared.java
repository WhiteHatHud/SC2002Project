package Patients;
import Login.DisplayFormat;
import Utilities.UserInputHandler;


public class PatientShared {
    private static final UserInputHandler userInputHandler = new UserInputHandler();
    private static final PatientDisplayManager displayManager = new PatientDisplayManager();
    private static final DisplayFormat displayFormat = new DisplayFormat();

    public static UserInputHandler getUserInputHandler() {
        return userInputHandler;
    }

    public static PatientDisplayManager getDisplayManager() {
        return displayManager;
    }
    public static DisplayFormat getDisplayFormat() {
        return displayFormat;
    }
}
