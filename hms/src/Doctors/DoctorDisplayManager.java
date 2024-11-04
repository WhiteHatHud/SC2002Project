package Doctors;
import Login.DisplayManager;

public class DoctorDisplayManager extends DisplayManager {

    public void getDisplayMenu(){
    System.out.println("=== Doctor Menu ===");
    System.out.println("1. View Patient Medical Records");
    System.out.println("2. Update Patient Medical Records");
    System.out.println("3. View Personal Schedule");
    System.out.println("4. Set Availability for Appointments");
    System.out.println("5. Accept or Decline Appointment Requests");
    System.out.println("6. View Upcoming Appointments");
    System.out.println("7. Record Appointment Outcome");
    System.out.println("8. Logout");
    }

}
