package Admins;

import Users.Staff;

/**
 * The {@code Admin} class represents an administrator in the system. 
 * This class extends the {@code Staff} class, inheriting common attributes for all staff members.
 * It can be used to store and manage information specific to admin users.
 * 
 * <p>There are two constructors available:
 * <ul>
 *     <li>A default constructor that initializes an empty {@code Admin} object.</li>
 *     <li>A parameterized constructor that initializes an {@code Admin} with specified attributes.</li>
 * </ul>
 * </p>
 */
public class Admin extends Staff {

    /**
     * Default constructor for creating an {@code Admin} object with default values.
     */
    public Admin() {
        super();
    }

    /**
     * Parameterized constructor for creating an {@code Admin} object with specified values.
     * 
     * @param staffID      the unique ID of the admin
     * @param name         the name of the admin
     * @param role         the role of the admin
     * @param gender       the gender of the admin
     * @param age          the age of the admin
     * @param officeNumber the office number of the admin
     * @param password     the password of the admin
     */
    public Admin(String staffID, String name, String role, String gender, int age, String officeNumber, String password) {
        super(staffID, name, role, gender, age, officeNumber, password);
    }
}
