package Patients;
import Login.DisplayFormat;
import Utilities.CSVUpdater;
import Utilities.CSVUtilities;
import Utilities.UserInputHandler;


public class PatientShared {
    private static final UserInputHandler userInputHandler = new UserInputHandler();
    private static final PatientDisplayManager displayManager = new PatientDisplayManager();
    private static final DisplayFormat displayFormat = new DisplayFormat();
    private static  CSVUpdater csvUpdater = new CSVUpdater("Patient List CSV.csv");
    private static  CSVUtilities csvUtilities = new CSVUtilities("Staff_List.csv");
    private static  CSVUtilities csvUtilities2 = new CSVUtilities("Diagnosis.csv");
    
    public static CSVUtilities getCSVUtilities2() {
        return csvUtilities2;
    }
    
    
    
    public static CSVUtilities getCSVUtilities() {
        return csvUtilities;
    }


    public static CSVUpdater getCSVUpdater() {
        return csvUpdater;
    }

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
