package appt;

import java.util.List;
import java.util.Scanner;

public class DoctorUI {
    private ApptController apptController;
    private Scanner scanner;
    private String doctorID;
    private String doctorName;

    // Constructor
    public DoctorUI(ApptController apptController, String doctorID, String doctorName) {
        this.apptController = apptController;
        this.scanner = new Scanner(System.in);
        this.doctorID = doctorID;
        this.doctorName = doctorName;
    }

    // Main menu for the Doctor UI
    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\nWelcome, Dr. " + doctorName);
            System.out.println("1. Create an Appointment");
            System.out.println("2. View Schedule");
            System.out.println("3. Reschedule an Appointment");
            System.out.println("4. Cancel an Appointment");
            System.out.println("5. Record Appointment Outcome");
            System.out.println("6. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createAppointment();
                    break;
                case 2:
                    viewSchedule();
                    break;
                case 3:
                    rescheduleAppointment();
                    break;
                case 4:
                    cancelAppointment();
                    break;
                case 5:
                    recordAppointmentOutcome();
                    break;
                case 6:
                    running = false;
                    System.out.println("Goodbye, Dr. " + doctorName + "!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to create a new appointment
    private void createAppointment() {
        System.out.println("\nEnter Appointment ID:");
        String appointmentID = scanner.nextLine();
        System.out.println("Enter Date (YYYY-MM-DD):");
        String date = scanner.nextLine();
        System.out.println("Enter Time (e.g., 09:00 AM):");
        String time = scanner.nextLine();
        System.out.println("Enter Patient ID:");
        String patientID = scanner.nextLine();
        System.out.println("Enter Patient Name:");
        String patientName = scanner.nextLine();

        apptController.scheduleDoctorAppointment(appointmentID, date, time, patientID, patientName, doctorID, doctorName);
    }

    // Method to view the doctor's schedule
    private void viewSchedule() {
        System.out.println("\nViewing schedule for Dr. " + doctorName + ":");
        List<Appointment> appointments = apptController.viewAppointmentsByDoctor(doctorID);
        for (Appointment appointment : appointments) {
            System.out.println("Appointment ID: " + appointment.getAppointmentID());
            System.out.println("Date: " + appointment.getDate());
            System.out.println("Time: " + appointment.getTime());
            System.out.println("Patient ID: " + appointment.getPatientID());
            System.out.println("Patient Name: " + appointment.getPatientName());
            System.out.println("Outcome: " + (appointment.getOutcome() == null ? "Pending" : appointment.getOutcome()));
            System.out.println("-------------------------------");
        }
    }

    // Method to reschedule an existing appointment
    private void rescheduleAppointment() {
        System.out.println("\nEnter Appointment ID to Reschedule:");
        String appointmentID = scanner.nextLine();
        System.out.println("Enter New Date (YYYY-MM-DD):");
        String newDate = scanner.nextLine();
        System.out.println("Enter New Time (e.g., 09:00 AM):");
        String newTime = scanner.nextLine();

        apptController.rescheduleDoctorAppointment(appointmentID, newDate, newTime);
    }

    // Method to cancel an appointment
    private void cancelAppointment() {
        System.out.println("\nEnter Appointment ID to Cancel:");
        String appointmentID = scanner.nextLine();
        apptController.cancelDoctorAppointment(appointmentID);
    }

    // Method to record the outcome of an appointment
    private void recordAppointmentOutcome() {
        System.out.println("\nEnter Appointment ID to Record Outcome:");
        String appointmentID = scanner.nextLine();
        System.out.println("Enter Outcome:");
        String outcome = scanner.nextLine();
        apptController.recordOutcome(appointmentID, outcome);
    }

    // Main method to test DoctorUI
    public static void main(String[] args) {
        ApptData apptData = new ApptData(); // Removed the incorrect constructor parameter
        ApptController controller = new ApptController(apptData);

        // Assuming doctorID and doctorName are known beforehand
        String doctorID = "DR001";
        String doctorName = "Smith";

        DoctorUI doctorUI = new DoctorUI(controller, doctorID, doctorName);
        doctorUI.start();
    }
}
