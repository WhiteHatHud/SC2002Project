package Patients;
import Login.DisplayManager;

public class PatientDisplayManager extends DisplayManager {
    
    public void displayMenu() {
        System.out.println("1. View Medical Record");
        System.out.println("2. Update Personal Information");
        System.out.println("3. View Available Appointment Slots");
        System.out.println("4. Schedule an Appointment"); //done
        System.out.println("5. Reschedule an Appointment"); //dont need
        System.out.println("6. Cancel an Appointment"); //View Cancelled Appointment
        System.out.println("7. View Scheduled Appointments");
        System.out.println("8. View Past Appointment Outcome Records");
        System.out.println("9. Logout");
    }




}