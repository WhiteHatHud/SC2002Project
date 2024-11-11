package Login;

public class DisplayManager extends DisplayFormat {

    public void showWelcomeScreen() {
        String[] hmsLogo = {"H   H  M   M  SSSSS", "H   H  MM MM  S     ", "HHHHH  M M M  SSSSS", "H   H  M   M      S", "H   H  M   M  SSSSS"};
        String welcomeMessage = "Welcome to the Hospital Management System";
        String[] menu = {"1. Login", "2. Reset password", "3. Quit \n", "Please enter your choice (1~3): "};
        
        for (String line : hmsLogo) {
            printCentered(line, 80);
        }
        printCentered(welcomeMessage, 80);
        for (String line : menu) {
            printCentered(line, 80);
        }
        divider();
        
    }

        public void showLoginScreen() {

            String[] loginMenu = {"I am a...\n", "1. Patient", "2. Staff\n", "Please enter your choice (1-2, ~ to return to main meu): "};

            for (String line : loginMenu) {
                printCentered(line, 80);
            }
            divider();

        }
        public void showStaffLoginID() {

            String[] msg = {"Please enter your staff ID (Enter ~ to return to main menu): "};
            for (String line : msg) {
                printCentered(line, 80);
            }

        }

        public void showPatientLoginID() {

            String[] msg = {"Please enter your patient ID (Enter ~ to return to main menu): "};
            for (String line : msg) {
                printCentered(line, 80);
            }

        }
        public void showEnterPW() {

            String[] msg = {"Please enter your password : "};
            for (String line : msg) {
                printCentered(line, 80);
            }

        }

        public static void retMainMenu() {
            String[] msg = {"Enter ~ to go back to the main menu."};
            for (String line : msg) {
                printCentered(line, 80);
            }

        }
        public static void retMainMenuAny() {
            String[] msg = {"Enter any key to go back to the main menu."};
            for (String line : msg) {
                printCentered(line, 80);
            }

        }

        public static void authFail() {
            String[] msg = {"Authentication failed. Please try again."};
            for (String line : msg) {
                printCentered(line, 80);
            }
        }

        public static void passowrdUpdate() {
            String[] msg = {"Please enter your new password"};
            for (String line : msg) {
                printCentered(line, 80);
            }
        }
        public static void invalidChoice() {
            String[] msg = {"Invalid choice! Try again"};
            for (String line : msg) {
                printCentered(line, 80);
            }
        }
        public static String loadErrorMessage(String errorMessage){
            if (errorMessage != ""){
                DisplayFormat.printCentered(errorMessage, 80);
            }
            return "";
        }

    }
    

