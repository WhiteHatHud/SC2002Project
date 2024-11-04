package Medicine;

public class Medicine{
    protected String medicineName;
    protected int initialStock;
    protected int lowStockLevelAlert;

    public Medicine(){
        medicineName = "";
        initialStock = 0;
        lowStockLevelAlert = 0;
    }

    public Medicine(String medicineName, int initialStock, int lowStockLevelAlert){
        this.medicineName = medicineName;
        this.initialStock = initialStock;
        this.lowStockLevelAlert = lowStockLevelAlert;
    }

    //getters
    public String getMedicineName(){
        return medicineName;
    }
    public int getInitialStock(){
        return initialStock;
    }
    public int getLowStockLevelAlert(){
        return lowStockLevelAlert;
    }
    //setters
    public void setMedicineName(String newName){
        medicineName = newName;
    }
    public void setInitialStock(int stock){
        initialStock = stock;
    }
    public void setLowStockLevel(int level){
        lowStockLevelAlert = level;
    }
    public String toCSV(){
        String stringStock = String.valueOf(initialStock);
        String stringLevel = String.valueOf(lowStockLevelAlert);
        return String.join(",", medicineName, stringStock, stringLevel);
    }
    
}