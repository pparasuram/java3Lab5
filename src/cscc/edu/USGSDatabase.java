package cscc.edu;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class USGSDatabase {
    static private Integer BATCH_COUNT = 100;
    static private String DB_NAME = "usgs";
    static private String TABLE_NAME = "earthquake_data";
    static private String CREATE_TABLE_STRING = "CREATE TABLE " + /* DB_NAME + "."+*/ TABLE_NAME +
            " (time datetime, " +
            " latitude real, " +
            " longitude real, " +
            " depth real, " +
            " mag real, " +
            " magType varchar(5), " +
            " nst varchar(5), " +
            " gap real, " +
            " dmin real, " +
            " rms real, " +
            " net varchar(8), " +
            " id varchar(30), " +
            " updated datetime, " +
            " place varchar (100), " +
            " type varchar(25), " +
            " horizontalError real, " +
            " depthError real, " +
            " magError real, " +
            " magNst varchar(5), " +
            " status varchar(10), " +
            " locationSource varchar(10), " +
            " magSource varchar(10))";
    private static final String SQL_INSERT = "INSERT INTO " + TABLE_NAME +
            " (time, " +
            "latitude, " +
            "longitude, " +
            "depth, " +
            "mag, " +
            "magType, " +
            "nst, " +
            "gap, " +
            "dmin, " +
            "rms, " +
            "net, " +
            "id, " +
            "updated, " +
            "place, " +
            "type, " +
            "horizontalError, " +
            "depthError, " +
            "magError, " +
            "magNst, " +
            "status, " +
            "locationSource, " +
            "magSource) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" ;
    static String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    static private String CSV_FILE_NAME = "2007-2017_large_quake.csv";
    static int count = 0;
    private String connectionUrl;
    private Connection connection;
    public PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);


    public static String getDbName() {
        return DB_NAME;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }
    public USGSDatabase(String connectionUrl) {
        this.connectionUrl = connectionUrl;
        connectDatabase(connectionUrl);
    }

    public void connectDatabase(String connection) {
        // Load SQL Server JDBC driver and establish connection.
        System.out.println("Connecting to SQL Server ... ");
        try {
            this.connection = DriverManager.getConnection(connectionUrl);
            this.statement = this.connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean createDB (String db_name) {
        String query = "CREATE DATABASE ";
        try {
            query = query + db_name;
            statement = connection.createStatement();
            statement.execute(query);
            return true;
        } catch (SQLException e) {
            // e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }
    }
    public boolean createTable (String db_name) {
        String query = CREATE_TABLE_STRING;
        try {
            query = CREATE_TABLE_STRING;
            statement = connection.createStatement();
            statement.execute(query);
            return true;
        } catch (SQLException e) {
            // e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }
    }

    public boolean closeDB(String db_name) {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            // System.out.println(e.getMessage());
            System.out.println("SQL Error: " + e.getMessage());
            // e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTable(String db_name) {
        String query = "DROP TABLE ";
        try {
            query = query + TABLE_NAME;
            statement = connection.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            // System.out.println(e.getMessage());
            System.out.println("SQL Error: " + e.getMessage() );
            // e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllRecordsInDB(String db_name) {
        String query = "DROP DATABASE ";
        try {
            query = query + db_name;
            statement = connection.createStatement();
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            // e.printStackTrace();
            return false;
        }
    }

    public boolean addRowToDBTable(USGSCSVData usgscsvData, USGSDatabase usgsDatabase) {
        // USGSCSVData usgscsvData has the data create a row in table and fill it with data
        boolean returnValue = true;
        count++;
        try {
            connection.setAutoCommit(false); // default true

            java.util.Date tempUtilDate = simpleDateFormat.parse(usgscsvData.time);
            java.sql.Timestamp tempSQLDate = new java.sql.Timestamp(tempUtilDate.getTime());
            usgsDatabase.preparedStatement.setTimestamp(1, tempSQLDate);
            if (usgscsvData.latitude == null || usgscsvData.latitude == "" || usgscsvData.latitude.isEmpty())
                usgscsvData.latitude = "0";
            usgsDatabase.preparedStatement.setFloat(2, Float.parseFloat(usgscsvData.latitude));
            if (usgscsvData.longitude == null || usgscsvData.longitude == "" || usgscsvData.longitude.isEmpty())
                usgscsvData.longitude = "0";
            usgsDatabase.preparedStatement.setFloat(3, Float.parseFloat(usgscsvData.longitude));
            if (usgscsvData.depth == null || usgscsvData.depth == "" || usgscsvData.depth.isEmpty())
                usgscsvData.depth = "0";
            usgsDatabase.preparedStatement.setFloat(4, Float.parseFloat(usgscsvData.depth));
            if (usgscsvData.mag == null || usgscsvData.mag == "" || usgscsvData.mag.isEmpty())
                usgscsvData.mag = "0";
            usgsDatabase.preparedStatement.setFloat(5, Float.parseFloat(usgscsvData.mag));
            usgsDatabase.preparedStatement.setString(6, usgscsvData.magType);
            usgsDatabase.preparedStatement.setString(7, usgscsvData.nst);
            if (usgscsvData.gap == null || usgscsvData.gap == "" || usgscsvData.gap.isEmpty())
                usgscsvData.gap = "0";
            usgsDatabase.preparedStatement.setFloat(8, Float.parseFloat(usgscsvData.gap));
            if (usgscsvData.dmin == null || usgscsvData.dmin == "" || usgscsvData.dmin.isEmpty())
                usgscsvData.dmin = "0";
            usgsDatabase.preparedStatement.setFloat(9, Float.parseFloat(usgscsvData.dmin));
            if (usgscsvData.rms == null || usgscsvData.rms == "" || usgscsvData.rms.isEmpty())
                usgscsvData.rms = "0";
            usgsDatabase.preparedStatement.setFloat(10, Float.parseFloat(usgscsvData.rms));
            usgsDatabase.preparedStatement.setString(11, usgscsvData.net);
            usgsDatabase.preparedStatement.setString(12, usgscsvData.id);
            tempUtilDate = simpleDateFormat.parse(usgscsvData.updated);
            tempSQLDate = new java.sql.Timestamp(tempUtilDate.getTime());
            usgsDatabase.preparedStatement.setTimestamp(13, tempSQLDate);
            usgsDatabase.preparedStatement.setString(14, usgscsvData.place);
            usgsDatabase.preparedStatement.setString(15, usgscsvData.type);
            if (usgscsvData.horizontalError == null || usgscsvData.horizontalError == "" || usgscsvData.horizontalError.isEmpty())
                usgscsvData.horizontalError = "0";
            usgsDatabase.preparedStatement.setFloat(16, Float.parseFloat(usgscsvData.horizontalError));
            if (usgscsvData.depthError == null || usgscsvData.depthError == "" || usgscsvData.depthError.isEmpty())
                usgscsvData.depthError = "0";
            usgsDatabase.preparedStatement.setFloat(17, Float.parseFloat(usgscsvData.depthError));
            if (usgscsvData.magError == null || usgscsvData.magError == "" || usgscsvData.magError.isEmpty())
                usgscsvData.magError = "0";
            usgsDatabase.preparedStatement.setFloat(18, Float.parseFloat(usgscsvData.magError));
            usgsDatabase.preparedStatement.setString(19, usgscsvData.magNst);
            usgsDatabase.preparedStatement.setString(20, usgscsvData.status);
            usgsDatabase.preparedStatement.setString(21, usgscsvData.locationSource);
            usgsDatabase.preparedStatement.setString(22, usgscsvData.magSource);
            usgsDatabase.preparedStatement.addBatch();
            if (count >= BATCH_COUNT) {
                int[] rows = usgsDatabase.preparedStatement.executeBatch();
                // System.out.println(Arrays.toString(rows));
                usgsDatabase.connection.commit();
                count = 0;
            }
        } catch (SQLException e) {
            // e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
            return(returnValue = false);
        } catch (ParseException e) {
            e.printStackTrace();
            return(returnValue = false);
        }
        return returnValue;
    }
    public boolean readCSVFileAndUpdateDatabase(USGSDatabase usgsDatabase) {
        boolean returnValue = true;
        USGSCSVData usgscsvData = new USGSCSVData();
        int j = 0;
        try {
            String query = "select count(*) from " + TABLE_NAME;
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("Table " + TABLE_NAME + " exists and has " + rs.getInt(1) + " rows");
                return false;
            }
            usgsDatabase.preparedStatement = connection.prepareStatement(SQL_INSERT);
            BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_NAME));
            // this variable points to the buffered line
            String line;
            // Keep buffering the lines and print it.
            boolean titleLine = true;
            String[] lineData = new String[25];
            while ((line = br.readLine()) != null) {
                int i = 0;
                if (titleLine) {
                    titleLine = false;
                    continue;
                }
                // we have a single line of data in line
                lineData = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
/*
                We use reflections to access class fields as an iterator and iterate through fields and
                assign them string values that were delimited with commas in the csv, we also took care to
                ensure we kept strings in quotations with commas from not being split
                At the end of below code usgscsvData fields have the data in the csv
 */
                Class<?> c = usgscsvData.getClass();
                Field[] f = c.getDeclaredFields();
                for (Field field : f) {
//                    System.out.println(field);
//                    System.out.println(field.getType());
//                    System.out.println(field.getAnnotatedType());
                    field.setAccessible(true);
                    try {
                        if (lineData[i].contains("\"")) {
                            String tempStr = lineData[i].replaceAll("\"", "");
                            lineData[i] = tempStr;
                        }
                        field.set(usgscsvData, lineData[i++]);
                        if (i >= lineData.length) {
                            break;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } // end of iterating through fields
                // now USGSCSVData usgscsvData is filled call a database update method
                if(!usgsDatabase.addRowToDBTable(usgscsvData, usgsDatabase))
                    return false;
                // System.out.println("processed line: " + j++ + " place: " + usgscsvData.place);
            } // end of while reading csv file is not empty
            // we may have a few prepared statements that are not done yet
            if (usgsDatabase.count > 0) {
                int[] rows = usgsDatabase.preparedStatement.executeBatch();
                usgsDatabase.connection.commit();
                count = 0;
            }
            // a few lines may need to be committed
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return(returnValue = false);
        } catch (IOException e) {
            e.printStackTrace();
            return(returnValue = false);
        } catch (SQLException e) {
            // e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
            return(returnValue = false);
        }
        return returnValue;
    }
    public void readCSVFileAndPrint() {
        int j = 0;
        try {
            BufferedReader br = null;
            br = new BufferedReader(new FileReader(CSV_FILE_NAME));
            // this variable points to the buffered line
            String line;
            // Keep buffering the lines and print it.
            boolean titleLine = true;
            String[] lineData = new String[25];
            while ((line = br.readLine()) != null) {
                int i = 0;
                if (titleLine) {
                    titleLine = false;
                    continue;
                }
                // we have a single line of data in line
                lineData = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                System.out.println(line);
                System.out.println(lineData);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ResultSet executeSingleSql(String sql) {
        System.out.print("Connecting to SQL Server ... ");
        try {
            // sql = "CREATE DATABASE my_temp_db_2";
            // preparedStatement.executeQuery(sql);
            // sql = "SELECT * FROM my_temp_db";
            // "SELECT * FROM INFORMATION_SCHEMA.TABLES"
            // sql = ("SELECT * FROM INFORMATION_SCHEMA.TABLES");
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("Done.");
            return  rs;
            // preparedStatement.addBatch("");
        }
        catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
