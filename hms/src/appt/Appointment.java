package appt;

public abstract class Appointment {
    protected String appointmentID;
    protected String date;
    protected String time;
    protected String patientID;
    protected String patientName;
    protected String doctorID;
    protected String doctorName;
    protected String appointmentStatus;
    protected String outcome;

    // Constructor for common attributes
    public Appointment(String appointmentID, String date, String time, String patientID,
                       String patientName, String doctorID, String doctorName, String appointmentStatus) {
        this.appointmentID = appointmentID;
        this.date = date;
        this.time = time;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // Convert the appointment to a CSV format for easy data storage
    public String toCSV() {
        return String.join(",", appointmentID, date, time, patientID, patientName, doctorID, doctorName, appointmentStatus, outcome == null ? "" : outcome);
    }

    // Abstract method for managing the appointment - to be implemented by subclasses
    public abstract void manageAppointment();
}
