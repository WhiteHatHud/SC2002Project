package Doctors;

import Login.DisplayFormat;
import Utilities.CSVUpdater;
import Utilities.CSVUtilities;
import Utilities.UserInputHandler;

/**
 * The {@code DoctorShared} class provides shared utility instances and configurations
 * that can be accessed by other classes within the {@code Doctors} package.
 * This includes access to display formatting, CSV handling utilities, 
 * and user input handling.
 */
public class DoctorShared {
    private static final UserInputHandler userInputHandler = new UserInputHandler();
    private static final DoctorDisplayManager displayManager = new DoctorDisplayManager();
    private static final DisplayFormat displayFormat = new DisplayFormat();
    private static CSVUpdater csvUpdater = new CSVUpdater("Staff_List.csv");
    private static CSVUtilities csvUtilities = new CSVUtilities("Patient List CSV.csv");
    private static CSVUtilities csvUtilities2 = new CSVUtilities("Staff_List.csv");
    private static CSVUtilities csvUtilities3 = new CSVUtilities("Diagnosis.csv");

    /**
     * Returns the {@code CSVUtilities} instance for accessing the "Diagnosis.csv" file.
     *
     * @return {@code CSVUtilities} instance for diagnosis records.
     */
    public static CSVUtilities getCSVUtilities3() {
        return csvUtilities3;
    }

    /**
     * Returns the {@code CSVUtilities} instance for accessing the "Staff_List.csv" file.
     *
     * @return {@code CSVUtilities} instance for staff records.
     */
    public static CSVUtilities getcsvUtilities2() {
        return csvUtilities2;
    }

    /**
     * Returns the {@code CSVUtilities} instance for accessing the "Patient List CSV.csv" file.
     *
     * @return {@code CSVUtilities} instance for patient records.
     */
    public static CSVUtilities getcsvUtilities() {
        return csvUtilities;
    }

    /**
     * Returns the {@code CSVUpdater} instance for modifying the "Staff_List.csv" file.
     *
     * @return {@code CSVUpdater} instance for updating staff records.
     */
    public static CSVUpdater getCSVUpdater() {
        return csvUpdater;
    }

    /**
     * Returns the {@code UserInputHandler} instance for handling user inputs.
     *
     * @return {@code UserInputHandler} instance.
     */
    public static UserInputHandler getUserInputHandler() {
        return userInputHandler;
    }

    /**
     * Returns the {@code DoctorDisplayManager} instance for displaying doctor-specific menu options.
     *
     * @return {@code DoctorDisplayManager} instance.
     */
    public static DoctorDisplayManager getDisplayManager() {
        return displayManager;
    }

    /**
     * Returns the {@code DisplayFormat} instance for formatting displays.
     *
     * @return {@code DisplayFormat} instance.
     */
    public static DisplayFormat getDisplayFormat() {
        return displayFormat;
    }
}
