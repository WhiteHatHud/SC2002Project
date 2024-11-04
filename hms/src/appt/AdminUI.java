package appt;

import java.util.Scanner;

public class AdminUI {
    private ApptController apptController;
    private Scanner scanner;

    public AdminUI(ApptController apptController) {
        this.apptController = apptController;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\nWelcome, Admin");
            System.out.println("1. View/Edit Doctor's Schedule");
            System.out.println("2. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> manageDoctorSchedule();
                case 2 -> {
                    running = false;
                    System.out.println("Goodbye, Admin!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Method for admin to select a doctor and manage their schedule.
     */
    private void manageDoctorSchedule() {
        System.out.print("\nEnter Doctor ID: ");
        String doctorID = scanner.nextLine().trim();

        // Call the method from ApptController to view and edit the doctor's schedule
        apptController.printDoctorScheduleOnDateAdmin(doctorID);
    }

    public static void main(String[] args) {
        ApptData apptData = new ApptData();
        ApptController apptController = new ApptController(apptData);

        // Create the Admin UI and start the session
        AdminUI adminUI = new AdminUI(apptController);
        adminUI.start();
    }
}
