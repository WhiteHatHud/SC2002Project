package appt;

import java.util.Scanner;

public class PatientUI {
    private ApptController apptController;
    private Scanner scanner;
    private String patientID;
    private String patientName;

    public PatientUI(ApptController apptController, String patientID, String patientName) {
        this.apptController = apptController;
        this.scanner = new Scanner(System.in);
        this.patientID = patientID;
        this.patientName = patientName;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\nWelcome, " + patientName);
            System.out.println("1. View My Appointments");
            System.out.println("2. Book a New Appointment");
            System.out.println("3. View Pending Appointments");
            System.out.println("4. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> apptController.printPatientAppointments(patientID); // View all appointments
                case 2 -> apptController.bookNewAppointment(patientID, patientName); // Book a new appointment
                case 3 -> viewPendingAppointments(); // View pending appointments
                case 4 -> {
                    running = false;
                    System.out.println("Goodbye, " + patientName + "!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to view pending appointments with status 'PendingToPatient'
    private void viewPendingAppointments() {
        System.out.println("\nViewing Pending Appointments:");
        apptController.viewRequests("patient", patientID); // Pass "patient" as the userType
    }

    public static void main(String[] args) {
        ApptData apptData = new ApptData();
        ApptController apptController = new ApptController(apptData);

        // Sample patient details for testing
        String patientID = "PT002";
        String patientName = "Hud";

        PatientUI patientUI = new PatientUI(apptController, patientID, patientName);
        patientUI.start();
    }
}
