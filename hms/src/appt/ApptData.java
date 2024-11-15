package appt;

import java.io.*;
import java.text.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Manages appointment data, including reading from, writing to, and updating appointments in a CSV file.
 */
public class ApptData {
    private static final String FILE_PATH = "appointments.csv";
    private static final SimpleDateFormat dateFormatterWithTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat dateFormatterWithoutTime = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Parses a date string and returns a Calendar object.
     * Attempts to parse with time included first, and without time if the first attempt fails.
     * 
     * @param dateStr The date string to parse.
     * @return A Calendar object representing the parsed date and time.
     * @throws ParseException If the date string cannot be parsed.
     */
    private Calendar parseDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.trim().isEmpty() || dateStr.equals("0000-00-00 00:00")) {
            return null;  // Return null if the date string is empty or the placeholder
        }
    
        Calendar appointmentTime = Calendar.getInstance();
        try {
            appointmentTime.setTime(dateFormatterWithTime.parse(dateStr));
        } catch (ParseException e) {
            // Try parsing without time if the first attempt fails
            appointmentTime.setTime(dateFormatterWithoutTime.parse(dateStr));
        }
        return appointmentTime;
    }

    /**
     * Reads all appointments from the CSV file.
     *
     * @return A list of Appointment objects read from the file.
     */
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines

                String[] data = line.split(",");

                if (data.length < 7) {
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

                if (data.length > 7) appointment.setDiagnosis(data[7]);
                if (data.length > 8) appointment.setTreatmentPlan(data[8]);
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

    /**
     * Adds a new appointment to the CSV file.
     *
     * @param appointment The Appointment object to add.
     */
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

    /**
     * Deletes an appointment from the CSV file based on the appointment ID.
     *
     * @param appointmentID The ID of the appointment to delete.
     */
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

    /**
     * Checks the status of an appointment slot for a specific date, time, and doctor.
     *
     * @param date The date to check.
     * @param time The time to check.
     * @param doctorID The ID of the doctor.
     * @return The status of the appointment slot ("Available", "Booked", etc.).
     */
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

    /**
     * Updates the CSV file with the current list of appointments.
     *
     * @param appointments The list of Appointment objects to write to the file.
     */
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

    /**
     * Updates a specific appointment in the CSV file.
     *
     * @param updatedAppointment The Appointment object with updated information.
     */
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
}
