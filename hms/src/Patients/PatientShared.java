package Patients;

import Utilities.UserInputHandler;


public class PatientShared {
    private static final UserInputHandler userInputHandler = new UserInputHandler();
    private static final PatientDisplayManager displayManager = new PatientDisplayManager();

    public static UserInputHandler getUserInputHandler() {
        return userInputHandler;
    }

    public static PatientDisplayManager getDisplayManager() {
        return displayManager;
    }
}
