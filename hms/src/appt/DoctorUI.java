package appt;

import java.util.Scanner;

public class DoctorUI {
    private ApptController apptController;
    private Scanner scanner;
    private String doctorID;
    private String doctorName;

    public DoctorUI(ApptController apptController, String doctorID, String doctorName) {
        this.apptController = apptController;
        this.scanner = new Scanner(System.in);
        this.doctorID = doctorID;
        this.doctorName = doctorName;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\nWelcome, Dr. " + doctorName);
            System.out.println("1. View Schedule");
            System.out.println("2. View Upcoming Sessions");
            System.out.println("3. View Pending Requests");
            System.out.println("4. Update Completed Sessions");  // New option
            System.out.println("5. Exit");
            System.out.print("Please select an option: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> apptController.printDoctorScheduleOnDate(doctorID);
                case 2 -> apptController.printUpcomingSessions(doctorID, "doctor");
                case 3 -> viewPendingAppointments();
                case 4 -> apptController.updateCompletedSessions();  // Calls the new method
                case 5 -> {
                    running = false;
                    System.out.println("Goodbye, Dr. " + doctorName + "!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // New method to view pending requests

    private void viewPendingAppointments() {
        System.out.println("\nViewing Pending Appointments:");
        apptController.viewRequests("doctor", doctorID); // Pass "patient" as the userType
    }

}
