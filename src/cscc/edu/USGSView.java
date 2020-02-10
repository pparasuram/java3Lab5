package cscc.edu;

public class USGSView {
    static public Integer MenuMaxNumber = 6;

    public USGSView() {
    }

    public void displayMenu(){
        System.out.println("!!!!!!!! USGS Database Welcome Screen !!!!!!!!");
        System.out.println("Please enter a choice from the below");
        System.out.println("===========================================");
        System.out.println("1. Create Database");
        System.out.println("2. Create Table");
        System.out.println("3. Delete All records");
        System.out.println("4. Delete Table");
        System.out.println("5. Load Database from CSV");
        System.out.println("6. Query Database");
        System.out.println("0. Exit Program");
        System.out.println("===========================================");
    }

    public void displayMessage(String s) {
        System.out.println(s);
    }

    public void displayExitScreen() {
        System.out.println("!!! Bye bye from USGS Database !!!");
    }
}
