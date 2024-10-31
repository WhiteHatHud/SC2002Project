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

    // Helper method to get session status
    private String getSessionStatus(List<Appointment> appointments, LocalDate date, LocalTime sessionTime) {
        for (Appointment appointment : appointments) {
            LocalDate appointmentDate = toLocalDate(appointment.getAppointmentTime());
            LocalTime appointmentLocalTime = toLocalTime(appointment.getAppointmentTime());
    
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
    


public LocalDate selectDateFromSchedule(String doctorID) {
    LocalDate today = LocalDate.now();
    List<LocalDate> next7Days = new ArrayList<>();

    System.out.println("\n====================");
    System.out.println(" Select a Date ");
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
        return null;  // Indicate that the user canceled
    }

    try {
        int choice = Integer.parseInt(input);

        if (choice < 1 || choice > 7) {
            System.out.println("Invalid selection. Please try again.");
            return selectDateFromSchedule(doctorID);  // Retry on invalid input
        }

        LocalDate selectedDate = next7Days.get(choice - 1);
        System.out.printf("Selected Date: %s\n", selectedDate);
        return selectedDate;

    } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a number (1-7) or '~' to return.");
        return selectDateFromSchedule(doctorID);  // Retry on invalid input
    }
}

public void printDoctorScheduleOnDate(String doctorID) {
    LocalDate selectedDate = selectDateFromSchedule(doctorID);

    if (selectedDate == null) {
        // User canceled the selection
        return;
    }

    System.out.printf("\nSessions for Dr. %s on %s:\n", doctorID, selectedDate);
    System.out.println("====================");

    viewSessionDetailsForDate(selectedDate, doctorID);
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
    
    private LocalTime selectSessionFromDate(LocalDate date, String doctorID) {
    List<Appointment> appointments = viewAppointmentsByDoctor(doctorID);
    LocalTime[] sessions = {
        LocalTime.of(9, 0),
        LocalTime.of(11, 0),
        LocalTime.of(14, 0)
    };

    System.out.printf("\nAvailable Sessions for Dr. %s on %s:\n", doctorID, date);
    System.out.println("====================");

    for (int i = 0; i < sessions.length; i++) {
        String status = getSessionStatus(appointments, date, sessions[i]);
        System.out.printf("%d. %s [%s]\n", (i + 1), sessions[i], status);
    }

    System.out.println("--------------------");
    System.out.print("Select a session (1-3) or '~' to return: ");
    String input = scanner.nextLine().trim();

    if (input.equals("~")) {
        System.out.println("Returning to the previous menu...");
        return null; // Indicate cancellation
    }

    try {
        int sessionChoice = Integer.parseInt(input);

        if (sessionChoice < 1 || sessionChoice > 3) {
            System.out.println("Invalid selection. Please try again.");
            return selectSessionFromDate(date, doctorID); // Retry on invalid input
        }

        return sessions[sessionChoice - 1];

    } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a number (1-3) or '~' to return.");
        return selectSessionFromDate(date, doctorID); // Retry on invalid input
    }
}

