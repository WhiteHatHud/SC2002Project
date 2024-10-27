package appt;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class DoctorAppointment extends Appointment {

    public DoctorAppointment(String appointmentID, Calendar appointmentTime, String patientID,
                             String patientName, String doctorID, String doctorName, String appointmentStatus) {
        super(appointmentID, appointmentTime, patientID, patientName, doctorID, doctorName, appointmentStatus);
    }

    @Override
    public void manageAppointment() {
        System.out.println("Managing appointment for Doctor: " + doctorName);
    }

    // Additional methods specific to doctor actions
    public void recordOutcome(String outcome) {
        this.setOutcome(outcome);
    }

    public void viewSchedule() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        System.out.println("Viewing schedule for Doctor: " + doctorName);
        System.out.println("Appointment Date and Time: " + dateFormatter.format(appointmentTime.getTime()));
    }
}
