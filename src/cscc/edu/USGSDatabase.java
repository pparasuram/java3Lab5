package cscc.edu;


import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class USGSDatabase {
    private String connectionUrl;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;
    static private Integer BATCH_COUNT = 50;
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
                                                        " id varchar(12), " +
                                                        " updated datetime, " +
                                                        " place varchar (100), " +
                                                        " type varchar(25), " +
                                                        " horizontalError real, " +
                                                        " depthError real, " +
                                                        " magError real, " +
                                                        " magNst varchar(5), " +
                                                        " status varchar(10), " +
                                                        " locationSource varchar(5), " +
                                                        " magSource varchar(5))";


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
        System.out.print("Connecting to SQL Server ... ");
        try {
            this.connection = DriverManager.getConnection(connectionUrl);
            this.statement = this.connection.createStatement();
        } catch (Exception e) {
            System.out.println();
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
            e.printStackTrace();
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
            e.printStackTrace();
            return false;
        }
    }

    public boolean closeDB(String db_name) {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getCause());
            e.printStackTrace();
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
            System.out.println(e.getCause());
            e.printStackTrace();
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
            System.out.println(e.getCause());
            e.printStackTrace();
            return false;
        }
    }


    //             query = CREATE_TABLE_STRING;
    //            statement.execute(query);
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
            System.out.println("SQLException " + e);
            e.printStackTrace();
        }
        catch (Exception e) {
            System.out.println();
            e.printStackTrace();
        }
        return null;
    }
    public ResultSet executePreparedSql(String sql) {
        System.out.print("Connecting to SQL Server ... ");
        try {
            sql = "CREATE DATABASE my_temp_db_2";
            // preparedStatement.executeQuery(sql);
            // sql = "SELECT * FROM my_temp_db";
            // "SELECT * FROM INFORMATION_SCHEMA.TABLES"
            // sql = ("SELECT * FROM INFORMATION_SCHEMA.TABLES");
            ResultSet rs = preparedStatement.executeQuery(sql);
            System.out.println("Done.");
            return rs;
            // preparedStatement.addBatch("");
        } catch (Exception e) {
            System.out.println();
            e.printStackTrace();
        }
        return null;
    }
    public ResultSet executePreparedBatchSql(String sql) {
        System.out.print("Connecting to SQL Server ... ");
        try {
            sql = "CREATE DATABASE my_temp_db_2";
            // preparedStatement.executeQuery(sql);
            // sql = "SELECT * FROM my_temp_db";
            // "SELECT * FROM INFORMATION_SCHEMA.TABLES"
            // sql = ("SELECT * FROM INFORMATION_SCHEMA.TABLES");
            ResultSet rs = preparedStatement.executeQuery(sql);
            System.out.println("Done.");
            return rs;
            // preparedStatement.addBatch("");
        } catch (Exception e) {
            System.out.println();
            e.printStackTrace();
        }
        return null;
    }
    public void addRowToDBTable(USGSCSVData usgscsvData) {
        // USGSCSVData usgscsvData has the data create a row in table and fill it with data
    }
    public boolean loadAllRecordsInDB(String dbName) {
        // create object of class ReadCSVFile
        // execute its loop readCSVFileAndUpdateDatabase
        // that loop will call method above to add records to table in a batch mode
        ReadCSVFile readCSVFile = new ReadCSVFile();
        readCSVFile.readCSVFileAndUpdateDatabase();
        return false;
    }
}
