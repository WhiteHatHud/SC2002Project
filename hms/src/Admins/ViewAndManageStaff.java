package Admins;

public class ViewAndManageStaff implements MenuAction {


    public ViewAndManageStaff() {

    }

    @Override
    public void execute() {
        System.out.println("=== View and Manage Hospital Staff ===");
        System.out.println("1. Manage Doctors");
        System.out.println("2. Manage Pharmacists");
        //Filter here option 3 
        System.out.print("Choose an option: ");
        int choice = AdminShared.getUserInputHandler().getUserChoice();

        switch (choice) {
            case 1:
                manageDoctors();
                break;
            case 2:
                managePharmacists();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void manageDoctors() {
        System.out.println("1. Add a New Doctor");
        System.out.println("2. Update Doctor Information");
        System.out.println("3. Remove a Doctor");
        System.out.print("Choose an option: ");
        int choice =AdminShared.getUserInputHandler().getUserChoice();

        switch (choice) {
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void managePharmacists() {
        System.out.println("1. Add Pharmacist");
        System.out.println("2. Update Pharmacist");
        System.out.println("3. Remove Pharmacist");
        System.out.print("Choose an option: ");
        int choice = AdminShared.getUserInputHandler().getUserChoice();

        switch (choice) {
            case 1:
                break;
            case 2:

                break;
            case 3:
               
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    @Override
    public String getDescription() {
        return "View and Manage Hospital Staff";
    }
}

