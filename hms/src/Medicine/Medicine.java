package Medicine;

/**
 * The {@code Medicine} class represents a medicine with basic properties such as
 * its name, stock level, and low stock alert level. It includes methods for 
 * getting and setting these properties, as well as a method to convert the 
 * medicine information to a CSV format.
 */
public class Medicine {

    /** The name of the medicine */
    protected String medicineName;

    /** The initial stock level of the medicine */
    protected int initialStock;

    /** The stock level at which a low stock alert is triggered */
    protected int lowStockLevelAlert;

    /**
     * Default constructor for {@code Medicine}, initializes the medicine with 
     * default values: empty name, 0 initial stock, and 0 low stock alert level.
     */
    public Medicine() {
        medicineName = "";
        initialStock = 0;
        lowStockLevelAlert = 0;
    }

    /**
     * Parameterized constructor for {@code Medicine}.
     *
     * @param medicineName       the name of the medicine
     * @param initialStock       the initial stock level of the medicine
     * @param lowStockLevelAlert the stock level at which a low stock alert is triggered
     */
    public Medicine(String medicineName, int initialStock, int lowStockLevelAlert) {
        this.medicineName = medicineName;
        this.initialStock = initialStock;
        this.lowStockLevelAlert = lowStockLevelAlert;
    }

    // Getters

    /**
     * Gets the name of the medicine.
     *
     * @return the name of the medicine
     */
    public String getMedicineName() {
        return medicineName;
    }

    /**
     * Gets the initial stock level of the medicine.
     *
     * @return the initial stock level
     */
    public int getInitialStock() {
        return initialStock;
    }

    /**
     * Gets the low stock level alert threshold.
     *
     * @return the low stock level alert threshold
     */
    public int getLowStockLevelAlert() {
        return lowStockLevelAlert;
    }

    // Setters

    /**
     * Sets a new name for the medicine.
     *
     * @param newName the new name of the medicine
     */
    public void setMedicineName(String newName) {
        medicineName = newName;
    }

    /**
     * Sets a new initial stock level for the medicine.
     *
     * @param stock the new initial stock level
     */
    public void setInitialStock(int stock) {
        initialStock = stock;
    }

    /**
     * Sets a new low stock alert level for the medicine.
     *
     * @param level the new low stock alert level
     */
    public void setLowStockLevel(int level) {
        lowStockLevelAlert = level;
    }

    /**
     * Converts the medicine's information to a CSV format.
     *
     * @return a CSV string representing the medicine's information
     */
    public String toCSV() {
        String stringStock = String.valueOf(initialStock);
        String stringLevel = String.valueOf(lowStockLevelAlert);
        return String.join(",", medicineName, stringStock, stringLevel);
    }
}
