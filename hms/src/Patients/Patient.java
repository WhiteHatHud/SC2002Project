package Patients;

import Users.Users;

/**
 * The {@code Patient} class represents a patient user within the system,
 * extending the {@code Users} class with additional attributes specific to patients.
 */
public class Patient extends Users {

    private String dob;
    private String gender;
    private String bloodType;
    private String email;
    private String number;
    private String enumber;
    private String password;

    /**
     * Constructs a {@code Patient} object with specified details.
     *
     * @param patientID  the unique identifier for the patient
     * @param name       the name of the patient
     * @param dob        the date of birth of the patient
     * @param gender     the gender of the patient
     * @param bloodType  the blood type of the patient
     * @param contactInfo the email address of the patient
     * @param number     the primary contact number of the patient
     * @param enumber    the emergency contact number of the patient
     * @param password   the password for the patient's account
     */
    public Patient(String patientID, String name, String dob, String gender, String bloodType, String contactInfo, 
                   String number, String enumber, String password) {
        super(patientID, name); 
        this.dob = dob;             
        this.gender = gender;
        this.bloodType = bloodType;
        this.email = contactInfo;
        this.number = number;
        this.enumber = enumber;
        this.password = password;
    }
    
    // Getter methods specific to Patient

    /**
     * Gets the date of birth of the patient.
     *
     * @return the date of birth as a {@code String}
     */
    public String getDateOfBirth() {
        return dob;
    }

    /**
     * Gets the gender of the patient.
     *
     * @return the gender as a {@code String}
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the blood type of the patient.
     *
     * @return the blood type as a {@code String}
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * Gets the password of the patient.
     *
     * @return the password as a {@code String}
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the patient.
     *
     * @param password the new password as a {@code String}
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email address of the patient.
     *
     * @return the email address as a {@code String}
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address for the patient.
     *
     * @param email the new email address as a {@code String}
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the primary contact number of the patient.
     *
     * @return the contact number as a {@code String}
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the primary contact number of the patient.
     *
     * @param number the new contact number as a {@code String}
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Gets the emergency contact number of the patient.
     *
     * @return the emergency contact number as a {@code String}
     */
    public String getenumber() {
        return enumber;
    }

    /**
     * Sets the emergency contact number of the patient.
     *
     * @param enumber the new emergency contact number as a {@code String}
     */
    public void setenumber(String enumber) {
        this.enumber = enumber;
    }

    /**
     * Displays the profile details of the patient, including patient ID,
     * name, email, date of birth, gender, blood type, contact number, 
     * and emergency contact.
     */
    public void getProfile() {
        System.out.println("=== Patient Profile ===");
        System.out.printf("%-20s: %s%n", "Patient ID", userID);
        System.out.printf("%-20s: %s%n", "Name", name);
        System.out.printf("%-20s: %s%n", "Email", email);
        System.out.printf("%-20s: %s%n", "Date of Birth", dob);
        System.out.printf("%-20s: %s%n", "Gender", gender);
        System.out.printf("%-20s: %s%n", "Blood Type", bloodType);
        System.out.printf("%-20s: %s%n", "Contact Number", number);
        System.out.printf("%-20s: %s%n", "Emergency Contact", enumber);
        System.out.println("========================");
    }
}
