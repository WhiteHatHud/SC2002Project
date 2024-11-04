package Patients;
import Login.DisplayManager;

public class PatientDisplayManager extends DisplayManager {
    
    public void displayMenu() {
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        System.out.println("3. Manage Appointment Matters"); //done
        System.out.println("4. Logout");
    }

    public void getUpdateMenu() {

            System.out.println("\nWhat would you like to update?");
            System.out.println("1. Email");
            System.out.println("2. Contact Number");
            System.out.println("3. Emergency Contact");
            System.out.println("4. Change Password");
            System.out.println("5. Done (Exit Update)");
            System.out.print("Enter your choice: ");
    }

    public void apptMenu() {
        System.out.println("1. Schedule an Appointment"); //done
        System.out.println("2. Reschedule an Appointment"); //dont need
        System.out.println("3. Cancel an Appointment"); //View Cancelled Appointment
        System.out.println("4. View Scheduled Appointments");
        System.out.println("5. View Past Appointment Outcome Records");
        System.out.println("6. Back To Main Menu");
    }

}