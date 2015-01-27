/*!
 * Universidad Industrial de Santander
 * Grupo de Desarrollo de Software Calumet
 * Weather | Aplicación | Datos
 * Romel Pérez prhone.blogspot.com, 2015
 */

package weather;

import java.io.*;
import java.net.URLDecoder;
import java.util.Properties;

public class Data {
  
  // Configuración de funcionalidad.
  public static int updateTime = 1000 * 60 * 5;
  public static String serverAdd = "/eisi/Clima/functions/agregarDatos.jsp";
  public static String serverGetLast = "/eisi/Clima/functions/conseguirConfig.jsp";
  
  // Configuración de usuario.
  public static String key = "";
  public static String station = "";
  public static String stationFolder = "";
  public static String last = "";
  public static String server = "";
  
  // Datos estáticos.
  public static String uimain_title = "Reportador del Clima";
  public static String uiconfig_title = "Configuración";
  public static String favicon = "resources/favicon.png";
  public static String uis = "resources/uis.png";
  public static String gph = "resources/gph.png";
  public static String calumet = "resources/calumet.png";
  public static String config = "resources/Config.properties";
  
  // Datos que se recolectan.
  // Todos los valores son números flotantes, a excepción de los especificados.
  public static String[] dataProps = {
    "date",  // Opcionales
    "time",  // Opcionales
    "TempOut",
    "HiTemp",
    "LowTemp",
    "OutHum",
    "DewPt",
    "WindSpeed",
    "WindDir",  // Texto
    "WindRun",
    "HiSpeed",
    "HiDir",  // Texto
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
  
  
  // Colocar direcciones a archivos de recursos a absolutas.
  public static boolean start() {
    
    try {
      
      // Conseguir la dirección del ejecutable.
      String path = Data.class.getProtectionDomain().getCodeSource().getLocation().getPath();
      String decodedPath = URLDecoder.decode(path, "UTF-8");
      decodedPath = decodedPath.replace("weather.jar", "");
      
      // Modificar las rutas de los archivos de recursos.
      favicon = decodedPath + favicon;
      uis = decodedPath + uis;
      gph = decodedPath + gph;
      calumet = decodedPath + calumet;
      config = decodedPath + config;
      
      // Modificados.
      return true;
      
    } catch (UnsupportedEncodingException e) {
      
      // Error consiguiendo dirección del ejecutable.
      return false;
    }
  }
  
  
  // Leer los datos del archivo de configuración y sincronizarlos.
  public static boolean sync() {

    try{
      
      // Sincronización de datos.
      Properties res = new Properties();
      res.load(new FileInputStream(config));
      if (!res.isEmpty()) {
        key = res.getProperty("KEY");
        station = res.getProperty("STATION");
        stationFolder = res.getProperty("STATION_FOLDER");
        server = res.getProperty("SERVER");
        last = res.getProperty("LAST");
      }
      
      // Fue sincronizado el archivo, aún si estaba vacio.
      return true;
    }
    catch (IOException ex) {
      
      // Error sincronizando.
      return false;
    }
  }


  // Actualizar configuración modificada.
  // @parameters es "prop=valor,prop2=valor2,prop3=valor3"...
  public static boolean update(String parameters) {
    Properties fileprops = new Properties();
    OutputStream fileout = null;
    
    try {
      fileout = new FileOutputStream(config);
      
      // Releer los datos cargados.
      fileprops.setProperty("KEY", key == null ? "" : key);
      fileprops.setProperty("STATION", station == null ? "" : station);
      fileprops.setProperty("STATION_FOLDER", stationFolder == null ? "" : stationFolder);
      fileprops.setProperty("SERVER", server == null ? "" : server);
      fileprops.setProperty("LAST", last == null ? "" : last);
      
      // Procesar nuevos datos y guardarlos.
      String[] properties = parameters.split(",");
      String[] data;
      for (String property : properties) {
        data = property.split("=");
        fileprops.setProperty(data[0], data[1]);
      }
      fileprops.store(fileout, null);
      
      // Resincronizar datos.
      return Data.sync();
    }
    catch (IOException io) {
      
      // Error actualizando archivo.
      return false;
    }
    finally {
      
      // Cerrar archivo.
      if (fileout != null) {
        try {
          fileout.close();
        } catch (IOException e) {}
      }
    }
  }
  
  
  // Formatos de datetime.
  public static class formatter {
    
    // Formatear de DD-MM-AA-hh-mm a AAAA-MM-DD-hh-mm.
    public static String toServer(String datetime) {
      String[] frags = datetime.split("-");
      
      // En caso de DD-MM-AA-hh-mm(a|p) entonces cambiar a hh-mm.
      if (frags[4].contains("a")) {
        frags[4] = frags[4].replace("a", "");
        frags[3] = "12".equals(frags[3]) ? "00" : frags[3];
      } else if (frags[4].contains("p")) {
        frags[4] = frags[4].replace("p", "");
        frags[3] = "12".equals(frags[3])
          ? frags[3]
          : (Integer.parseInt(frags[3]) + 12) + "";
      }
      
      datetime = "20"+ frags[2]
        +"-"+ (frags[1].length() == 1 ? "0" + frags[1] : frags[1])
        +"-"+ (frags[0].length() == 1 ? "0" + frags[0] : frags[0])
        +"-"+ (frags[3].length() == 1 ? "0" + frags[3] : frags[3])
        +"-"+ (frags[4].length() == 1 ? "0" + frags[4] : frags[4]);
      
      return datetime;
    }
    
  }
  
}
