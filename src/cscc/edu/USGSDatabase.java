package cscc.edu;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class USGSDatabase {
    static private final Integer BATCH_COUNT = 100;
    static private final String DB_NAME = "usgs";
    static public final String TABLE_NAME = "earthquake_data";
    static private final String CREATE_TABLE_STRING = "CREATE TABLE " + /* DB_NAME + "."+*/ TABLE_NAME +
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
    public static final String SELECT_STRING = "SELECT * FROM " + TABLE_NAME + " ";

    public static String getDeleteString() {
        return DELETE_STRING;
    }

    public static final String DELETE_STRING = "DELETE FROM " + TABLE_NAME + " ";
    public static String getCountString() {
        return COUNT_STRING;
    }

    public static final String COUNT_STRING = "SELECT count(*) AS [rowcount]  FROM " + TABLE_NAME + " ";
    public static String getSqlInsert() {
        return SQL_INSERT;
    }
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
    static private final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    static private int count = 0;
    private String connectionUrl;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    public static int getCount() {
        return count;
    }
    public static void setCount(int count) {
        USGSDatabase.count = count;
    }

    public static String getSelectString() {
        return SELECT_STRING;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }
    public Statement getStatement() {
        return statement;
    }
    public void setStatement(Statement statement) {
        this.statement = statement;
    }
    private USGSView usgsView = new USGSView();
    public static String getDbName() {
        return DB_NAME;
    }
    public static String getTableName() {
        return TABLE_NAME;
    }
    public USGSDatabase(String connectionUrl) {
        this.connectionUrl = connectionUrl;
        connectDatabase();
    }

    public void connectDatabase() {
        // Load SQL Server JDBC driver and establish connection.
        System.out.println("Connecting to SQL Server ... ");
        try {
            // see database exists
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
    public boolean createTable () {
        String query;
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
    public boolean closeDB() {
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

    public boolean deleteTable() {
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

            Date tempUtilDate = simpleDateFormat.parse(usgscsvData.time);
            Timestamp tempSQLDate = new Timestamp(tempUtilDate.getTime());
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
            tempSQLDate = new Timestamp(tempUtilDate.getTime());
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
    public ResultSet searchDatabase (StringBuilder queryString) {
        /*
        String query = "select * from " + USGSDatabase.TABLE_NAME  + " where " +
                                " latitude > ? and latitude < ? and" +
                                " longitude > ? and longitude < ? and" +
                                " depth > ? and depth < ? and" +
                                " mag > ? and mag < ?";
        // String query = "select count(*) from " + USGSDatabase.TABLE_NAME;
        // luckily for us queryString is ready to send to sql
         */
        // queryString.append(" LIMIT ? OFFSET ? ");
        ResultSet rs = null;
        try {
            // this.setStatement(this.getConnection().createStatement());
            this.setPreparedStatement(this.getConnection().prepareStatement(queryString.toString()));
            System.out.println("qs is: " + queryString.toString());
            if (!queryString.toString().toUpperCase().contains("DELETE"))
                rs = this.getPreparedStatement().executeQuery();
            else {
                this.getPreparedStatement().execute();
                usgsView.displayMessage("Successful Delete !!!");
            }
        } catch (SQLServerException e) {

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    public ResultSet searchDatabaseWithLimitAndOffset (StringBuilder queryString, Integer limit, Integer offset) {
        /*
        String query = "select * from " + USGSDatabase.TABLE_NAME  + " where " +
                                " latitude > ? and latitude < ? and" +
                                " longitude > ? and longitude < ? and" +
                                " depth > ? and depth < ? and" +
                                " mag > ? and mag < ?";
        // String query = "select count(*) from " + USGSDatabase.TABLE_NAME;
        // luckily for us queryString is ready to send to sql
         */
        ResultSet rs = null;
        try {
            // this.setStatement(this.getConnection().createStatement());
            this.preparedStatement = this.getConnection().prepareStatement(queryString.toString());
            System.out.println("qs is: " + queryString.toString());
            this.preparedStatement.setInt(1, offset);
            this.preparedStatement.setInt(2, limit);
            rs = this.preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    public ResultSet countSearchedDatabase(StringBuilder queryCountString) {
        /*
        String query = "select * from " + USGSDatabase.TABLE_NAME  + " where " +
                                " latitude > ? and latitude < ? and" +
                                " longitude > ? and longitude < ? and" +
                                " depth > ? and depth < ? and" +
                                " mag > ? and mag < ?";
        // String query = "select count(*) from " + USGSDatabase.TABLE_NAME;
        // luckily for us queryString is ready to send to sql
         */
        // queryString.append(" LIMIT ? OFFSET ? ");
        ResultSet rs = null;
        try {
            // this.setStatement(this.getConnection().createStatement());
            usgsView.displayMessage("String is: " + queryCountString.toString());
            this.setPreparedStatement(this.getConnection().prepareStatement(queryCountString.toString()));
            rs = this.getPreparedStatement().executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    public ResultSet executePreparedSql(String sql) {
        System.out.print("Connecting to SQL Server ... ");
        try {
            // preparedStatement.executeQuery(sql);
            // sql = "SELECT * FROM my_temp_db";
            // "SELECT * FROM INFORMATION_SCHEMA.TABLES"
            // sql = ("SELECT * FROM INFORMATION_SCHEMA.TABLES");
            ResultSet rs = preparedStatement.executeQuery(sql);
            System.out.println("Done.");
            return rs;
            // preparedStatement.addBatch("");
        }
        catch (SQLException e) {
            System.out.println("SQL Error: " + e.getCause());
        }
        catch (Exception e) {
            System.out.println();
            e.printStackTrace();
        }
        return null;
    }

}
