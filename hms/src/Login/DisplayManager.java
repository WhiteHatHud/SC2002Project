package Login;

public class DisplayManager {

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

            String[] loginMenu = {"I am a...\n", "1. Patient", "2. Staff\n", "Please enter your choice (1~2): "};

            for (String line : loginMenu) {
                printCentered(line, 80);
            }
            divider();

        }

        public void showPatientLoginID() {

            String[] msg = {"Please enter your patient ID : "};
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

        public void divider(){
            System.out.println("=========================================================================");
        }


        private void printCentered(String text, int width) {
            int padding = (width - text.length()) / 2;
            System.out.print(" ".repeat(padding));
            System.out.println(text);
        }

        
    }
    

