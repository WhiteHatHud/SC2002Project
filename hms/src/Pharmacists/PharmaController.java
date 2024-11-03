package Pharmacists;
import Login.ControllerInt;

public class PharmaController implements ControllerInt{

    private Pharmacist pharma;

    public PharmaController(Pharmacist pharma){
        this.pharma = pharma;
    }

    public void start() {

        
    }
    @Override
    public boolean handleChoice(int choice){




       // System.out.println("Welcome, " + pharma.getName()); 
        return true;
        
    }
    
}
