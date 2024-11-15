package Login;

import java.util.Map;
import Utilities.UserInputHandler;

/**
 * Defines a strategy for creating a login manager.
 * This interface allows for the implementation of different login approaches
 * by providing a method to create instances of {@link LoginInt} based on specific requirements.
 */
public interface LoginStrategy {

    /**
     * Creates a login manager instance based on the specified display manager, input handler, and user registries.
     *
     * @param displayManager Manages screen displays for login prompts and messages.
     * @param inputHandler   Handles user input during the login process.
     * @param registries     A map of user type to user registry, which is used to manage different types of users.
     * @return A {@link LoginInt} instance configured according to the specific login strategy.
     */
    LoginInt createLoginManager(DisplayManager displayManager, UserInputHandler inputHandler, Map<String, UserRegistry> registries);
}
