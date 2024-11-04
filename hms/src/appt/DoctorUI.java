package appt;

import Doctors.DoctorShared;
import java.util.Scanner;

public class DoctorUI {
    
    private Scanner scanner;
    private String doctorID;
    private String doctorName;
    ApptController apptController = new ApptController();
    
    public DoctorUI(String doctorID, String doctorName) {
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        start();
    }


    
    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\nWelcome, Dr. " + doctorName);
            System.out.println("1. View/Edit Personal Schedule");
            System.out.println("2. Accept/Decline Appointment Requests");
            System.out.println("3. View Upcoming Appointments");
            System.out.println("4. View Cancelled Appointments by Patients");
            System.out.println("5. Record Appointment Outcome");
            System.out.println("6. Logout");
            System.out.print("Please select an option: ");

            int choice = DoctorShared.getUserInputHandler().getUserChoice();

            switch (choice) {
                case 1 -> apptController.printDoctorScheduleOnDate(doctorID);
                case 2 -> viewPendingAppointments();
                case 3 -> apptController.printUpcomingSessions(doctorID, "doctor");
                case 4 -> viewCanceledAppointments();
                case 5 -> recordAppointmentOutcome(); // New method for recording outcome
                case 6 -> {
                    running = false;
                    System.out.println("Goodbye, Dr. " + doctorName + "!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to view pending requests
    private void viewPendingAppointments() {
        System.out.println("\nViewing Pending Appointments:");
        apptController.viewRequests("doctor", doctorID); // Pass "doctor" as the userType
    }

    // Method to view canceled appointments
    private void viewCanceledAppointments() {
        System.out.println("\nViewing Canceled Appointments:");
        apptController.printCancelledAppointments("doctor", doctorID); // Pass "doctor" as the userType
    }

    // New method to record the outcome of completed appointments
    private void recordAppointmentOutcome() {
        System.out.println("\nRecording Outcome for Completed Sessions:");
        apptController.updateCompletedSessions();
        apptController.fillCompletedSessions(doctorID); // Calls fillCompletedSessions method in ApptController
    }

}
