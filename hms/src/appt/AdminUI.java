package appt;

import java.util.Scanner;
import Login.DisplayManager;

/**
 * Provides the user interface for an administrator to view and manage appointment schedules.
 * The admin can view schedules by doctor, by patient, or view all booked schedules.
 */
public class AdminUI {

    /** Scanner for capturing user input. */
    private Scanner scanner = new Scanner(System.in);

    /** Controller for handling appointment-related actions. */
    private ApptController apptController = new ApptController();

    /**
     * Constructs an AdminUI instance.
     */
    public AdminUI() {
    }

    /**
     * Starts the admin interface, presenting options for viewing schedules.
     * The admin can choose to view schedules by doctor, by patient, or view all booked schedules.
     */
    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\nWelcome, Admin");
            System.out.println("1. View by Doctor Schedule");
            System.out.println("2. View by Patient Schedule");
            System.out.println("3. View All Booked Schedules");
            System.out.println("4. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewByDoctorSchedule();
                case 2 -> viewByPatientSchedule();
                case 3 -> viewAllBookedSchedules();
                case 4 -> {
                    running = false;
                    System.out.println("Goodbye, Admin!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Allows the admin to view appointments by a specific doctor's schedule.
     * Prompts for the doctor ID and displays the schedule.
     */
    private void viewByDoctorSchedule() {
        System.out.print("\nEnter Doctor ID: ");
        String doctorID = scanner.nextLine().trim();
        apptController.printDoctorScheduleOnDateAdmin(doctorID);
        DisplayManager.pauseContinue();
    }

    /**
     * Allows the admin to view appointments by a specific patient's schedule.
     * Prompts for the patient ID and displays the appointments.
     */
    private void viewByPatientSchedule() {
        System.out.print("\nEnter Patient ID: ");
        String patientID = scanner.nextLine().trim();
        apptController.printPatientAppointments(patientID);
        DisplayManager.pauseContinue();
    }

    /**
     * Displays all booked schedules.
     * Calls the appointment controller to retrieve and print all schedules.
     */
    private void viewAllBookedSchedules() {
        apptController.printAllBookedSchedules();
        DisplayManager.pauseContinue();
    }
}
