package appt;

import java.util.Calendar;

public class PatientAppointment extends Appointment {

    // Constructor to initialize a patient-specific appointment
    public PatientAppointment(String appointmentID, Calendar appointmentTime, String patientID,
                              String patientName, String doctorID, String doctorName, String appointmentStatus) {
        super(appointmentID, appointmentTime, patientID, patientName, doctorID, doctorName, appointmentStatus);
    }

    @Override
    public void manageAppointment() {
        System.out.println("Managing patient appointment:");
        System.out.printf("Appointment ID: %s\n", getAppointmentID());
        System.out.printf("Patient ID: %s\n", getPatientID());
        System.out.printf("Patient Name: %s\n", getPatientName());
        System.out.printf("Doctor Name: %s\n", getDoctorName());
        System.out.printf("Status: %s\n", getAppointmentStatus());
        System.out.printf("Service: %s\n", getTreatmentPlan() == null ? "N/A" : getTreatmentPlan());
        System.out.printf("Medicine: %s\n", getMedicine() == null ? "N/A" : getMedicine());
        System.out.printf("Medicine Status: %s\n", getMedicineStatus() == null ? "N/A" : getMedicineStatus());
        System.out.printf("Notes: %s\n", getNotes() == null ? "N/A" : getNotes());
    }

    // Additional logic for patient-specific actions, if needed
    public void confirmAppointment() {
        setAppointmentStatus("Confirmed");
        System.out.println("Appointment confirmed.");
    }

    public void cancelAppointment() {
        setAppointmentStatus("Canceled");
        System.out.println("Appointment canceled.");
    }
}
