package cscc.edu;

public class USGSView {
    static public Integer DatabaseCreationMenuMaxNumber = 2;
    public static Integer MainMenuMaxNumber = 6;

    public USGSView() {
    }
    public void displayMainMenu() {
        System.out.println("!!!!!!!! USGS Database Welcome Screen !!!!!!!!");
        System.out.println("Please enter a choice from the below");
//        System.out.println("===============================================================");
        displayDashesWithEquals();

        System.out.println("1. Database Creation and Load Menu " + "USGSTableData");
        System.out.println("2. Select Column(s) to display in " + "USGSTableData");
        System.out.println("3. Enter selected Column(s) Ranges and then Search " + "USGSTableData");
        System.out.println("4. Search only on earlier selected Columns, and ranges in " + "USGSTableData");
        System.out.println("5. Delete Rows based on selected column(s) values in " + "USGSTableData");
        System.out.println("6. Count Rows based on selected column(s) values in " + "USGSTableData");
        System.out.println("0. Exit Program");
        displayDashesWithEquals();
    }

    public void displayDDLMenu() {
        System.out.println("!!!!!!!! USGS Database Creation and Load Menu !!!!!!!!");
        System.out.println("Please enter a choice from the below");
//        System.out.println("===============================================================");
        displayDashesWithEquals();
        System.out.println("1. Load Database Table " + "USGSTableData" + " from CSV");
        System.out.println("2. Read CSV file (Debug only, just display csv file)");
        System.out.println("0. Exit this Menu");
        displayDashesWithEquals();
    }

    public void displayDashesWithEquals() {
        System.out.println("================================================================");
    }

    public void displayMessage(String s) {
        System.out.println(s);
    }
    public void displayMessageNoLineBreak (String s) {
        System.out.print(s);
    }
    public void displayQueryDataBaseMenu() {
        System.out.println("!!!!!!!! USGS Database Query Screen !!!!!!!!");
        System.out.println("Please enter your SQL string below: This field is not edited");
        System.out.println("Please enter End to exit:");
    }
    public void displayExitScreen() {
        System.out.println("!!! Bye bye from USGS Database !!!");
    }

    public void displayGoingBackToMainMenuScreen() {
        System.out.println("!!! Going back to previous Menu !!!");
    }
}
