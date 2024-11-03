package appt;

import java.util.Calendar; // Ensures the format of the date and time when users do appointments
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
    protected String service;
    protected String medicine;
    protected String medicineStatus;
    protected String notes;

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

    public String getAppointmentStatus() {
        return appointmentStatus;
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

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    // New getters and setters for the additional attributes
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getMedicineStatus() {
        return medicineStatus;
    }

    public void setMedicineStatus(String medicineStatus) {
        this.medicineStatus = medicineStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Convert the appointment to a CSV format for easy data storage
    public String toCSV() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formattedDateTime = (appointmentTime == null) ? "0000-00-00 00:00" : dateFormatter.format(appointmentTime.getTime());
        
        return String.join(",", 
            appointmentID, 
            formattedDateTime, 
            patientID, 
            patientName, 
            doctorID, 
            doctorName, 
            appointmentStatus, 
            outcome == null ? "" : outcome, 
            service == null ? "" : service, 
            medicine == null ? "" : medicine, 
            medicineStatus == null ? "" : medicineStatus, 
            notes == null ? "" : notes
        );
    }
    

    // Abstract method for managing the appointment - to be implemented by subclasses
    public abstract void manageAppointment();
}
