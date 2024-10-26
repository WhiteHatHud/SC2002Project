package appt;

import java.util.List;
import java.util.Scanner;

public class ApptController {
    private ApptData apptData;

    // Constructor to initialize ApptController with ApptData
    public ApptController(ApptData apptData) {
        this.apptData = apptData;
    }

    // Method to schedule a new appointment for a doctor
    public void scheduleDoctorAppointment() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter Appointment ID:");
        String appointmentID = scanner.nextLine();
        System.out.println("Enter Date (YYYY-MM-DD):");
        String date = scanner.nextLine();
        System.out.println("Enter Time (e.g., 09:00 AM):");
        String time = scanner.nextLine();
        System.out.println("Enter Patient ID:");
        String patientID = scanner.nextLine();
        System.out.println("Enter Patient Name:");
        String patientName = scanner.nextLine();
        System.out.println("Enter Doctor ID:");
        String doctorID = scanner.nextLine();
        System.out.println("Enter Doctor Name:");
        String doctorName = scanner.nextLine();

        // Instantiate as DoctorAppointment
        DoctorAppointment newAppointment = new DoctorAppointment(
            appointmentID, date, time, patientID, patientName, doctorID, doctorName, "Scheduled"
        );
        apptData.addAppointment(newAppointment);
        System.out.println("Doctor appointment scheduled successfully!");
    }

    // Method to cancel an existing doctor appointment
    public void cancelDoctorAppointment(String appointmentID) {
        List<Appointment> appointments = apptData.getAllAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                appointment.setAppointmentStatus("Cancelled");
                apptData.updateAppointment(appointment);
                System.out.println("Doctor appointment cancelled successfully.");
                return;
            }
        }
        System.out.println("No appointment found with ID: " + appointmentID);
    }

    // Method to reschedule an appointment for a doctor
    public void rescheduleDoctorAppointment(String appointmentID) {
        Scanner scanner = new Scanner(System.in);
        
        List<Appointment> appointments = apptData.getAllAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                System.out.println("Enter New Date (YYYY-MM-DD):");
                String newDate = scanner.nextLine();
                System.out.println("Enter New Time (e.g., 09:00 AM):");
                String newTime = scanner.nextLine();
                
                appointment.setDate(newDate);
                appointment.setTime(newTime);
                appointment.setAppointmentStatus("Rescheduled");
                apptData.updateAppointment(appointment);
                System.out.println("Doctor appointment rescheduled successfully.");
                return;
            }
        }
        System.out.println("No appointment found with ID: " + appointmentID);
    }

    // Method to view an appointment by ID
    public void viewDoctorAppointment(String appointmentID) {
        apptData.printAppointmentById(appointmentID);
    }

    // Test the ApptController
    public static void main(String[] args) {
        ApptData apptData = new ApptData();
        ApptController controller = new ApptController(apptData);

        // Example usage
        controller.scheduleDoctorAppointment();
        controller.viewDoctorAppointment("1");
        controller.cancelDoctorAppointment("1");
        controller.viewDoctorAppointment("1");
    }
}
