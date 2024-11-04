package MedicalHistory;

public class MedicalDoctorUI {
        public static void main(String[] args) {
        MedicalData medicalData = new MedicalData();
        medicalData.loadPatientsFromCSV("Patient LIST CSV.csv");

        // Print loaded patients for verification
        medicalData.printPatients();
    }
}