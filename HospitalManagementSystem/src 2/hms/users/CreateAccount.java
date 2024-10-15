package hms.users;
import java.util.*;

public class CreateAccount {
    // HashMap to store accounts
    private HashMap<String, Users> accounts;


    public CreateAccount() {
        accounts = new HashMap<>();  
    }


    public boolean createAccount(String userID, String name, String role) {

        if (accounts.containsKey(userID)) {
            System.out.println("Error: User ID already exists. Please choose a different ID.");
            return false;  // Account creation failed
        }

        // Create the User object based on the role
        Users newUser = null;
        Scanner sc = new Scanner(System.in);
        
        
        System.out.println ("Please choose following options:" +
        					"1. Patient account" + 
        					"2. Doctor account" );
        
        int choice= sc.nextInt();
        
        switch (choice) {
            case 1:
                newUser = new Patient(userID, name, role);
                break;
            case 2:
                newUser = new Doctor(userID, name, role);
                break;
            case 3:
                newUser = new Pharmacist(userID, name, role);
                break;
            default:
                System.out.println("Error: Invalid role specified.");
                return false;  // Account creation failed due to invalid role
        }

        // Add the new account to the HashMap
        accounts.put(userID, newUser);
        System.out.println("Account created successfully for user: " + name + " with role: " + role);
        return true;  // Account creation successful
    }

    // Method to retrieve an account by userID
    public User getAccount(String userID) {
        return accounts.get(userID);  // Returns null if the userID is not found
    }

    // Method to display all accounts
    public void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        System.out.println("List of accounts:");
        for (String userID : accounts.keySet()) {
            User user = accounts.get(userID);
            System.out.println("User ID: " + userID + ", Name: " + user.getName() + ", Role: " + user.getRole());
        }
    }
}

