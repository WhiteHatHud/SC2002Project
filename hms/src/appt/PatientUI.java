package appt;

import java.util.Scanner;

import Doctors.DoctorShared;
import Patients.PatientShared;

public class PatientUI {
    private String patientID;
    private String patientName;
    ApptController apptController = new ApptController();
    Scanner scanner = new Scanner(System.in);

    public PatientUI( String patientID, String patientName) {
        this.patientID = patientID;
        this.patientName = patientName;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome, " + patientName);
            System.out.println("1. View My Appointments");
            System.out.println("2. Book a New Appointment");
            System.out.println("3. Accept/Decline Appointments");
            System.out.println("4. View Canceled Appointments");
            System.out.println("5. View Completed Appointment Outcomes");
            System.out.println("6. View Upcoming Sessions");
            System.out.println("7. Reschedule/Cancel Appointments"); // New option for rescheduling/canceling
            System.out.println("8. Exit");
            System.out.print("Please select an option: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (choice) {
                case 1 -> apptController.printPatientAppointments(patientID);
                case 2 -> bookNewAppointment();
                case 3 -> viewPendingAppointments();
                case 4 -> viewCanceledAppointments();
                case 5 -> viewCompletedOutcomes();
                case 6 -> viewUpcomingSessions();
                case 7 -> rescheduleOrCancelAppointment(); // New case for rescheduling or canceling appointments
                case 8 -> {
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

        String doctorName = PatientShared.getCSVUtilities().getDoctorNameByID(doctorID);
        if (doctorName == null || doctorName.isEmpty()) {
            System.out.println("Error: Doctor ID not found. Please check the ID and try again.");
            return; // Exit if the patient does not exist
        }
        System.out.println("The Doctor name is: " + doctorName);

        // Call ApptController's bookNewAppointment with patient and doctor details
        apptController.bookNewAppointment(patientID, patientName, doctorID, doctorName, "Patient");
    }

    // Method to view pending appointments with status 'PendingToPatient'
    private void viewPendingAppointments() {
        System.out.println("\nShowing Pending Appointments:");
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

    // Method to reschedule or cancel an appointment
private void rescheduleOrCancelAppointment() {
    System.out.println("\nReschedule or Cancel Appointments:");
    apptController.cancelOrRescheduleAppointment(patientID);
}


}
