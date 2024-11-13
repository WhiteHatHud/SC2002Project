package appt;

import Doctors.CreateRecord;
import Doctors.DoctorShared;
import Patients.PatientShared;
import Login.DisplayManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ApptController {
    private ApptData apptData;
    private final Scanner scanner = new Scanner(System.in); // Reusable scanner

    public ApptController() {
        this.apptData = new ApptData();
    }
    
    public List<String> getPatientsUnderCare(String doctorID) {
        // List to store unique patient names under the specified doctor’s care
        Set<String> patientNames = new HashSet<>();

        // Get all appointments from the apptData
        List<Appointment> appointments = apptData.getAllAppointments();

        // Iterate through appointments to check if each appointment has the specified doctorID
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorID().equals(doctorID)) {
                // Add the patient's name to the set if under this doctor's care
                patientNames.add(appointment.getPatientID());
            }
        }

        // Convert the set to a list to return unique patient names
        return new ArrayList<>(patientNames);
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
    private String getSessionStatus(List<Appointment> appointments, LocalDate date, LocalTime sessionTime, String doctorID) {
        for (Appointment appointment : appointments) {
            LocalDate appointmentDate = toLocalDate(appointment.getAppointmentTime());
            LocalTime appointmentLocalTime = toLocalTime(appointment.getAppointmentTime());
    
            // Check if the appointment matches the date, time, and doctorID
            if (appointmentDate.equals(date) && 
                appointmentLocalTime.equals(sessionTime) && 
                appointment.getDoctorID().equals(doctorID)) {
                
                // Check if the session is blocked
                if (appointment.getAppointmentStatus().equalsIgnoreCase("Blocked")) {
                    return "Blocked";
                }
                // Otherwise, it is booked
                return "Booked";
            }
        }
        // If no matching appointment is found for the given doctor, the session is available
        return "Available"; 
    }
    
    
    public void rescheduleAppointmentByPatient(String patientID) {
        System.out.println("\n--- Reschedule an Appointment ---");
    
        // Retrieve all appointments for the patient
        List<Appointment> patientAppointments = apptData.getAllAppointments().stream()
                .filter(app -> app.getPatientID().equals(patientID) && app.getAppointmentStatus().equalsIgnoreCase("Booked"))
                .toList();
    
        if (patientAppointments.isEmpty()) {
            System.out.println("No appointments available for rescheduling.");
            return;
        }
    
        // Display patient's booked appointments
        System.out.println("Select an appointment to reschedule:");
        for (int i = 0; i < patientAppointments.size(); i++) {
            Appointment appointment = patientAppointments.get(i);
            LocalDate appointmentDate = toLocalDate(appointment.getAppointmentTime());
            LocalTime appointmentTime = toLocalTime(appointment.getAppointmentTime());
            System.out.printf("%d. Appointment ID: %s | Date: %s | Time: %s | Doctor: %s\n",
                    i + 1, appointment.getAppointmentID(), appointmentDate, appointmentTime, appointment.getDoctorName());
        }
    
        // Get the user's choice
        System.out.print("Enter the number of the appointment to reschedule: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        if (choice < 1 || choice > patientAppointments.size()) {
            System.out.println("Invalid selection.");
            return;
        }
    
        // Select the chosen appointment
        Appointment appointmentToReschedule = patientAppointments.get(choice - 1);
        String doctorID = appointmentToReschedule.getDoctorID();
    
        // Select a new date
        LocalDate newDate = selectDateFromSchedule(doctorID);
        if (newDate == null) {
            System.out.println("Rescheduling canceled. Returning to main menu...");
            return;
        }
    
        // Select a new session time for the chosen date
        LocalTime newTime = selectSessionFromDate(newDate, doctorID);
        if (newTime == null) {
            System.out.println("Rescheduling canceled. Returning to main menu...");
            return;
        }
    
        // Check if the selected session is available
        String sessionStatus = getSessionStatus(apptData.getAllAppointments(), newDate, newTime, doctorID);
        if (!sessionStatus.equalsIgnoreCase("Available")) {
            System.out.println("Selected session is not available. Please choose another session.");
            return;
        }
    
        // Confirm and update appointment details
        Calendar newAppointmentTime = Calendar.getInstance();
        newAppointmentTime.set(newDate.getYear(), newDate.getMonthValue() - 1, newDate.getDayOfMonth(),
                               newTime.getHour(), newTime.getMinute());
        appointmentToReschedule.setAppointmentTime(newAppointmentTime);
        appointmentToReschedule.setAppointmentStatus("PendingToDoctor"); // Update status for doctor approval
    
        // Save the updated appointment to the CSV
        apptData.updateAppointmentInCSV(appointmentToReschedule);
    
        System.out.println("Appointment successfully rescheduled and pending doctor approval.");
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
    
            // This method already has a null check on appointment times
            int bookedSessions = countBookedSessions(date, doctorID); 
            System.out.printf("%d. %s [%d/3]\n", (i + 1), date, bookedSessions); // Display with [x/3]
        }
    
        System.out.println("--------------------");
        System.out.print("Enter the date (1-7):");
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


//============================== Doctor [View Personal Schedule] =====================



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


