package appt;

import Login.DisplayManager;
import java.util.Calendar; // Ensures the format of the date and time when users do appointments
import java.text.SimpleDateFormat;

public abstract class Appointment {

    protected String adminID;
    protected String adminName;
    protected String appointmentID;
    protected Calendar appointmentTime; // Use Calendar to represent both date and time
    protected String patientID;
    protected String patientName;
    protected String doctorID;
    protected String doctorName;
    protected String appointmentStatus;
    protected String diagnosis; // Changed from outcome to diagnosis
    protected String treatmentPlan; // Changed from service to treatmentPlan
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

    public String getAdminID() {
        
        return adminID;

    }

    public void setAdminID(String adminName){
        this.adminName = adminName;
    }

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

    public String getDiagnosis() { // Changed from getOutcome to getDiagnosis
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) { // Changed from setOutcome to setDiagnosis
        this.diagnosis = diagnosis;
    }

    // New getters and setters for the additional attributes
    public String getTreatmentPlan() { // Changed from getService to getTreatmentPlan
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) { // Changed from setService to setTreatmentPlan
        this.treatmentPlan = treatmentPlan;
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
            diagnosis == null ? "" : diagnosis,  // Changed from outcome to diagnosis
            treatmentPlan == null ? "" : treatmentPlan, // Changed from service to treatmentPlan
            medicine == null ? "" : medicine, 
            medicineStatus == null ? "" : medicineStatus, 
            notes == null ? "" : notes
        );
    }
    
    // Abstract method for managing the appointment - to be implemented by subclasses
    public abstract void manageAppointment();
}
