package Pharmacists;
import Login.DisplayManager;

public class PharmaDisplayManager extends DisplayManager{

    public void displayMainMenu(){
        System.out.println("\n=== Pharmacist Menu ===");
        System.out.println("1. View appointment outcome");
        System.out.println("2. Prescriptions Menu");
        System.out.println("3. Display all medicines");
        System.out.println("4. Submit Replemnishment Request");
        System.out.println("5. Logout");
        System.out.print("Enter your choice: ");
    }
}