private void viewSessionDetailsForDate(LocalDate date, String doctorID) {
    List<Appointment> appointments = viewAppointmentsByDoctor(doctorID);

    LocalTime[] allSessions = {
        LocalTime.of(9, 0),
        LocalTime.of(11, 0),
        LocalTime.of(14, 0)
    };

    // Filter sessions based on current time
    LocalTime now = LocalTime.now();
    LocalTime[] remainingSessions = java.util.Arrays.stream(allSessions)
        .filter(session -> session.isAfter(now) || !date.equals(LocalDate.now()))
        .toArray(LocalTime[]::new);

    // Check if there are any remaining sessions
    if (remainingSessions.length == 0) {
        System.out.println("There are no more sessions available to book on this date.");
    }
    // Display available sessions with their statuses, passing doctorID to getSessionStatus
    for (int i = 0; i < remainingSessions.length; i++) {
        String status = getSessionStatus(appointments, date, remainingSessions[i], doctorID);
        System.out.printf("%d. %s [%s]\n", (i + 1), remainingSessions[i], status);
    }

    System.out.println("--------------------");
    System.out.print("Select a session to view details (1-" + remainingSessions.length + ") or '~' to return: ");
    String input = scanner.nextLine().trim();

    if (input.equals("~")) {
        System.out.println("Returning to the previous menu...");
        return;
    }

    try {
        int sessionChoice = Integer.parseInt(input);

        if (sessionChoice < 1 || sessionChoice > remainingSessions.length) {
            System.out.println("Invalid selection. Please try again.");
            return;
        }

        LocalTime selectedSession = remainingSessions[sessionChoice - 1];
        printSessionDetailsAndManage(appointments, date, selectedSession, doctorID);
    } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a number (1-" + remainingSessions.length + ") or '~' to return.");
    }
}

    
    private LocalTime selectSessionFromDate(LocalDate date, String doctorID) {
    List<Appointment> appointments = viewAppointmentsByDoctor(doctorID);
    LocalTime[] sessions = {
        LocalTime.of(9, 0),
        LocalTime.of(11, 0),
        LocalTime.of(14, 0)
    };
    String doctorName = DoctorShared.getcsvUtilities2().getDoctorNameByID(doctorID);
    System.out.printf("\nAvailable Sessions for Dr. %s on %s:\n", doctorName, date);
    System.out.println("====================");

    for (int i = 0; i < sessions.length; i++) {
        String status = getSessionStatus(appointments, date, sessions[i], doctorID);
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

    for (Appointment appointment : appointments) {
        Calendar appointmentTime = appointment.getAppointmentTime();
        if (appointmentTime == null) continue;

        LocalDate appointmentDate = toLocalDate(appointmentTime);
        LocalTime appointmentLocalTime = toLocalTime(appointmentTime);

        if (appointmentDate.equals(date) && appointmentLocalTime.equals(sessionTime)) {
            hasAppointments = true;
            System.out.printf("Appointment ID: %s\n", appointment.getAppointmentID());
            System.out.printf("Patient ID: %s\n", appointment.getPatientID());
            System.out.printf("Patient Name: %s\n", appointment.getPatientName() != null ? appointment.getPatientName() : "N/A");
            System.out.printf("Doctor Name: %s\n", appointment.getDoctorName() != null ? appointment.getDoctorName() : "N/A");
            System.out.printf("Appointment Status: %s\n", appointment.getAppointmentStatus());
            System.out.printf("Diagnosis: %s\n", appointment.getDiagnosis() != null ? appointment.getDiagnosis() : "N/A");
            System.out.printf("Treatment Plan: %s\n", appointment.getTreatmentPlan() != null ? appointment.getTreatmentPlan() : "N/A");
            System.out.printf("Medicine: %s\n", appointment.getMedicine() != null ? appointment.getMedicine() : "N/A");
            System.out.printf("Medicine Status: %s\n", appointment.getMedicineStatus() != null ? appointment.getMedicineStatus() : "N/A");
            System.out.printf("Notes: %s\n", appointment.getNotes() != null ? appointment.getNotes() : "N/A");
            System.out.println("--------------------");
        }
    }

    if (!hasAppointments) {
        System.out.println("No appointments scheduled for this session.");
    }
}



private void printSessionDetailsAndManage(List<Appointment> appointments, LocalDate date, LocalTime sessionTime, String doctorID) {
    // Display the session details for the date and time
    System.out.printf("\nDetails for session at %s on %s:\n", sessionTime, date);
    System.out.println("====================");

    String doctorName = DoctorShared.getcsvUtilities2().getDoctorNameByID(doctorID);

    // Check session status to decide if we should allow creation or blocking
    String status = getSessionStatus(appointments, date, sessionTime, doctorID);

    // Offer options based on the session status
    switch (status) {
        case "Available" -> {
            System.out.println("This session is available.");
            System.out.print("Do you want to block this session or create a new appointment? (block/create): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("block")) {
                blockSession(date, sessionTime, doctorID, doctorName);
            } else if (input.equalsIgnoreCase("create")) {
                newAppointment(date, sessionTime, doctorID, doctorName);
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
        default -> System.out.println("Unknown status for the session.");
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
                
                // Skip this appointment if appointmentTime is null
                if (appointmentTime == null) continue;
        
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
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
    
        if (date.isBefore(currentDate) || (date.isEqual(currentDate) && time.isBefore(currentTime))) {
            System.out.println("Sorry, the session you selected has elapsed. Please book another session. Thank you.");
            return;
        }
    
        // Check if the session is available specifically for the given doctor ID
        String status = getSessionStatus(apptData.getAllAppointments(), date, time, doctorID);
    
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
            System.out.println("Session blocked successfully for Dr. " + doctorName + ".");
        } else {
            System.out.println("This session is already booked or blocked for Dr. " + doctorName + ".");
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
        // Prompt the doctor to enter patient details
        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine().trim();
    
        // Validate if the patient exists
        String patientName = DoctorShared.getcsvUtilities().getPatientNameByID(patientID);
        if (patientName == null || patientName.isEmpty()) {
            System.out.println("Error: Patient ID not found. Please check the ID and try again.");
            return; // Exit if the patient does not exist
        }
    
        System.out.println("The patient name is: " + patientName);
    
        // Generate a new appointment ID
        String newAppointmentID = generateNewAppointmentID();
    
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
        System.out.println("===========================================");
    }
    
    //==============================================================================================
    public void printUpcomingSessions(String doctorID, String userType) {
        List<Appointment> doctorAppointments = viewAppointmentsByDoctor(doctorID);
    
        // Filter out blocked, completed, and cancelled appointments, then sort by Appointment ID
        List<Appointment> filteredAppointments = doctorAppointments.stream()
                .filter(appointment -> !appointment.getAppointmentStatus().equalsIgnoreCase("Blocked"))
                .filter(appointment -> !appointment.getAppointmentStatus().equalsIgnoreCase("Completed"))
                .filter(appointment -> !appointment.getAppointmentStatus().toLowerCase().contains("cancelled"))
                .sorted((a1, a2) -> a1.getAppointmentID().compareTo(a2.getAppointmentID()))
                .toList();
    
        // Display each filtered appointment in a box-like format
        filteredAppointments.forEach(appointment -> {
            Calendar appointmentTime = appointment.getAppointmentTime();
            LocalDate appointmentDate = LocalDate.of(
                    appointmentTime.get(Calendar.YEAR),
                    appointmentTime.get(Calendar.MONTH) + 1,
                    appointmentTime.get(Calendar.DAY_OF_MONTH)
            );
    
            System.out.println("===========================================");
            System.out.printf("| Appointment ID    : %s\n", appointment.getAppointmentID());
            System.out.printf("| Date              : %s\n", appointmentDate);
            System.out.printf("| Patient ID        : %s\n", appointment.getPatientID());
            System.out.printf("| Patient Name      : %s\n", appointment.getPatientName());
            System.out.printf("| Appointment Status: %s\n", appointment.getAppointmentStatus());
            System.out.println("===========================================");
            System.out.println(); // Add an extra line for spacing between appointments
        });
    
        // Check if there are any upcoming sessions to display
        if (filteredAppointments.isEmpty()) {
            System.out.println("No upcoming sessions available.");
        } else {
            while (true) {
                System.out.print("\nEnter the Appointment ID to manage (or '~' to return): ");
                String appointmentID = scanner.nextLine().trim();
    
                // Exit if user types '~'
                if (appointmentID.equals("~")) {
                    System.out.println("Returning to the previous menu...");
                    return;
                }
    
                // Check if the entered Appointment ID exists in the filtered list
                Appointment selectedAppointment = filteredAppointments.stream()
                        .filter(app -> app.getAppointmentID().equals(appointmentID))
                        .findFirst()
                        .orElse(null);
    
                if (selectedAppointment != null) {
                    // Valid appointment ID found; proceed to manage it
                    manageScheduledAppointments(appointmentID, userType);  // Pass userType to manageScheduledAppointments
                    break;
                } else {
                    // Invalid appointment ID; prompt the user to try again
                    System.out.println("Invalid Appointment ID. Please try again.");
                }
            }
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
            // Format the cancellation message based on userType
            if ("Doctor".equalsIgnoreCase(userType)) {
                System.out.printf("The patient with ID %s has been informed to rebook a session.\n", appointment.getPatientID());
    
                // Update the appointment status with details of doctor cancellation
                appointment.setAppointmentStatus(
                    String.format("Cancelled by Dr. %s, ID %s on Session: %s",
                                  appointment.getDoctorName(),
                                  appointment.getDoctorID(),
                                  toLocalDate(appointment.getAppointmentTime()) + " " + toLocalTime(appointment.getAppointmentTime()))
                );
    
            } else if ("Patient".equalsIgnoreCase(userType)) {
                System.out.printf("The doctor with ID %s has been informed to rebook a session.\n", appointment.getDoctorID());
    
                // Update the appointment status with details of patient cancellation
                appointment.setAppointmentStatus(
                    String.format("Cancelled by Patient %s, ID %s on Session: %s",
                                  appointment.getPatientName(),
                                  appointment.getPatientID(),
                                  toLocalDate(appointment.getAppointmentTime()) + " " + toLocalTime(appointment.getAppointmentTime()))
                );
            }
    
            // Set the outcome to "Cancelled"
            appointment.setDiagnosis("Cancelled");
    
            // Set the appointment time to a placeholder value instead of null
            Calendar placeholderTime = Calendar.getInstance();
            placeholderTime.set(0, Calendar.JANUARY, 1, 0, 0, 0);
            appointment.setAppointmentTime(placeholderTime);
    
            // Save the updated status and outcome to the CSV
            apptData.updateAppointmentInCSV(appointment);
            System.out.println("Appointment successfully marked as cancelled.");
    
        } else {
            System.out.println("Appointment not found. Unable to cancel.");
        }
    }
    

    private void rescheduleAppointment(Appointment appointment, String userType) {
        // Check if the appointment is pending for the patient to view
        if ("PendingToPatient".equalsIgnoreCase(appointment.getAppointmentStatus())) {
            System.out.println("The patient has not yet seen the previous message.");
            System.out.print("Are you sure you want to reschedule? (y/n): ");
            
            String confirmation = scanner.nextLine().trim();
            
            if (!confirmation.equalsIgnoreCase("y")) {
                System.out.println("Rescheduling canceled. Returning to the previous menu...");
                return;
            }
        }
    
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
        String status = getSessionStatus(apptData.getAllAppointments(), newDate, newTime, appointment.getDoctorID());
    
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
    LocalDateTime now = LocalDateTime.now(); // Capture the current date and time

    appointments.stream()
            .filter(app -> app.getPatientID().equals(patientID))
            .filter(app -> {
                Calendar appointmentTime = app.getAppointmentTime();
                // Exclude appointments with placeholder date and time (0001-01-01 00:00)
                return !(appointmentTime.get(Calendar.YEAR) == 1 &&
                         appointmentTime.get(Calendar.MONTH) == Calendar.JANUARY &&
                         appointmentTime.get(Calendar.DAY_OF_MONTH) == 1 &&
                         appointmentTime.get(Calendar.HOUR_OF_DAY) == 0 &&
                         appointmentTime.get(Calendar.MINUTE) == 0);
            })
            .filter(app -> !app.getAppointmentStatus().equalsIgnoreCase("Completed")) // Exclude "Completed" status
            .filter(app -> !app.getAppointmentStatus().toLowerCase().contains("cancelled")) // Exclude statuses with "cancelled"
            .filter(app -> {
                // Exclude "PendingToDoctor" or "PendingToPatient" appointments if the session time has passed
                if (app.getAppointmentStatus().equalsIgnoreCase("PendingToDoctor") ||
                    app.getAppointmentStatus().equalsIgnoreCase("PendingToPatient")) {
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
                    LocalDateTime appointmentDateTime = LocalDateTime.of(appointmentDate, appointmentLocalTime);
                    return appointmentDateTime.isAfter(now); // Only include if the appointment is in the future
                }
                return true; // Include all other statuses
            })
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

                System.out.println("===========================================");
                System.out.printf("| Appointment ID : %s\n", app.getAppointmentID());
                System.out.printf("| Date           : %s\n", date);
                System.out.printf("| Time           : %s\n", time);
                System.out.printf("| Doctor         : %s\n", app.getDoctorName());
                System.out.printf("| Status         : %s\n", app.getAppointmentStatus());
                System.out.println("===========================================");
            });

    // Check if there are no upcoming appointments
    boolean hasUpcomingAppointments = appointments.stream()
            .filter(app -> app.getPatientID().equals(patientID))
            .filter(app -> {
                Calendar appointmentTime = app.getAppointmentTime();
                return !(appointmentTime.get(Calendar.YEAR) == 1 &&
                         appointmentTime.get(Calendar.MONTH) == Calendar.JANUARY &&
                         appointmentTime.get(Calendar.DAY_OF_MONTH) == 1 &&
                         appointmentTime.get(Calendar.HOUR_OF_DAY) == 0 &&
                         appointmentTime.get(Calendar.MINUTE) == 0);
            })
            .anyMatch(app -> 
                !app.getAppointmentStatus().equalsIgnoreCase("Completed") && 
                !app.getAppointmentStatus().toLowerCase().contains("cancelled") &&
                (!app.getAppointmentStatus().equalsIgnoreCase("PendingToDoctor") && 
                 !app.getAppointmentStatus().equalsIgnoreCase("PendingToPatient") ||
                 LocalDateTime.of(app.getAppointmentTime().get(Calendar.YEAR),
                                  app.getAppointmentTime().get(Calendar.MONTH) + 1,
                                  app.getAppointmentTime().get(Calendar.DAY_OF_MONTH),
                                  app.getAppointmentTime().get(Calendar.HOUR_OF_DAY),
                                  app.getAppointmentTime().get(Calendar.MINUTE)).isAfter(now))
            );

    if (!hasUpcomingAppointments) {
        System.out.println("No upcoming appointments.");
    }
}

    
    
    public void bookNewAppointment(String patientID, String patientName, String doctorID, String doctorName, String initiator) {
    System.out.println("\nBooking a New Appointment:");

    // Validate if the patient exists
    patientName = DoctorShared.getcsvUtilities().getPatientNameByID(patientID);
    if (patientName == null || patientName.isEmpty()) {
        System.out.println("Error: Patient ID not found. Booking cannot proceed.");
        return; // Exit if the patient does not exist
    }

    System.out.println("The patient name is: " + patientName);

    // Validate if the doctor exists
    doctorName = PatientShared.getCSVUtilities().getDoctorNameByID(doctorID);
    if (doctorName == null || doctorName.isEmpty()) {
        System.out.println("Error: Doctor ID not found. Booking cannot proceed.");
        return; // Exit if the doctor does not exist
    }

    //System.out.println("The doctor name is: " + doctorName);

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

    // Check if the selected date and time have already passed
    if (appointmentDate.isBefore(LocalDate.now()) || 
        (appointmentDate.isEqual(LocalDate.now()) && appointmentTime.isBefore(LocalTime.now()))) {
        System.out.println("There are no more sessions available to book on this date.");
        scanner.nextLine(); // Wait for user to press any key
        return;
    }

    // Check the status of the selected session
    String sessionStatus = getSessionStatus(apptData.getAllAppointments(), appointmentDate, appointmentTime, doctorID);
    if (!sessionStatus.equalsIgnoreCase("Available")) {
        System.out.println("Error: The selected session is already " + sessionStatus + ". Please choose another session.");
        return;
    }

    // Generate a new appointment ID
    String newAppointmentID = generateNewAppointmentID();

    // Set appointment status based on initiator
    String appointmentStatus = initiator.equalsIgnoreCase("patient") ? "PendingToDoctor" : "PendingToPatient";

    // Create the new appointment object
    Calendar newAppointmentTime = Calendar.getInstance();
    newAppointmentTime.set(appointmentDate.getYear(), appointmentDate.getMonthValue() - 1,
                           appointmentDate.getDayOfMonth(), appointmentTime.getHour(), appointmentTime.getMinute());

    Appointment newAppointment = new DoctorAppointment(
            newAppointmentID, newAppointmentTime, patientID, patientName,
            doctorID, doctorName, appointmentStatus);

    // Save the new appointment to the CSV
    apptData.addAppointment(newAppointment);
    System.out.println("Appointment successfully booked with ID: " + newAppointmentID);
    System.out.println("===========================================");
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
// Updated Helper method to convert Calendar to LocalDate
private LocalDate toLocalDate(Calendar calendar) {
    if (calendar == null) return null;
    return LocalDate.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
    );
}

// Updated Helper method to convert Calendar to LocalTime
private LocalTime toLocalTime(Calendar calendar) {
    if (calendar == null) return null;
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
            }
        }
    }
}

