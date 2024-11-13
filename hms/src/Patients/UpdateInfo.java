package Patients;

public class UpdateInfo {
    
    private Patient patient;

    public UpdateInfo(Patient patient) {
        this.patient = patient;
        displayCurrentProfile();
    }

    private void displayCurrentProfile() {
        patient.getProfile();
    }

    public void updatePersonalInformation() {
        boolean updating = true;
        
        while (updating) {
            PatientShared.getDisplayManager().getUpdateMenu();
            
            int choice = Integer.parseInt(PatientShared.getUserInputHandler().getInput());
            
            switch (choice) {
                case 1:
                    updateEmail();
                    break;
                case 2:
                    updateContactNumber();
                    break;
                case 3:
                    updateEmergencyContact();
                    break;
                case 4:
                    changePassword();
                    break;
                case 5:
                    updating = false;
                    System.out.println("Exiting update...");
                    try {
                        Thread.sleep(2000);  
                    } catch (InterruptedException e) {
                        e.printStackTrace();  
                    }
                    
                    Login.DisplayFormat.clearScreen();
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }

        System.out.println("\nProfile update complete.");
    }

    private void updateEmail() {
        PatientShared.getUserInputHandler().getNextLine(); 
        System.out.print("New Email (leave blank to keep current): ");
        String newEmail = PatientShared.getUserInputHandler().getNextLine();
        if (!newEmail.isBlank()) {
            patient.setEmail(newEmail);
            Login.DisplayFormat.clearScreen();
            PatientShared.getCSVUpdater().updateField(patient.getUserID(), "Contact Information", newEmail);
            Login.DisplayFormat.clearScreen(); 
            System.out.println("Email updated successfully.");
        } else {
            Login.DisplayFormat.clearScreen(); 
            System.out.println("Email remains unchanged.");
        }
    }

    private void updateContactNumber() {
        PatientShared.getUserInputHandler().getNextLine(); 
        System.out.print("New Contact Number (leave blank to keep current): ");
        String newContactNumber = PatientShared.getUserInputHandler().getNextLine();
        
        // Validate if the number is exactly 8 digits
        if (!newContactNumber.isBlank() && newContactNumber.matches("\\d{8}")) {
            patient.setNumber(newContactNumber);
            Login.DisplayFormat.clearScreen();
            PatientShared.getCSVUpdater().updateField(patient.getUserID(), "Number", newContactNumber);
            Login.DisplayFormat.clearScreen(); 
            System.out.println("Contact number updated successfully.");
        } else if (!newContactNumber.isBlank()) {
            Login.DisplayFormat.clearScreen(); 
            System.out.println("Invalid phone number!");
        } else {
            Login.DisplayFormat.clearScreen(); 
            System.out.println("Contact number remains unchanged.");
        }
    }

    private void updateEmergencyContact() {
        PatientShared.getUserInputHandler().getNextLine(); 
        System.out.print("New Emergency Contact (leave blank to keep current): ");
        String newEmergencyContact = PatientShared.getUserInputHandler().getNextLine();
        
        // Validate if the emergency contact number is exactly 8 digits
        if (!newEmergencyContact.isBlank() && newEmergencyContact.matches("\\d{8}")) {
            patient.setenumber(newEmergencyContact);
            Login.DisplayFormat.clearScreen();
            PatientShared.getCSVUpdater().updateField(patient.getUserID(), "Emergency Number", newEmergencyContact);
            System.out.println("Emergency contact updated successfully.");
        } else if (!newEmergencyContact.isBlank()) {
            Login.DisplayFormat.clearScreen(); 
            System.out.println("Invalid phone number! ");
        } else {
            Login.DisplayFormat.clearScreen(); 
            System.out.println("Emergency contact remains unchanged.");
        }
    }

    private void changePassword() {
        System.out.print("Enter Current Password: ");
        String currentPassword = PatientShared.getUserInputHandler().getInput();
    
        // Verify current password
        if (!patient.getPassword().equals(currentPassword)) {
            Login.DisplayFormat.clearScreen();
            System.out.println("Incorrect current password. Password change aborted.");
            return;
        }
    
        // Prompt for new password
        System.out.print("Enter New Password (Password must be more than 8 characters, include at least one symbol (!@#$), and contain at least one number.): " );
        
        String newPassword = PatientShared.getUserInputHandler().getInput();
    
        // Check if the new password meets the requirements
        if (newPassword.length() <= 8 || 
            !newPassword.matches(".*[!@#$].*") || 
            !newPassword.matches(".*\\d.*")) {
            
            Login.DisplayFormat.clearScreen();
            System.out.println("Password must be more than 8 characters, include at least one symbol (!@#$), and contain at least one number. Password change aborted.");
            return;
        }
    
        System.out.print("Confirm New Password: ");
        String confirmPassword = PatientShared.getUserInputHandler().getInput();
    
        // Check if the new password matches the confirmation
        if (newPassword.equals(confirmPassword)) {
            patient.setPassword(newPassword);  // Update password in the patient object
            PatientShared.getCSVUpdater().updateField(patient.getUserID(), "Password", newPassword);
            Login.DisplayFormat.clearScreen();
            System.out.println("Password updated successfully.");
        } else {
            Login.DisplayFormat.clearScreen();
            System.out.println("Passwords do not match. Password change aborted!");
        }
    }
    
    
}
