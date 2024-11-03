package Pharmacists;

public class UpdatePassword {
    private Pharmacist pharma;
    

    public void start(Pharmacist pharma){
        this.pharma = pharma ;
        setPW();
    }

    public void setPW(){
        String newPassword = PharmaShared.getUserInputHandler().getInput();
        pharma.setPassword(newPassword);
        PharmaShared.getCSVUpdater().updateField(pharma.getUserID(),"password", newPassword);
        System.out.println("Password updated succesfully!");
    }
    
}
