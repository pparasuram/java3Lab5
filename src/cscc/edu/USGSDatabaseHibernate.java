package cscc.edu;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class USGSDatabaseHibernate {
    static private final Integer BATCH_COUNT = 100;
    static private final String DB_NAME = "usgs";
    // static public final String TABLE_NAME = "earthquake_data"; "USGSTableData"
    static public final String TABLE_NAME = "USGSTableData";
    // public static final String SELECT_STRING = "SELECT * FROM " + TABLE_NAME + " ";
    public static final String SELECT_STRING = "FROM " + TABLE_NAME + " ";
    public static String getDeleteString() {
        return DELETE_STRING;
    }
    public static final String DELETE_STRING = "DELETE FROM " + TABLE_NAME + " ";
    public static String getCountString() {
        return COUNT_STRING;
    }
    public static final String COUNT_STRING = "select count(*) from " + TABLE_NAME + " ";

    static private final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    static private int count = 0;
    private String connectionUrl;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
    StandardServiceRegistry ssr;
    Metadata meta;
    SessionFactory factory;
    Session session;
    Transaction transaction;
    public static int getCount() {
        return count;
    }
    public static void setCount(int count) {
        USGSDatabaseHibernate.count = count;
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
    public USGSDatabaseHibernate(String connectionUrl) {
        this.connectionUrl = connectionUrl;
        connectDatabase();
    }
    public void connectDatabase() {
        //Create typesafe ServiceRegistry object
        try {
            ssr = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            meta = new MetadataSources(ssr).getMetadataBuilder().build();
            factory = meta.getSessionFactoryBuilder().build();
            session = factory.openSession();

        } catch (HibernateException e) {
            System.out.println("sky fell down in Factory, call Prakash: " + e.getMessage());
            e.printStackTrace();
        }
/*

        // Load SQL Server JDBC driver and establish connection.
        System.out.println("Connecting to SQL Server ... ");
        try {
            // see database exists
            this.connection = DriverManager.getConnection(connectionUrl);
            this.statement = this.connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
    }
    public boolean addRowToDBTable(USGSCSVData usgscsvData, USGSDatabaseHibernate usgsDatabase) {
        // USGSCSVData usgscsvData has the data create a row in table and fill it with data
        boolean returnValue = true;
        count++;
        try {
            // connection.setAutoCommit(false); // default true
            transaction = session.beginTransaction();

            // new OldSQLInsert(usgscsvData, usgsDatabase).invoke();
            // linear update
            USGSTableData usgsTableData = new USGSTableData();
            fillUSGSTableDataFromCSVData(usgscsvData, usgsTableData);
            session.saveOrUpdate(usgsTableData);

            /*Query query = session.createQuery(getInsertQueryString());
            int rowsAffected = query.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);*/
/*
            if (count >= BATCH_COUNT) {
                int[] rows = usgsDatabase.preparedStatement.executeBatch();
                // System.out.println(Arrays.toString(rows));
                usgsDatabase.connection.commit();
                count = 0;
            }
*/
            transaction.commit();
        } catch ( Throwable e) {
            e.printStackTrace();
            return(returnValue = false);
        }
        return returnValue;
    }


    private void fillUSGSTableDataFromCSVData(USGSCSVData usgscsvData, USGSTableData usgsTableData) {
        if (usgscsvData.latitude == null || Objects.equals(usgscsvData.latitude, "") || usgscsvData.latitude.isEmpty())
            usgscsvData.latitude = "0";
        if (usgscsvData.longitude == null || usgscsvData.longitude == "" || usgscsvData.longitude.isEmpty())
            usgscsvData.longitude = "0";
        if (usgscsvData.depth == null || usgscsvData.depth == "" || usgscsvData.depth.isEmpty())
            usgscsvData.depth = "0";
        if (usgscsvData.mag == null || usgscsvData.mag == "" || usgscsvData.mag.isEmpty())
            usgscsvData.mag = "0";
        if (usgscsvData.gap == null || usgscsvData.gap == "" || usgscsvData.gap.isEmpty())
            usgscsvData.gap = "0";
        if (usgscsvData.dmin == null || usgscsvData.dmin == "" || usgscsvData.dmin.isEmpty())
            usgscsvData.dmin = "0";
        if (usgscsvData.rms == null || usgscsvData.rms == "" || usgscsvData.rms.isEmpty())
            usgscsvData.rms = "0";
        if (usgscsvData.horizontalError == null || usgscsvData.horizontalError == "" || usgscsvData.horizontalError.isEmpty())
            usgscsvData.horizontalError = "0";
        if (usgscsvData.depthError == null || usgscsvData.depthError == "" || usgscsvData.depthError.isEmpty())
            usgscsvData.depthError = "0";
        if (usgscsvData.magError == null || usgscsvData.magError == "" || usgscsvData.magError.isEmpty())
            usgscsvData.magError = "0";
        usgsTableData.setId(usgscsvData.getId());
        usgsTableData.setTime(usgscsvData.getTime());
        usgsTableData.setLatitude(Double.valueOf(usgscsvData.getLatitude()));
        usgsTableData.setLongitude(Double.valueOf(usgscsvData.getLongitude()));
        usgsTableData.setDepth(Double.valueOf(usgscsvData.getDepth()));
        usgsTableData.setMag(Double.valueOf(usgscsvData.getMag()));
        usgsTableData.setMagType(usgscsvData.getMagType());
        usgsTableData.setNst(usgscsvData.getNst());
        usgsTableData.setGap(Double.valueOf(usgscsvData.getGap()));
        usgsTableData.setDmin(Double.valueOf(usgscsvData.getDmin()));
        usgsTableData.setRms(Double.valueOf(usgscsvData.getRms()));
        usgsTableData.setNet(usgscsvData.getNet());
        usgsTableData.setEq_id(usgscsvData.getEq_id());
        usgsTableData.setUpdated(usgscsvData.getUpdated());
        usgsTableData.setPlace(usgscsvData.getPlace());
        usgsTableData.setType(usgscsvData.getType());
        usgsTableData.setHorizontalError(Double.valueOf(usgscsvData.getHorizontalError()));
        usgsTableData.setDepthError(Double.valueOf(usgscsvData.getDepthError()));
        usgsTableData.setMagError(Double.valueOf(usgscsvData.getMagError()));
        usgsTableData.setMagNst(usgscsvData.getMagNst());
        usgsTableData.setStatus(usgscsvData.getStatus());
        usgsTableData.setLocationSource(usgscsvData.getLocationSource());
        usgsTableData.setMagSource(usgscsvData.getMagSource());
    }
    public boolean deleteRowsFromUSGSTableData(String hql) {
        // USGSCSVData usgscsvData has the data create a row in table and fill it with data
        boolean returnValue = true;
        try {
            // connection.setAutoCommit(false); // default true
            transaction = session.beginTransaction();
            // new OldSQLInsert(usgscsvData, usgsDatabase).invoke();
            // linear update
            Query query = session.createQuery(hql);
            query.executeUpdate();
            transaction.commit();
            /*Query query = session.createQuery(getInsertQueryString());
            int rowsAffected = query.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);*/

        } catch ( Throwable e) {
            e.printStackTrace();
            transaction.rollback();
            return(returnValue = false);
        }
        return returnValue;
    }
    public List<USGSTableData> searchDatabase (String queryString) {
        Query query = session.createQuery(queryString);
        List<USGSTableData> resultList = query.getResultList();
        return resultList;
    }

    public int getTableRowCountWithHQL(String getCountString) {
        // USGSCSVData usgscsvData has the data create a row in table and fill it with data
        int returnValue = -1;
        // count++;
        try {
            // connection.setAutoCommit(false); // default true
            // transaction = session.beginTransaction();

            // new OldSQLInsert(usgscsvData, usgsDatabase).invoke();
            // linear update
           // Query query =
           //  session.createQuery("select count(*) from USGSTableData)").list().size();

            String hql =  getCountString ;
            Query query = session.createQuery(hql);
            List listResult = query.list();
            Number number = (Number) listResult.get(0);
            return(number.intValue());
            // return (int) listResult.get(0);

/*
            int rowsAffected = query.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
*/
/*
            if (count >= BATCH_COUNT) {
                int[] rows = usgsDatabase.preparedStatement.executeBatch();
                // System.out.println(Arrays.toString(rows));
                usgsDatabase.connection.commit();
                count = 0;
            }
*/
            // transaction.commit();
        } catch ( Throwable e) {
            e.printStackTrace();
            return(returnValue = -1);
        }
    }
}
