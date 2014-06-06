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
    public static String uimain_title = "";
    public static String uiconfig_title = "";
    public static String favicon = "";
    public static String uis = "";
    public static String civil = "";
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
            	uimain_title = res.getProperty("UI_MAIN_TITLE");
            	uiconfig_title = res.getProperty("UI_CONFIG_TITLE");
            	favicon = res.getProperty("FAVICON");
            	uis = res.getProperty("UIS");
                civil = res.getProperty("CIVIL");
            	calumet = res.getProperty("CALUMET");
            }
            return true;
        } catch (IOException ex) {
            // Error synchronizing
            return false;
        }

    }


    // Update modified config
    public static boolean update(String parameters) {
        return true;
    }


}
