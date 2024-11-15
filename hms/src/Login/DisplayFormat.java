package Login;

/**
 * Provides display formatting utilities for console output, including methods
 * to print dividers, center text, and clear the screen.
 */
public class DisplayFormat {
    
    /**
     * Prints a divider line to the console to separate sections of output.
     */
    public void divider() {
        System.out.println("=========================================================================");
    }

    /**
     * Prints the specified text centered within a specified width.
     * If the width is greater than the text length, padding spaces are added
     * on both sides to center-align the text.
     *
     * @param text  The text to print centered.
     * @param width The width within which to center the text.
     */
    public static void printCentered(String text, int width) {
        int padding = (width - text.length()) / 2;
        System.out.print(" ".repeat(padding));
        System.out.println(text);
    }

    /**
     * Clears the console screen by moving the cursor to the top and clearing all content.
     * This method works in most terminal environments.
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();
    }
}