//==============================Print Completed Sessions===================

public void printCompletedSessions(String doctorID) {
    List<Appointment> appointments = apptData.getAllAppointments();

    // Filter completed appointments for the specific doctor
    List<Appointment> completedAppointments = appointments.stream()
            .filter(app -> app.getDoctorID().equals(doctorID))
            .filter(app -> app.getAppointmentStatus().equalsIgnoreCase("Completed"))
            .toList();

    if (completedAppointments.isEmpty()) {
        System.out.println("No completed sessions to display.");
        return;
    }

    System.out.println("\nCompleted Sessions for Dr. " + doctorID + ":");
    System.out.println("==============================");

    for (Appointment app : completedAppointments) {
        Calendar appointmentTime = app.getAppointmentTime();
        LocalDate appointmentDate = toLocalDate(appointmentTime);
        LocalTime appointmentLocalTime = toLocalTime(appointmentTime);

        System.out.printf("Appointment ID: %s\n", app.getAppointmentID());
        System.out.printf("Date: %s\n", appointmentDate);
        System.out.printf("Time: %s\n", appointmentLocalTime);
        System.out.printf("Patient ID: %s\n", app.getPatientID());
        System.out.printf("Patient Name: %s\n", app.getPatientName());
        System.out.printf("Status: %s\n", app.getAppointmentStatus());
        System.out.println("------------------------------");
    }
}

