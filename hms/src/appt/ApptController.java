package appt;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

public class ApptController {
    private ApptData apptData;

    // Constructor to initialize ApptController with ApptData
    public ApptController(ApptData apptData) {
        this.apptData = apptData;
    }

    // Method to schedule a new appointment for a doctor
    public void scheduleDoctorAppointment(String appointmentID, Calendar appointmentTime, String patientID, String patientName, String doctorID, String doctorName) {
        if (isClashingAppointment(doctorID, appointmentTime)) {
            System.out.println("Error: Appointment time clashes with an existing appointment for Dr. " + doctorName);
            return;
        }

        DoctorAppointment newAppointment = new DoctorAppointment(
            appointmentID, appointmentTime, patientID, patientName, doctorID, doctorName, "Scheduled"
        );
        apptData.addAppointment(newAppointment);
        System.out.println("Doctor appointment scheduled successfully!");
    }

    // Method to reschedule an appointment for a doctor
    public void rescheduleDoctorAppointment(String appointmentID, Calendar newAppointmentTime) {
        List<Appointment> appointments = apptData.getAllAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                if (isClashingAppointment(appointment.getDoctorID(), newAppointmentTime)) {
                    System.out.println("Error: Appointment time clashes with an existing appointment for Dr. " + appointment.getDoctorName());
                    return;
                }

                appointment.setAppointmentTime(newAppointmentTime);
                appointment.setAppointmentStatus("Rescheduled");
                apptData.updateAppointment(appointment);
                System.out.println("Doctor appointment rescheduled successfully.");
                return;
            }
        }
        System.out.println("No appointment found with ID: " + appointmentID);
    }

    // Method to cancel an appointment for a doctor
    public void cancelDoctorAppointment(String appointmentID) {
        List<Appointment> appointments = apptData.getAllAppointments();
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentID().equals(appointmentID)) {
                appointments.remove(i);
                apptData.updateAllAppointments(appointments);
                System.out.println("Appointment with ID " + appointmentID + " has been successfully cancelled.");
                return;
            }
        }
        System.out.println("No appointment found with ID: " + appointmentID);
    }

    // Method to check if there is a clashing appointment for the doctor
    private boolean isClashingAppointment(String doctorID, Calendar newAppointmentTime) {
        List<Appointment> appointments = apptData.getAllAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorID().equals(doctorID) && appointment.getAppointmentTime().equals(newAppointmentTime)) {
                return true; // Clash found
            }
        }
        return false;
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

    // Method to record the outcome of an appointment
    public void recordOutcome(String appointmentID, String outcome) {
        List<Appointment> appointments = apptData.getAllAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                if (appointment instanceof DoctorAppointment) {
                    ((DoctorAppointment) appointment).setOutcome(outcome);
                    apptData.updateAppointment(appointment);
                    System.out.println("Outcome recorded successfully.");
                }
                return;
            }
        }
        System.out.println("No appointment found with ID: " + appointmentID);
    }
}
