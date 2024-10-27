package Patients;
import Login.ControllerInt;

public class PatientController implements ControllerInt {

    PatientDisplayManager patientDM = new PatientDisplayManager();

    public void start(){
        patientDM.displayMenu();

    }

    public void handleChoice(int choice)
    {
        
    }
    
}
