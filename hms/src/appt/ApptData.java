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
                // Create a DoctorAppointment object from CSV data (currently only handling doctor appointments)
                DoctorAppointment appointment = new DoctorAppointment(
                    data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]
                );
                // Set outcome if it exists
                if (data.length > 8) {
                    appointment.recordOutcome(data[8]);
                }
                appointments.add(appointment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    // Method to add a new appointment to the CSV file
    public void addAppointment(Appointment appointment) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(appointment.toCSV());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to update an appointment in the CSV file
    public void updateAppointment(Appointment updatedAppointment) {
        List<Appointment> appointments = getAllAppointments();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Write header
            bw.write("AppointmentID,Date,Time,PatientID,PatientName,DoctorID,DoctorName,AppointmentStatus,Outcome");
            bw.newLine();

            for (Appointment appointment : appointments) {
                if (appointment.getAppointmentID().equals(updatedAppointment.getAppointmentID())) {
                    bw.write(updatedAppointment.toCSV());
                } else {
                    bw.write(appointment.toCSV());
                }
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        apptData.printAppointmentById("2");  // Print appointment with ID 2
    }
}
