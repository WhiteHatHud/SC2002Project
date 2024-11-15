package Utilities;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUpdater implements CSVHandler{
    private String csvFile;

    public CSVUpdater(String csvFile) {
        this.csvFile = csvFile;
    }

    public void updateField(String userID, String fieldName, String newValue) {
        List<String[]> csvData = new ArrayList<>();
        String[] headers = null;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                
                if (isHeader) {
                    headers = data; 
                    isHeader = false;
                    csvData.add(headers);  
                    continue;
                }

                // If this is the row of the patient to update, modify the specified field
                if (data[0].equals(userID)) {
                    for (int i = 0; i < headers.length; i++) {
                        if (headers[i].trim().equalsIgnoreCase(fieldName)) {
                            data[i] = newValue;  // Update the specific field
                            break;
                        }
                    }
                }

                csvData.add(data);  // Add the modified row to output data
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            return;
        }

        // Write the modified data back to the CSV file
        try (PrintWriter pw = new PrintWriter(new FileWriter(csvFile))) {
            for (String[] row : csvData) {
                pw.println(String.join(",", row));
            }
           // System.out.println("Patient information successfully updated in CSV.");
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }


    public void addNewLine(String[] newData) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(csvFile, true))) { 
            pw.println(String.join(",", newData)); 
            //System.out.println("New entry added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    public void addNewLineToAppt(String[] newData, String csvFile1) {
        String csvFile = csvFile1;

        try (PrintWriter pw = new PrintWriter(new FileWriter(csvFile, true))) { 
            pw.println(String.join(",", newData)); 
            //System.out.println("New entry added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }
    public void addNewLineToCSV(String[] data, String csvFilePath, int expectedFields) {
        // Build a line with comma placeholders for empty fields
        StringBuilder lineBuilder = new StringBuilder();
        for (int i = 0; i < expectedFields; i++) {
            if (i < data.length && data[i] != null && !data[i].isEmpty()) {
                lineBuilder.append(data[i]);
            }
            lineBuilder.append(",");
        }
        // Remove the trailing comma
        if (lineBuilder.length() > 0) {
            lineBuilder.setLength(lineBuilder.length() - 1);
        }
        
        // Write the line to the specified CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {
            writer.write(lineBuilder.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }
    public void addNewPatient(String[] patientData) {
        // Headers for patient CSV
        String[] headers = {"Patient ID", "Name", "Date of Birth", "Gender", "Blood Type", "Contact Information", "Number", "Emergency Number", "Password"};
        
        // Ensure the input data matches the number of expected fields
        int expectedFields = headers.length;

        if (patientData.length != expectedFields) {
            System.out.println("Error: Patient data does not match the expected number of fields.");
            return;
        }

        // Build a line with patient data
        StringBuilder lineBuilder = new StringBuilder();
        for (int i = 0; i < expectedFields; i++) {
            if (i < patientData.length && patientData[i] != null && !patientData[i].isEmpty()) {
                lineBuilder.append(patientData[i]);
            }
            lineBuilder.append(",");
        }

        // Remove the trailing comma
        if (lineBuilder.length() > 0) {
            lineBuilder.setLength(lineBuilder.length() - 1);
        }

        // Write the new line to the patient CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            writer.write(lineBuilder.toString());
            writer.newLine();
            System.out.println("New patient entry added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
