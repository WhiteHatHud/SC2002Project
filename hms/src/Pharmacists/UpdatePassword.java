package Pharmacists;

import Login.DisplayManager;

public class UpdatePassword {
    private Pharmacist pharma;
    

    public void start(Pharmacist pharma){
        this.pharma = pharma ;
        setPW();
    }

    public void setPW(){

        DisplayManager.passowrdUpdate();
        String newPassword = PharmaShared.getUserInputHandler().getInput();
        pharma.setPassword(newPassword);
        PharmaShared.getCSVUpdater().updateField(pharma.getUserID(),"password", newPassword);
        DisplayManager.clearScreen();
        System.out.println("Password updated succesfully!");
    }
    
}
