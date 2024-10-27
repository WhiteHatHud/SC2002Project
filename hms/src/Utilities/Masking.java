package Utilities;

public class Masking {
    
    public String readPasswordWithMasking() {
        StringBuilder password = new StringBuilder();
        try {
            while (true) {
                int ch = System.in.read();
                if (ch == '\n' || ch == '\r') {
                    break;
                } else if (ch == '\b' && password.length() > 0) {
                    // Handle backspace
                    password.deleteCharAt(password.length() - 1);
                    System.out.print("\b \b");  // Remove last * from the console
                } else {
                    password.append((char) ch);
                    System.out.print("*");  // Display * for each character
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password.toString();
    }
}
