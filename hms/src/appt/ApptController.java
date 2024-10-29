package appt;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class ApptController {
    private ApptData apptData;
    private final Scanner scanner = new Scanner(System.in); // Reusable scanner

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

    // Integrated method to prompt the user to select a date and view session details
    public void printDoctorScheduleOnDate(String doctorID) {
        LocalDate today = LocalDate.now();
        List<LocalDate> next7Days = new ArrayList<>();

        System.out.println("\n====================");
        System.out.println(" Select a Date to View Appointments ");
        System.out.println("====================");
        System.out.println("Or type '~' to return to the previous menu:");
        System.out.println("--------------------");

        // Generate the next 7 days with booked sessions count
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.plusDays(i);
            next7Days.add(date);

            int bookedSessions = countBookedSessions(date, doctorID); // Count booked sessions
            System.out.printf("%d. %s [%d/3]\n", (i + 1), date, bookedSessions); // Display with [x/3]
        }

        System.out.println("--------------------");
        System.out.print("Enter a number (1-7) or '~' to return: ");
        String input = scanner.nextLine().trim();

        if (input.equals("~")) {
            System.out.println("Returning to the previous menu...");
            return;
        }

        try {
            int choice = Integer.parseInt(input);

            if (choice < 1 || choice > 7) {
                System.out.println("Invalid selection. Please try again.");
                return;
            }

            LocalDate selectedDate = next7Days.get(choice - 1);
            System.out.printf("\nSessions for Dr. %s on %s:\n", doctorID, selectedDate);
            System.out.println("====================");

            viewSessionDetailsForDate(selectedDate, doctorID);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number (1-7) or '~' to return.");
        }
    }

    // Integrated method to view session details for a selected date
    private void viewSessionDetailsForDate(LocalDate date, String doctorID) {
        List<Appointment> appointments = viewAppointmentsByDoctor(doctorID);
        LocalTime[] sessions = {
            LocalTime.of(9, 0),
            LocalTime.of(11, 0),
            LocalTime.of(14, 0)
        };

        // Display available sessions with their statuses
        for (int i = 0; i < sessions.length; i++) {
            String status = getSessionStatus(appointments, date, sessions[i]);
            System.out.printf("%d. %s [%s]\n", (i + 1), sessions[i], status);
        }

        System.out.println("--------------------");
        System.out.print("Select a session to view details (1-3) or '~' to return: ");
        String input = scanner.nextLine().trim();

        if (input.equals("~")) {
            System.out.println("Returning to the previous menu...");
            return;
        }

        try {
            int sessionChoice = Integer.parseInt(input);

            if (sessionChoice < 1 || sessionChoice > 3) {
                System.out.println("Invalid selection. Please try again.");
                return;
            }

            LocalTime selectedSession = sessions[sessionChoice - 1];
            printSessionDetails(appointments, date, selectedSession);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number (1-3) or '~' to return.");
        }
    }

    // Helper method to print session details
    private void printSessionDetails(List<Appointment> appointments, LocalDate date, LocalTime sessionTime) {
        System.out.printf("\nDetails for session at %s:\n", sessionTime);
        System.out.println("====================");

        boolean hasAppointments = false;

        for (Appointment appointment : appointments) {
            Calendar appointmentTime = appointment.getAppointmentTime();
            LocalDate appointmentDate = LocalDate.of(
                    appointmentTime.get(Calendar.YEAR),
                    appointmentTime.get(Calendar.MONTH) + 1,
                    appointmentTime.get(Calendar.DAY_OF_MONTH)
            );

            LocalTime appointmentLocalTime = LocalTime.of(
                    appointmentTime.get(Calendar.HOUR_OF_DAY),
                    appointmentTime.get(Calendar.MINUTE)
            );

            if (appointmentDate.equals(date) && appointmentLocalTime.equals(sessionTime)) {
                hasAppointments = true;
                System.out.printf("Patient ID: %s\n", appointment.getPatientID());
                System.out.printf("Patient Name: %s\n", appointment.getPatientName());
                System.out.printf("Doctor ID: %s\n", appointment.getDoctorID());
                System.out.printf("Doctor Name: %s\n", appointment.getDoctorName());
                System.out.printf("Appointment Status: %s\n", appointment.getAppointmentStatus());
                System.out.printf("Service: %s\n", appointment.getService());
                System.out.printf("Medicine: %s\n", appointment.getMedicine());
                System.out.printf("Medicine Status: %s\n", appointment.getMedicineStatus());
                System.out.printf("Notes: %s\n", appointment.getNotes());
                System.out.println("--------------------");
            }
        }

        if (!hasAppointments) {
            System.out.println("No appointments scheduled for this session.");
        }
    }

    // Helper method to get session status
    private String getSessionStatus(List<Appointment> appointments, LocalDate date, LocalTime sessionTime) {
        for (Appointment appointment : appointments) {
            Calendar appointmentTime = appointment.getAppointmentTime();
            LocalDate appointmentDate = LocalDate.of(
                    appointmentTime.get(Calendar.YEAR),
                    appointmentTime.get(Calendar.MONTH) + 1,
                    appointmentTime.get(Calendar.DAY_OF_MONTH)
            );

            LocalTime appointmentLocalTime = LocalTime.of(
                    appointmentTime.get(Calendar.HOUR_OF_DAY),
                    appointmentTime.get(Calendar.MINUTE)
            );

            if (appointmentDate.equals(date) && appointmentLocalTime.equals(sessionTime)) {
                return "Booked";
            }
        }
        return "Available";
    }

    // Helper method to count booked sessions on a given date
    private int countBookedSessions(LocalDate date, String doctorID) {
        List<Appointment> appointments = viewAppointmentsByDoctor(doctorID);
        LocalTime[] sessions = {
            LocalTime.of(9, 0),
            LocalTime.of(11, 0),
            LocalTime.of(14, 0)
        };

        int bookedCount = 0;

        for (LocalTime session : sessions) {
            for (Appointment appointment : appointments) {
                Calendar appointmentTime = appointment.getAppointmentTime();
                LocalDate appointmentDate = LocalDate.of(
                        appointmentTime.get(Calendar.YEAR),
                        appointmentTime.get(Calendar.MONTH) + 1,
                        appointmentTime.get(Calendar.DAY_OF_MONTH)
                );

                LocalTime appointmentLocalTime = LocalTime.of(
                        appointmentTime.get(Calendar.HOUR_OF_DAY),
                        appointmentTime.get(Calendar.MINUTE)
                );

                if (appointmentDate.equals(date) && appointmentLocalTime.equals(session)) {
                    bookedCount++;
                    break;
                }
            }
        }
        return bookedCount;
    }
}
