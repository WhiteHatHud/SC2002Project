package MedicalHistory;

/**
 * The {@code PatientHistory} class extends the {@code MedicalHistory} class and represents
 * a patient's detailed medical history. This class provides specific functionality to 
 * view and update a patient's history.
 * 
 * <p>Each instance of {@code PatientHistory} contains details about a specific patient, 
 * including personal information such as name, date of birth, gender, blood type, 
 * and contact information.</p>
 * 
 * @see MedicalHistory
 */
public class PatientHistory extends MedicalHistory {

    /**
     * Constructs a new {@code PatientHistory} object with the specified patient details.
     *
     * @param patientID   the unique ID of the patient
     * @param name        the name of the patient
     * @param dateOfBirth the date of birth of the patient
     * @param gender      the gender of the patient
     * @param bloodType   the blood type of the patient
     * @param contactInfo the contact information of the patient
     */
    public PatientHistory(String patientID, String name, String dateOfBirth, String gender, String bloodType, String contactInfo) {
        super(patientID, name, dateOfBirth, gender, bloodType, contactInfo);
    }

    /**
     * Displays the complete medical history of the patient.
     * <p>This method is intended to show an overview of the patient’s
     * medical history information for viewing purposes.</p>
     */
    @Override
    public void viewHistory() {
        System.out.println("Viewing history for patient: " + this);
    }

    /**
     * Updates a specific field in the patient's medical history with a new value.
     * <p>This method allows modification of specific attributes in the patient’s history
     * (e.g., name, date of birth, gender, blood type, contact information).</p>
     *
     * @param field    the field name to be updated (e.g., "name", "dateOfBirth")
     * @param newValue the new value to assign to the specified field
     */
    @Override
    public void updateHistory(String field, Object newValue) {
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
