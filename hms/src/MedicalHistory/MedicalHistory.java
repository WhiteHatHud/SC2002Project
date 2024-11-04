package MedicalHistory;

public abstract class MedicalHistory {
    protected String patientID;
    protected String name;
    protected String dateOfBirth;
    protected String gender;
    protected String bloodType;
    protected String contactInfo;

    // Constructor
    public MedicalHistory(String patientID, String name, String dateOfBirth, String gender, String bloodType, String contactInfo) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        this.contactInfo = contactInfo;
    }

    // Getters for each field
    public String getPatientID() { return patientID; }
    public String getName() { return name; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getBloodType() { return bloodType; }
    public String getContactInfo() { return contactInfo; }

    @Override
    public String toString() {
        return "Patient ID: " + patientID + ", Name: " + name + ", Date of Birth: " + dateOfBirth + 
               ", Gender: " + gender + ", Blood Type: " + bloodType + ", Contact Information: " + contactInfo;
    }

    // Abstract methods that can be defined by subclasses as needed
    public abstract void viewHistory();
    public abstract void updateHistory(String field, Object newValue);
}
