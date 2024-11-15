package MedicalHistory;

/**
 * The {@code MedicalDoctorUI} class serves as the user interface for loading and displaying 
 * patient data for medical history records. It loads patient information from a CSV file 
 * and prints the loaded data for verification.
 */
public class MedicalDoctorUI {

    /**
     * The main method serves as the entry point of the program. It initializes the 
     * {@code MedicalData} object, loads patient data from a specified CSV file, 
     * and prints the loaded data to verify successful loading.
     *
     * @param args command-line arguments (not used in this program)
     */
    public static void main(String[] args) {
        MedicalData medicalData = new MedicalData();
        
        // Load patient data from a specified CSV file
        medicalData.loadPatientsFromCSV("Patient LIST CSV.csv");

        // Print loaded patients for verification
        medicalData.printPatients();
    }
}
