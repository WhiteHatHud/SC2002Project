package Login;

public class DisplayFormat {
    
    public void divider(){
        System.out.println("=========================================================================");
    }


    public void printCentered(String text, int width) {
        int padding = (width - text.length()) / 2;
        System.out.print(" ".repeat(padding));
        System.out.println(text);
    }
}
