package cscc.edu;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class USGSController {
    private static Scanner input = new Scanner(System.in);
    USGSView usgsView;
    String connectionStringMasterDB = "jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=reallyStrongPwd123";
    String connectionStringUSGSDB = "jdbc:sqlserver://localhost:1433;databaseName="+ USGSDatabase.getDbName()+";user=sa;password=reallyStrongPwd123";
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
        ReadCSVFilex readCSVFile = new ReadCSVFilex();
        readCSVFile.readCSVFileAndPrint();
    }

    private void deleteTable() {
        databaseUSGSDB = new USGSDatabase(connectionStringUSGSDB);
        if (databaseUSGSDB.deleteTable(USGSDatabase.getDbName())) {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() +
                    " Table " + USGSDatabase.getTableName() +" deleted successfully!");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() +
                    " Table " + USGSDatabase.getTableName() + " could not be deleted! Sorry!");
        }
    }

    private void createTable() {
        databaseUSGSDB = new USGSDatabase(connectionStringUSGSDB);
        if (databaseUSGSDB.createTable(USGSDatabase.getDbName())) {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() +
                                            " Table " + USGSDatabase.getTableName() +" created successfully!");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() +
                    " Table " + USGSDatabase.getTableName() + " could not be created! Sorry!");
        }

    }

    private void deleteAllRecordsInDataBase() {
        if (databaseMasterDB.deleteAllRecordsInDB(USGSDatabase.getDbName())) {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + " deleted successfully!");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + " could not be deleted! Sorry!");
        }
    }

    private void loadDataBase() {
        databaseUSGSDB = new USGSDatabase(connectionStringUSGSDB);
        // if (databaseUSGSDB.loadAllRecordsInDB(databaseUSGSDB)) {
        if (databaseUSGSDB.readCSVFileAndUpdateDatabase(databaseUSGSDB)) {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + " loaded successfully!");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + " could not be loaded! Sorry!");
        }
    }

    private void queryDataBase() {
        boolean done = false;
        databaseUSGSDB = new USGSDatabase(connectionStringUSGSDB);
        while (!done) {
            usgsView.displayQueryDataBaseMenu();
            String word = input.nextLine();
            try {
                if (word.toLowerCase().matches("end")) {
                    usgsView.displayMessage("Going back to Main Menu!");
                    done = true;
                    break;
                }
            } catch (Exception e) {
                usgsView.displayMessage("Invalid SQL syntax try again: End to exit!");
            }
            // now word has SQL string to process
            ResultSet rs = databaseUSGSDB.executeSingleSql(word);
            usgsView.displayMessage("Result is: " + rs);
            ResultSetMetaData rsmd = null;
            try {
                rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(",  ");
                        String columnValue = rs.getString(i);
                        System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
                    }
                    System.out.println("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void createDataBase() {
        if (!databaseMasterDB.createDB(USGSDatabase.getDbName())) {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + " exists, going back to main menu");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + " Created Successfully!!");
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
                if (i > USGSView.MenuMaxNumber || i < 0) {
                    usgsView.displayMessage(" Number must be between 0 and "+ USGSView.MenuMaxNumber);
                } else {
                    done = true;
                    return i;
                }
            }
        }
        return 0;
    }

}