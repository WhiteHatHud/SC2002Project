package Doctors;
import Login.ControllerInt;
import Login.DisplayFormat;
import appt.DoctorUI;

public class DoctorController implements ControllerInt{

    private Doctor doctor;

    public DoctorController(Doctor doctor){
        this.doctor=doctor;
    }

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

    
    @Override
    public boolean handleChoice(int choice) {
        DisplayFormat.clearScreen();
        switch (choice) {
            case 1: 
            break;

            case 2:

                break;
                
            case 3: 
                DoctorUI ui = new DoctorUI(doctor.getUserID(),doctor.getName());
                ui.start();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }

        return true; // Continue session if choice is not logout
    }
}
