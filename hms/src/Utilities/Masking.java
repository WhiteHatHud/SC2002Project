/**
 * Utility class for masking input while reading sensitive data like passwords.
 */
package Utilities;

public class Masking {
    
    /**
     * Reads a password from the standard input while masking the characters with '*'.
     * This method captures user input character by character, displaying an asterisk ('*')
     * for each character entered and handles backspace to allow correction.
     * 
     * @return the password entered by the user as a String.
     */
    public String readPasswordWithMasking() {
        StringBuilder password = new StringBuilder();
        try {
            while (true) {
                int ch = System.in.read();
                if (ch == '\n' || ch == '\r') {
                    break; // Stop reading input on newline or carriage return
                } else if (ch == '\b' && password.length() > 0) {
                    // Handle backspace character
                    password.deleteCharAt(password.length() - 1);
                    System.out.print("\b \b");  // Erase last '*' from the console
                } else {
                    password.append((char) ch);
                    System.out.print("*");  // Print '*' to the console for each character
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace if an error occurs
        }
        return password.toString();
    }
}