//===========================Fill in Completed Sessions==============+

public void fillCompletedSessions(String doctorID, boolean isEdit) {
    List<Appointment> appointments = apptData.getAllAppointments();

    // Filter for completed appointments specific to the doctor, including both "Completed" and "Completed*"
    List<Appointment> completedAppointments = appointments.stream()
            .filter(app -> app.getDoctorID().equals(doctorID))
            .filter(app -> app.getAppointmentStatus().equalsIgnoreCase("Completed") 
                        || app.getAppointmentStatus().equalsIgnoreCase("Completed*")) // Include both statuses
            .toList();

    if (completedAppointments.isEmpty()) {
        System.out.println("No completed sessions available for updates.");
        return;
    }

    System.out.println("\nSelect a completed session to " + (isEdit ? "edit" : "fill") + " details:");
    for (int i = 0; i < completedAppointments.size(); i++) {
        Appointment app = completedAppointments.get(i);
        Calendar appointmentTime = app.getAppointmentTime();
        LocalDate appointmentDate = toLocalDate(appointmentTime);
        LocalTime appointmentLocalTime = toLocalTime(appointmentTime);
        
        System.out.printf("%d. Appointment ID: %s | Date: %s | Time: %s | Patient: %s\n",
                i + 1, app.getAppointmentID(), appointmentDate, appointmentLocalTime, app.getPatientName());
    }

    System.out.print("Enter the number of the session you want to " + (isEdit ? "edit" : "fill") + " details for: ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if (choice < 1 || choice > completedAppointments.size()) {
        System.out.println("Invalid selection. Return2ing to main menu...");
        return;
    }

    // Select the chosen completed appointment
    Appointment selectedAppointment = completedAppointments.get(choice - 1);

    // Display a summary of the appointment details before filling or editing
    System.out.println("\nCurrent Appointment Details:");
    System.out.println("--------------------------------------------------");
    Calendar appointmentTime = selectedAppointment.getAppointmentTime();
    LocalDate appointmentDate = toLocalDate(appointmentTime);
    LocalTime appointmentLocalTime = toLocalTime(appointmentTime);
    System.out.printf("| %-15s | %-20s |\n", "Field", "Details");
    System.out.println("--------------------------------------------------");
    System.out.printf("| %-15s | %-20s |\n", "Appointment ID", selectedAppointment.getAppointmentID());
    System.out.printf("| %-15s | %-20s |\n", "Date", appointmentDate);
    System.out.printf("| %-15s | %-20s |\n", "Time", appointmentLocalTime);
    System.out.printf("| %-15s | %-20s |\n", "Patient ID", selectedAppointment.getPatientID());
    System.out.printf("| %-15s | %-20s |\n", "Patient Name", selectedAppointment.getPatientName());
    System.out.printf("| %-15s | %-20s |\n", "Status", selectedAppointment.getAppointmentStatus());
    System.out.println("--------------------------------------------------");
    
    CreateRecord create = new CreateRecord(doctorID);
    create.createRecord();

    
}


