/*!
 * Grupo de Desarrollo de Software Calumet
 * Weather | Application | Data
 * Romel Pérez @prhonedev, 2014
 */

package weather;

import java.io.*;
import java.net.URLDecoder;
import java.util.Properties;

public class Data {
    
    
    // Configuración de funcionalidad
    public static int updateTime = 300;
    public static String server = "http://localhost:6000/addData";
    
    // Configuración de usuario
    public static String key = "";
    public static String station = "";
    public static String stationFolder = "";
    public static String lastSent = "";
    
    // Datos estáticos
    public static String uimain_title = "Reportador del Clima";
    public static String uiconfig_title = "Configuración";
    public static String favicon = "resources/favicon.png";
    public static String uis = "resources/uis.png";
    public static String civil = "resources/civil.png";
    public static String calumet = "resources/calumet.png";
    public static String config = "weather/Config.properties";
    
    // Datos que se recolectan
    public static String[] dataProps = {
        "date",
        "time",
        "Temp Out",
        "Hi Temp",
        "Low Temp",
        "Out Hum",
        "Dew Pt.",
        "Wind Speed",
        "Wind Dir",
        "Wind Run",
        "Hi Speed",
        "Hi Dir",
        "Wind Chill",
        "Heat Index",
        "THW index",
        "THSW Index",
        "Bar",
        "Rain",
        "Rain Rate",
        "Solar Rad.",
        "Solar Energy",
        "Hi Solar Rad.",
        "UV Index",
        "UV Dose",
        "Hi UV",
        "Heat D-D",
        "Cool D-D",
        "In Temp",
        "In Hum",
        "In Dew",
        "In Heat",
        "ET",
        "Wind Samp",
        "Wind Tx",
        "ISS Recept",
        "Arc. Int."
    };
    
    
    // Decore files direcctions
    public static boolean start() {
        try {
            String path = Data.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            
            favicon = decodedPath + favicon;
            uis = decodedPath + uis;
            civil = decodedPath + civil;
            calumet = decodedPath + calumet;
            config = decodedPath + config;
            
            return true;
        }
        catch (UnsupportedEncodingException e) {
            return false;
        }
    }
    
    
    // Leer los datos del archivo de configuración y sincronizarlos
    public static boolean sync() {

        try{
            // Sincronización de datos
            Properties res = new Properties();
            res.load(new FileInputStream(config));
            if (!res.isEmpty()) {
            	key = res.getProperty("KEY");
            	station = res.getProperty("STATION");
            	stationFolder = res.getProperty("STATION_FOLDER");
            	lastSent = res.getProperty("DATA_LAST");
            }
            return true;
        }
        catch (IOException ex) {
            // Error sincronizando
            return false;
        }

    }


    // Actualizar configuración modificada
    // @parameters es "prop|valor,prop2|valor2,prop3|valor3"...
    public static boolean update(String parameters) {
        
        Properties fileprops = new Properties();
        OutputStream fileout = null;
        
        try {
            fileout = new FileOutputStream(config);
            
            // Releer los datos cargados
            fileprops.setProperty("STATION", station);
            fileprops.setProperty("KEY", key);
            fileprops.setProperty("STATION_FOLDER", stationFolder);
            fileprops.setProperty("DATA_LAST", lastSent);
            
            // Procesar nuevos datos y guardarlos
            String[] properties = parameters.split(",");
            String[] data;
            for (int prop = 0; prop < properties.length; prop++) {
                data = properties[prop].split(":");
                fileprops.setProperty(data[0], data[1]);
            }
            fileprops.store(fileout, null);
            
            // Actualizar datos
            return Data.sync();
        }
        catch (IOException io) {
            // Error escribiendo archivo
            return false;
        }
        finally {
            if (fileout != null) {
                try {
                    fileout.close();
                } catch (IOException e) {}
            }
        }
        
    }
    

}
