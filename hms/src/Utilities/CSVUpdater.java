package Utilities;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUpdater {
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
}
