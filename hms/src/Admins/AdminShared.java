package Admins;
import Login.DisplayFormat;
import Utilities.CSVUpdater;
import Utilities.CSVUtilities;
import Utilities.UserInputHandler;


public class AdminShared {
    private static final UserInputHandler userInputHandler = new UserInputHandler();
    private static final DisplayFormat displayFormat = new DisplayFormat();
    private static final CSVUpdater csvUpdater = new CSVUpdater("Staff_List.csv");
    private static final CSVUtilities csvUtil = new CSVUtilities("Staff_List.csv");
    private static final CSVUpdater csvUpdaterPatient = new CSVUpdater("Patient List CSV.csv");
    private static final CSVUtilities csvUtilPatient = new CSVUtilities("Patient List CSV.csv");
    private static final AdminDisplayManager displayManager = new AdminDisplayManager();

    public static AdminDisplayManager getDisplayManager() {
        return displayManager;
    }

    public static CSVUpdater getCSVUpdater() {
        return csvUpdater;
    }

    public static CSVUpdater getCSVUpdaterPatient() {
        return csvUpdaterPatient;
    }

    public static UserInputHandler getUserInputHandler() {
        return userInputHandler;
    }

    public static DisplayFormat getDisplayFormat() {
        return displayFormat;
    }

    public static CSVUtilities getCSVUtilitiesPatient() {
        return csvUtilPatient;
    }
    
    public static CSVUtilities getCSVUtilities() {
        return csvUtil;
    }
}
