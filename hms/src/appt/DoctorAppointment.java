package appt;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public class DoctorAppointment extends Appointment {
    private boolean accepted; // Tracks if the doctor accepted the appointment
    private String typeOfService;
    private String medicationName;
    private String medicationStatus = "Pending"; // Default status
    private String consultationNotes;

    public DoctorAppointment(String appointmentID, Calendar appointmentTime, String patientID,
                             String patientName, String doctorID, String doctorName, String appointmentStatus) {
        super(appointmentID, appointmentTime, patientID, patientName, doctorID, doctorName, appointmentStatus);
        this.accepted = false;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void acceptAppointment() {
        this.accepted = true;
        this.appointmentStatus = "Accepted";
    }

    public void declineAppointment() {
        this.accepted = false;
        this.appointmentStatus = "Declined";
    }

    public void setOutcomeDetails(String typeOfService, String medicationName, String consultationNotes) {
        this.typeOfService = typeOfService;
        this.medicationName = medicationName;
        this.consultationNotes = consultationNotes;
    }

    @Override
    public void manageAppointment() {
        System.out.println("Managing appointment for Doctor: " + doctorName);
    }

    public String viewDetails() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return String.format(
            "Appointment ID: %s\nDate: %s\nPatient: %s\nService: %s\nMedication: %s (%s)\nNotes: %s\nStatus: %s",
            appointmentID, dateFormatter.format(appointmentTime.getTime()), patientName, 
            typeOfService, medicationName, medicationStatus, consultationNotes, appointmentStatus
        );
    }

    // New method to allow the doctor to update session availability
    public void updateSessionStatus(String newStatus) {
        if (newStatus.equalsIgnoreCase("Available") || 
            newStatus.equalsIgnoreCase("Unavailable") || 
            newStatus.equalsIgnoreCase("Booked")) {
            this.appointmentStatus = newStatus;
            System.out.println("Session status updated successfully.");
        } else {
            System.out.println("Invalid status. Please enter Available, Unavailable, or Booked.");
        }
    }
}
