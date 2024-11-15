package appt;

import Doctors.DoctorShared;
import Login.DisplayManager;
import java.util.InputMismatchException;

/**
 * The DoctorUI class provides a user interface for doctors, allowing them to view, edit, 
 * and manage their schedules and appointments. This class interacts with the ApptController 
 * to access and manipulate appointment data.
 */
public class DoctorUI {

    private String doctorID;
    private String doctorName;
    ApptController apptController = new ApptController();

    /**
     * Constructs a DoctorUI with the specified doctor ID and doctor name.
     *
     * @param doctorID   The ID of the doctor.
     * @param doctorName The name of the doctor.
     */
    public DoctorUI(String doctorID, String doctorName) {
        this.doctorID = doctorID;
        this.doctorName = doctorName;
    }

    /**
     * Starts the doctor interface, presenting a menu with various options 
     * to view, edit, and manage appointments.
     */
    public void start() {
        boolean running = true;
        while (running) {
            apptController.updateCompletedSessions();
            System.out.println("Welcome, Dr. " + doctorName);
            System.out.println("1. View/Edit Personal Schedule");
            System.out.println("2. Accept/Decline Appointment Requests");
            System.out.println("3. View Upcoming Appointments");
            System.out.println("4. View Cancelled Appointments by Patients");
            System.out.println("5. Manage Appointment Outcome");
            System.out.println("6. Back to Menu"); // Option to go back to DoctorController
            System.out.print("Please select an option: ");

            int choice = DoctorShared.getUserInputHandler().getUserChoice();

            switch (choice) {
                case 1 -> printDoctorScheduleOnDate();
                case 2 -> viewPendingAppointments();
                case 3 -> printUpcomingSessions();
                case 4 -> viewCanceledAppointments();
                case 5 -> recordAppointmentOutcome();
                case 6 -> {
                    running = false; // Exit loop to return to DoctorController
                }
                default -> System.out.println("Invalid input. Please enter a number corresponding to a menu option.");
            }
        }
    }

    /**
     * Prints the doctor's schedule on a specified date.
     */
    private void printDoctorScheduleOnDate() {
        apptController.printDoctorScheduleOnDate(doctorID);
        DisplayManager.pauseContinue();
    }

    /**
     * Views pending appointment requests that require the doctor's action.
     */
    private void viewPendingAppointments() {
        System.out.println("\nViewing Pending Appointments:");
        apptController.viewRequests("doctor", doctorID);
        DisplayManager.pauseContinue();
    }

    /**
     * Views canceled appointments for the doctor.
     */
    private void viewCanceledAppointments() {
        System.out.println("\nViewing Canceled Appointments:");
        apptController.printCancelledAppointments("doctor", doctorID);
        DisplayManager.pauseContinue();
    }

    /**
     * Prints the upcoming appointments for the doctor.
     */
    private void printUpcomingSessions() {
        apptController.printUpcomingSessions(doctorID, "doctor");
        DisplayManager.pauseContinue();
    }

    /**
     * Records the outcome for completed sessions, allowing the doctor to update details.
     */
    private void recordAppointmentOutcome() {
        System.out.println("\nRecording Outcome for Completed Sessions:");
        System.out.println("1. Update New Completed Sessions");
        System.out.print("Please select an option: ");
        
        int choice;
    
        try {
            choice = DoctorShared.getUserInputHandler().getUserChoice(); // Get user input for the choice
    
            switch (choice) {
                case 1 -> {
                    System.out.println("Updating New Completed Sessions...");
                    apptController.manageCompletedSessions(doctorID, false); // Pass false to update new sessions
                }

                default -> System.out.println("Invalid choice. Returning to the main menu...");
            }
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }
        DisplayManager.pauseContinue();
    }
}
