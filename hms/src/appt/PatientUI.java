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
            System.out.println("4. View Canceled Appointments");
            System.out.println("5. View Completed Appointment Outcomes");
            System.out.println("6. View Upcoming Sessions"); // New option for upcoming sessions
            System.out.println("7. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> apptController.printPatientAppointments(patientID); // View all appointments
                case 2 -> bookNewAppointment(); // Book a new appointment using patient and doctor info
                case 3 -> viewPendingAppointments(); // View pending appointments
                case 4 -> viewCanceledAppointments(); // View canceled appointments
                case 5 -> viewCompletedOutcomes(); // View completed appointment outcomes
                case 6 -> viewUpcomingSessions(); // View upcoming sessions
                case 7 -> {
                    running = false;
                    System.out.println("Goodbye, " + patientName + "!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to book a new appointment, prompting for doctor details
    private void bookNewAppointment() {
        System.out.print("Enter Doctor ID: ");
        String doctorID = scanner.nextLine().trim();

        System.out.print("Enter Doctor Name: ");
        String doctorName = scanner.nextLine().trim();

        // Call ApptController's bookNewAppointment with patient and doctor details
        apptController.bookNewAppointment(patientID, patientName, doctorID, doctorName);
    }

    // Method to view pending appointments with status 'PendingToPatient'
    private void viewPendingAppointments() {
        System.out.println("\nViewing Pending Appointments:");
        apptController.viewRequests("patient", patientID); // Pass "patient" as the userType
    }

    // Method to view canceled appointments
    private void viewCanceledAppointments() {
        System.out.println("\nViewing Canceled Appointments:");
        apptController.printCancelledAppointments("patient", patientID); // Pass "patient" as the userType
    }

    // Method to view completed appointment outcomes
    private void viewCompletedOutcomes() {
        System.out.println("\nViewing Completed Appointment Outcomes:");
        apptController.printCompletedSessionsPatient(patientID); // Calls method to print completed sessions
    }

    // New method to view upcoming sessions and manage appointments
    private void viewUpcomingSessions() {
        System.out.println("\nViewing Upcoming Sessions:");
        apptController.printUpcomingPatientSessions(patientID, "patient"); // Call to view upcoming patient sessions
    }

    public static void main(String[] args) {
        ApptData apptData = new ApptData();
        ApptController apptController = new ApptController();

        // Sample patient details for testing
        String patientID = "PT002";
        String patientName = "Hud";

        PatientUI patientUI = new PatientUI(apptController, patientID, patientName);
        patientUI.start();
    }
}
