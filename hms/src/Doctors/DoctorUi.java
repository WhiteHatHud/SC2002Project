package Doctors;  // Package declaration

import hms.appointmentpkg.Appointment; // Import Appointment class
import java.util.*;  // Import utilities like Scanner and List


// Main class for Doctor UI
public class DoctorUi {
    // Scanner object to take input from the user
    private Scanner scanner = new Scanner(System.in);

    // Dummy medical records with patient ID mapped to medical records as placeholders
    private Map<Integer, String> medicalRecords = new HashMap<>() {{
        put(101, "Diagnosis: Flu. Prescription: Paracetamol.");
        put(102, "Diagnosis: Migraine. Treatment: Rest and hydration.");
    }};

    // Dummy list of appointments with placeholder data including time
private List<Appointment> appointments = new ArrayList<>() {{
    add(new Appointment(1, "2024-10-20", "10:30 AM"));
    add(new Appointment(2, "2024-10-22", "02:00 PM"));
}};


    // Main method to start the program
    public static void main(String[] args) {
        DoctorUi doctorUi = new DoctorUi();  // Create an instance of the UI
        doctorUi.run();  // Run the UI
    }

    // Main loop to display options and take user input
    public void run() {
        int selection;  // Store user's selection

        // Keep displaying the menu until the user chooses to quit
        do {
            System.out.println("(1) View Medical Records");
            System.out.println("(2) Update Medical Record");
            System.out.println("(3) View Schedule and Availability");
            System.out.println("(4) Manage Appointments");
            System.out.println("(5) Record Appointment Outcome");
            System.out.println("(6) Quit");
            System.out.print("Choose an option: ");
            selection = scanner.nextInt();  // Read user input

            // Handle the user's selection using a switch statement
            switch (selection) {
                case 1:
                    viewMedicalRecords();  // Option to view medical records
                    break;
                case 2:
                    updateMedicalRecord();  // Option to update a medical record
                    break;
                case 3:
                    viewSchedule();  // Option to view doctor's schedule
                    break;
                case 4:
                    manageAppointments();  // Option to manage appointments
                    break;
                case 5:
                    recordAppointmentOutcome();  // Option to record appointment outcomes
                    break;
            }
        } while (selection != 6);  // Loop until the user chooses to quit
    }

    // (1) View Medical Records
    public void viewMedicalRecords() {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();  // Get patient ID input

        // Check if the patient has a record, else show a default message
        if (medicalRecords.containsKey(patientId)) {
            System.out.println("Medical Record for Patient " + patientId + ":");
            System.out.println(medicalRecords.get(patientId));  // Display the record
        } else {
            System.out.println("No record found for this patient.");  // Placeholder message
        }
    }

    // (2) Update Medical Record
    public void updateMedicalRecord() {
        System.out.print("Enter Patient ID: ");
        int patientId = scanner.nextInt();  // Get patient ID input
        scanner.nextLine();  // Consume leftover newline

        System.out.println("Enter new diagnosis or treatment: ");
        String newRecord = scanner.nextLine();  // Get new record input

        // Append new info to the existing record or create a new one
        String existingRecord = medicalRecords.getOrDefault(patientId, "");
        medicalRecords.put(patientId, existingRecord + "\n" + newRecord);

        System.out.println("Medical record updated successfully.");  // Confirmation message
    }

    // (3) View Schedule and Availability
    public void viewSchedule() {
        System.out.println("Upcoming Appointments:");
        for (Appointment appointment : appointments) {
            System.out.println(appointment);  // Print each appointment
        }
    }

    // (4) Manage Appointments
    public void manageAppointments() {
        System.out.print("(1) Accept Appointment\n(2) Decline Appointment\nChoose an option: ");
        int choice = scanner.nextInt();  // Get user choice

        System.out.print("Enter Appointment ID: ");
        int appId = scanner.nextInt();  // Get appointment ID input

        // Find the appointment by ID and update its status
        Optional<Appointment> appointment = findAppointmentById(appId);
        if (appointment.isPresent()) {
            if (choice == 1) {
                appointment.get().setStatus("Accepted");
                System.out.println("Appointment accepted.");
            } else {
                appointment.get().setStatus("Declined");
                System.out.println("Appointment declined.");
            }
        } else {
            System.out.println("Appointment not found.");  // Placeholder message
        }
    }

    // (5) Record Appointment Outcome
    public void recordAppointmentOutcome() {
        System.out.print("Enter Appointment ID: ");
        int appId = scanner.nextInt();  // Get appointment ID input
        scanner.nextLine();  // Consume leftover newline

        Optional<Appointment> appointment = findAppointmentById(appId);
        if (appointment.isPresent()) {
            System.out.print("Enter service provided: ");
            String service = scanner.nextLine();  // Get service input

            System.out.print("Enter prescribed medication: ");
            String medication = scanner.nextLine();  // Get medication input

            appointment.get().setOutcome(service, medication);
            System.out.println("Appointment outcome recorded.");  // Confirmation message
        } else {
            System.out.println("Appointment not found.");  // Placeholder message
        }
    }

    // Helper method to find an appointment by ID
    private Optional<Appointment> findAppointmentById(int id) {
        return appointments.stream().filter(a -> a.getId() == id).findFirst();
    }
}