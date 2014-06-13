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
    public static int updateTime = 1000 * 60 * 5;
    public static String serverAdd = "/weather/addData";
    public static String serverGetLast = "/weather/getDataLast";
    
    // Configuración de usuario
    public static String key = "";
    public static String station = "";
    public static String stationFolder = "";
    public static String last = "";
    public static String server = "";
    
    // Datos estáticos
    public static String uimain_title = "Reportador del Clima";
    public static String uiconfig_title = "Configuración";
    public static String favicon = "resources/favicon.png";
    public static String uis = "resources/uis.png";
    public static String gph = "resources/gph.png";
    public static String calumet = "resources/calumet.png";
    public static String config = "weather/Config.properties";
    
    // Datos que se recolectan
    public static String[] dataProps = {
        "date",
        "time",
        "TempOut",
        "HiTemp",
        "LowTemp",
        "OutHum",
        "DewPt",
        "WindSpeed",
        "WindDir",
        "WindRun",
        "HiSpeed",
        "HiDir",
        "WindChill",
        "HeatIndex",
        "THWIndex",
        "THSWIndex",
        "Bar",
        "Rain",
        "RainRate",
        "SolarRad",
        "SolarEnergy",
        "HiSolarRad",
        "UVIndex",
        "UVDose",
        "HiUV",
        "HeatDD",
        "CoolDD",
        "InTemp",
        "InHum",
        "InDew",
        "InHeat",
        "ET",
        "WindSamp",
        "WindTx",
        "ISSRecept",
        "ArcInt"
    };
    
    
    // Colocar direcciones a archivos de recursos a absolutas
    public static boolean start() {
        
        try {
            
            // Conseguir la dirección del ejecutable
            String path = Data.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            
            // Modificar las rutas de los archivos de recursos
            favicon = decodedPath + favicon;
            uis = decodedPath + uis;
            gph = decodedPath + gph;
            calumet = decodedPath + calumet;
            config = decodedPath + config;
            
            // Modificados
            return true;
            
        } catch (UnsupportedEncodingException e) {
            
            // Error consiguiendo dirección del ejecutable
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
            	server = res.getProperty("SERVER");
            	last = res.getProperty("LAST");
            }
            
            // Fue sincronizado el archivo, aún si estaba vacio
            return true;
        
        } catch (IOException ex) {
            
            // Error sincronizando
            return false;
            
        }

    }


    // Actualizar configuración modificada
    // @parameters es "prop=valor,prop2=valor2,prop3=valor3"...
    public static boolean update(String parameters) {
        
        Properties fileprops = new Properties();
        OutputStream fileout = null;
        
        try {
            
            fileout = new FileOutputStream(config);
            
            // Releer los datos cargados
            fileprops.setProperty("KEY", key);
            fileprops.setProperty("STATION", station);
            fileprops.setProperty("STATION_FOLDER", stationFolder);
            fileprops.setProperty("SERVER", server);
            fileprops.setProperty("LAST", last);
            
            // Procesar nuevos datos y guardarlos
            String[] properties = parameters.split(",");
            String[] data;
            for (int prop = 0; prop < properties.length; prop++) {
                data = properties[prop].split("=");
                fileprops.setProperty(data[0], data[1]);
            }
            fileprops.store(fileout, null);
            
            // Resincronizar datos
            return Data.sync();
            
        } catch (IOException io) {
            
            // Error actualizando archivo
            return false;
            
        } finally {
            
            // Cerrar archivo
            if (fileout != null) {
                try {
                    fileout.close();
                } catch (IOException e) {}
            }
            
        }
        
    }
    

}
