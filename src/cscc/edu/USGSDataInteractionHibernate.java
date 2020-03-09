package cscc.edu;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


import static java.lang.Character.compare;
import static java.lang.Character.toUpperCase;


public class USGSDataInteractionHibernate {
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String DEPTH = "depth";
    private static final String MAG = "mag";
    private static final String [] SEARCH_FIELDS = {LATITUDE,LONGITUDE, DEPTH,  MAG};
    public static final int LIMIT = 100;
    private static final int TABS_PER_COL = 6;
    private static final int CHARS_PER_TAB = 5;
    private static Scanner input = new Scanner(System.in);
    boolean firstTime = true;
    USGSView usgsView;
    String connectionStringMasterDB = "jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=reallyStrongPwd123";
    String connectionStringUSGSDB = "jdbc:sqlserver://localhost:1433;databaseName="+ USGSDatabaseHibernate.getDbName()+";user=sa;password=reallyStrongPwd123";
    USGSDatabaseHibernate databaseMasterDB = new USGSDatabaseHibernate(connectionStringMasterDB);
    USGSDatabaseHibernate databaseUSGSDB = new USGSDatabaseHibernate("");
    // we will store user selections in this, the keys can be:
    // "latitude" , "longitude" , "depth" , and "mag"
    private HashMap<String,Character> searchColumns = new HashMap<String,Character>();
    private HashMap<String,DoubleLowHigh<Double,Double>> searchColumnsDoubleValue = new HashMap<String, DoubleLowHigh<Double,Double>>();
    private Boolean firstTimeForHeadings = true;
    // DoubleLowHigh<Double,Double> doubleDoubleLowHigh = new DoubleLowHigh<Double,Double>(0.0,0.0);
    public USGSDataInteractionHibernate() {
        this.usgsView = new USGSView();
        searchColumns.put(LATITUDE,null);
        searchColumns.put(LONGITUDE,null);
        searchColumns.put(DEPTH,null);
        searchColumns.put(MAG,null);
        searchColumnsDoubleValue.put(LATITUDE, new DoubleLowHigh<Double,Double>(null, null));
        searchColumnsDoubleValue.put(LONGITUDE,new DoubleLowHigh<Double,Double>(null, null));
        searchColumnsDoubleValue.put(DEPTH,new DoubleLowHigh<Double,Double>(null, null));
        searchColumnsDoubleValue.put(MAG,new DoubleLowHigh<Double,Double>(null, null));
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
                    databaseSelectedColumnsGetRangeMenuInteraction();
                    databaseSearchOnSelectedColumns();
                    break;
                case 4:
                    databaseSearchOnSelectedColumns();
                    break;
                case 5:
                    databaseDeleteRowOnSelectedColumnsMenuInteraction();
                    break;
                case 6:
                    databaseCountRowsOnSelectedColumnsMenuInteraction();
                    break;
            }
        }
    }

    private void databaseCountRowsOnSelectedColumnsMenuInteraction() {
        databaseUSGSDB = new USGSDatabaseHibernate(connectionStringUSGSDB);
        StringBuilder queryString = new StringBuilder (USGSDatabaseHibernate.getCountString());
        // queryString now has get * from earthquake_data add stuff to it
        // collect user supplied parameters
        String tempString = getCompleteQueryString(queryString);
        int rowCount = databaseUSGSDB.getTableRowCountWithHQL(tempString);
        if (rowCount < 0 ) {
            usgsView.displayMessage("!!!!!! dbError encountered !!!!!!!!!!");
        } else {
            usgsView.displayMessage("!!!!!! Count of rows is: !!!!!!!!!!");
            usgsView.displayMessage("!!!!!! Row Count: "+ rowCount + " !!!!!");
            usgsView.displayMessage("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }

    }

    private String getCompleteQueryString(StringBuilder queryString) {
        Map<String, String> params = new HashMap<String, String>();
        params = fillParams (params);
        if (!params.isEmpty()) {
            queryString.append(" where ");
            firstTime = true;
            queryString = completeQueryString(queryString, params);
            firstTime = true;
        }
        return queryString.toString();
    }

    private void databaseSearchOnSelectedColumns() {
        // here the challenge is as follows:
        // searchColumns has the columns he wants to search on and has filled
        // searchColumnsDoubleValue has the low and high for these 2 columns
        // we need to call the database search with these 2 hashmaps, and formulate the search string
        // databaseUSGSDB = new USGSDatabaseHibernate(connectionStringUSGSDB);
        StringBuilder queryString = new StringBuilder (USGSDatabaseHibernate.getSelectString());
        StringBuilder queryCountString = new StringBuilder (USGSDatabaseHibernate.getCountString());
        // queryString now has get * from earthquake_data add stuff to it
        // collect user supplied parameters
        String tempQueryString = getCompleteQueryString(queryString);
        String tempCountQueryString = getCompleteQueryString(queryCountString);
        //////////////////////////////////////////////////////////////////////////////
        /*Map<String, String> params = new HashMap<String, String>();
        Integer limit, offset;

        params = fillParams (params);
        if (!params.isEmpty()) {
            queryString.append(" where ");
            queryCountString.append(" where ");
            firstTime = true;
            queryString = completeQueryString(queryString, params);
            firstTime = true;
            queryCountString = completeQueryString(queryCountString, params);
        }*/
        // String tempString = queryCountString.toString();
        ///////////////////////////////////////////////////////////////////////////////////
        int rowCount; //getTableRowCountWithHQL(String whereClause)
        rowCount = databaseUSGSDB.getTableRowCountWithHQL(tempCountQueryString);
        try {
                usgsView.displayMessage("There are: " + rowCount + " in your search Query!!");
                // limit = rowCount;
                List<USGSTableData> resultList = databaseUSGSDB.searchDatabase (tempQueryString);
                displaySearchResult(resultList);
            } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void displaySearchResult(List<USGSTableData> resultList) {
        String columnValue;
        // usgsView.displayMessage("Result is: " + resultList);
        firstTimeForHeadings = true;
        USGSTableData tempUSGSTableData = new USGSTableData();
        printHeadings(tempUSGSTableData);
        firstTimeForHeadings = false;
        for (USGSTableData usgsTableData : resultList) {
            displayUSGSTableData(usgsTableData);
            firstTimeForHeadings = false;
        }
    }

    private void displayUSGSTableData(USGSTableData usgsTableData) {
        Class<?> c = usgsTableData.getClass();
        Field[] f = c.getDeclaredFields();
        for (Field field : f) {
//                    System.out.println(field);
//                    System.out.println(field.getType());
//                    System.out.println(field.getAnnotatedType());
            field.setAccessible(true);
            int tabsBefore;
            int tabsAfter;
            int fieldLength = field.getName().length();
                try {
                    fieldLength = field.get(usgsTableData).toString().length();
                    tabsBefore = calcBeforeTabs(fieldLength);
                    if ((tabsAfter = (TABS_PER_COL - tabsBefore - fieldLength)) < 0 )
                        tabsAfter = 0;
                    printTabs(tabsBefore);
                    usgsView.displayMessageNoLineBreak(field.get(usgsTableData).toString());
                    printTabs(tabsAfter);
                    usgsView.displayMessageNoLineBreak("|");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        } // end of iterating through fields
        firstTimeForHeadings = false;
        usgsView.displayMessage("");
    }
    private void printHeadings(USGSTableData usgsTableData) {
        Class<?> c = usgsTableData.getClass();
        Field[] f = c.getDeclaredFields();
        for (Field field : f) {
            field.setAccessible(true);
            int tabsBefore;
            int tabsAfter;
            int fieldLength = field.getName().length();
            tabsBefore = calcBeforeTabs(fieldLength);
            if ((tabsAfter = (TABS_PER_COL - tabsBefore - fieldLength)) < 0)
                tabsAfter = 0;
            printTabs(tabsBefore);
            usgsView.displayMessageNoLineBreak(field.getName());
            printTabs(tabsAfter);
            usgsView.displayMessageNoLineBreak("|");
        }
        usgsView.displayMessage("");
    }

    private void printTabs(int numTabs) {
        for (int i = 1; i < numTabs; i++)
            usgsView.displayMessageNoLineBreak("\t");
    }

    private int calcBeforeTabs(int fieldLength) {
        int tabsByField = fieldLength / CHARS_PER_TAB; // this int div will give number of tabs taken by field
        if ((TABS_PER_COL - tabsByField) > 0)
            if ((TABS_PER_COL - tabsByField) > 1)
                return 1;
        return 0;
    }

    private StringBuilder completeQueryString(StringBuilder queryString, Map<String, String> params) {
        params.forEach((k, v) -> {
            assembleQueryString(k,v,queryString);
        });
        return queryString;
    }

    private void assembleQueryString(String k, String v, StringBuilder queryString) {
        if (firstTime) {
            queryString.append(" " + k + " " + v + " ");
            firstTime = false;
        } else
            queryString.append(" AND " + k + " " + v + " ");
    }

    private Map<String, String> fillParams(Map<String, String> params) {
        for (String searchField : SEARCH_FIELDS) {
            if (searchColumns.get(searchField) == null)
                continue;
            if (toUpperCase(searchColumns.get(searchField)) == 'Y') {
                // yes he wants to query on the searchField
                params.put(searchField, "");
                // now build the string
                DoubleLowHigh<Double, Double> dLH = searchColumnsDoubleValue.get(searchField);
                if (dLH.getHigh() == null) {// means he does not want a range
                    params.put(searchField, " = " + (dLH.getLow()== null ? 0: dLH.getLow().toString()));
                } else {
                    // he wants a range
                    if (dLH.getLow() != null)
                            if (dLH.getLow() <= dLH.getHigh())
                                params.put(searchField, " >= " + dLH.getLow() + " AND " + searchField + " <= " + dLH.getHigh());
                            else
                                params.put(searchField, " >= " + dLH.getHigh() + " AND " + searchField + " <= " + dLH.getLow());
                    else // dLH.Low is null but high is not
                        params.put(searchField, " = " + dLH.getHigh());
                }
                // now build the search field string based on low and high values
            } // yes he wants to search on this searchField
        } // end of foreach searchField
        return params;
    }  // end of fillParams
    private void databaseDeleteRowOnSelectedColumnsMenuInteraction() {

        // here the challenge is as follows:
        // searchColumns has the columns he wants to search on and has filled
        // searchColumnsDoubleValue has the low and high for these 2 columns
        // we need to call the database search with these 2 hashmaps, and formulate the search string
        databaseUSGSDB = new USGSDatabaseHibernate(connectionStringUSGSDB);
        StringBuilder queryString = new StringBuilder (USGSDatabaseHibernate.getDeleteString());
        StringBuilder queryCountString = new StringBuilder (USGSDatabaseHibernate.getCountString());
        // queryString now has get * from earthquake_data add stuff to it
        // collect user supplied parameters
        String tempQueryString = getCompleteQueryString(queryString);
        String tempCountQueryString = getCompleteQueryString(queryCountString);
        ////////////////////////////////////////////////////////////////
        /*Map<String, String> params = new HashMap<String, String>();
        params = fillParams (params);
        if (!params.isEmpty()) {
            queryString.append(" where ");
            queryCountString.append(" where ");
            firstTime = true;
            queryString = completeQueryString(queryString, params);
            firstTime = true;
            queryCountString = completeQueryString(queryCountString, params);
        }
        String tempString = queryCountString.toString();*/
        ///////////////////////////////////////////////////////////////////////
        int rowCount; //getTableRowCountWithHQL(String whereClause)
        rowCount = databaseUSGSDB.getTableRowCountWithHQL(tempCountQueryString);
        try {
            if (rowCount == 0)
                usgsView.displayMessage("Nothing to delete Count of records is Zero, returning to Menu!");
            else {
                usgsView.displayMessageNoLineBreak("There are: " + rowCount + " in your search Query, do you want to DELETE them all Y/n?");
                if (toUpperCase(getValidMenuCharacterInput("YyNn")) == 'Y') {
                    databaseUSGSDB.deleteRowsFromUSGSTableData(tempQueryString);
                } else
                    usgsView.displayMessage("Okay no deletion done, returning to Menu!");
            }
        } catch (Exception e) {
            // e.printStackTrace();
            usgsView.displayMessage("error: " + e.getCause());
        }

    }

    private void databaseSelectedColumnsGetRangeMenuInteraction() {
                /*
        Here the challenge is to make user add ranges for columns he wants to search on
        we will fill up the HashMap searchColumns with his selections
         */
        searchColumns.forEach((k, v) -> {
            getSearchColumnHighLowValueInput(k, v);
        });

    }

    private void getSearchColumnHighLowValueInput(String k, Character v) {
        if ( v != null)
            if (compare(toUpperCase(v), 'Y')== 0) {
                getSearchColumnHighLowDoubleValueInput(k);
            }
    }
    private void getSearchColumnHighLowDoubleValueInput(String searchColumn) {
        usgsView.displayMessage("!!!!!!!!     USGS Database Select Values for Columns Menu         !!!!!!!!");
        usgsView.displayMessage("!!!!! You will give low and high values for columns selected earlier !!!!!");
        usgsView.displayMessage("!!!!!!!! Enter Low and High Values for : " + searchColumn + ": !!!!!!!!");
        DoubleLowHigh<Double,Double> doubleRange = searchColumnsDoubleValue.get (searchColumn);
        getDoubleRange (searchColumn, doubleRange);
    }

    private void getDoubleRange(String searchColumn, DoubleLowHigh<Double, Double> doubleRange) {

        doubleRange.setLow(getDoubleFromKeyboard("Low", searchColumn));
        doubleRange.setHigh(getDoubleFromKeyboard("High", searchColumn));
    }

    private Double getDoubleFromKeyboard(String lowOrHigh, String searchColumn) {

        boolean done = false;
        while (!done) {
            usgsView.displayMessage("!!!!!!!! Enter Low and High bound Values, one at a time! to search a exact value, enter 0 for the low or high Value !!!!!!");
            // , to search a exact value, enter 0 for the high Value
            usgsView.displayMessage("!!!!!!!! , the other value becomes the "+ "exact value".toUpperCase() +  " we search for! if you want range to start at 0 use -0.00001 for low !!!!!!");
            usgsView.displayMessage("!!!!!!!! Enter " + lowOrHigh + " Value for " + searchColumn + ": ");
            if (!input.hasNextDouble()){
                String word = input.next();
                usgsView.displayMessage(word + " is not a Double: needs number with decimals, or Integer");
            } else {
                Double d = input.nextDouble();
                input.nextLine();
                done = true;
                if (d == 0)
                    d = null;
                return d;
            }
        }
        return 0.0;
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
    private void databaseCreationMenuInteraction() {
        boolean done = false;
        while (!done) {
            usgsView.displayDDLMenu();
            switch (getValidMenuIntegerInput(USGSView.DatabaseCreationMenuMaxNumber)) {
                case 0:
                    done = true;
                    usgsView.displayGoingBackToMainMenuScreen();
                    break;
                case 1:
                    loadDataBase();
                    break;
                case 2:
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
    private void loadDataBase() {
        usgsView.displayMessage("Trying to load database from CSV, it take a few minutes, please wait!");
        ReadCSVFile readCSVFile = new ReadCSVFile();
        databaseUSGSDB = new USGSDatabaseHibernate(connectionStringUSGSDB);
        // if (databaseUSGSDB.loadAllRecordsInDB(databaseUSGSDB)) {
        if (readCSVFile.readCSVFileAndUpdateDatabase(databaseUSGSDB)) {
            usgsView.displayMessage("DataBase: " + USGSDatabaseHibernate.getDbName() + " loaded successfully!");
        } else {
            usgsView.displayMessage("DataBase: " + USGSDatabaseHibernate.getDbName() + " could not be loaded! Sorry!");
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