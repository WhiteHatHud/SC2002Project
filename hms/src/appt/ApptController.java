package appt;

import java.util.List;
import java.util.ArrayList;

public class ApptController {
    private ApptData apptData;

    // Constructor to initialize ApptController with ApptData
    public ApptController(ApptData apptData) {
        this.apptData = apptData;
    }

    // Method to schedule a new appointment for a doctor
    public void scheduleDoctorAppointment(String appointmentID, String date, String time, String patientID, String patientName, String doctorID, String doctorName) {
        DoctorAppointment newAppointment = new DoctorAppointment(
            appointmentID, date, time, patientID, patientName, doctorID, doctorName, "Scheduled"
        );
        apptData.addAppointment(newAppointment);
        System.out.println("Doctor appointment scheduled successfully!");
    }

    // Method to cancel an existing doctor appointment
    public void cancelDoctorAppointment(String appointmentID) {
        List<Appointment> appointments = apptData.getAllAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                appointment.setAppointmentStatus("Cancelled");
                apptData.updateAppointment(appointment);
                System.out.println("Doctor appointment cancelled successfully.");
                return;
            }
        }
        System.out.println("No appointment found with ID: " + appointmentID);
    }

    // Method to reschedule an appointment for a doctor
    public void rescheduleDoctorAppointment(String appointmentID, String newDate, String newTime) {
        List<Appointment> appointments = apptData.getAllAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                appointment.setDate(newDate);
                appointment.setTime(newTime);
                appointment.setAppointmentStatus("Rescheduled");
                apptData.updateAppointment(appointment);
                System.out.println("Doctor appointment rescheduled successfully.");
                return;
            }
        }
        System.out.println("No appointment found with ID: " + appointmentID);
    }

    // Method to view all appointments for a doctor
    public List<Appointment> viewAppointmentsByDoctor(String doctorID) {
        List<Appointment> appointments = apptData.getAllAppointments();
        List<Appointment> doctorAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.doctorID.equals(doctorID)) {
                doctorAppointments.add(appointment);
            }
        }
        return doctorAppointments;
    }

    // Method to record the outcome of an appointment
    public void recordOutcome(String appointmentID, String outcome) {
        List<Appointment> appointments = apptData.getAllAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                if (appointment instanceof DoctorAppointment) {
                    ((DoctorAppointment) appointment).recordOutcome(outcome);
                    apptData.updateAppointment(appointment);
                    System.out.println("Outcome recorded successfully.");
                }
                return;
            }
        }
        System.out.println("No appointment found with ID: " + appointmentID);
    }
}
