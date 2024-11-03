package Pharmacists;
import Login.DisplayManager;

public class PharmaDisplayManager extends DisplayManager{

    public void getDisplayMenu(){
        System.out.println("\n=== Pharmacist Menu ===");
        System.out.println("1. View Prescriptions");
        System.out.println("2. Update Prescription Status");
        System.out.println("3. Check Medication Inventory");
        System.out.println("4. Submit Replenishment Request");
        System.out.println("5. Logout");
        System.out.print("Please enter your choice: ");
    }



}