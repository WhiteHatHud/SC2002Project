package appt;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class ApptController {
    private ApptData apptData;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Scanner scanner = new Scanner(System.in); // Moved scanner to class level for reuse

    // Constructor to initialize ApptController with ApptData
    public ApptController(ApptData apptData) {
        this.apptData = apptData;
    }

    // Method to view all appointments for a doctor
    public List<Appointment> viewAppointmentsByDoctor(String doctorID) {
        List<Appointment> appointments = apptData.getAllAppointments();
        List<Appointment> doctorAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorID().equals(doctorID)) {
                doctorAppointments.add(appointment);
            }
        }
        return doctorAppointments;
    }

    // Method to prompt the user to choose a date from the next 7 days or return to the previous menu
    public void printDoctorScheduleOnDate(String doctorID) {
        LocalDate today = LocalDate.now();
        List<LocalDate> next7Days = new ArrayList<>();

        // Generate the next 7 days
        System.out.println("Select a date to view appointments or type '~' to return to the previous menu:");
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            next7Days.add(date);
            System.out.println((i + 1) + ". " + date);
        }

        System.out.print("Enter a number (1-7) or '~' to return: ");
        String input = scanner.nextLine().trim();

        if (input.equals("~")) {
            System.out.println("Returning to the previous menu...");
            return; // Exit to the previous menu
        }

        try {
            int choice = Integer.parseInt(input);

            if (choice < 1 || choice > 7) {
                System.out.println("Invalid selection. Please try again.");
                return;
            }

            LocalDate selectedDate = next7Days.get(choice - 1);
            List<Appointment> appointments = viewAppointmentsByDoctor(doctorID);

            System.out.println("\nAppointments for Dr. " + doctorID + " on " + selectedDate + ":");
            printAppointmentsOnDate(appointments, selectedDate);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number (1-7) or '~' to return.");
        }
    }

    // Helper method to print appointments on a specific date
    private void printAppointmentsOnDate(List<Appointment> appointments, LocalDate date) {
        boolean hasAppointments = false;

        for (Appointment appointment : appointments) {
            Calendar appointmentTime = appointment.getAppointmentTime();
            LocalDate appointmentDate = LocalDate.of(
                    appointmentTime.get(Calendar.YEAR),
                    appointmentTime.get(Calendar.MONTH) + 1,
                    appointmentTime.get(Calendar.DAY_OF_MONTH)
            );

            if (appointmentDate.equals(date)) {
                System.out.println("  - Appointment ID: " + appointment.getAppointmentID() +
                        ", Patient: " + appointment.getPatientName() +
                        ", Time: " + appointmentTime.get(Calendar.HOUR_OF_DAY) + ":" +
                        String.format("%02d", appointmentTime.get(Calendar.MINUTE)));
                hasAppointments = true;
            }
        }

        if (!hasAppointments) {
            System.out.println("  No appointments for this day.");
        }
    }
}