public void manageCompletedSessions(String doctorID, boolean isEdit) {
    List<Appointment> appointments = apptData.getAllAppointments();
    String targetStatus = isEdit ? "Completed*" : "Completed"; // Set target status based on edit mode

    // Filter for appointments specific to the doctor and with the target status
    List<Appointment> targetAppointments = appointments.stream()
            .filter(app -> app.getDoctorID().equals(doctorID))
            .filter(app -> app.getAppointmentStatus().equalsIgnoreCase(targetStatus))
            .toList();

    // Check if there are any matching sessions
    if (targetAppointments.isEmpty()) {
        System.out.println("No " + (isEdit ? "editable" : "newly completed") + " sessions available.");
        System.out.println("Please choose another option.");
        return; // Exit the method and return to the main menu
    }

    System.out.println("\nSelect a " + (isEdit ? "completed*" : "completed") + " session to " + (isEdit ? "edit" : "fill") + " details:");
    for (int i = 0; i < targetAppointments.size(); i++) {
        Appointment app = targetAppointments.get(i);
        Calendar appointmentTime = app.getAppointmentTime();
        LocalDate appointmentDate = toLocalDate(appointmentTime);
        LocalTime appointmentLocalTime = toLocalTime(appointmentTime);
        
        System.out.printf("%d. Appointment ID: %s | Date: %s | Time: %s | Patient: %s\n",
                i + 1, app.getAppointmentID(), appointmentDate, appointmentLocalTime, app.getPatientName());
    }

    System.out.print("Enter the number of the session you want to " + (isEdit ? "edit" : "fill") + " details for: ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    if (choice < 1 || choice > targetAppointments.size()) {
        System.out.println("Invalid selection. Returning to main menu...");
        return;
    }

    // Select the chosen appointment
    Appointment selectedAppointment = targetAppointments.get(choice - 1);

    // Display and update details of the selected appointment
    displayAndFillAppointmentDetails(selectedAppointment, isEdit, doctorID);
}


