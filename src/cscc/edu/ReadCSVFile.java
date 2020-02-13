package cscc.edu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ReadCSVFile {
    static private String CSV_FILE_NAME ="2007-2017_large_quake.csv";
    public USGSCSVData readCSVFile() {
        try {
            BufferedReader br = new BufferedReader( new FileReader(CSV_FILE_NAME) );
            // this variable points to the buffered line
            String line;
// Keep buffering the lines and print it.
            int i = 0;
            String[] lineData = new String[25];
            while ((line = br.readLine()) != null) {
// line has the data
                lineData = line.split(",");
                /*
                System.out.println(lineData);
                i++;
                if (i >= 11)
                    break;
                 */
                USGSCSVData usgscsvData = new USGSCSVData();
                Class<?> c = usgscsvData.getClass();
                Field f [] = c.getDeclaredFields();
                for (Field field: f) {
                    System.out.println(field);
                    System.out.println(field.getType());
                    System.out.println(field.getAnnotatedType());

                  /*  switch (field.getType()) {
                        case "java.lang.String" :
                            break;
                    }

                   */
                }
                //
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
