package appt;

import java.io.*;
import java.util.*;
import java.text.*;
import java.time.LocalDate;
import java.time.LocalTime;


public class ApptData {
    private static final String FILE_PATH = "appointments.csv";
    private static final SimpleDateFormat dateFormatterWithTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat dateFormatterWithoutTime = new SimpleDateFormat("yyyy-MM-dd");

    // Method to parse a date with different formats
    private Calendar parseDate(String dateStr) throws ParseException {
        Calendar appointmentTime = Calendar.getInstance();
        try {
            appointmentTime.setTime(dateFormatterWithTime.parse(dateStr));
        } catch (ParseException e) {
            // Try parsing without time if the first attempt fails
            appointmentTime.setTime(dateFormatterWithoutTime.parse(dateStr));
        }
        return appointmentTime;
    }

    // Method to read all appointments from the CSV file
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines

                String[] data = line.split(",");

                if (data.length < 7) {
                    System.out.println("Skipping malformed row: " + line);
                    continue;
                }

                Calendar appointmentTime;
                try {
                    appointmentTime = parseDate(data[1]); // Parse date and time
                } catch (ParseException e) {
                    System.out.println("Error parsing date: " + data[1]);
                    e.printStackTrace();
                    continue;
                }

                DoctorAppointment appointment = new DoctorAppointment(
                    data[0], appointmentTime, data[2], data[3], data[4], data[5], data[6]
                );

                if (data.length > 7) appointment.setOutcome(data[7]);
                if (data.length > 8) appointment.setService(data[8]);
                if (data.length > 9) appointment.setMedicine(data[9]);
                if (data.length > 10) appointment.setMedicineStatus(data[10]);
                if (data.length > 11) appointment.setNotes(data[11]);

                appointments.add(appointment);
            }
        } catch (IOException e) {
            System.out.println("Error reading appointments from file.");
            e.printStackTrace();
        }
        return appointments;
    }

    public void addAppointment(Appointment appointment) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(appointment.toCSV());
            writer.newLine();
            System.out.println("Appointment added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing appointment to file.");
            e.printStackTrace();
        }
    }

    // Method to delete an appointment from the CSV file based on appointment ID
    public void deleteAppointment(String appointmentID) {
        List<Appointment> appointments = getAllAppointments();
        boolean removed = appointments.removeIf(app -> app.getAppointmentID().equals(appointmentID));

        if (removed) {
            updateCSV(appointments);
            System.out.println("Appointment deleted successfully.");
        } else {
            System.out.println("Appointment not found.");
        }
    }

    // Method to check the status of an appointment slot
    public String statusAppointment(LocalDate date, LocalTime time, String doctorID) {
        List<Appointment> appointments = getAllAppointments();
        for (Appointment app : appointments) {
            Calendar appointmentTime = app.getAppointmentTime();
            LocalDate appointmentDate = LocalDate.of(
                    appointmentTime.get(Calendar.YEAR),
                    appointmentTime.get(Calendar.MONTH) + 1,
                    appointmentTime.get(Calendar.DAY_OF_MONTH)
            );
            LocalTime appointmentLocalTime = LocalTime.of(
                    appointmentTime.get(Calendar.HOUR_OF_DAY),
                    appointmentTime.get(Calendar.MINUTE)
            );

            if (appointmentDate.equals(date) && appointmentLocalTime.equals(time) 
                && app.getDoctorID().equals(doctorID)) {
                return app.getAppointmentStatus();
            }
        }
        return "Available";
    }

    private void updateCSV(List<Appointment> appointments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("appointmentID,date,patientID,patientName,doctorID,doctorName,status,outcome,service,medicine,medicineStatus,notes");
            writer.newLine();
            for (Appointment app : appointments) {
                writer.write(app.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating the CSV file.");
            e.printStackTrace();
        }
    }

    public void updateAppointmentInCSV(Appointment updatedAppointment) {
        List<Appointment> appointments = getAllAppointments(); // Read existing appointments
    
        // Replace the old appointment with the updated one
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentID().equals(updatedAppointment.getAppointmentID())) {
                appointments.set(i, updatedAppointment);
                break;
            }
        }
    
        // Write the updated list back to the CSV
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            // Write the CSV header
            writer.write("AppointmentID,DateTime,PatientID,PatientName,DoctorID,DoctorName,AppointmentStatus,Outcome,Service,Medicine,MedicineStatus,Notes\n");
    
            // Write each appointment as a CSV row
            for (Appointment appointment : appointments) {
                writer.write(appointment.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to the CSV file.");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
    }
}
