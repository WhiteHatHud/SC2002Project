package appt;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;


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
            // Check if the session is blocked
            if (appointment.getAppointmentStatus().equalsIgnoreCase("Blocked")) {
                return "Blocked";
            }
            // Otherwise, it is booked
            return "Booked";
        }
    }
    // If no matching appointment is found, the session is available
    return "Available";
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
            printSessionDetailsAndManage(appointments, date, selectedSession);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number (1-3) or '~' to return.");
        }
    }
    

    // Helper method to print session details
    private void printSessionDetailsAndManage(List<Appointment> appointments, LocalDate date, LocalTime sessionTime) {
        String status = getSessionStatus(appointments, date, sessionTime);
    
        System.out.printf("\nDetails for session at %s:\n", sessionTime);
        System.out.println("====================");
    
        boolean hasAppointments = false;
    
        // Check and display appointment details if the session is booked
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
    
        // Handle options based on the session status
        switch (status) {
            case "Available" -> {
                System.out.println("This session is available.");
                System.out.print("Do you want to block this session or create a new appointment? (block/create): ");
                String input = scanner.nextLine().trim();
    
                if (input.equalsIgnoreCase("block")) {
                    blockSession(date, sessionTime, appointments.get(0).getDoctorID(), appointments.get(0).getDoctorName());
                } else if (input.equalsIgnoreCase("create")) {
                    newAppointment(date, sessionTime, appointments.get(0).getDoctorID(), appointments.get(0).getDoctorName());
                } else {
                    System.out.println("Returning to previous menu...");
                }
            }
            case "Blocked" -> {
                System.out.println("This session is currently blocked.");
                System.out.print("Do you want to unblock this session? (yes/no): ");
                String input = scanner.nextLine().trim();
    
                if (input.equalsIgnoreCase("yes")) {
                    unblockSessionByDateAndTime(date, sessionTime, appointments);
                } else {
                    System.out.println("Returning to previous menu...");
                }
            }
            case "Booked" -> {
                System.out.println("This session is booked and cannot be modified.");
            }
        }
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

    //Task 1 Block or Unblock Available Dates
    public void blockSession(LocalDate date, LocalTime time, String doctorID, String doctorName) {
        String status = getSessionStatus(apptData.getAllAppointments(), date, time);
    
        if (status.equals("Available")) {
            Calendar appointmentTime = Calendar.getInstance();
            appointmentTime.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(),
                                time.getHour(), time.getMinute());
    
            Appointment blockedAppointment = new DoctorAppointment(
                    UUID.randomUUID().toString(), appointmentTime, "BLOCKED", "N/A", 
                    doctorID, doctorName, "Blocked");
    
            apptData.addAppointment(blockedAppointment);
            System.out.println("Session blocked successfully.");
        } else {
            System.out.println("This session is already booked or blocked.");
        }
    }
    

    public void unblockSessionByDateAndTime(LocalDate date, LocalTime time, List<Appointment> appointments) {
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
    
            if (appointmentDate.equals(date) && appointmentLocalTime.equals(time) &&
                appointment.getAppointmentStatus().equalsIgnoreCase("Blocked")) {
                apptData.deleteAppointment(appointment.getAppointmentID());
                System.out.println("Session unblocked successfully.");
                return;
            }
        }
        System.out.println("No blocked session found for the given date and time.");
    }
    
    private void newAppointment(LocalDate date, LocalTime time, String doctorID, String doctorName) {
        // Generate a new appointment ID by incrementing the largest existing one
        List<Appointment> appointments = apptData.getAllAppointments();
        int maxID = appointments.stream()
                                .mapToInt(app -> Integer.parseInt(app.getAppointmentID().replaceAll("[^0-9]", "")))
                                .max()
                                .orElse(0);
        String newAppointmentID = String.format("A%04d", maxID + 1);
    
        // Prompt the doctor to enter patient details
        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine().trim();
    
        System.out.print("Enter Patient Name: ");
        String patientName = scanner.nextLine().trim();
    
        // Create a new appointment object with the status "PendingToPatient"
        Calendar appointmentTime = Calendar.getInstance();
        appointmentTime.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(),
                            time.getHour(), time.getMinute());
    
        Appointment newAppointment = new DoctorAppointment(
                newAppointmentID, appointmentTime, patientID, patientName,
                doctorID, doctorName, "PendingToPatient");
    
        // Add the new appointment to the data
        apptData.addAppointment(newAppointment);
        System.out.println("New appointment created successfully with ID: " + newAppointmentID);
    }
    //==============================================================================================
    public void printUpcomingSessions(String doctorID) {
        List<Appointment> doctorAppointments = viewAppointmentsByDoctor(doctorID);
    
        // Filter out blocked appointments and sort by Appointment ID
        doctorAppointments.stream()
                .filter(appointment -> !appointment.getAppointmentStatus().equalsIgnoreCase("Blocked"))
                .sorted((a1, a2) -> a1.getAppointmentID().compareTo(a2.getAppointmentID()))
                .forEach(appointment -> {
                    Calendar appointmentTime = appointment.getAppointmentTime();
                    LocalDate appointmentDate = LocalDate.of(
                            appointmentTime.get(Calendar.YEAR),
                            appointmentTime.get(Calendar.MONTH) + 1,
                            appointmentTime.get(Calendar.DAY_OF_MONTH)
                    );
    
                    System.out.printf("Appointment ID: %s\n", appointment.getAppointmentID());
                    System.out.printf("Date: %s\n", appointmentDate);
                    System.out.printf("Patient ID: %s\n", appointment.getPatientID());
                    System.out.printf("Patient Name: %s\n", appointment.getPatientName());
                    System.out.printf("Appointment Status: %s\n", appointment.getAppointmentStatus());
                    System.out.println("------------------------------");
                });
    
        if (doctorAppointments.stream()
                .noneMatch(appointment -> !appointment.getAppointmentStatus().equalsIgnoreCase("Blocked"))) {
            System.out.println("No upcoming sessions available.");
        }
    }
    
    

}
