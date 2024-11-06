package appt;

import Doctors.DoctorShared;
import java.time.LocalDate;
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
                //System.out.println("booked");
                return "Booked";
            } 
        }
        // If no matching appointment is found, the session is available
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
        String sessionStatus = getSessionStatus(apptData.getAllAppointments(), newDate, newTime);
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
            printSessionDetailsAndManage(appointments, date, selectedSession,doctorID);
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
    String status = getSessionStatus(appointments, date, sessionTime);

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
    }
    
    //==============================================================================================
    public void printUpcomingSessions(String doctorID, String userType) { //here
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
            // Format the cancellation message based on userType
            if ("Doctor".equalsIgnoreCase(userType)) {
                System.out.printf("The patient with ID %s has been informed to rebook a session.\n", appointment.getPatientID());
    
                // Update the appointment status with details of doctor cancellation
                appointment.setAppointmentStatus(
                    String.format("Cancelled by %s, ID %s on Session: %s",
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
                .filter(app -> {
                    Calendar appointmentTime = app.getAppointmentTime();
                    // Exclude appointments with placeholder date and time (0001-01-01 00:00)
                    return !(appointmentTime.get(Calendar.YEAR) == 1 &&
                             appointmentTime.get(Calendar.MONTH) == Calendar.JANUARY &&
                             appointmentTime.get(Calendar.DAY_OF_MONTH) == 1 &&
                             appointmentTime.get(Calendar.HOUR_OF_DAY) == 0 &&
                             appointmentTime.get(Calendar.MINUTE) == 0);
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
    
                    System.out.printf("Appointment ID: %s\n", app.getAppointmentID());
                    System.out.printf("Date: %s\n", date);
                    System.out.printf("Time: %s\n", time);
                    System.out.printf("Doctor: %s\n", app.getDoctorName());
                    System.out.printf("Status: %s\n", app.getAppointmentStatus());
                    System.out.println("------------------------------");
                });
    
        // Check if there are no upcoming appointments
        if (appointments.stream()
                .filter(app -> app.getPatientID().equals(patientID))
                .noneMatch(app -> {
                    Calendar appointmentTime = app.getAppointmentTime();
                    return !(appointmentTime.get(Calendar.YEAR) == 1 &&
                             appointmentTime.get(Calendar.MONTH) == Calendar.JANUARY &&
                             appointmentTime.get(Calendar.DAY_OF_MONTH) == 1 &&
                             appointmentTime.get(Calendar.HOUR_OF_DAY) == 0 &&
                             appointmentTime.get(Calendar.MINUTE) == 0);
                })) {
            System.out.println("No upcoming appointments.");
        }
    }
    
    public void bookNewAppointment(String patientID, String patientName, String doctorID, String doctorName) {
        System.out.println("\nBooking a New Appointment:");
    
        // Validate if the patient exists
        patientName = DoctorShared.getcsvUtilities().getPatientNameByID(patientID);
        if (patientName == null || patientName.isEmpty()) {
            System.out.println("Error: Patient ID not found. Booking cannot proceed.");
            return; // Exit if the patient does not exist
        }
        
        System.out.println("The patient name is: " + patientName);
    
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
                doctorID, doctorName, "PendingToDoctor");
    
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
        System.out.println("Invalid selection. Returning to main menu...");
        return;
    }

    // Select the chosen completed appointment
    Appointment selectedAppointment = completedAppointments.get(choice - 1);

    // Display a summary of the appointment details before filling or editing
    System.out.println("\nCurrent Appointment Details:");
    Calendar appointmentTime = selectedAppointment.getAppointmentTime();
    LocalDate appointmentDate = toLocalDate(appointmentTime);
    LocalTime appointmentLocalTime = toLocalTime(appointmentTime);
    
    System.out.printf("Appointment ID: %s\n", selectedAppointment.getAppointmentID());
    System.out.printf("Date: %s\n", appointmentDate);
    System.out.printf("Time: %s\n", appointmentLocalTime);
    System.out.printf("Patient ID: %s\n", selectedAppointment.getPatientID());
    System.out.printf("Patient Name: %s\n", selectedAppointment.getPatientName());
    System.out.printf("Status: %s\n", selectedAppointment.getAppointmentStatus());

    // Prompt the doctor to fill or edit details for the completed appointment
    System.out.print("Do you want to update the outcome of the appointment? (y/n): ");
    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
        System.out.print("Enter the outcome: ");
        selectedAppointment.setDiagnosis(scanner.nextLine().trim());
    }

    System.out.print("Do you want to update the service provided? (y/n): ");
    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
        System.out.print("Enter the Treatment Plan: ");
        selectedAppointment.setTreatmentPlan(scanner.nextLine().trim());
    }

    System.out.print("Do you want to update the prescribed medicine? (y/n): ");
    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
        System.out.print("Enter the medicine: ");
        selectedAppointment.setMedicine(scanner.nextLine().trim());
    }

    System.out.print("Do you want to update the medicine status? (y/n): ");
    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
        System.out.print("Enter the medicine status (Given/Not Given): ");
        selectedAppointment.setMedicineStatus(scanner.nextLine().trim());
    }

    System.out.print("Do you want to add additional notes? (y/n): ");
    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
        System.out.print("Enter notes: ");
        selectedAppointment.setNotes(scanner.nextLine().trim());
    }

    // Update the appointment status if this is an edit
    if (isEdit) {
        selectedAppointment.setAppointmentStatus("Completed*");
    }

    // Display summary of the filled or edited details
    System.out.println("\nUpdated Appointment Summary:");
    System.out.printf("Appointment ID: %s\n", selectedAppointment.getAppointmentID());
    System.out.printf("Date: %s\n", appointmentDate);
    System.out.printf("Time: %s\n", appointmentLocalTime);
    System.out.printf("Patient ID: %s\n", selectedAppointment.getPatientID());
    System.out.printf("Patient Name: %s\n", selectedAppointment.getPatientName());
    System.out.printf("Status: %s\n", selectedAppointment.getAppointmentStatus());
    System.out.printf("Diagnosis: %s\n", selectedAppointment.getDiagnosis());
    System.out.printf("Treatment Plan: %s\n", selectedAppointment.getTreatmentPlan());
    System.out.printf("Prescribed Medicine: %s\n", selectedAppointment.getMedicine());
    System.out.printf("Medicine Status: %s\n", selectedAppointment.getMedicineStatus());
    System.out.printf("Additional Notes: %s\n", selectedAppointment.getNotes());

    // Save the updated appointment to the CSV file
    apptData.updateAppointmentInCSV(selectedAppointment);
    System.out.println("Appointment details " + (isEdit ? "edited" : "filled") + " and saved successfully.");
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
    displayAndFillAppointmentDetails(selectedAppointment, isEdit);
}


