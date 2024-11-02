package Patients;

public class UpdateInfo {
    
    private Patient patient;

    public UpdateInfo(Patient patient) {
        this.patient = patient;
        displayCurrentProfile();
        updatePersonalInformation();
    }

    private void displayCurrentProfile() {
        System.out.println("\n=== Current Profile ===");
        patient.getProfile();
        System.out.println("========================\n");
    }

    private void updatePersonalInformation() {
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
        String newEmail = PatientShared.getUserInputHandler().getInput();
        if (!newEmail.isBlank()) {
            patient.setEmail(newEmail);
            Login.DisplayFormat.clearScreen();
            PatientShared.getCSVUpdater().updatePatientField(patient.getUserID(),"Contact Information",newEmail);
            System.out.println("Email updated successfully.");
        } else {
            System.out.println("Email remains unchanged.");
        }
    }

    private void updateContactNumber() {
        System.out.print("New Contact Number (leave blank to keep current): ");
        String newContactNumber = PatientShared.getUserInputHandler().getInput();
        if (!newContactNumber.isBlank()) {
            patient.setNumber(newContactNumber);
            Login.DisplayFormat.clearScreen();
            PatientShared.getCSVUpdater().updatePatientField(patient.getUserID(),"Number",newContactNumber);
            System.out.println("Contact number updated successfully.");
        } else {
            System.out.println("Contact number remains unchanged.");
        }
    }

    private void updateEmergencyContact() {
        System.out.print("New Emergency Contact (leave blank to keep current): ");
        String newEmergencyContact = PatientShared.getUserInputHandler().getInput();
        if (!newEmergencyContact.isBlank()) {
            patient.setenumber(newEmergencyContact);
            Login.DisplayFormat.clearScreen();
            PatientShared.getCSVUpdater().updatePatientField(patient.getUserID(),"Emergency Number",newEmergencyContact);

            System.out.println("Emergency contact updated successfully.");
        } else {
            System.out.println("Emergency contact remains unchanged.");
        }
    }
}
