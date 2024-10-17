package Patients;

import java.util.ArrayList;
import java.util.List;


public class Patient {
    private String PatientID;
    private String Name;
    private String DOB;
    private String Gender;
    private String contactInfo;
    private String bloodType;
    private String pastDiagnoses;

    // TODO: implement Appointment class
    // private List<Appointment> Appointments;

    // TODO: implement MedicalRecords class
    // private MedicalRecord medicalRecord;
    
    // constructor
    public Patient(String PatientID, String Name, String DOB, String Gender, String contactInfo, String bloodType, String pastDiagnoses){
        this.PatientID = PatientID;
        this.Name = Name;
        this.DOB = DOB;
        this.Gender = Gender;
        this.contactInfo = contactInfo;
        this.bloodType = bloodType;
        this.pastDiagnoses = pastDiagnoses;

        // this.Appointments = new ArrayList<>();
    }
    public void viewMedicalRecord(){

        // TODO: viewRecord()
        // this.medicalRecord.viewRecord();
    }
    public void updateContactInfo(String contactInfo){
        this.contactInfo = contactInfo;
        System.out.println("Contact information has been successfully been updated.");
    }
    public void scheduleAppointment(){

        // add appointment to list of appointments
        // this.Appointment.schedule()
    }
    public void rescheduleAppointment(){
        // this.Appointment.reschedule;
    }
    public void cancelAppointment(){
        //this.Appointment.cancel();
    }
    public void viewAppointmentStatus(){
        //this.Appointment.viewStatus();
    }
    public void viewAppointmentOutcomeRecords(){
        // i dont know what this is supposed to do
    }
}
