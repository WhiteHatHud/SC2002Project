package Admins;

import Utilities.CSVUpdater;

public class UpdatePharmacist {
    private CSVUpdater csvUpdater;

    public UpdatePharmacist(String csvFilePath) {
        this.csvUpdater = new CSVUpdater(csvFilePath);
    }

    public void start() {

        System.out.print("Enter Pharmacist ID to update: ");
        String pharmaID = AdminShared.getUserInputHandler().getNextLine();

        // Check if the Pharmacist ID is valid (should start with "P")
        if (!pharmaID.startsWith("P")) {
            System.out.println("Error: Invalid Pharmacist ID. Pharmacist IDs must start with 'P'.");
            return;
        }

        // Check if the Pharmacist ID exists in the CSV file
        if (!AdminShared.getCSVUtilities().checkIfUserExists(pharmaID)) {
            System.out.println("Pharmacist ID not found.");
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
            System.out.println("6. Finish updating");  // Option to finish updating
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
                    continue; // To restart
            }

            if (fieldName != null) {
                System.out.print("Enter new value for " + fieldName + ": ");
                String newValue = AdminShared.getUserInputHandler().getNextLine();

                // Update the specified field for the given Pharmacist ID
                csvUpdater.updateField(pharmaID, fieldName, newValue);
                System.out.println(fieldName + " updated successfully for Pharmacist ID " + pharmaID + ".");
            }
        }
    }
}