private void displayAndFillAppointmentDetails(Appointment selectedAppointment, boolean isEdit, String doctorID) {
    System.out.println("\nCurrent Appointment Details:");
System.out.println("--------------------------------------------------");
System.out.printf("| %-15s | %-20s |\n", "Field", "Details");
System.out.println("--------------------------------------------------");
Calendar appointmentTime = selectedAppointment.getAppointmentTime();
LocalDate appointmentDate = toLocalDate(appointmentTime);
LocalTime appointmentLocalTime = toLocalTime(appointmentTime);

System.out.printf("| %-15s | %-20s |\n", "Appointment ID", selectedAppointment.getAppointmentID());
System.out.printf("| %-15s | %-20s |\n", "Date", appointmentDate);
System.out.printf("| %-15s | %-20s |\n", "Time", appointmentLocalTime);
System.out.printf("| %-15s | %-20s |\n", "Patient ID", selectedAppointment.getPatientID());
System.out.printf("| %-15s | %-20s |\n", "Patient Name", selectedAppointment.getPatientName());
System.out.println("--------------------------------------------------");



    // Save the updated appointment to the CSV file
    apptData.updateAppointmentInCSV(selectedAppointment);
    CreateRecord create = new CreateRecord(doctorID);
    create.createRecord(selectedAppointment.getPatientID(),selectedAppointment.getPatientName());

    System.out.println("Appointment details " + (isEdit ? "edited" : "filled") + " and saved successfully.");
}




//========================print for Patients===========================

public void printCompletedSessionsPatient(String patientID) {
    List<Appointment> appointments = apptData.getAllAppointments();

    // Filter completed appointments for the specific patient
    List<Appointment> completedAppointments = appointments.stream()
            .filter(app -> app.getPatientID().equals(patientID))
            .filter(app -> app.getAppointmentStatus().equalsIgnoreCase("Completed"))
            .toList();

    if (completedAppointments.isEmpty()) {
        System.out.println("No completed sessions to display.");
        return;
    }

    System.out.println("\nCompleted Sessions for Patient " + patientID + ":");
    System.out.println("==============================");

    int count = 1;
    for (Appointment app : completedAppointments) {
        Calendar appointmentTime = app.getAppointmentTime();
        LocalDate appointmentDate = toLocalDate(appointmentTime);
        LocalTime appointmentLocalTime = toLocalTime(appointmentTime);

        // Summary line for the appointment
        System.out.printf("%d. Appointment ID: %s | Date: %s | Time: %s | Doctor: %s (%s)\n",
                count, app.getAppointmentID(), appointmentDate, appointmentLocalTime,
                app.getDoctorName(), app.getDoctorID());
        
        // Detailed information
        System.out.println("   Diagnosis: " + app.getDiagnosis());
        System.out.println("   Treatment Plan Provided: " + app.getTreatmentPlan());
        System.out.println("   Prescribed Medicine: " + app.getMedicine());
        System.out.println("   Medicine Status: " + app.getMedicineStatus());
        System.out.println("   Additional Notes: " + app.getNotes());
        System.out.println("------------------------------");
        
        count++;
    }
}


