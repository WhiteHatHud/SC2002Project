package appt;

import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Represents a doctor's appointment, extending the base Appointment class.
 * This class includes additional fields and methods for managing doctor-specific appointment details.
 */
public class DoctorAppointment extends Appointment {
    private boolean accepted; // Tracks if the doctor accepted the appointment
    private String typeOfService;
    private String medicationName;
    private String medicationStatus = "Pending"; // Default status
    private String consultationNotes;

    /**
     * Constructs a DoctorAppointment with specified details.
     *
     * @param appointmentID     The unique ID of the appointment.
     * @param appointmentTime   The time of the appointment.
     * @param patientID         The ID of the patient.
     * @param patientName       The name of the patient.
     * @param doctorID          The ID of the doctor.
     * @param doctorName        The name of the doctor.
     * @param appointmentStatus The status of the appointment.
     */
    public DoctorAppointment(String appointmentID, Calendar appointmentTime, String patientID,
                             String patientName, String doctorID, String doctorName, String appointmentStatus) {
        super(appointmentID, appointmentTime, patientID, patientName, doctorID, doctorName, appointmentStatus);
        this.accepted = false;
    }

    /**
     * Checks if the appointment has been accepted by the doctor.
     *
     * @return True if the appointment is accepted, false otherwise.
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Accepts the appointment, setting its status to "Accepted".
     */
    public void acceptAppointment() {
        this.accepted = true;
        this.appointmentStatus = "Accepted";
    }

    /**
     * Declines the appointment, setting its status to "Declined".
     */
    public void declineAppointment() {
        this.accepted = false;
        this.appointmentStatus = "Declined";
    }

    /**
     * Sets the outcome details of the appointment, including type of service, medication, and consultation notes.
     *
     * @param typeOfService      The type of service provided in the appointment.
     * @param medicationName     The name of the medication prescribed.
     * @param consultationNotes  Notes from the consultation.
     */
    public void setOutcomeDetails(String typeOfService, String medicationName, String consultationNotes) {
        this.typeOfService = typeOfService;
        this.medicationName = medicationName;
        this.consultationNotes = consultationNotes;
    }

    /**
     * Manages the appointment, performing tasks related to the doctor’s involvement in the appointment.
     * This method should be overridden to define specific management tasks.
     */
    @Override
    public void manageAppointment() {
        System.out.println("Managing appointment for Doctor: " + doctorName);
    }

    /**
     * Provides a detailed view of the appointment, including date, patient details, service, medication, and status.
     *
     * @return A string representing the appointment details.
     */
    public String viewDetails() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return String.format(
            "Appointment ID: %s\nDate: %s\nPatient: %s\nService: %s\nMedication: %s (%s)\nNotes: %s\nStatus: %s",
            appointmentID, dateFormatter.format(appointmentTime.getTime()), patientName, 
            typeOfService, medicationName, medicationStatus, consultationNotes, appointmentStatus
        );
    }

    /**
     * Updates the session status of the appointment, allowing the doctor to set it as Available, Unavailable, or Booked.
     *
     * @param newStatus The new status to be set for the appointment.
     */
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
