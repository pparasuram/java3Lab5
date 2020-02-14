package cscc.edu;

import java.util.Scanner;

public class USGSController {
    private static Scanner input = new Scanner(System.in);
    USGSView usgsView;
    String connectionStringMasterDB = "jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=reallyStrongPwd123";;
    String connectionStringUSGSDB = "jdbc:sqlserver://localhost:1433;databaseName="+ USGSDatabase.getDbName()+";user=sa;password=reallyStrongPwd123";;
    USGSDatabase databaseMasterDB = new USGSDatabase(connectionStringMasterDB);
    USGSDatabase databaseUSGSDB;
    public USGSController() {
        this.usgsView = new USGSView();
    }

    public void inputLoop() {
        boolean done = false;
        while (!done) {
            usgsView.displayMenu();
            switch (getValidMenuInput()) {
                case 0:
                    done = true;
                    usgsView.displayExitScreen();
                    break;
                case 1:
                    createDataBase();
                    break;
                case 2:
                    createTable();
                    break;
                case 3:
                    deleteAllRecordsInDataBase();
                    break;
                case 4:
                    deleteTable();
                    break;
                case 5:
                    loadDataBase();
                    break;
                case 6:
                    queryDataBase();
                    break;
                case 7:
                    readCSVFile();
                    break;
            }
        }
    }

    private void readCSVFile() {
        ReadCSVFile readCSVFile = new ReadCSVFile();
        readCSVFile.readCSVFileAndUpdateDatabase();
    }

    private void deleteTable() {
    }

    private void createTable() {
        databaseUSGSDB = new USGSDatabase(connectionStringUSGSDB);
        if (databaseUSGSDB.createTable(USGSDatabase.getDbName())) {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() +
                                            " Table " + USGSDatabase.getTableName() +"created successfully!");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() +
                    " Table " + USGSDatabase.getTableName() + "could not be created! Sorry!");
        }

    }

    private void deleteAllRecordsInDataBase() {
        if (databaseUSGSDB != null)
            databaseUSGSDB.closeDB(USGSDatabase.getDbName());
        if (databaseMasterDB.deleteAllRecordsInDB(USGSDatabase.getDbName())) {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + "deleted successfully!");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + "could not be deleted! Sorry!");
        }
    }

    private void loadDataBase() {
        if (databaseUSGSDB.loadAllRecordsInDB(USGSDatabase.getDbName())) {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + "loaded successfully!");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + "could not be loaded! Sorry!");
        }
    }

    private void queryDataBase() {
    }

    private void createDataBase() {
        if (!databaseMasterDB.createDB(USGSDatabase.getDbName())) {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + "exists, going back to main menu");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + "Created Successfully!!");
        }
    }
    public Integer getValidMenuInput() {
        boolean done = false;
        while (!done) {
            if (!input.hasNextInt()){
                String word = input.next();
                usgsView.displayMessage(word + " is not a number");
            } else {
                Integer i = input.nextInt();
                input.nextLine();
                if (i > usgsView.MenuMaxNumber || i < 0) {
                    usgsView.displayMessage(" Number must be between 0 and "+ usgsView.MenuMaxNumber);
                } else {
                    done = true;
                    return i;
                }
            }
        }
        return 0;
    }

}