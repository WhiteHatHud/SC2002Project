package Patients;
import Login.ControllerInt;

public class PatientController implements ControllerInt {

    PatientShared patientInstances = new PatientShared();


    public void start(){
        patientInstances.getDisplayManager().displayMenu();

    }

    public void handleChoice(int choice)
    {
        
    }
    
}
