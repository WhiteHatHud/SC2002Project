package Admins;

import Utilities.CSVUpdater;

public class UpdateDoctor {
    private CSVUpdater csvUpdater;

    public UpdateDoctor(String csvFilePath) {
        this.csvUpdater = new CSVUpdater(csvFilePath);
    }

    public void start() {

        System.out.print("Enter Doctor ID to update: ");
        String doctorID = AdminShared.getUserInputHandler().getInputString();

        // Check if the Doctor ID is valid (should start with "D")
        if (!doctorID.startsWith("D")) {
            System.out.println("Error: Invalid Doctor ID. Doctor IDs must start with 'D'.");
            return;
        }

        // Check if the Doctor ID exists in the CSV file
        if (!AdminShared.getCSVUtilities().checkIfUserExists(doctorID)) {
            System.out.println("Doctor ID not found.");
            return;
        }

        boolean continueUpdating = true;
        while (continueUpdating) {
            System.out.println("\nWhich field would you like to update?");
            System.out.println("1. Name");
            System.out.println("2. Gender");
            System.out.println("3. Age");
            System.out.println("4. Office Number");
            System.out.println("5. Password");
            System.out.println("6. Finish updating");  
            System.out.print("Enter the number of the field to update: ");

            int choice = AdminShared.getUserInputHandler().getUserChoice();

            String fieldName = null;
            switch (choice) {
                case 1:
                    fieldName = "Name";
                    break;
                case 2:
                    fieldName = "Gender";
                    break;
                case 3:
                    fieldName = "Age";
                    break;
                case 4:
                    fieldName = "Office Number";
                    break;
                case 5:
                    fieldName = "Password";
                    break;
                case 6:
                    continueUpdating = false; // Exit the loop
                    System.out.println("Finished updating.");
                    continue; // To exit
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                    continue; // Restart loop to allow valid input
            }

            if (fieldName != null) {
                System.out.print("Enter new value for " + fieldName + ": ");
                String newValue = AdminShared.getUserInputHandler().getNextLine();

                // Update the specified field for the given Doctor ID
                csvUpdater.updateField(doctorID, fieldName, newValue);
                System.out.println(fieldName + " updated successfully for Doctor ID " + doctorID + ".");
            }
        }
    }
}
