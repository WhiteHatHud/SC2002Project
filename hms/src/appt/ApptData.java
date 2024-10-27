package appt;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.text.SimpleDateFormat;

public class ApptData {
    private static final String FILE_PATH = "appointments.csv";
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    // Method to read all appointments from the CSV file
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Calendar appointmentTime = Calendar.getInstance();
                appointmentTime.setTime(dateFormatter.parse(data[1])); // Parse the date and time
                // Create a DoctorAppointment object from CSV data (currently only handling doctor appointments)
                DoctorAppointment appointment = new DoctorAppointment(
                    data[0], appointmentTime, data[2], data[3], data[4], data[5], data[6]
                );
                // Set outcome if it exists
                if (data.length > 7) {
                    appointment.recordOutcome(data[7]);
                }
                appointments.add(appointment);
            }
        } catch (Exception e) {
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
            bw.write("AppointmentID,DateTime,PatientID,PatientName,DoctorID,DoctorName,AppointmentStatus,Outcome");
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

    // Method to update all appointments in the CSV file
    public void updateAllAppointments(List<Appointment> updatedAppointments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Write header
            bw.write("AppointmentID,DateTime,PatientID,PatientName,DoctorID,DoctorName,AppointmentStatus,Outcome");
            bw.newLine();

            for (Appointment appointment : updatedAppointments) {
                bw.write(appointment.toCSV());
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
