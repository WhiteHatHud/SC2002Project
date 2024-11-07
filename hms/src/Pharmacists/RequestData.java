package Pharmacists;

import Login.DisplayFormat;
import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class RequestData {

    public void request(String pharmaID, String pharmaName) {
        Set<String> medicineList = new HashSet<>();

        // Read all meds from Medicine_List.csv
        try (BufferedReader reader = new BufferedReader(new FileReader("Medicine_List.csv"))) {
            String line = reader.readLine(); 

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String medicineName = data[0].trim();
                medicineList.add(medicineName); // Add to set to avoid duplicates
            }
        } catch (IOException e) {
            System.out.println("Error reading Medicine_List.csv: " + e.getMessage());
            return;
        }

        // Display medicines according to the CSV file
        if (medicineList.isEmpty()) {
            System.out.println("No medicines available in the inventory.");
            return;
        }

        System.out.println("Available Medicines:");
        int index = 1;
        String[] medicinesArray = medicineList.toArray(new String[0]);
        for (String medicine : medicinesArray) {
            System.out.println(index++ + ". " + medicine);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Select a medicine by entering the corresponding number: ");
        System.out.print("Enter any other character to return: ");
        int choice = scanner.nextInt();
        
        if (choice < 1 || choice > medicinesArray.length) {
            System.out.println("Invalid choice. Exiting request.");
            scanner.close();
            return;
        }
        
        String selectedMedicine = medicinesArray[choice - 1];

        System.out.print("Enter the quantity of " + selectedMedicine + " to request (1=100mg): ");
        int quantity = scanner.nextInt();
        
        if (quantity <= 0) {
            System.out.println("Invalid quantity. Exiting request.");
            scanner.close();
            return;
        }

        DisplayFormat.clearScreen();

        System.out.println("Medicine requested: " + selectedMedicine);
        System.out.println("Quantity requested: " + quantity);

        // Call the method to write to Req CSV
        writeToRequestFile(selectedMedicine, quantity, pharmaID, pharmaName);
        scanner.close();
    }

    private void writeToRequestFile(String medicineName, int quantity, String pharmaID, String pharmaName) {
        int nextID = 1;  // Start ID from 1
    
        // Determine the next available ID
        try (BufferedReader reader = new BufferedReader(new FileReader("RequestFromPharma.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip the header line without trying to parse it
                if (line.startsWith("ID,")) {
                    continue;
                }
                
                String[] rowData = line.split(",");
                try {
                    int currentID = Integer.parseInt(rowData[0].trim());
                    if (currentID >= nextID) {
                        nextID = currentID + 1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Skipping line due to invalid ID format: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading RequestFromPharma.csv: " + e.getMessage());
        }
    
        // Append the new request with the new ID
        try (FileWriter fileWriter = new FileWriter("RequestFromPharma.csv", true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            
            
            BufferedReader reader = new BufferedReader(new FileReader("RequestFromPharma.csv"));
            if (reader.readLine() == null) {
                bufferedWriter.write("ID,Medicine Name,Quantity,PharmaID,PharmaName\n");
            }
            reader.close();
    
            // Write the data to the file
            bufferedWriter.write(String.format("%d,%s,%d,%s,%s%n", nextID, medicineName, quantity, pharmaID, pharmaName));
            System.out.println("Request submitted successfully with Order ID: " + nextID + ". Awaiting administrative approval.");
        } catch (IOException e) {
            System.out.println("Error writing to RequestFromPharma.csv: " + e.getMessage());
        }
    }
    
}
