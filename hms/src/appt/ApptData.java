package appt;

import java.io.*;
import java.util.*;
import java.text.*;

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

    public static void main(String[] args) {
        ApptData apptData = new ApptData();
    }
}
