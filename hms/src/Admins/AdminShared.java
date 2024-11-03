package Admins;
import Login.DisplayFormat;
import Utilities.CSVUpdater;
import Utilities.UserInputHandler;


public class AdminShared {
    private static final UserInputHandler userInputHandler = new UserInputHandler();
    private static final AdminDisplayManager displayManager = new AdminDisplayManager();
    private static final DisplayFormat displayFormat = new DisplayFormat();
    private static final CSVUpdater csvUpdater = new CSVUpdater("Staff_List.csv");


    public static CSVUpdater getCSVUpdater() {
        return csvUpdater;
    }

    public static UserInputHandler getUserInputHandler() {
        return userInputHandler;
    }

    public static AdminDisplayManager getDisplayManager() {
        return displayManager;
    }
    public static DisplayFormat getDisplayFormat() {
        return displayFormat;
    }
}
