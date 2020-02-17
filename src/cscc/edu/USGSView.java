package cscc.edu;

public class USGSView {
    static public Integer MenuMaxNumber = 7;

    public USGSView() {
    }

    public void displayMenu() {
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

    public void displayQueryDataBaseMenu() {
        System.out.println("!!!!!!!! USGS Database Query Screen !!!!!!!!");
        System.out.println("Please enter your SQL string below: This field is not edited");
        System.out.println("Please enter End to exit:");
    }
    public void displayExitScreen() {
        System.out.println("!!! Bye bye from USGS Database !!!");
    }
}
