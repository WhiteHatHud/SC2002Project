package appt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ApptData {
    private static final String FILE_PATH = "appointments.csv";

    // Method to read all appointments from the CSV file
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Create an Appointment object from CSV data
                Appointment appointment = new Appointment(
                    data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data.length > 8 ? data[8] : ""
                );
                appointments.add(appointment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    // Method to print out one appointment by its ID
    public void printAppointmentById(String appointmentID) {
        List<Appointment> appointments = getAllAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                System.out.println(appointment);
                return;
            }
        }
        System.out.println("Appointment not found for ID: " + appointmentID);
    }

    // Test the functionality
    public static void main(String[] args) {
        ApptData apptData = new ApptData();
        apptData.printAppointmentById("1");  // Print appointment with ID 1
    }
}

// Appointment class
class Appointment {
    private String appointmentID;
    private String date;
    private String time;
    private String patientID;
    private String patientName;
    private String doctorID;
    private String doctorName;
    private String appointmentStatus;
    private String outcome;

    // Constructor
    public Appointment(String appointmentID, String date, String time, String patientID,
                       String patientName, String doctorID, String doctorName, String appointmentStatus, String outcome) {
        this.appointmentID = appointmentID;
        this.date = date;
        this.time = time;
        this.patientID = patientID;
        this.patientName = patientName;
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.appointmentStatus = appointmentStatus;
        this.outcome = outcome;
    }

    // Getters
    public String getAppointmentID() {
        return appointmentID;
    }

    // To string method for displaying appointment details
    @Override
    public String toString() {
        return "Appointment ID: " + appointmentID + "\n" +
               "Date: " + date + "\n" +
               "Time: " + time + "\n" +
               "Patient ID: " + patientID + "\n" +
               "Patient Name: " + patientName + "\n" +
               "Doctor ID: " + doctorID + "\n" +
               "Doctor Name: " + doctorName + "\n" +
               "Appointment Status: " + appointmentStatus + "\n" +
               "Outcome: " + outcome + "\n";
    }
}
