package Admins;

import Login.DisplayManager;

public class LogoutAction implements MenuAction {
    @Override
    public void execute(){
        DisplayManager.clearScreen();
        System.out.println("=== Log out ===");
    }

    @Override
    public String getDescription() {
        return "Logout";
    }
}
