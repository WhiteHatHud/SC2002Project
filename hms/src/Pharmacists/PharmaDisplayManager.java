package Pharmacists;
import Login.DisplayManager;

public class PharmaDisplayManager extends DisplayManager{

    public void getDisplayMenu(){
        System.out.println("\n=== Pharmacist Menu ===");
        System.out.println("1. Update Account Information");
        System.out.println("2. View Prescriptions");
        System.out.println("3. Dispense medicine");
        System.out.println("4. Check Medication Inventory");
        System.out.println("5. Submit Replenishment Request");
        System.out.println("6. Logout");
        
        System.out.print("Please enter your choice: ");
    }



}