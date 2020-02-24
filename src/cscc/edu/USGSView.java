package cscc.edu;

public class USGSView {
    static public Integer DatabaseCreationMenuMaxNumber = 7;
    public static Integer MainMenuMaxNumber = 5;

    public USGSView() {
    }
    public void displayMainMenu() {
        System.out.println("!!!!!!!! USGS Database Welcome Screen !!!!!!!!");
        System.out.println("Please enter a choice from the below");
        System.out.println("===============================================================");
        System.out.println("1. Database Creation and Load Menu" + USGSDatabase.getDbName());
        System.out.println("2. Select Column(s) to display in " + USGSDatabase.getTableName());
        System.out.println("3. Perform Search using selected Column(s) in " + USGSDatabase.getDbName());
        System.out.println("4. Delete Rows based on selected column(s) values in " + USGSDatabase.getTableName());
        System.out.println("5. Count Rows based on selected column(s) values in " + USGSDatabase.getTableName() + " from CSV");
        System.out.println("0. Exit Program");
        System.out.println("================================================================");
    }

    public void displayDDLMenu() {
        System.out.println("!!!!!!!! USGS Database Creation and Load Menu !!!!!!!!");
        System.out.println("!!! WARNING THIS PROGRAM WILL CRASH IF INCORRECT SEQUENCE USED !!!");
        System.out.println("!!! to properly create database and fill table: select 1, 2, and 5 !!!");
        System.out.println("Please enter a choice from the below");
        System.out.println("===============================================================");
        System.out.println("1. Create Database " + USGSDatabase.getDbName());
        System.out.println("2. Create Table " + USGSDatabase.getTableName());
        System.out.println("3. Delete Database " + USGSDatabase.getDbName());
        System.out.println("4. Delete Table " + USGSDatabase.getTableName());
        System.out.println("5. Load Database Table " + USGSDatabase.getTableName() + " from CSV");
        System.out.println("6. Free Form SQL Query Database " + USGSDatabase.getTableName());
        System.out.println("7. Read CSV file (Debug only, just display csv file)");
        System.out.println("0. Exit Program");
        System.out.println("================================================================");
    }

    public void displayDMLDQLMenu() {
        System.out.println("!!!!!!!! USGS Database Welcome Screen !!!!!!!!");
        System.out.println("!!! WARNING THIS PROGRAM WILL CRASH IF INCORRECT SEQUENCE USED !!!");
        System.out.println("!!! to properly create database and fill table: select 1, 2, and 5 !!!");
        System.out.println("Please enter a choice from the below");
        System.out.println("===============================================================");
        System.out.println("1. Create Database " + USGSDatabase.getDbName());
        System.out.println("2. Create Table " + USGSDatabase.getTableName());
        System.out.println("3. Delete Database " + USGSDatabase.getDbName());
        System.out.println("4. Delete Table " + USGSDatabase.getTableName());
        System.out.println("5. Load Database Table " + USGSDatabase.getTableName() + " from CSV");
        System.out.println("6. Free Form SQL Query Database " + USGSDatabase.getTableName());
        System.out.println("7. Read CSV file (Debug only, just display csv file)");
        System.out.println("0. Exit Program");
        System.out.println("================================================================");
    }

    public void displayMessage(String s) {
        System.out.println(s);
    }
    public void displayMessageNoLineBreak (String s) {
        System.out.println(s);
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
