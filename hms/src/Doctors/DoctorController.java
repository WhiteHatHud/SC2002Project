package Doctors;

import Login.ControllerInt;
import Login.DisplayFormat;
import Login.DisplayManager;
import Patients.PatientShared;
import Utilities.LogoutTimer;
import appt.DoctorUI;
import java.util.List;

/**
 * The {@code DoctorController} class is responsible for managing the interactions and
 * actions performed by a doctor in the system. This includes viewing patients under care,
 * creating and updating medical records, and logging out.
 */
public class DoctorController implements ControllerInt {

    private Doctor doctor;

    /**
     * Constructs a {@code DoctorController} for a specific doctor.
     *
     * @param doctor the {@code Doctor} object associated with this controller
     */
    public DoctorController(Doctor doctor) {
        this.doctor = doctor;
    }

    /**
     * Starts the doctor session, displaying a welcome message and presenting the doctor
     * with various options until they choose to log out.
     *
     * @return {@code false} if the session is ended, typically when the doctor logs out
     */
    public boolean start() {
        DisplayFormat.clearScreen();
        System.out.println("Welcome, " + doctor.getName());

        boolean isActive = true;
        while (isActive) {
            DoctorShared.getDisplayManager().getDisplayMenu();
            int choice = DoctorShared.getUserInputHandler().getUserChoice();
            isActive = handleChoice(choice);
        }

        return false;
    }

    /**
     * Handles the menu choice selected by the doctor. Depending on the choice, the doctor
     * can view patients, create or update records, manage appointments, or log out.
     *
     * @param choice the menu option selected by the doctor
     * @return {@code true} to continue the session, {@code false} if logging out
     */
    @Override
    public boolean handleChoice(int choice) {
        DisplayFormat.clearScreen();
        switch (choice) {
            case 1:
                viewPatientUnderMyCare();
                break;
    
            case 2:
                CreateRecord record = new CreateRecord(doctor.getUserID());
                record.createRecord();
                break;
    
            case 3:
                UpdateRecord update = new UpdateRecord(doctor.getUserID());
                update.updateRecord();
                break;
    
            case 4:
                DoctorUI ui = new DoctorUI(doctor.getUserID(), doctor.getName());
                ui.start();
                DisplayManager.clearScreen();
                break;
    
            case 5:
                if (LogoutTimer.confirmLogout()) {
                    return false;
                } else {
                    DisplayManager.clearScreen();
                    return true;
                }
    
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    
        return true;
    }

    /**
     * Displays a list of patients under the care of the doctor along with their diagnosis and
     * prescription history. For each patient, the system retrieves basic information and displays
     * relevant medical records if available.
     */
    public void viewPatientUnderMyCare() {
        List<String> patientIDs = DoctorShared.getcsvUtilities().getPatientIDsUnderDoctorCare(doctor.getUserID());
    
        if (patientIDs.isEmpty()) {
            System.out.println("No patients found under your care.");
            return;
        }
    
        System.out.println("There are " + patientIDs.size() + " patient(s) under your care.");
        System.out.println("=== Patient(s) Under Your Care ===");
    
        int count = 1;
        for (String patientID : patientIDs) {
            System.out.println("\n--- Patient Information " + count + " ---");
            DoctorShared.getcsvUtilities().getPatientInformation(patientID);
            
            List<String[]> diagnosisData = DoctorShared.getCSVUtilities3().getDiagnosisAndPrescriptionByPatientID(patientID, doctor.getUserID());
    
            if (diagnosisData.isEmpty()) {
                System.out.println("No Diagnosis found for PatientID: " + patientID);
            } else {
                System.out.println("Diagnosis and Prescription for PatientID: " + patientID);
                System.out.println("-------------------------");
                int count2 = 1;
                for (String[] details : diagnosisData) {
                    System.out.println("Record ID " + count2 + ":");
                    System.out.println("Diagnosis Description: " + details[0]);
                    System.out.println("Prescription(mg): " + details[1]);
                    System.out.println("Date of Diagnosis: " + details[2]);
                    System.out.println("-------------------------");
                    count2++;
                }
            }
            count++;
        }
        DisplayManager.pauseContinue();
    }
}