//=======================Handles cancelled appts=======================
public void printCancelledAppointments(String userType, String userID) {
    List<Appointment> appointments = apptData.getAllAppointments();
    
    // Check if there are any appointments with status containing "cancelled"
    boolean hasCancelledAppointments = appointments.stream()
            .anyMatch(app -> app.getAppointmentStatus().toLowerCase().contains("cancelled"));
    
    if (!hasCancelledAppointments) {
        System.out.println("No canceled appointments to display.");
        return;
    }

    // Filter canceled appointments with placeholder date "0001-01-01 00:00" and based on user type
    List<Appointment> cancelledAppointments = appointments.stream()
            .filter(app -> app.getAppointmentStatus().toLowerCase().contains("cancelled"))
            .filter(app -> {
                Calendar appointmentTime = app.getAppointmentTime();
                return appointmentTime != null &&
                       appointmentTime.get(Calendar.YEAR) == 1 &&
                       appointmentTime.get(Calendar.MONTH) == Calendar.JANUARY &&
                       appointmentTime.get(Calendar.DAY_OF_MONTH) == 1;
            })
            .filter(app -> (userType.equalsIgnoreCase("doctor") && 
                            app.getDoctorID().equals(userID)) ||
                           (userType.equalsIgnoreCase("patient") && 
                            app.getPatientID().equals(userID)))
            .toList();

    if (cancelledAppointments.isEmpty()) {
        System.out.println("No canceled appointments to display for your specific criteria.");
        return;
    }

    // Display canceled appointments
    System.out.println("\nThese are your canceled appointments:");
    cancelledAppointments.forEach(app -> {
        System.out.printf("Appointment ID: %s\n", app.getAppointmentID());
        System.out.printf("Patient ID: %s\n", app.getPatientID());
        System.out.printf("Patient Name: %s\n", app.getPatientName());
        System.out.printf("Doctor ID: %s\n", app.getDoctorID());
        System.out.printf("Doctor Name: %s\n", app.getDoctorName());
        System.out.printf("Status: %s\n", app.getAppointmentStatus());
        System.out.println("------------------------------");
    });

    // Prompt for specific appointment to manage
    System.out.print("\nKey an Appointment ID you want to edit: ");
    String selectedAppointmentID = scanner.nextLine().trim();

    // Find the selected appointment
    Appointment selectedAppointment = cancelledAppointments.stream()
            .filter(app -> app.getAppointmentID().equals(selectedAppointmentID))
            .findFirst()
            .orElse(null);

    if (selectedAppointment == null) {
        System.out.println("Invalid Appointment ID. Returning to the main menu...");
        return;
    }

    // Display options for managing the selected appointment
    System.out.println("\nSelect an option:");
    System.out.println("1. Acknowledge the cancellation (delete)");
    System.out.println("2. Rebook the appointment");
    System.out.println("3. Exit");

    System.out.print("Enter your choice (1-3): ");
    int choice = scanner.nextInt();
    scanner.nextLine(); // Consume newline

    switch (choice) {
        case 1 -> {
            // Acknowledge and delete the appointment
            apptData.deleteAppointment(selectedAppointmentID);
            System.out.println("Appointment acknowledged and deleted successfully.");
            scanner.nextLine(); // Wait for user to press any key

        }
        case 2 -> {
            // Book a new appointment
            System.out.println("Proceeding to book a new appointment...");
        
            if (userType.equalsIgnoreCase("doctor")) {
                bookNewAppointment(
                        selectedAppointment.getPatientID(),
                        selectedAppointment.getPatientName(),
                        selectedAppointment.getDoctorID(),
                        selectedAppointment.getDoctorName(), "doctor");
            } else if (userType.equalsIgnoreCase("patient")) {
                bookNewAppointment(
                        selectedAppointment.getPatientID(),
                        selectedAppointment.getPatientName(),
                        selectedAppointment.getDoctorID(),
                        selectedAppointment.getDoctorName(), "patient");
            }
        
            // Delete the original canceled appointment after attempting the new booking
            apptData.deleteAppointment(selectedAppointmentID);
            System.out.println("Original canceled appointment deleted successfully.");
        }
        case 3 -> System.out.println("Exiting without changes.");
        default -> System.out.println("Invalid choice. Returning to the main menu...");
    }
}



//==================Admin============================
public void printDoctorScheduleOnDateAdmin(String doctorID) {
    LocalDate selectedDate = selectDateFromSchedule(doctorID);

    if (selectedDate == null) {
        // User canceled the selection
        return;
    }

    System.out.printf("\nAdmin View - Sessions for Dr. %s on %s:\n", doctorID, selectedDate);
    System.out.println("====================");

    // Use the admin version of viewing session details with edit capabilities
    viewSessionDetailsForDateAdmin(selectedDate, doctorID);
}
private void viewSessionDetailsForDateAdmin(LocalDate date, String doctorID) {
    List<Appointment> appointments = viewAppointmentsByDoctor(doctorID);
    LocalTime[] sessions = {
        LocalTime.of(9, 0),
        LocalTime.of(11, 0),
        LocalTime.of(14, 0)
    };

    // Display sessions with admin-level editing capabilities
    for (int i = 0; i < sessions.length; i++) {
        String status = getSessionStatus(appointments, date, sessions[i], doctorID);
        System.out.printf("%d. %s [%s]\n", (i + 1), sessions[i], status);
    }

    System.out.println("--------------------");
    System.out.print("Select a session to manage (1-3) or '~' to return: ");
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
        printSessionDetailsAndManageAdmin(appointments, date, selectedSession, doctorID);
    } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a number (1-3) or '~' to return.");
    }
}

