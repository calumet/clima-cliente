/*!
 * Grupo de Desarrollo de Software Calumet
 * Weather | Application | Data
 * Romel Pérez @prhonedev, 2014
 */

package weather;

import java.io.IOException;
import java.util.Properties;

public class Data {
    
    
    public static String key = "";
    public static String server = "";
    public static String station = "";
    public static String stationFolder = "";
    public static String title = "";
    public static String favicon = "";
    public static String uis = "";
    public static String calumet = "";
    
    
    // Search config file and sync with app
    public static boolean sync() {

        try{
            // Synchronizing data
            Properties res = new Properties();
            res.load(Data.class.getClassLoader().getResourceAsStream("weather/Config.properties"));
            if (!res.isEmpty()) {
            	key = res.getProperty("KEY");
            	server = res.getProperty("SERVER");
            	station = res.getProperty("STATION");
            	stationFolder = res.getProperty("STATIONFOLDER");
            	title = res.getProperty("TITLE");
            	favicon = res.getProperty("FAVICON");
            	uis = res.getProperty("UIS");
            	calumet = res.getProperty("CALUMET");
            }
            return true;
        } catch (IOException ex) {
            // Error synchronizing
            return false;
        }

    }


    // Save config
    public static boolean save(String parameters) {
        return true;
    }


}
