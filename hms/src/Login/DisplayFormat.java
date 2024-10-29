package Login;

public class DisplayFormat {
    
    public void divider(){
        System.out.println("=========================================================================");
    }


    public static void printCentered(String text, int width) {
        int padding = (width - text.length()) / 2;
        System.out.print(" ".repeat(padding));
        System.out.println(text);
    }

    public static void clearScreen() {

        System.out.print("\033[H\033[2J");  
        System.out.flush();
}

}
