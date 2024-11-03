package Admins;

import Login.ControllerInt;
import Login.DisplayFormat;

public class AdminController implements ControllerInt {
    private final Admin admin;

    public AdminController(Admin admin) {
        this.admin = admin;
    }

    public boolean start() {
        DisplayFormat.clearScreen();
        System.out.println("Welcome, " + admin.getName());

        AdminMenu adminMenu = new AdminMenu();
        adminMenu.displayMenu();

        // When displayMenu returns, it means the user chose to logout
        return false;  
    }

    @Override
    public boolean handleChoice(int choice) {
        System.out.println("In admin now");
        return true;
    }
}
