package cscc.edu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadCSVFile {
    static private String CSV_FILE_NAME = "2007-2017_large_quake.csv";

    public boolean readCSVFileAndUpdateDatabase(USGSDatabase usgsDatabase) {
        boolean returnValue = true;
        USGSCSVData usgscsvData = new USGSCSVData();
        int j = 0;
        try {
            String query = "select count(*) from " + USGSDatabase.TABLE_NAME;
            usgsDatabase.setStatement(usgsDatabase.getConnection().createStatement());
            ResultSet rs = usgsDatabase.getStatement().executeQuery(query);
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("Table " + USGSDatabase.TABLE_NAME + " exists and has " + rs.getInt(1) + " rows");
                return false;
            }
            usgsDatabase.setPreparedStatement(usgsDatabase.getConnection().prepareStatement(USGSDatabase.getSqlInsert()));
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
                if (!usgsDatabase.addRowToDBTable(usgscsvData, usgsDatabase))
                    return false;
                // System.out.println("processed line: " + j++ + " place: " + usgscsvData.place);
            } // end of while reading csv file is not empty
            // we may have a few prepared statements that are not done yet
            if (usgsDatabase.getCount() > 0) {
                int[] rows = usgsDatabase.getPreparedStatement().executeBatch();
                usgsDatabase.getConnection().commit();
                usgsDatabase.setCount(0);
            }
            // a few lines may need to be committed
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return (returnValue = false);
        } catch (IOException e) {
            e.printStackTrace();
            return (returnValue = false);
        } catch (SQLException e) {
            // e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
            return (returnValue = false);
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
}