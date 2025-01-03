package Utilities;

import Users.Staff;
import java.io.*;
import java.util.*;

public class CSVUtilities {
    private String csvFile;

    public CSVUtilities(String csvFile) {
        this.csvFile = csvFile;
    }

    // Method to read CSV and count roles
    public Map<String, Integer> countRolesAndGenerateID() {
        Map<String, Integer> roleCountMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length < 3) {
                    System.out.println("Invalid line in CSV: " + line);
                    continue;
                }

                String role = data[2].trim();
                roleCountMap.put(role, roleCountMap.getOrDefault(role, 0) + 1);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }

        return roleCountMap;
    }
    public String generateLatestPatientID() {
        int highestID = 1000; 

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                String[] data = line.split(",");
                if (data.length > 0 && data[0].startsWith("P")) {
                    try {
                        int currentID = Integer.parseInt(data[0].substring(1));
                        if (currentID > highestID) {
                            highestID = currentID; 
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid ID format: " + data[0]);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }


        return "P" + (highestID + 1);
    }

    public String generateNewStaffID(String role, Map<String, Integer> roleCountMap) {
        String prefix;
        switch (role.toLowerCase()) {
            case "doctor":
                prefix = "D";
                break;
            case "pharmacist":
                prefix = "P";
                break;
            case "administrator":
                prefix = "A";
                break;
            default:
                prefix = "U";
        }

        int highestID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields[0].startsWith(prefix)) {
                    int idNumber = Integer.parseInt(fields[0].substring(1));
                    if (idNumber > highestID) {
                        highestID = idNumber;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        int newIDNumber = highestID + 1;
        roleCountMap.put(role, newIDNumber);
        return prefix + String.format("%03d", newIDNumber);
    }

    public boolean checkIfUserExists(String userID) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length > 0 && data[0].trim().equals(userID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        return false;
    }

    public List<Staff> readStaffList() {
        List<Staff> staffList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int age;
                try {
                    age = Integer.parseInt(data[4]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid age format for staff ID " + data[0] + ". Skipping this record.");
                    continue;
                }
                Staff staff = new Staff(data[0], data[1], data[2], data[3], age, data[5],data[6]);
                staffList.add(staff);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
        return staffList;
    }

    public String getPatientNameByID(String patientID) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length > 1 && data[0].trim().equals(patientID)) {
                    return data[1].trim();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file for patient ID: " + e.getMessage());
        }
        return null;
    }

    public String getDoctorNameByID(String doctorID) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length > 1 && data[0].trim().equals(doctorID)) {
                    return data[1].trim();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file for doctor ID: " + e.getMessage());
        }
        return null;
    }

    public List<String> getPatientIDsUnderDoctorCare(String doctorID) {
        List<String> patientIDs = new ArrayList<>();
        String filepath = "Diagnosis.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean isHeader = true;
    
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip the header row
                    continue;
                }
    
                String[] data = line.split(",");
                if (data.length > 3 && data[3].trim().equals(doctorID)) {
                    String patientID = data[1].trim(); // Get PatientID from the second column
                    if (!patientIDs.contains(patientID)) { // Avoid duplicates
                        patientIDs.add(patientID);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file for doctor care: " + e.getMessage());
        }
    
        return patientIDs;
    }

    public void getPatientInformation(String patientID) {
        String patientFilePath = "Patient List CSV.csv"; // Path to the patient data CSV file
    
        try (BufferedReader br = new BufferedReader(new FileReader(patientFilePath))) {
            String line;
            boolean isHeader = true;
    
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip the header row
                    continue;
                }
    
                String[] data = line.split(",");
                if (data.length >= 8 && data[0].trim().equals(patientID)) {
                    // Print patient information excluding the password (assuming password is the last field)
                    System.out.println("Patient ID        : " + data[0].trim());
                    System.out.println("Name              : " + data[1].trim());
                    System.out.println("Date of Birth     : " + data[2].trim());
                    System.out.println("Gender            : " + data[3].trim());
                    System.out.println("Blood Type        : " + data[4].trim());
                    System.out.println("Contact Info      : " + data[5].trim());
                    System.out.println("Phone Number      : " + data[6].trim());
                    System.out.println("Emergency Number  : " + data[7].trim());
                    System.out.println("");
                    return;
                }
            }
            System.out.println("No information found for Patient ID: " + patientID);
        } catch (IOException e) {
            System.out.println("Error reading patient CSV file: " + e.getMessage());
        }
    }

        public void updateDiagnosisRecord(String patientID, String diagnosisDescription, String prescription,
                                          String treatmentEndDate, String treatmentOutcome,
                                          String followUpInstructions, String notes) {
            String filepath = "Diagnosis.csv";
            List<String[]> records = new ArrayList<>();
    
            try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
                String line;
                boolean isHeader = true;
    
                while ((line = br.readLine()) != null) {
                    if (isHeader) {
                        isHeader = false; // Skip the header row for processing
                        if (!line.startsWith("DiagnosisID")) {
                            records.add(line.split(",")); // Add any existing first line if it's not a header
                        }
                        continue;
                    }
    
                    String[] record = line.split(",");
    
                    // If this record matches the given PatientID, update the specified fields
                    if (record[1].trim().equals(patientID)) {
                        if (diagnosisDescription != null && !diagnosisDescription.isEmpty()) {
                            record[5] = diagnosisDescription;
                        }
                        if (prescription != null && !prescription.isEmpty()) {
                            record[6] = prescription;
                        }
                        if (treatmentEndDate != null && !treatmentEndDate.isEmpty()) {
                            record[8] = treatmentEndDate;
                        }
                        if (treatmentOutcome != null && !treatmentOutcome.isEmpty()) {
                            record[9] = treatmentOutcome;
                        }
                        if (followUpInstructions != null && !followUpInstructions.isEmpty()) {
                            record[10] = followUpInstructions;
                        }
                        if (notes != null && !notes.isEmpty()) {
                            if (record.length > 11) {
                                record[11] = notes;
                            } else {
                                record = Arrays.copyOf(record, 12); // Extend array to include Notes field
                                record[11] = notes;
                            }
                        }
                    }
                    records.add(record);
                }
            } catch (IOException e) {
                System.out.println("Error reading Diagnosis CSV file: " + e.getMessage());
                return;
            }
    
            // Write updated records back to the Diagnosis.csv file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
                // Always write the header once
                writer.write("DiagnosisID,PatientID,PatientName,DoctorID,DiagnosisDate,DiagnosisDescription," +
                             "Prescription,TreatmentStartDate,TreatmentEndDate,TreatmentOutcome,FollowUpInstructions,Notes");
                writer.newLine();
    
                for (String[] record : records) {
                    writer.write(String.join(",", record));
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error saving updated diagnosis records: " + e.getMessage());
            }
        

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
                // Write header row only once
                writer.write("DiagnosisID,PatientID,PatientName,DoctorID,DiagnosisDate,DiagnosisDescription," +
                             "Prescription,TreatmentStartDate,TreatmentEndDate,TreatmentOutcome,FollowUpInstructions,Notes");
                writer.newLine();
    
                // Write all records
                for (String[] record : records) {
                    writer.write(String.join(",", record));
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error saving updated diagnosis records: " + e.getMessage());
            }
        
    }
    
    public boolean isPatientUnderDoctorCare(String patientID, String doctorID) {
        String filepath = "Diagnosis.csv"; // Ensure this path is accurate

        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Skip the header row
                    continue;
                }

                String[] data = line.split(",");
                
                // Check if patientID and doctorID match the respective columns (assuming 2nd and 4th positions)
                if (data.length > 3 && data[1].trim().equals(patientID) && data[3].trim().equals(doctorID)) {
                    return true; // Patient is under this doctor’s care
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Diagnosis CSV file: " + e.getMessage());
        }

        return false; // No matching record found
    }

    public List<String[]> getRecordsForPatientUnderDoctor(String patientID, String doctorID) {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Diagnosis.csv"))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header
                }
                String[] record = line.split(",");
                if (record[1].equals(patientID) && record[3].equals(doctorID)) {
                    records.add(record);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Diagnosis CSV file: " + e.getMessage());
        }
        return records;
    }
    
    public void updateDiagnosisRecordByID(String diagnosisID, String diagnosisDescription, String prescription,
                                          String treatmentEndDate, String treatmentOutcome,
                                          String followUpInstructions, String notes) {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Diagnosis.csv"))) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    records.add(line.split(",")); // Add header
                    continue;
                }
                String[] record = line.split(",");
                if (record[0].equals(diagnosisID)) {
                    // Update fields as needed
                    record[5] = diagnosisDescription;
                    record[6] = prescription;
                    record[8] = treatmentEndDate;
                    record[9] = treatmentOutcome;
                    record[10] = followUpInstructions;
                    record[11] = notes;
                }
                records.add(record);
            }
        } catch (IOException e) {
            System.out.println("Error reading Diagnosis CSV file: " + e.getMessage());
        }
    
        // Write updates back to CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Diagnosis.csv"))) {
            for (String[] record : records) {
                writer.write(String.join(",", record));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving Diagnosis CSV file: " + e.getMessage());
        }
    }
    public List<String[]> getDiagnosisAndPrescriptionByPatientID(String patientID) {
        List<String[]> result = new ArrayList<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader("Diagnosis.csv"))) {
            String line;
            br.readLine(); // Skip header
    
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
    
                // Check that the array has the expected number of columns
                if (data.length >= 12 && data[1].equals(patientID)) {
                    String[] diagnosisDetails = new String[5];
                    diagnosisDetails[0] = data[5]; // DiagnosisDescription
                    diagnosisDetails[1] = data[6]; // Prescription
                    diagnosisDetails[2] = data[4]; // Date of Diagnosis
                    diagnosisDetails[3] = data[7]; // Treatment Start Date
                    diagnosisDetails[4] = data[8]; // Treatment End Date
                    result.add(diagnosisDetails);
                } else if (data.length < 12) {
                    System.out.println("Skipping record due to insufficient data: " + Arrays.toString(data));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    
        return result;
    }
    
    public List<String[]> getDiagnosisAndPrescriptionByPatientID(String patientID, String doctorID) {
        List<String[]> result = new ArrayList<>();
    
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            // Read the header line and ignore it
            br.readLine();
    
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
    
                // Check if the line matches the given PatientID and DoctorID
                if (data.length > 4 && data[1].equals(patientID) && data[3].equals(doctorID)) {
                    String[] diagnosisDetails = new String[3];
                    diagnosisDetails[0] = data[5]; // DiagnosisDescription
                    diagnosisDetails[1] = data[6]; // Prescription
                    diagnosisDetails[2] = data[4]; // Date of diagnosis
    
                    result.add(diagnosisDetails);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    
        return result;
    }
    

    public void updateDoctorNameInCSV(String doctorID, String newDoctorName, String csvFilePath, int idIndex, int nameIndex) {
        List<String[]> records = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            boolean isHeader = true;
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                
                // Add header directly if it's the first line
                if (isHeader) {
                    isHeader = false;
                    records.add(data);
                    continue;
                }
                
                // Check if the doctor ID matches the given ID
                if (data.length > idIndex && data[idIndex].trim().equals(doctorID)) {
                    // Update the doctor's name
                    data[nameIndex] = newDoctorName;
                }
                
                // Add the (potentially modified) record to the list
                records.add(data);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
            return;
        }

        // Write the updated data back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            for (String[] record : records) {
                writer.write(String.join(",", record));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
        
       // System.out.println("Doctor's name is updated!");
    }

    public void updateDoctorNameInAllCSVs(String doctorID, String newDoctorName) {
        // Update in appointments.csv (index 4 is doctor ID, 5 is doctor name)
        updateDoctorNameInCSV(doctorID, newDoctorName, "appointments.csv", 4, 5);

        // Update in Diagnosis.csv (index 2 is doctor ID, 3 is doctor name)
        updateDoctorNameInCSV(doctorID, newDoctorName, "Diagnosis.csv", 2, 3);

        // Update in Staff_List.csv (index 1 is doctor ID, 2 is doctor name)
        updateDoctorNameInCSV(doctorID, newDoctorName, "Staff_List.csv", 1, 2);

        // Update in To be To be prescribed.csv (index 3 is doctor ID, 4 is doctor name)
        updateDoctorNameInCSV(doctorID, newDoctorName, "To be prescribed.csv", 3, 4);
    }

    

    
    
    
}
