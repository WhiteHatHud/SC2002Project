package Login;
import java.util.Map;
import Utilities.UserInputHandler;

public interface LoginStrategy {
        LoginInt createLoginManager(DisplayManager displayManager, UserInputHandler inputHandler, Map<String, UserRegistry> registries);

}
