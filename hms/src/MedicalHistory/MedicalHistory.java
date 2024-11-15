package MedicalHistory;

/**
 * The {@code MedicalHistory} abstract class provides a structure for storing and managing
 * basic patient information, including ID, name, date of birth, gender, blood type, and 
 * contact information. This class also provides methods for viewing and updating medical
 * history, which are intended to be implemented by subclasses.
 */
public abstract class MedicalHistory {
    protected String patientID;
    protected String name;
    protected String dateOfBirth;
    protected String gender;
    protected String bloodType;
    protected String contactInfo;

    /**
     * Constructs a {@code MedicalHistory} object with the specified patient information.
     *
     * @param patientID    the unique identifier for the patient
     * @param name         the patient's name
     * @param dateOfBirth  the patient's date of birth
     * @param gender       the patient's gender
     * @param bloodType    the patient's blood type
     * @param contactInfo  the patient's contact information
     */
    public MedicalHistory(String patientID, String name, String dateOfBirth, String gender, String bloodType, String contactInfo) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        this.contactInfo = contactInfo;
    }

    /**
     * Returns the patient's ID.
     *
     * @return the patient's ID
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Returns the patient's name.
     *
     * @return the patient's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the patient's date of birth.
     *
     * @return the patient's date of birth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Returns the patient's gender.
     *
     * @return the patient's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Returns the patient's blood type.
     *
     * @return the patient's blood type
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * Returns the patient's contact information.
     *
     * @return the patient's contact information
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Returns a string representation of the patient's medical history information.
     *
     * @return a string with patient details including ID, name, date of birth, gender, 
     *         blood type, and contact information
     */
    @Override
    public String toString() {
        return "Patient ID: " + patientID + ", Name: " + name + ", Date of Birth: " + dateOfBirth +
               ", Gender: " + gender + ", Blood Type: " + bloodType + ", Contact Information: " + contactInfo;
    }

    /**
     * Abstract method to view the patient's medical history. This method should be 
     * implemented by subclasses to provide specific functionality.
     */
    public abstract void viewHistory();

    /**
     * Abstract method to update the patient's medical history. This method should be 
     * implemented by subclasses to provide specific functionality.
     *
     * @param field     the field in the medical history to update
     * @param newValue  the new value to set for the specified field
     */
    public abstract void updateHistory(String field, Object newValue);
}
