package Doctors;
import Login.DisplayFormat;
import Utilities.CSVUpdater;
import Utilities.UserInputHandler;

public class DoctorShared {
    private static final UserInputHandler userInputHandler = new UserInputHandler();
    private static final DoctorDisplayManager displayManager = new DoctorDisplayManager();
    private static final DisplayFormat displayFormat = new DisplayFormat();
    private static  CSVUpdater csvUpdater = new CSVUpdater("Staff_List.csv");


    public static CSVUpdater getCSVUpdater() {
        return csvUpdater;
    }

    public static UserInputHandler getUserInputHandler() {
        return userInputHandler;
    }

    public static DoctorDisplayManager getDisplayManager() {
        return displayManager;
    }
    public static DisplayFormat getDisplayFormat() {
        return displayFormat;
    }
}
