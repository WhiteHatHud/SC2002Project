package Admins;

import Login.DisplayFormat;
import Utilities.CSVUpdater;
import Utilities.CSVUtilities;
import Utilities.UserInputHandler;

/**
 * The AdminShared class provides shared resources and utilities for the Admin-related classes.
 * It centralizes access to commonly used instances, such as the user input handler, display formats,
 * CSV updater, CSV utilities, and display manager, used across the Admin-related classes.
 */
public class AdminShared {

    private static final UserInputHandler userInputHandler = new UserInputHandler();
    private static final DisplayFormat displayFormat = new DisplayFormat();
    private static final CSVUpdater csvUpdater = new CSVUpdater("Staff_List.csv");
    private static final CSVUtilities csvUtil = new CSVUtilities("Staff_List.csv");
    private static final CSVUpdater csvUpdaterPatient = new CSVUpdater("Patient List CSV.csv");
    private static final CSVUtilities csvUtilPatient = new CSVUtilities("Patient List CSV.csv");
    private static final AdminDisplayManager displayManager = new AdminDisplayManager();

    /**
     * Gets the AdminDisplayManager instance used for displaying admin-specific menus.
     *
     * @return An instance of AdminDisplayManager.
     */
    public static AdminDisplayManager getDisplayManager() {
        return displayManager;
    }

    /**
     * Gets the CSVUpdater instance for updating the "Staff_List.csv" file.
     *
     * @return The CSVUpdater instance for staff list updates.
     */
    public static CSVUpdater getCSVUpdater() {
        return csvUpdater;
    }

    /**
     * Gets the CSVUpdater instance for updating the "Patient List CSV.csv" file.
     *
     * @return The CSVUpdater instance for patient list updates.
     */
    public static CSVUpdater getCSVUpdaterPatient() {
        return csvUpdaterPatient;
    }

    /**
     * Gets the UserInputHandler instance for handling user input.
     *
     * @return An instance of UserInputHandler.
     */
    public static UserInputHandler getUserInputHandler() {
        return userInputHandler;
    }

    /**
     * Gets the DisplayFormat instance used for formatting console output.
     *
     * @return An instance of DisplayFormat.
     */
    public static DisplayFormat getDisplayFormat() {
        return displayFormat;
    }

    /**
     * Gets the CSVUtilities instance for accessing utilities related to "Patient List CSV.csv" file.
     *
     * @return The CSVUtilities instance for patient list utilities.
     */
    public static CSVUtilities getCSVUtilitiesPatient() {
        return csvUtilPatient;
    }

    /**
     * Gets the CSVUtilities instance for accessing utilities related to "Staff_List.csv" file.
     *
     * @return The CSVUtilities instance for staff list utilities.
     */
    public static CSVUtilities getCSVUtilities() {
        return csvUtil;
    }
}
