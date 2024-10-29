package Medicine;

public class MedicineUI extends Medicine{
    MedicineData dataInterface = new MedicineData();
    public void displayAllMedicines(){
        for (Medicine med : dataInterface.getAllMedicines()){
            print(med);
        }
    }
    public void updateMedicine(String name, int changeAmount){
        if(dataInterface.updateStock(name, changeAmount) == false){
            print("Medicine already exists in database");
        }
    }
    public void addNewMedicine(String name, int stock, int level){
        Medicine test = new Medicine(name, stock, level);
        if (dataInterface.addMedicine(test) == false){
            print("Failed to add medicine to database");
        }
    }
    public void displayOneMedicine(String name){
        Medicine med = dataInterface.getMedicineByName(name);
        print(med);
    }
    public void print(Medicine med){
        if (med == null){
            print("Medicine not found");
            return;
        }
        System.out.println(
            med.getMedicineName()+", "+ 
            med.getInitialStock()+", "+
            med.getLowStockLevelAlert()
        );
    }
    public void print(String s){
        System.out.println(s);
    }
}