private void displayAndFillAppointmentDetails(Appointment selectedAppointment, boolean isEdit) {
    System.out.println("\nCurrent Appointment Details:");
    Calendar appointmentTime = selectedAppointment.getAppointmentTime();
    LocalDate appointmentDate = toLocalDate(appointmentTime);
    LocalTime appointmentLocalTime = toLocalTime(appointmentTime);
    
    System.out.printf("Appointment ID: %s\n", selectedAppointment.getAppointmentID());
    System.out.printf("Date: %s\n", appointmentDate);
    System.out.printf("Time: %s\n", appointmentLocalTime);
    System.out.printf("Patient ID: %s\n", selectedAppointment.getPatientID());
    System.out.printf("Patient Name: %s\n", selectedAppointment.getPatientName());
    System.out.printf("Status: %s\n", selectedAppointment.getAppointmentStatus());

    // Prompt the doctor to fill or edit details for the appointment
    System.out.print("Do you want to update the outcome of the appointment? (y/n): ");
    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
        System.out.print("Enter the outcome: ");
        selectedAppointment.setDiagnosis(scanner.nextLine().trim());
    }

    System.out.print("Do you want to update the service provided? (y/n): ");
    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
        System.out.print("Enter the Treatment Plan: ");
        selectedAppointment.setTreatmentPlan(scanner.nextLine().trim());
    }

    System.out.print("Do you want to update the prescribed medicine? (y/n): ");
    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
        System.out.print("Enter the medicine: ");
        selectedAppointment.setMedicine(scanner.nextLine().trim());
    }

    System.out.print("Do you want to update the medicine status? (y/n): ");
    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
        System.out.print("Enter the medicine status (Given/Not Given): ");
        selectedAppointment.setMedicineStatus(scanner.nextLine().trim());
    }

    System.out.print("Do you want to add additional notes? (y/n): ");
    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
        System.out.print("Enter notes: ");
        selectedAppointment.setNotes(scanner.nextLine().trim());
    }

    // Update the appointment status if this is an edit
    if (isEdit) {
        selectedAppointment.setAppointmentStatus("Completed*");
    }

    // Display summary of the filled or edited details
    System.out.println("\nUpdated Appointment Summary:");
    System.out.printf("Appointment ID: %s\n", selectedAppointment.getAppointmentID());
    System.out.printf("Date: %s\n", appointmentDate);
    System.out.printf("Time: %s\n", appointmentLocalTime);
    System.out.printf("Patient ID: %s\n", selectedAppointment.getPatientID());
    System.out.printf("Patient Name: %s\n", selectedAppointment.getPatientName());
    System.out.printf("Status: %s\n", selectedAppointment.getAppointmentStatus());
    System.out.printf("Diagnosis: %s\n", selectedAppointment.getDiagnosis());
    System.out.printf("Treatment Plan: %s\n", selectedAppointment.getTreatmentPlan());
    System.out.printf("Prescribed Medicine: %s\n", selectedAppointment.getMedicine());
    System.out.printf("Medicine Status: %s\n", selectedAppointment.getMedicineStatus());
    System.out.printf("Additional Notes: %s\n", selectedAppointment.getNotes());

    // Save the updated appointment to the CSV file
    apptData.updateAppointmentInCSV(selectedAppointment);
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
                            app.getDoctorID().equals(userID) && 
                            app.getAppointmentStatus().toLowerCase().contains("cancelled by patient")) ||
                           (userType.equalsIgnoreCase("patient") && 
                            app.getPatientID().equals(userID) &&
                            app.getAppointmentStatus().toLowerCase().contains("cancelled by dr")))
            .toList();

    if (cancelledAppointments.isEmpty()) {
        System.out.println("No canceled appointments to display.");
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
        }
        case 2 -> {
            // Book a new appointment
            System.out.println("Proceeding to book a new appointment...");
            if (userType.equalsIgnoreCase("doctor")) {
                bookNewAppointment(
                        selectedAppointment.getPatientID(),
                        selectedAppointment.getPatientName(),
                        selectedAppointment.getDoctorID(),
                        selectedAppointment.getDoctorName());
            } else if (userType.equalsIgnoreCase("patient")) {
                bookNewAppointment(
                        selectedAppointment.getPatientID(),
                        selectedAppointment.getPatientName(),
                        selectedAppointment.getDoctorID(),
                        selectedAppointment.getDoctorName());
            }
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
        String status = getSessionStatus(appointments, date, sessions[i]);
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

    String status = getSessionStatus(appointments, date, sessionTime);

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
    String status = getSessionStatus(apptData.getAllAppointments(), newDate, newTime);
    System.out.println("Debug: Selected session status is: " + status);  // Debugging output

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



           
}