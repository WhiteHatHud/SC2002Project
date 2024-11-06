package Pharmacists;
import Login.DisplayManager;

public class PharmaDisplayManager extends DisplayManager{

    public void displayMainMenu(){
        System.out.println("\n=== Pharmacist Menu ===");
        System.out.println("1. Update Account Information");
        System.out.println("2. Prescriptions Menu");
        System.out.println("3. Medicines Menu");
        System.out.println("4. Logout");
        System.out.print("Enter your choice: ");
    }
}