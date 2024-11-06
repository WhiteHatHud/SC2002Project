package Pharmacists.Old_Files;

import Login.DisplayFormat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ViewPendingPrescriptions {
    private String csvFilePath = "To be prescribed.csv"; 


    public void displayPrescriptions() {
        System.out.println("=== Pending Prescriptions ===\n");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");

                if (data.length >= 9) {
                    System.out.println("Prescription ID    : " + data[0].trim());
                    System.out.println("Patient ID         : " + data[1].trim());
                    System.out.println("Patient Name       : " + data[2].trim());
                    System.out.println("Doctor ID          : " + data[3].trim());
                    System.out.println("Doctor Name        : " + data[4].trim());
                    System.out.println("Date Prescribed    : " + data[5].trim());
                    System.out.println("Medications (mg)   : " + data[6].trim());
                    System.out.println("Status             : " + data[7].trim());
                    System.out.println("Notes              : " + data[8].trim());
                    System.out.println("---------------------------------------------------\n");
                } else {
                    System.out.println("Error: Malformed row in CSV - " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }


        System.out.println("Enter any key to return to main menu...");
        PharmaShared.getUserInputHandler().getInput();
        DisplayFormat.clearScreen();
    }
}
