package MedicalHistory;

public class PatientHistory extends MedicalHistory {

    public PatientHistory(String patientID, String name, String dateOfBirth, String gender, String bloodType, String contactInfo) {
        super(patientID, name, dateOfBirth, gender, bloodType, contactInfo);
    }

    @Override
    public void viewHistory() {
        // Implement code to view patient history details
        System.out.println("Viewing history for patient: " + this);
    }

    @Override
    public void updateHistory(String field, Object newValue) {
        // Implement code to update specific fields in patient history
        switch (field) {
            case "name":
                this.name = (String) newValue;
                break;
            case "dateOfBirth":
                this.dateOfBirth = (String) newValue;
                break;
            case "gender":
                this.gender = (String) newValue;
                break;
            case "bloodType":
                this.bloodType = (String) newValue;
                break;
            case "contactInfo":
                this.contactInfo = (String) newValue;
                break;
            default:
                System.out.println("Invalid field name.");
                break;
        }
        System.out.println("Updated " + field + " to: " + newValue);
    }
}
