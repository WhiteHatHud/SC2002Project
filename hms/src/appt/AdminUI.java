package appt;
import java.util.Scanner;

import Login.DisplayManager;

public class AdminUI {

    Scanner scanner = new Scanner(System.in);
    ApptController apptController = new ApptController();

    public AdminUI() {
    }

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

    // Method to view appointments by Doctor's schedule
    private void viewByDoctorSchedule() {
        System.out.print("\nEnter Doctor ID: ");
        String doctorID = scanner.nextLine().trim();
        apptController.printDoctorScheduleOnDateAdmin(doctorID);
        DisplayManager.pauseContinue();
    }

    // Method to view appointments by Patient's schedule
    private void viewByPatientSchedule() {
        System.out.print("\nEnter Patient ID: ");
        String patientID = scanner.nextLine().trim();
        apptController.printPatientAppointments(patientID);
        DisplayManager.pauseContinue();
    }

    // Method to view all booked schedules
    private void viewAllBookedSchedules() {
        apptController.printAllBookedSchedules();
        DisplayManager.pauseContinue();
    }
}
