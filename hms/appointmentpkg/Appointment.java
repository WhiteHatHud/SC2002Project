package hms.appointmentpkg;  // Package declaration

public class Appointment {  // Make the class public
    private int id;
    private String status = "Pending";
    private String date;
    private String time;  // Time field added
    private String serviceProvided;
    private String medication;

    // Constructor
    public Appointment(int id, String date, String time) {
        this.id = id;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOutcome(String service, String medication) {
        this.serviceProvided = service;
        this.medication = medication;
    }

    @Override
    public String toString() {
        return "Appointment ID: " + id + ", Date: " + date + ", Time: " + time + ", Status: " + status;
    }
}
