package Patients;

import Login.DisplayFormat;
import Utilities.CSVUpdater;
import Utilities.CSVUtilities;
import Utilities.UserInputHandler;

/**
 * The PatientShared class provides shared utilities, display managers,
 * and handlers specific to patient-related operations in the application.
 * This class includes static references for CSV management, display formats,
 * and user input handling.
 */
public class PatientShared {

    private static final UserInputHandler userInputHandler = new UserInputHandler();
    private static final PatientDisplayManager displayManager = new PatientDisplayManager();
    private static final DisplayFormat displayFormat = new DisplayFormat();
    private static final CSVUpdater csvUpdater = new CSVUpdater("Patient List CSV.csv");
    private static final CSVUtilities csvUtilities = new CSVUtilities("Staff_List.csv");
    private static final CSVUtilities csvUtilities2 = new CSVUtilities("Diagnosis.csv");

    /**
     * Gets the CSVUtilities instance for the diagnosis CSV file.
     *
     * @return The CSVUtilities instance for the "Diagnosis.csv" file.
     */
    public static CSVUtilities getCSVUtilities2() {
        return csvUtilities2;
    }

    /**
     * Gets the CSVUtilities instance for the staff CSV file.
     *
     * @return The CSVUtilities instance for the "Staff_List.csv" file.
     */
    public static CSVUtilities getCSVUtilities() {
        return csvUtilities;
    }

    /**
     * Gets the CSVUpdater instance for the patient CSV file.
     *
     * @return The CSVUpdater instance for the "Patient List CSV.csv" file.
     */
    public static CSVUpdater getCSVUpdater() {
        return csvUpdater;
    }

    /**
     * Gets the UserInputHandler instance for managing user inputs.
     *
     * @return The UserInputHandler instance for patient-related input handling.
     */
    public static UserInputHandler getUserInputHandler() {
        return userInputHandler;
    }

    /**
     * Gets the PatientDisplayManager instance for managing patient-specific display operations.
     *
     * @return The PatientDisplayManager instance for patient displays.
     */
    public static PatientDisplayManager getDisplayManager() {
        return displayManager;
    }

    /**
     * Gets the DisplayFormat instance for handling display formatting.
     *
     * @return The DisplayFormat instance for formatting patient displays.
     */
    public static DisplayFormat getDisplayFormat() {
        return displayFormat;
    }
}
