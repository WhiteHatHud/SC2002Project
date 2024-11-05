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
    private static final AdminDisplayManager displayManager = new AdminDisplayManager();

    public static AdminDisplayManager getDisplayManager() {
        return displayManager;
    }

    public static CSVUpdater getCSVUpdater() {
        return csvUpdater;
    }

    public static UserInputHandler getUserInputHandler() {
        return userInputHandler;
    }

    public static DisplayFormat getDisplayFormat() {
        return displayFormat;
    }

    public static CSVUtilities getCSVUtilities() {
        return csvUtil;
    }
}
