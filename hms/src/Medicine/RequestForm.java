package Medicine;

public class RequestForm {
    private String ID;
    private String medicineName;
    private int quantity;
    private String pharmacistID;
    private String pharmacistName;

    public RequestForm(String ID, String medicineName, int quantity, String pharmacistID, String pharmacistName) {
        this.ID = ID;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.pharmacistID = pharmacistID;
        this.pharmacistName = pharmacistName;
    }
    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getMedicineName() {
        return medicineName;
    }
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getPharmacistID() {
        return pharmacistID;
    }
    public void setPharmacistID(String pharmacistID) {
        this.pharmacistID = pharmacistID;
    }
    public String getPharmacistName() {
        return pharmacistName;
    }
    public void setPharmacistName(String pharmacistName) {
        this.pharmacistName = pharmacistName;
    }
    public String[] toCSVArray() {
        return new String[]{
            String.valueOf(ID), // Convert int ID to String
            medicineName,
            String.valueOf(quantity), // Convert int quantity to String
            pharmacistID,
            pharmacistName
        };
    }
    
}

