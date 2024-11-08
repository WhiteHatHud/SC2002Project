package appt;
import java.util.Scanner;
public class AdminUI {
    
    Scanner scanner = new Scanner(System.in);
    ApptController apptController = new ApptController();
    
        public AdminUI() {
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
}
