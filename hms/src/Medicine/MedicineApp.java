package Medicine;
//Medicine app is for testing functions
public class MedicineApp {
    public static void main(String[] args) {
        MedicineUI ui = new MedicineUI();
        ui.displayAllMedicines();
        ui.updateStock("Penicillin", 400);
        ui.displayAllMedicines();
    }
}