private void printSessionDetailsAndManageAdmin(List<Appointment> appointments, LocalDate date, LocalTime sessionTime, String doctorID) {
    printSessionDetailsForDate(date, sessionTime, doctorID);

    String status = getSessionStatus(appointments, date, sessionTime, doctorID);

    // Handle options based on the session status with admin privileges
    switch (status) {
        case "Available" -> {
            System.out.println("This session is available.");
            System.out.print("Do you want to block this session or create a new appointment? (block/create): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("block")) {
                blockSession(date, sessionTime, doctorID, "Dr. Admin");
            } else if (input.equalsIgnoreCase("create")) {
                newAppointment(date, sessionTime, doctorID, "Dr. Admin");
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
// ================= Reschedule for patients===========
public void printUpcomingPatientSessions(String patientID, String userType) {
    System.out.println("Upcoming Appointments for Patient ID: " + patientID);
    
    // Call printPatientAppointments to display upcoming appointments for the patient
    printPatientAppointments(patientID);

    // Prompt for Appointment ID to manage if there are any appointments
    List<Appointment> patientAppointments = apptData.getAllAppointments().stream()
            .filter(app -> app.getPatientID().equals(patientID))
            .filter(app -> !app.getAppointmentStatus().equalsIgnoreCase("Blocked"))
            .toList();

    if (patientAppointments.isEmpty()) {
        System.out.println("No upcoming appointments available.");
        return;
    }

    System.out.print("\nEnter the Appointment ID to manage: ");
    String appointmentID = scanner.nextLine().trim();
    manageScheduledAppointmentsForPatient(appointmentID, userType, patientID);  // Pass userType and patientID
}

private void manageScheduledAppointmentsForPatient(String appointmentID, String userType, String patientID) {
    Appointment appointment = apptData.getAllAppointments().stream()
            .filter(app -> app.getAppointmentID().equals(appointmentID) && app.getPatientID().equals(patientID))
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
        case 1 -> cancelAppointmentByPatient(appointmentID, userType); // Pass userType to cancelAppointmentByPatient
        case 2 -> rescheduleAppointmentByPatient(appointment, userType); // Pass userType to rescheduleAppointmentByPatient
        default -> System.out.println("Invalid option. Returning to the previous menu...");
    }
}

private void cancelAppointmentByPatient(String appointmentID, String userType) {
    Appointment appointment = apptData.getAllAppointments().stream()
            .filter(app -> app.getAppointmentID().equals(appointmentID))
            .findFirst()
            .orElse(null);

    if (appointment != null) {
        System.out.printf("The doctor with ID %s has been informed of your cancellation.\n", appointment.getDoctorID());

        // Update the appointment status with details of patient cancellation
        appointment.setAppointmentStatus(
            String.format("Cancelled by Patient %s, ID %s on Session: %s",
                          appointment.getPatientName(),
                          appointment.getPatientID(),
                          toLocalDate(appointment.getAppointmentTime()) + " " + toLocalTime(appointment.getAppointmentTime()))
        );

        // Set the outcome to "Cancelled"
        appointment.setDiagnosis("Cancelled");

        // Set the appointment time to a placeholder value instead of null
        Calendar placeholderTime = Calendar.getInstance();
        placeholderTime.set(0, Calendar.JANUARY, 1, 0, 0, 0);
        appointment.setAppointmentTime(placeholderTime);

        // Save the updated status and outcome to the CSV
        apptData.updateAppointmentInCSV(appointment);
        System.out.println("Appointment successfully marked as cancelled.");
    } else {
        System.out.println("Appointment not found. Unable to cancel.");
    }
}

private void rescheduleAppointmentByPatient(Appointment appointment, String userType) {
    // Check if the appointment is pending for the doctor to view
    if ("PendingToDoctor".equalsIgnoreCase(appointment.getAppointmentStatus())) {
        System.out.println("The doctor has not yet seen the previous message.");
        System.out.print("Are you sure you want to reschedule? (y/n): ");
        
        String confirmation = scanner.nextLine().trim();
        
        if (!confirmation.equalsIgnoreCase("y")) {
            System.out.println("Rescheduling canceled. Returning to the previous menu...");
            return;
        }
    }

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
    String status = getSessionStatus(apptData.getAllAppointments(), newDate, newTime, appointment.getDoctorID());

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
    appointment.setAppointmentStatus("PendingToDoctor");

    // Save the updated appointment to the CSV
    apptData.updateAppointmentInCSV(appointment);

    System.out.println("Appointment successfully rescheduled and saved.");
}

public void cancelOrRescheduleAppointment(String patientID) {
    // Step 1: Call printPatientAppointments to display the scheduled bookings
    printPatientAppointments(patientID);

    // Step 2: Prompt the user to enter the Appointment ID they want to edit
    System.out.print("\nEnter the Appointment ID you want to edit (or '~' to return): ");
    String appointmentID = scanner.nextLine().trim();

    if (appointmentID.equals("~")) {
        System.out.println("Returning to the previous menu...");
        return;
    }

    // Find the selected appointment
    Appointment selectedAppointment = apptData.getAllAppointments().stream()
            .filter(app -> app.getPatientID().equals(patientID))
            .filter(app -> app.getAppointmentID().equals(appointmentID))
            .findFirst()
            .orElse(null);

    if (selectedAppointment == null) {
        System.out.println("Invalid Appointment ID. Please try again.");
        return;
    }

    // Step 3: Prompt the user for action (Cancel or Reschedule)
    System.out.println("\nSelect an option:");
    System.out.println("1. Cancel the appointment");
    System.out.println("2. Reschedule the appointment");
    System.out.print("Enter your choice (1-2): ");
    String choice = scanner.nextLine().trim();

    switch (choice) {
        case "1" -> {
            // Option 1: Cancel appointment
            if ("PendingToDoctor".equalsIgnoreCase(selectedAppointment.getAppointmentStatus())) {
                // Immediate cancellation for PendingToDoctor
                apptData.deleteAppointment(selectedAppointment.getAppointmentID());
            } else if ("Booked".equalsIgnoreCase(selectedAppointment.getAppointmentStatus())) {
                // Call cancelAppointmentByPatient for booked appointments
                cancelAppointmentByPatient(selectedAppointment.getAppointmentID(), "Patient");
            } else {
                System.out.println("Appointment cannot be canceled in its current status.");
            }
        }
        case "2" -> {
            // Option 2: Reschedule appointment
            rescheduleAppointmentByPatient(selectedAppointment, "Patient");
        }
        default -> System.out.println("Invalid choice. Returning to the previous menu...");
    }
}
public void printAllBookedSchedules() {
    System.out.println("\n--- All Booked Schedules ---");

    // Retrieve all appointments
    List<Appointment> appointments = apptData.getAllAppointments();

    // Counter for listing the appointments
    int count = 1;

    // Check if there are any appointments
    if (appointments.isEmpty()) {
        System.out.println("No booked schedules available.");
        return;
    }

    // Loop through each appointment and display its details
    for (Appointment appointment : appointments) {
        // Only print booked appointments
        if (appointment.getAppointmentStatus().equalsIgnoreCase("Booked")) {
            LocalDate date = toLocalDate(appointment.getAppointmentTime());
            LocalTime time = toLocalTime(appointment.getAppointmentTime());

            System.out.println("===========================================");
            System.out.printf("| %d. Appointment ID    : %s\n", count++, appointment.getAppointmentID());
            System.out.printf("| Date                  : %s\n", date);
            System.out.printf("| Time                  : %s\n", time);
            System.out.printf("| Doctor                : %s\n", appointment.getDoctorName());
            System.out.printf("| Doctor ID             : %s\n", appointment.getDoctorID());
            System.out.printf("| Patient ID            : %s\n", appointment.getPatientID());
            System.out.printf("| Patient Name          : %s\n", appointment.getPatientName());
            System.out.println("===========================================");
        }
    }
    // Message if no booked appointments were found
    if (count == 1) {
        System.out.println("No booked schedules available.");
    }
}
}