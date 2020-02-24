package cscc.edu;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;


import static java.lang.Character.toUpperCase;


public class USGSDataInteraction {
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String DEPTH = "depth";
    public static final String MAG = "mag";
    private static Scanner input = new Scanner(System.in);
    USGSView usgsView;
    String connectionStringMasterDB = "jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=reallyStrongPwd123";
    String connectionStringUSGSDB = "jdbc:sqlserver://localhost:1433;databaseName="+ USGSDatabase.getDbName()+";user=sa;password=reallyStrongPwd123";
    USGSDatabase databaseMasterDB = new USGSDatabase(connectionStringMasterDB);
    USGSDatabase databaseUSGSDB;
    // we will store user selections in this, the keys can be:
    // "latitude" , "longitude" , "depth" , and "mag"
    private HashMap<String,Character> searchColumns = new HashMap<String,Character>();
    private HashMap<String,DoubleLowHigh<Double,Double>> searchColumnsDoubleValue = new HashMap<String, DoubleLowHigh<Double,Double>>();
    DoubleLowHigh<Double,Double> doubleDoubleLowHigh = new DoubleLowHigh<Double,Double>(0.0,0.0);
    public USGSDataInteraction() {
        this.usgsView = new USGSView();
        searchColumns.put(LATITUDE,null);
        searchColumns.put(LONGITUDE,null);
        searchColumns.put(DEPTH,null);
        searchColumns.put(MAG,null);
        searchColumnsDoubleValue.put(LATITUDE,null);
        searchColumnsDoubleValue.put(LONGITUDE,null);
        searchColumnsDoubleValue.put(DEPTH,null);
        searchColumnsDoubleValue.put(MAG,null);
    }

    public void inputLoop() {
        boolean done = false;
        while (!done) {
            usgsView.displayMainMenu();
            switch (getValidMenuIntegerInput(USGSView.MainMenuMaxNumber)) {
                case 0:
                    done = true;
                    usgsView.displayExitScreen();
                    break;
                case 1:
                    databaseCreationMenuInteraction();
                    break;
                case 2:
                    databaseSelectColumnsMenuInteraction();
                    break;
                case 3:
                    databaseSearchSelectedColumnsMenuInteraction();
                    break;
                case 4:
                    databaseDeleteRowOnSelectedColumnsMenuInteraction();
                    break;
                case 5:
                    databaseCountRowsOnSelectedColumnsMenuInteraction();
                    break;
            }
        }
    }

    private void databaseCountRowsOnSelectedColumnsMenuInteraction() {
    }

    private void databaseDeleteRowOnSelectedColumnsMenuInteraction() {
    }

    private void databaseSearchSelectedColumnsMenuInteraction() {
    }

    private void databaseSelectColumnsMenuInteraction() {
        /*
        Here the challenge is to make user select column(s) he wants to search by
        we will fill up the HashMap searchSolumns with his selections
         */
        usgsView.displayMessage("!!!!!!!!                 USGS Database Select Columns             !!!!!!!!");
        usgsView.displayMessage("!!!!!!!! You will select Y or N for columns you want to search on !!!!!!!!");
        usgsView.displayMessageNoLineBreak("!!!!!!!! Columns are: ");
        searchColumns.forEach((k, v) -> { usgsView.displayMessageNoLineBreak(k + " ");});
        usgsView.displayMessage("!!!!!!!!");
        searchColumns.forEach((k, v) -> {
            getSearchColumnOptionInput(k, v);
        });
    }

    private void getSearchColumnOptionInput(String k, Character v) {
        usgsView.displayMessage("Do you want to use Search on " + k + "? y/N: ");
        searchColumns.put(k, null);
        if (toUpperCase(getValidMenuCharacterInput("YyNn")) == 'Y') {
            searchColumns.put(k, 'Y');
        }
    }

    private Double getDoubleInput(String latititude) {
    }

