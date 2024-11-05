package Medicine;

import java.util.List;
import Login.DisplayFormat;

public class PrescriptionsUI extends DisplayFormat{
    PrescriptionData prescriptionInteface = new PrescriptionData();
    Prescription data;
    List<Prescription> prescriptionList;

    public void printPrescription(Prescription pres){
        System.out.println(
            pres.getPatientID() + ", " +
            pres.getDoctorID() + ", " +
            pres.getPharmacistID() + ", " +
            pres.getMedicineName() + ", " +
            pres.getPrescriptionAmount() + ", " +
            pres.getStatus()
        );
    }
    public void showAll(){
        prescriptionList = prescriptionInteface.getAllPrescriptions();
        for (Prescription pres: prescriptionList){
            printPrescription(pres);
        }
    }

    public void showPrescriptions(String patientID){
        prescriptionList = prescriptionInteface.getPatientPrescriptionList(patientID);
        clearScreen();
        if (prescriptionList.size() == 0){
            printCentered("Patient has no prescription record", 80);
            return;
        }
        printCentered("Patients medicine prescriptions", 80);
        for (Prescription pres : prescriptionList){
            printPrescription(pres);
        }
    }

}
