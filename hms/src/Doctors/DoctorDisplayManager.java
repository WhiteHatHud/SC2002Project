package Doctors;

import Login.DisplayManager;

/**
 * The {@code DoctorDisplayManager} class extends the {@code DisplayManager} class 
 * to provide a custom display menu specific to the actions that a doctor can perform.
 */
public class DoctorDisplayManager extends DisplayManager {

    /**
     * Displays the main menu options for the doctor. This includes options for viewing,
     * creating, and updating patient medical records, managing appointment matters, and logging out.
     */
    public void getDisplayMenu() {
        System.out.println("=== Doctor Menu ===");
        System.out.println("1. View Patient Medical Records");
        System.out.println("2. Create Patient Medical Record");
        System.out.println("3. Update Patient Medical Records");
        System.out.println("4. Manage Appointment Matters");
        System.out.println("5. Logout");
    }
}

