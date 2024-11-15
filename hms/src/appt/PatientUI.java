package appt;

import java.util.InputMismatchException;
import java.util.Scanner;
import Login.DisplayManager;
import Patients.PatientShared;

/**
 * The PatientUI class provides a user interface for patients to view, manage, 
 * and book appointments. It interacts with the ApptController to manage appointments 
 * and provides various options for patients to interact with their schedules.
 */
public class PatientUI {
    
    private String patientID;
    private String patientName;
    ApptController apptController = new ApptController();
    Scanner scanner = new Scanner(System.in);

    /**
     * Constructs a PatientUI with the specified patient ID and patient name.
     *
     * @param patientID   The ID of the patient.
     * @param patientName The name of the patient.
     */
    public PatientUI(String patientID, String patientName) {
        this.patientID = patientID;
        this.patientName = patientName;
    }

    /**
     * Starts the patient interface, displaying a menu for various appointment-related 
     * options that the patient can select.
     */
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
            System.out.println("7. Reschedule/Cancel Appointments");
            System.out.println("8. Exit");
            System.out.print("Please select an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> printPatientAppointments();
                    case 2 -> bookNewAppointment();
                    case 3 -> viewPendingAppointments();
                    case 4 -> viewCanceledAppointments();
                    case 5 -> viewCompletedOutcomes();
                    case 6 -> viewUpcomingSessions();
                    case 7 -> rescheduleOrCancelAppointment();
                    case 8 -> {
                        running = false;
                        System.out.println("Goodbye, " + patientName + "!");
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number corresponding to a menu option.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    /**
     * Prints the patient's appointments.
     */
    private void printPatientAppointments() {
        apptController.printPatientAppointments(patientID);
        DisplayManager.pauseContinue();
    }

    /**
     * Books a new appointment by prompting for doctor details.
     */
    private void bookNewAppointment() {
        System.out.print("Enter Doctor ID: ");
        String doctorID = scanner.nextLine().trim();

        String doctorName = PatientShared.getCSVUtilities().getDoctorNameByID(doctorID);
        if (doctorName == null || doctorName.isEmpty()) {
            System.out.println("Error: Doctor ID not found. Please check the ID and try again.");
            DisplayManager.pauseContinue();
            return;
        }
        System.out.println("The Doctor name is: " + doctorName);

        apptController.bookNewAppointment(patientID, patientName, doctorID, doctorName, "Patient");
        DisplayManager.pauseContinue();
    }

    /**
     * Views pending appointments with status 'PendingToPatient'.
     */
    private void viewPendingAppointments() {
        System.out.println("\nShowing Pending Appointments:");
        apptController.viewRequests("patient", patientID);
        DisplayManager.pauseContinue();
    }

    /**
     * Views canceled appointments.
     */
    private void viewCanceledAppointments() {
        System.out.println("\nViewing Canceled Appointments:");
        apptController.printCancelledAppointments("patient", patientID);
        DisplayManager.pauseContinue();
    }

    /**
     * Views the outcomes of completed appointments.
     */
    private void viewCompletedOutcomes() {
        System.out.println("\nViewing Completed Appointment Outcomes:");
        apptController.printCompletedSessionsPatient(patientID);
        DisplayManager.pauseContinue();
    }

    /**
     * Views upcoming sessions and manages appointments.
     */
    private void viewUpcomingSessions() {
        System.out.println("\nViewing Upcoming Sessions:");
        apptController.printUpcomingPatientSessions(patientID, "patient");
        DisplayManager.pauseContinue();
    }

    /**
     * Reschedules or cancels an appointment.
     */
    private void rescheduleOrCancelAppointment() {
        System.out.println("\nReschedule or Cancel Appointments:");
        apptController.cancelOrRescheduleAppointment(patientID);
        DisplayManager.pauseContinue();
    }
}
