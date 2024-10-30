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
            System.out.println("3. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> apptController.printDoctorScheduleOnDate(doctorID);
                case 2 -> apptController.printUpcomingSessions(doctorID);
                case 3 -> {
                    running = false;
                    System.out.println("Goodbye, Dr. " + doctorName + "!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        ApptData apptData = new ApptData();
        ApptController apptController = new ApptController(apptData);

        // Sample doctor details for testing
        String doctorID = "DR001";
        String doctorName = "Smith";

        DoctorUI doctorUI = new DoctorUI(apptController, doctorID, doctorName);
        doctorUI.start();
    }
}
