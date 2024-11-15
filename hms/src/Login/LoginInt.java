package Login;

/**
 * Interface for managing login operations. 
 * This interface defines the basic methods required to handle login flows 
 * and manage navigation back to the main menu.
 */
public interface LoginInt {

    /**
     * Starts the login process.
     *
     * @return {@code true} if the login was successful, {@code false} otherwise.
     */
    boolean start();

    /**
     * Determines whether the user should be returned to the main menu.
     *
     * @return {@code true} if the user should return to the main menu, {@code false} otherwise.
     */
    boolean shouldReturnToMenu();
    
    // Uncomment and document this method if needed in the future
    // /**
    //  * Starts the login controller to manage the login session.
    //  */
    // void startController();
}
