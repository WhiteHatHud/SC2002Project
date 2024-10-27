package appt;

import java.util.Calendar; //Uses calendar as per the example, this ensures the format of the date and time when users do appointments
import java.text.SimpleDateFormat;

public abstract class Appointment {
    protected String appointmentID;
    protected Calendar appointmentTime; // Use Calendar to represent both date and time
    protected String patientID;
    protected String patientName;
    protected String doctorID;
    protected String doctorName;
    protected String appointmentStatus;
    protected String outcome;

    // Constructor for common attributes
    public Appointment(String appointmentID, Calendar appointmentTime, String patientID,
                       String patientName, String doctorID, String doctorName, String appointmentStatus) {
        this.appointmentID = appointmentID;
        this.appointmentTime = appointmentTime;
        this.patientID = patientID;
        this.patientName = patientName;
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.appointmentStatus = appointmentStatus;
    }

    // Common getters and setters
    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentStatus(String status) {
        this.appointmentStatus = status;
    }

    public Calendar getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Calendar appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    // Convert the appointment to a CSV format for easy data storage
    public String toCSV() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedDateTime = dateFormatter.format(appointmentTime.getTime());
        return String.join(",", appointmentID, formattedDateTime, patientID, patientName, doctorID, doctorName, appointmentStatus, outcome == null ? "" : outcome);
    }

    // Abstract method for managing the appointment - to be implemented by subclasses
    public abstract void manageAppointment();
}
