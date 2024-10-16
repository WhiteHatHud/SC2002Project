package SC2002Project.hms.ui;

public class DisplayManager {

    public void showWelcomeScreen() {
        String[] hmsLogo = {"H   H  M   M  SSSSS", "H   H  MM MM  S     ", "HHHHH  M M M  SSSSS", "H   H  M   M      S", "H   H  M   M  SSSSS"};
        String welcomeMessage = "Welcome to the Hospital Management System";
        String[] menu = {"1. Login", "2. Reset password", "3. Quit \n", "Please enter your choice: "};
        
        for (String line : hmsLogo) {
            printCentered(line, 80);
        }
        printCentered(welcomeMessage, 80);
        for (String line : menu) {
            printCentered(line, 80);
        }
    }

        private void printCentered(String text, int width) {
            int padding = (width - text.length()) / 2;
            System.out.print(" ".repeat(padding));
            System.out.println(text);
        }
    
}
