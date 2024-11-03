package Doctors;
import Login.ControllerInt;

public class DoctorController implements ControllerInt{


    @Override
    public boolean handleChoice(int choice){
        System.out.println("In doctor now ");
        return true;
    }
}
