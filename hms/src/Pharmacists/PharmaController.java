package Pharmacists;
import Login.ControllerInt;

public class PharmaController implements ControllerInt{

    @Override
    public boolean handleChoice(int choice){

        System.out.println("In pharma now");
        return true;
        
    }
    
}
