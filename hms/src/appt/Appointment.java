package appt;

import Login.DisplayManager;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Abstract class representing an appointment. Contains common attributes and methods
 * for appointments, including appointment ID, patient and doctor information, status,
 * diagnosis, treatment plan, and more.
 */
public abstract class Appointment {

    /** The ID of the admin managing the appointment. */
    protected String adminID;

    /** The name of the admin managing the appointment. */
    protected String adminName;

    /** The unique ID of the appointment. */
    protected String appointmentID;

    /** The scheduled date and time of the appointment. */
    protected Calendar appointmentTime;

    /** The ID of the patient associated with the appointment. */
    protected String patientID;

    /** The name of the patient associated with the appointment. */
    protected String patientName;

    /** The ID of the doctor associated with the appointment. */
    protected String doctorID;

    /** The name of the doctor associated with the appointment. */
    protected String doctorName;

    /** The current status of the appointment (e.g., scheduled, completed, cancelled). */
    protected String appointmentStatus;

    /** The diagnosis for the patient, provided by the doctor. */
    protected String diagnosis;

    /** The treatment plan for the patient, provided by the doctor. */
    protected String treatmentPlan;

    /** The medicine prescribed to the patient. */
    protected String medicine;

    /** The status of the medicine (e.g., prescribed, dispensed). */
    protected String medicineStatus;

    /** Additional notes related to the appointment. */
    protected String notes;

    /**
     * Constructs an Appointment with the specified common attributes.
     *
     * @param appointmentID The unique ID of the appointment.
     * @param appointmentTime The scheduled date and time of the appointment.
     * @param patientID The ID of the patient.
     * @param patientName The name of the patient.
     * @param doctorID The ID of the doctor.
     * @param doctorName The name of the doctor.
     * @param appointmentStatus The current status of the appointment.
     */
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

    /** @return the ID of the admin managing the appointment. */
    public String getAdminID() {
        return adminID;
    }

    /**
     * Sets the admin name for this appointment.
     * @param adminName The name of the admin.
     */
    public void setAdminID(String adminName) {
        this.adminName = adminName;
    }

    /** @return the unique ID of the appointment. */
    public String getAppointmentID() {
        return appointmentID;
    }

    /**
     * Sets the status of the appointment.
     * @param status The new status of the appointment.
     */
    public void setAppointmentStatus(String status) {
        this.appointmentStatus = status;
    }

    /** @return the current status of the appointment. */
    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    /** @return the scheduled date and time of the appointment. */
    public Calendar getAppointmentTime() {
        return appointmentTime;
    }

    /**
     * Sets the date and time of the appointment.
     * @param appointmentTime The new date and time.
     */
    public void setAppointmentTime(Calendar appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    /** @return the ID of the patient associated with the appointment. */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Sets the ID of the patient for this appointment.
     * @param patientID The ID of the patient.
     */
    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    /** @return the name of the patient associated with the appointment. */
    public String getPatientName() {
        return patientName;
    }

    /**
     * Sets the name of the patient for this appointment.
     * @param patientName The name of the patient.
     */
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    /** @return the ID of the doctor associated with the appointment. */
    public String getDoctorID() {
        return doctorID;
    }

    /** @return the name of the doctor associated with the appointment. */
    public String getDoctorName() {
        return doctorName;
    }

    /** @return the diagnosis for the patient. */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * Sets the diagnosis for the patient.
     * @param diagnosis The diagnosis provided by the doctor.
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /** @return the treatment plan for the patient. */
    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    /**
     * Sets the treatment plan for the patient.
     * @param treatmentPlan The treatment plan provided by the doctor.
     */
    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    /** @return the medicine prescribed to the patient. */
    public String getMedicine() {
        return medicine;
    }

    /**
     * Sets the prescribed medicine for the patient.
     * @param medicine The medicine name.
     */
    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    /** @return the status of the prescribed medicine. */
    public String getMedicineStatus() {
        return medicineStatus;
    }

    /**
     * Sets the status of the prescribed medicine.
     * @param medicineStatus The status (e.g., dispensed).
     */
    public void setMedicineStatus(String medicineStatus) {
        this.medicineStatus = medicineStatus;
    }

    /** @return additional notes for the appointment. */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets additional notes for the appointment.
     * @param notes Notes related to the appointment.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Converts the appointment details to CSV format for easy data storage.
     *
     * @return A CSV-formatted string of the appointment details.
     */
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
            diagnosis == null ? "" : diagnosis, 
            treatmentPlan == null ? "" : treatmentPlan,
            medicine == null ? "" : medicine, 
            medicineStatus == null ? "" : medicineStatus, 
            notes == null ? "" : notes
        );
    }

    /**
     * Abstract method for managing the appointment. Subclasses must implement this method.
     */
    public abstract void manageAppointment();
}