    private void databaseCreationMenuInteraction() {
        boolean done = false;
        while (!done) {
            usgsView.displayDDLMenu();
            switch (getValidMenuIntegerInput(usgsView.DatabaseCreationMenuMaxNumber)) {
                case 0:
                    done = true;
                    usgsView.displayGoingBackToMainMenuScreen();
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
                    queryDataBaseFreeFormForDebugging();
                    break;
                case 7:
                    readCSVFile();
                    break;
            }
        }
        
    }

    private void readCSVFile() {
        usgsView.displayMessage("Just simply reads CSV file and scrolls on display, NO Database functions are done!");
        ReadCSVFile readCSVFile = new ReadCSVFile();
        readCSVFile.readCSVFileAndPrint();
    }
    private void deleteTable() {
        usgsView.displayMessage("Trying to drop table, please wait!");
        databaseUSGSDB = new USGSDatabase(connectionStringUSGSDB);
        if (databaseUSGSDB.deleteTable()) {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() +
                    " Table " + USGSDatabase.getTableName() +" deleted successfully!");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() +
                    " Table " + USGSDatabase.getTableName() + " could not be deleted! Sorry!");
        }
    }

    private void createTable() {
        usgsView.displayMessage("Trying to create Table, please wait!");
        databaseUSGSDB = new USGSDatabase(connectionStringUSGSDB);
        if (databaseUSGSDB.createTable()) {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() +
                                            " Table " + USGSDatabase.getTableName() +" created successfully!");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() +
                    " Table " + USGSDatabase.getTableName() + " could not be created! Sorry!");
        }

    }

    private void deleteAllRecordsInDataBase() {
        usgsView.displayMessage("Trying to drop database, this works only when started new, it take a few minutes,please wait!");
        if (databaseMasterDB.deleteAllRecordsInDB(USGSDatabase.getDbName())) {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + " deleted successfully!");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + " could not be deleted! Sorry!");
        }
    }

    private void loadDataBase() {
        usgsView.displayMessage("Trying to load database from CSV, it take a few minutes, please wait!");
        ReadCSVFile readCSVFile = new ReadCSVFile();
        databaseUSGSDB = new USGSDatabase(connectionStringUSGSDB);
        // if (databaseUSGSDB.loadAllRecordsInDB(databaseUSGSDB)) {
        if (readCSVFile.readCSVFileAndUpdateDatabase(databaseUSGSDB)) {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + " loaded successfully!");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabase.getDbName() + " could not be loaded! Sorry!");
        }
    }

    private void queryDataBaseFreeFormForDebugging() {
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
            try {
                // now word has SQL string to process
                ResultSet rs = databaseUSGSDB.executeSingleSql(word);
                usgsView.displayMessage("Result is: " + rs);
                ResultSetMetaData rsmd = null;
                rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) usgsView.displayMessage(",  ");
                        String columnValue = rs.getString(i);
                        usgsView.displayMessage(rsmd.getColumnName(i) + ": " + columnValue);
                    }
                    usgsView.displayMessage("");
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
    public Integer getValidMenuIntegerInput(Integer maxNumber) {
        boolean done = false;
        while (!done) {
            if (!input.hasNextInt()){
                String word = input.next();
                usgsView.displayMessage(word + " is not a number");
            } else {
                Integer i = input.nextInt();
                input.nextLine();
                if (i > maxNumber || i < 0) {
                    usgsView.displayMessage(" Number must be between 0 and "+ maxNumber);
                } else {
                    done = true;
                    return i;
                }
            }
        }
        return 0;
    }
    public Character getValidMenuCharacterInput(String allowedChars) {
        boolean done = false;
        while (!done) {
            String inString = input.nextLine();
            if (inString.matches("[" + allowedChars +"]{1}")){
                return inString.charAt(0);
            } else {
                    usgsView.displayMessage("Try again, can only enter one character of: " + allowedChars);
                }
        }
        return 'N';
    }
}