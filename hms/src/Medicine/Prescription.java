package Medicine;

public class Prescription extends Medicine{
    String patientID;
    String doctorID;
    String pharmacistID;
    int prescriptionAmount;
    boolean status;

    public Prescription(){}

    public Prescription(String patientID, String doctorID, String pharmacistID,String medicineName, int prescriptionAmount, boolean status){
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.pharmacistID = pharmacistID;
        this.prescriptionAmount = prescriptionAmount;
        this.medicineName = medicineName;
        this.status = status;
        status = false;
    }

    public String getPatientID(){
        return patientID;
    }
    public String getDoctorID(){
        return doctorID;
    }
    public String getPharmacistID(){
        return pharmacistID;
    }
    public int getPrescriptionAmount(){
        return prescriptionAmount;
    }
    public boolean getStatus(){
        return status;
    }
    public void setPatientID(String patientID){
        this.patientID = patientID;
    }
    public void setDoctorID(String doctorID){
        this.doctorID = doctorID;
    }
    public void setPharmacistId(String pharmacistID){
        this.pharmacistID = pharmacistID;
    }
    public void setPrescriptionAmount(int newAmount){
        this.prescriptionAmount = newAmount;
    }
    public void setCurrent(boolean current){
        status = current;
    }
}
