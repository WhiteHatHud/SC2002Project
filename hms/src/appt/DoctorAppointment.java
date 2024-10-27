package appt;

public class DoctorAppointment extends Appointment {

    public DoctorAppointment(String appointmentID, String date, String time, String patientID,
                             String patientName, String doctorID, String doctorName, String appointmentStatus) {
        super(appointmentID, date, time, patientID, patientName, doctorID, doctorName, appointmentStatus);
    }

    @Override
    public void manageAppointment() {
        System.out.println("Managing appointment for Doctor: " + doctorName);
    }

    // Additional methods specific to doctor actions
    public void recordOutcome(String outcome) {
        this.outcome = outcome;
    }

    public void viewSchedule() {
        System.out.println("Viewing schedule for Doctor: " + doctorName);
    }
}
