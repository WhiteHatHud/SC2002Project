package Login;

/**
 * Interface for handling user choices in various controllers.
 * This interface can be implemented by different classes to provide specific behavior
 * for handling choices in a menu or user interface.
 */
public interface ControllerInt {

    /**
     * Handles a user choice based on an integer input.
     *
     * @param choice The integer choice input by the user.
     * @return A boolean indicating whether the choice was successfully handled.
     */
    boolean handleChoice(int choice);
    
    // Uncomment the method below and add JavaDocs if needed for starting the controller
    // void start();
}
