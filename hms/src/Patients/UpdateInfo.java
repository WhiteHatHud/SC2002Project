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
            
            int choice = Integer.parseInt(PatientShared.getUserInputHandler().getInputString());
            
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
        System.out.print("New Email (leave blank to keep current): ");
        String newEmail = PatientShared.getUserInputHandler().getInputString();
        if (!newEmail.isBlank()) {
            patient.setEmail(newEmail);
            Login.DisplayFormat.clearScreen();
            PatientShared.getCSVUpdater().updateField(patient.getUserID(),"Contact Information",newEmail);
            System.out.println("Email updated successfully.");
        } else {
            System.out.println("Email remains unchanged.");
        }
    }

    private void updateContactNumber() {
        System.out.print("New Contact Number (leave blank to keep current): ");
        String newContactNumber = PatientShared.getUserInputHandler().getInputString();
        if (!newContactNumber.isBlank()) {
            patient.setNumber(newContactNumber);
            Login.DisplayFormat.clearScreen();
            PatientShared.getCSVUpdater().updateField(patient.getUserID(),"Number",newContactNumber);
            System.out.println("Contact number updated successfully.");
        } else {
            System.out.println("Contact number remains unchanged.");
        }
    }

    private void updateEmergencyContact() {
        System.out.print("New Emergency Contact (leave blank to keep current): ");
        String newEmergencyContact = PatientShared.getUserInputHandler().getInputString();
        if (!newEmergencyContact.isBlank()) {
            patient.setenumber(newEmergencyContact);
            Login.DisplayFormat.clearScreen();
            PatientShared.getCSVUpdater().updateField(patient.getUserID(),"Emergency Number",newEmergencyContact);
            System.out.println("Emergency contact updated successfully.");
        } else {
            System.out.println("Emergency contact remains unchanged.");
        }
    }

    private void changePassword() {
        System.out.print("Enter Current Password: ");
        String currentPassword = PatientShared.getUserInputHandler().getInputString();

        // Verify current password
        if (!patient.getPassword().equals(currentPassword)) {
            System.out.println("Incorrect current password. Password change aborted.");
            return;
        }

        // Prompt for new password
        System.out.print("Enter New Password: ");
        String newPassword = PatientShared.getUserInputHandler().getInputString();

        System.out.print("Confirm New Password: ");
        String confirmPassword = PatientShared.getUserInputHandler().getInputString();

        // Check if the new password matches the confirmation
        if (newPassword.equals(confirmPassword)) {
            patient.setPassword(newPassword);  // Update password in the patient object
            PatientShared.getCSVUpdater().updateField(patient.getUserID(), "Password", newPassword);
            System.out.println("Password updated successfully.");
        } else {
            System.out.println("Passwords do not match. Password change aborted.");
        }
    }
}
