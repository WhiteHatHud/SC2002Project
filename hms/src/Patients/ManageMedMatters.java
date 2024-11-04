package Patients;

import appt.ApptController;
import java.util.Scanner;

public class ManageMedMatters {
    private Patient patient;
    private ApptController apptController;
    private Scanner scanner;

    public ManageMedMatters(Patient patient) {
        this.patient = patient;
        this.apptController = new ApptController();  
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean isManaging = true;
        
        while (isManaging) {
            PatientShared.getDisplayManager().apptMenu();
            int choice = PatientShared.getUserInputHandler().getUserChoice();
            switch (choice) {
                case 1:
                    scheduleAppointment();
                    break;
                case 2:
                    rescheduleAppointment();
                    break;
                    /* 
                case 3:
                    cancelAppointment();
                    break;
                case 4:
                    viewScheduled();
                    break;
                    
                case 5:
                    viewPastOutcome();
                    */
                case 6:
                    System.out.println("Returning to the previous menu...");
                    isManaging = false; // Exits the loop and returns to the previous menu
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;


            }
        }
    }

    private void scheduleAppointment() {
        System.out.println("\n--- Schedule an Appointment ---");
    
        System.out.print("Enter Doctor ID for Appointment: ");
        String doctorID = scanner.nextLine().trim();
    
        // Use CSVUtilities to get the doctor’s name by ID
        String doctorName = PatientShared.getCSVUtilities().getDoctorNameByID(doctorID);
    
        
        if (doctorName == null) {
            System.out.println("Invalid Doctor ID. Please try again.");
            return; 
        }
        System.out.println("You will be making an apppointment with Dr." + doctorName);

        apptController.bookNewAppointment(patient.getUserID(), patient.getName(), doctorID, doctorName);

    }

    private void rescheduleAppointment() {
        apptController.rescheduleAppointmentByPatient(patient.getUserID());

    }
/* 
    private void cancelAppointment() {
        System.out.println("\n--- Cancel an Appointment ---");

        // Fetch scheduled appointments
        List<Appointment> appointments = apptController.getScheduledAppointments(patient.getUserID());
        if (appointments.isEmpty()) {
            System.out.println("You have no scheduled appointments to cancel.");
            return;
        }

        // Display appointments
        for (int i = 0; i < appointments.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, appointments.get(i).getSummary());
        }

        // Select an appointment to cancel
        System.out.print("Enter the number of the appointment to cancel: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        if (choice < 1 || choice > appointments.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        // Use ApptController to cancel appointment
        Appointment selectedAppointment = appointments.get(choice - 1);
        boolean success = apptController.cancelAppointment(selectedAppointment.getAppointmentID());

        if (success) {
            System.out.println("Appointment successfully canceled.");
        } else {
            System.out.println("Failed to cancel appointment. Please try again.");
        }
    }

    private void viewScheduledAppointments() {
        System.out.println("\n--- Scheduled Appointments ---");

        // Fetch scheduled appointments
        List<Appointment> appointments = apptController.getScheduledAppointments(patient.getUserID());
        if (appointments.isEmpty()) {
            System.out.println("No scheduled appointments found.");
            return;
        }

        // Display each scheduled appointment
        for (Appointment appointment : appointments) {
            System.out.println(appointment.getSummary());
        }
    }

    private void viewPastAppointmentOutcomes() {
        System.out.println("\n--- Past Appointment Outcomes ---");

        // Fetch completed appointments for patient
        List<Appointment> pastAppointments = apptController.getCompletedAppointments(patient.getUserID());
        if (pastAppointments.isEmpty()) {
            System.out.println("No past appointments found.");
            return;
        }

        // Display each completed appointment's outcome summary
        for (Appointment appointment : pastAppointments) {
            System.out.println(appointment.getOutcomeSummary());
        }
    }
        */
}