private void printSessionDetailsForDate(LocalDate date, LocalTime sessionTime, String doctorID) {
    List<Appointment> appointments = viewAppointmentsByDoctor(doctorID);

    System.out.printf("\nDetails for session at %s on %s:\n", sessionTime, date);
    System.out.println("====================");

    boolean hasAppointments = false;

    // Display details if the session is booked
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

private void printSessionDetailsAndManage(List<Appointment> appointments, LocalDate date, LocalTime sessionTime) {
    printSessionDetailsForDate(date, sessionTime, appointments.get(0).getDoctorID());

    String status = getSessionStatus(appointments, date, sessionTime);

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
    // Helper method to print session details
        
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
    
            // Generate a new appointment ID
            String newAppointmentID = generateNewAppointmentID();
    
            Appointment blockedAppointment = new DoctorAppointment(
                    newAppointmentID, appointmentTime, "BLOCKED", "N/A",
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
        String newAppointmentID = generateNewAppointmentID();
    
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
    public void printUpcomingSessions(String doctorID, String userType) {
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
        } else {
            System.out.print("\nEnter the Appointment ID to manage: ");
            String appointmentID = scanner.nextLine().trim();
            manageScheduledAppointments(appointmentID, userType);  // Pass userType to manageScheduledAppointments
        }
    }
    
    
    private void manageScheduledAppointments(String appointmentID, String userType) {
        Appointment appointment = apptData.getAllAppointments().stream()
                .filter(app -> app.getAppointmentID().equals(appointmentID))
                .findFirst()
                .orElse(null);
    
        if (appointment == null) {
            System.out.println("Invalid Appointment ID. Returning to the previous menu...");
            return;
        }
    
        System.out.printf("Managing Appointment ID: %s\n", appointmentID);
        System.out.println("1. Cancel Appointment");
        System.out.println("2. Reschedule Appointment");
        System.out.print("Select an option: ");
    
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        switch (choice) {
            case 1 -> cancelAppointment(appointmentID, userType); // Pass userType to cancelAppointment
            case 2 -> rescheduleAppointment(appointment, userType); // Pass userType to rescheduleAppointment
            default -> System.out.println("Invalid option. Returning to the previous menu...");
        }
    }
    
    
    private void cancelAppointment(String appointmentID, String userType) {
        Appointment appointment = apptData.getAllAppointments().stream()
                .filter(app -> app.getAppointmentID().equals(appointmentID))
                .findFirst()
                .orElse(null);
    
        if (appointment != null) {
            // Update appointment status to "Cancelled"
            appointment.setAppointmentStatus("Cancelled");
            apptData.updateAppointmentInCSV(appointment);  // Save the updated status in the CSV
            System.out.println("Appointment successfully marked as cancelled.");
    
            // Notify the other party to rebook if necessary
            if ("Doctor".equalsIgnoreCase(userType)) {
                System.out.printf("The patient with ID %s has been informed to rebook a session.\n", appointment.getPatientID());
                // Additional logic can be added here to inform the patient (e.g., update patient UI)
            } else if ("Patient".equalsIgnoreCase(userType)) {
                System.out.printf("The doctor with ID %s has been informed to rebook a session.\n", appointment.getDoctorID());
                // Additional logic can be added here to inform the doctor (e.g., update doctor UI)
            }
        } else {
            System.out.println("Appointment not found. Unable to cancel.");
        }
    }
    
    

    private void rescheduleAppointment(Appointment appointment, String userType) {
        System.out.println("\nSelect a new date for the appointment.");
        LocalDate newDate = selectDateFromSchedule(appointment.getDoctorID());
    
        if (newDate == null) {
            System.out.println("Rescheduling canceled. Returning to the previous menu...");
            return;
        }
    
        // Use selectSessionFromDate to choose a new session time
        LocalTime newTime = selectSessionFromDate(newDate, appointment.getDoctorID());
    
        if (newTime == null) {
            System.out.println("Rescheduling canceled. Returning to the previous menu...");
            return;
        }
    
        // Check if the new session is available
        String status = getSessionStatus(apptData.getAllAppointments(), newDate, newTime);
    
        if (!status.equalsIgnoreCase("Available")) {
            System.out.println("Selected session is not available. Please choose another session.");
            return;
        }
    
        // Update appointment details
        Calendar newAppointmentTime = Calendar.getInstance();
        newAppointmentTime.set(newDate.getYear(), newDate.getMonthValue() - 1, newDate.getDayOfMonth(),
                               newTime.getHour(), newTime.getMinute());
    
        appointment.setAppointmentTime(newAppointmentTime);
    
        // Set appointment status based on the user type
        if ("Doctor".equalsIgnoreCase(userType)) {
            appointment.setAppointmentStatus("PendingToPatient");
        } else if ("Patient".equalsIgnoreCase(userType)) {
            appointment.setAppointmentStatus("PendingToDoctor");
        } else {
            System.out.println("Invalid user type. Unable to reschedule.");
            return;
        }
    
        // Save the updated appointment to the CSV
        apptData.updateAppointmentInCSV(appointment);
    
        System.out.println("Appointment successfully rescheduled and saved.");
    }
    

    //-==---------------Patient Methods -======================
    public void printPatientAppointments(String patientID) {
        List<Appointment> appointments = apptData.getAllAppointments();
    
        appointments.stream()
                .filter(app -> app.getPatientID().equals(patientID))
                .forEach(app -> {
                    Calendar appointmentTime = app.getAppointmentTime();
                    LocalDate date = LocalDate.of(
                            appointmentTime.get(Calendar.YEAR),
                            appointmentTime.get(Calendar.MONTH) + 1,
                            appointmentTime.get(Calendar.DAY_OF_MONTH)
                    );
                    LocalTime time = LocalTime.of(
                            appointmentTime.get(Calendar.HOUR_OF_DAY),
                            appointmentTime.get(Calendar.MINUTE)
                    );
    
                    System.out.printf("Appointment ID: %s\n", app.getAppointmentID());
                    System.out.printf("Date: %s\n", date);
                    System.out.printf("Time: %s\n", time);
                    System.out.printf("Doctor: %s\n", app.getDoctorName());
                    System.out.printf("Status: %s\n", app.getAppointmentStatus());
                    System.out.println("------------------------------");
                });
    
        if (appointments.stream().noneMatch(app -> app.getPatientID().equals(patientID))) {
            System.out.println("No upcoming appointments.");
        }
    }

    public void bookNewAppointment(String patientID, String patientName) {
        System.out.println("\nBooking a New Appointment:");
        System.out.print("Enter Doctor ID: ");
        String doctorID = scanner.nextLine().trim();
    
        // Select a date for the new appointment
        LocalDate appointmentDate = selectDateFromSchedule(doctorID);
        if (appointmentDate == null) {
            System.out.println("Booking canceled. Returning to the main menu...");
            return;
        }
    
        // Select a session time for the chosen date
        LocalTime appointmentTime = selectSessionFromDate(appointmentDate, doctorID);
        if (appointmentTime == null) {
            System.out.println("Booking canceled. Returning to the main menu...");
            return;
        }
    
        // Check the status of the selected session
        String sessionStatus = getSessionStatus(apptData.getAllAppointments(), appointmentDate, appointmentTime);
    
        if (!sessionStatus.equalsIgnoreCase("Available")) {
            System.out.println("Error: The selected session is already " + sessionStatus + ". Please choose another session.");
            return;
        }
    
        // Generate a new appointment ID
        String newAppointmentID = generateNewAppointmentID();
    
        // Create the new appointment object
        Calendar newAppointmentTime = Calendar.getInstance();
        newAppointmentTime.set(appointmentDate.getYear(), appointmentDate.getMonthValue() - 1,
                               appointmentDate.getDayOfMonth(), appointmentTime.getHour(), appointmentTime.getMinute());
    
        Appointment newAppointment = new DoctorAppointment(
                newAppointmentID, newAppointmentTime, patientID, patientName,
                doctorID, "Dr. Smith", "PendingToDoctor"
        );
    
        // Save the new appointment to the CSV
        apptData.addAppointment(newAppointment);
    
        System.out.println("Appointment successfully booked with ID: " + newAppointmentID);
    }
    
    
    
    public String generateNewAppointmentID() {
        List<Appointment> appointments = apptData.getAllAppointments(); // Fetch all existing appointments
        int maxID = appointments.stream()
                                .mapToInt(app -> Integer.parseInt(app.getAppointmentID().replaceAll("[^0-9]", "")))
                                .max()
                                .orElse(0);
    
        // Generate new ID in the format A0001, A0002, etc.
        return String.format("A%04d", maxID + 1);
    }

    public void viewRequests(String userType, String userID) {
        List<Appointment> appointments = apptData.getAllAppointments(); // Fetch all appointments only once
    
        switch (userType.toLowerCase()) {
            case "patient" -> viewPatientAppointments(userID, appointments);
            case "doctor" -> viewDoctorAppointments(userID, appointments);
            default -> System.out.println("Error: Invalid user type. Please provide 'patient' or 'doctor'.");
        }
    }
    
    private void viewPatientAppointments(String patientID, List<Appointment> appointments) {
        System.out.println("\nViewing your Appointments with status 'PendingToPatient':");
    
        List<Appointment> pendingAppointments = appointments.stream()
                .filter(app -> app.getPatientID().equals(patientID) &&
                               app.getAppointmentStatus().equalsIgnoreCase("PendingToPatient"))
                .toList();
    
        if (pendingAppointments.isEmpty()) {
            System.out.println("No 'PendingToPatient' appointments.");
            return;
        }
    
        for (Appointment app : pendingAppointments) {
            printSessionDetailsForDate(
                    toLocalDate(app.getAppointmentTime()),
                    toLocalTime(app.getAppointmentTime()),
                    app.getDoctorID()
            );
    
            System.out.print("Do you want to accept this appointment? (yes/no): ");
            String input = scanner.nextLine().trim();
    
            if (input.equalsIgnoreCase("yes")) {
                app.setAppointmentStatus("Booked");
                apptData.updateAppointmentInCSV(app);  // Update status in CSV
                System.out.println("Appointment accepted and status set to 'Booked'.");
            } else if (input.equalsIgnoreCase("no")) {
                cancelAppointment(app.getAppointmentID(), "Patient");  // Call cancellation method
                System.out.println("Appointment declined. Please make a new booking if needed.");
            }
        }
    }
    
    private void viewDoctorAppointments(String doctorID, List<Appointment> appointments) {
        System.out.println("\nViewing your Appointments with status 'PendingToDoctor':");
    
        List<Appointment> pendingAppointments = appointments.stream()
                .filter(app -> app.getDoctorID().equals(doctorID) &&
                               app.getAppointmentStatus().equalsIgnoreCase("PendingToDoctor"))
                .toList();
    
        if (pendingAppointments.isEmpty()) {
            System.out.println("No 'PendingToDoctor' appointments.");
            return;
        }
    
        for (Appointment app : pendingAppointments) {
            printSessionDetailsForDate(
                    toLocalDate(app.getAppointmentTime()),
                    toLocalTime(app.getAppointmentTime()),
                    app.getDoctorID()
            );
    
            System.out.print("Do you want to accept this appointment? (yes/no): ");
            String input = scanner.nextLine().trim();
    
            if (input.equalsIgnoreCase("yes")) {
                app.setAppointmentStatus("Booked");
                apptData.updateAppointmentInCSV(app);  // Update status in CSV
                System.out.println("Appointment accepted and status set to 'Booked'.");
            } else if (input.equalsIgnoreCase("no")) {
                cancelAppointment(app.getAppointmentID(), "Doctor");  // Call cancellation method
                System.out.println("Appointment declined. Patient can make a new booking if needed.");
            }
        }
    }  
     

//====================================Call methods===================
// Helper method to convert Calendar to LocalDate
private LocalDate toLocalDate(Calendar calendar) {
    return LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
    );
}

// Helper method to convert Calendar to LocalTime
private LocalTime toLocalTime(Calendar calendar) {
    return LocalTime.of(
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
    );
}

//==============================Completed Sessions===================

public void updateCompletedSessions() {
    List<Appointment> appointments = apptData.getAllAppointments();

    // Get current date and time using LocalDate and LocalTime
    LocalDate currentDate = LocalDate.now();
    LocalTime currentTime = LocalTime.now();

    for (Appointment appointment : appointments) {
        if (appointment.getAppointmentStatus().equalsIgnoreCase("Booked")) {
            LocalDate appointmentDate = toLocalDate(appointment.getAppointmentTime());
            LocalTime appointmentTime = toLocalTime(appointment.getAppointmentTime());

            // Check if the appointment date and time are before the current date and time
            if (appointmentDate.isBefore(currentDate) || 
                (appointmentDate.isEqual(currentDate) && appointmentTime.isBefore(currentTime))) {
                
                // Update status to "Completed" for past appointments
                appointment.setAppointmentStatus("Completed");
                apptData.updateAppointmentInCSV(appointment);
                System.out.println("Updated session to Completed: " + appointment.getAppointmentID());
            }
        }
    }
}

   
        
}
