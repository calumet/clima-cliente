/*!
 * Universidad Industrial de Santander
 * Grupo de Desarrollo de Software Calumet
 * Clima | Aplicación Cliente | Principal
 * Creado por Romel Pérez (romelperez.blogspot.com), 2014
 * Actualizado por Romel Pérez, Mayo del 2015
 */

package weather;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Weather {

  /**
   * Objetos principales.
   */
  public static String VERSION = "1.2.6";
  public static UIMain uimain = null;
  public static UIConfig uiconfig = null;
  public static Timer update = null;
  public static String State = "NORMAL";
  public static Thread syncThread = null;


  /**
   * Controlador principal de aplicación.
   * @param args Argumentos pasados al programa.
   */
  public static void main(String[] args) {

    // Interfaces: Look and Feel.
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
      | UnsupportedLookAndFeelException ex) {
      Logger.getLogger(UIMain.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    // Sincronizar datos del usuario.
    Data.start();
    Data.sync();

    // Iniciar interfaces.
    uimain = new UIMain();  // Iniciar y mostrar.
    uiconfig = new UIConfig();  // Sólo iniciar.

    // Iniciar intervalo de sincronización.
    interval(true);
  }


  /**
   * Intervalo de tiempo para sincronizar.
   * @param start Si comienza a sincronizar o la cancela.
   */
  public static void interval(boolean start) {
    
    // Iniciar sincronizaciones cada intervalo.
    // Iniciar la primera a los 60 segundos.
    if (start) {
      update = new Timer();
      update.schedule(new TimerTask() {
        @Override public void run() {
          Weather.startSync();
        }
      }, 1000 * 60, Data.updateTime);
    }
    
    // Detener sincronizaciones si ha sido iniciado.
    else {
      if (update != null) {
        update.cancel();
      }
    }
  }


  /**
   * Iniciar proceso de sincronización.
   */
  public static void startSync() {
    
    // Activar estado de procesamiento de datos.
    if (State.equals("PROCESSING")) {
      return;
    }
    State = "PROCESSING";
    
    // Crear nuevo hilo para el proceso.
    Runnable tr = new Runnable() {
      @Override public void run() {
        try {
          SwingUtilities.invokeAndWait(new Runnable() {
            @Override public void run() {
              uimain.TXA_State.setText("Procesando datos. Esto "
                +"puede tomar tiempo. Por favor espere...");
            }
          });
        } catch (Exception e) {}
        uiconfig.setVisible(false);
        Weather.sync();
      }
    };
    syncThread = new Thread(tr);
    syncThread.start();
  }


  /**
   * Revisar qué datos no han sido sincronizados si se encuentran, procesarlos y
   * enviarlos.
   */
  public static void sync() {
    
    // Tiempo en el momento de sincronizar.
    Calendar calendario = new GregorianCalendar();
    String momento = calendario.get(Calendar.HOUR_OF_DAY) +":"+
      calendario.get(Calendar.MINUTE) +":"+ calendario.get(Calendar.SECOND);

    // Buscar datos de acuerdo a la configuración.
    // Si la configuración está incorrecta, detener el proceso search(true)
    // para enviar todo lo que encuentre, en caso contrario, el último registro.
    String newData;
    newData = ("NONE".equals(Data.last.toUpperCase()) || "NINGUNO".equals(Data.last.toUpperCase()))
          ? search(true) : search(false);
    if ("ERROR_CONFIG".equals(newData)) {
      uimain.TXA_State.setText("El valor del \"último dato enviado\" está "
        +"mal configurado. Cambielo por favor.");
      State = "NORMAL";
      return;
    }

    // Procesar nuevos datos.
    String answer;
    int updates = 0;
    String log = "";
    while (!"ERROR_READ".equals(newData) && !"INFO_NODATA".equals(newData)) {
      String dataToSend = "";
      String[] dataParts = newData.split(",");
      String datetime = dataParts[0] + "-"+ dataParts[1];
      
      // Crear strings de datos para enviar y mostrar en el log.
      dataToSend += "{";
      dataToSend += "\"" + Data.dataProps[0] + "\":\"" + dataParts[0] + "\"";
      log = Data.dataProps[0] + ": " + dataParts[0] + "\r\n";
      for (int d = 1; d < dataParts.length; d++) {
        dataToSend += ",\"" + Data.dataProps[d] + "\":\"" + dataParts[d] + "\"";
        log += Data.dataProps[d] + ": " + dataParts[d] + "\r\n";
      }
      dataToSend += "}";

      // Enviar registro de datos al servidor.
      answer = sendRegister(datetime, dataToSend);
      if ("PROCESSED".equals(answer) || "EXIST".equals(answer)) {
        
        // Actualizar último registro enviado.
        Data.update("LAST=" + datetime);
        
        // Sumar total enviados y volver a buscar.
        updates++;
        newData = search(false);
      }
      
      // Mostrar mensaje de error con el servidor en tal caso.
      else {
        uimain.TXA_State.setText("Ha ocurrido un error conectándose al "
          +"servidor a las "+ momento +".\r\nIntente de nuevo en unos "
          +"momentos. Si persiste el problema, revise la configuración.");
        State = "NORMAL";
        return;
      }
    }
    
    // Mostrar resultado del procesamiento.
    if (updates == 0 && "ERROR_READ".equals(newData)) {
      
      // Error leyendo el archivo de datos.
      uimain.TXA_State.setText("Ha ocurrido un error leyendo los datos del "
      + "clima de la carpeta de la estación a las " + momento + ".");
    }
    else if (updates == 0 && "INFO_NODATA".equals(newData)) {
      
      // No se encontraron datos a la primera buscada.
      uimain.TXA_State.setText("No se han encontrado datos del clima nuevos "
      + "de la carpeta de la estación a las " + momento + ".");
    }
    else if (updates > 0) {

      // Se encontraron datos y se enviaron, al menos un registro.
      if (updates == 1) {
        uimain.TXA_State.setText("Ha sido enviado 1 nuevo registro de "
          +"datos al servidor a las "+ momento +".");
      } else {
        uimain.TXA_State.setText("Han sido enviados "+ updates
          +" nuevos registros de datos al servidor a las "+ momento +".");
      }
      uimain.TXA_Data.setText(log);
      
      // Si ocurrió un error al final.
      if ("ERROR_READ".equals(newData)) {
        uimain.TXA_State.setText("Ocurrió un error al final del "
        + "procesado de datos. Revise el último dato procesado.");
      }
    } else {
      uimain.TXA_State.setText("La aplicación no ha sincronizado datos hasta el momento.");
    }
    
    // Volver al estado normal.
    State = "NORMAL";
  }


  /**
   * Buscar datos no enviados en la carpeta del servidor de acuerdo a Data.last.
   * @param  whatever si se va a buscar el primer registro encontrado.
   * @return          Retorna el primer registro de datos nuevo encontrado.
   */
  public static String search(boolean whatever) {
    
    // Procesar datetime de últimos datos enviados.
    if (!whatever) {
      if (Data.last.split("-").length < 5) {
        return "ERROR_CONFIG";
      }
    }

    // Leer archivo de datos.
    BufferedReader lector = null;
    try {
      boolean valid;
      boolean found = whatever;
      String line;
      String[] fragments;
      
      lector = new BufferedReader(new FileReader(Data.stationFolder + "download.txt"));
      
      // Pasarse las primeras 3 lineas.
      for (int i = 0; i < 3; i++) {
        lector.readLine();
      }
      
      // Leer linea a linea el archivo de la estación.
      while ((line = lector.readLine()) != null) {
        
        // Procesar linea en cuestión y el datetime.
        line = line.trim().replaceAll("  ", " ").replaceAll("  ", " ")
             .replaceAll("  ", " ").trim().replaceAll(" ", ",");
        line = line.replace("/","-").replace("/","-").replace(":","-");
        fragments = line.split(",");
        valid = fragments.length == 36;
        
        // Si existen datos nuevos procesarlos y retornarlos.
        if (valid && found) {
          System.out.println("search() - leyendo datos del "+ fragments[0]
            +" "+ fragments[1]);
          return line;
        }
        
        // Se encontró el último dato enviado.
        if (valid && Data.last.equals(fragments[0] + "-" + fragments[1])) {
          found = true;
        }
      }
    }
    catch (IOException e) {
      try {
        if (lector != null) lector.close();
      } catch (IOException ex) {}
      return "ERROR_READ";
    }

    return "INFO_NODATA";
  }


  /**
   * Enviar un registro de datos al servidor.
   * @param  datetime   Es el momento con formato DD-MM-AA-hh-mm.
   * @param  parameters Son los datos a enviar.
   * @return            Retorna respuesta del servidor ó mensaje de error.
   */
  public static String sendRegister(String datetime, String parameters) {
    
    // Formatear datetime.
    datetime = Data.formatter.toServer(datetime);
    
    try {
      
      // Crear query codificado.
      String url = Data.server + Data.serverAdd;
      String method = "POST";
      String query = "key=" + Data.key + "&station=" + Data.station
        + "&datetime=" + datetime
        + "&data=" + URLEncoder.encode(parameters, "UTF-8");

      // Requerir.
      return request(url, method, query, datetime);
    }
    catch(UnsupportedEncodingException e) {
      
      // Error de codificación.
      return "ERROR";
    }
  }


  /**
   * Pedir al servidor una solicitud.
   * @param  address    URL de requisito.
   * @param  method     Si es GET o POST.
   * @param  parameters Es el query.
   * @param  comments   Comentarios de logging. Esto no se envía.
   * @return            Retorna la respuesta del servidor.
   */
  public static String request(String address, String method, String parameters, String comments) {

    URL url;
    HttpURLConnection connection = null;

    try {

      // Crear conexión.
      url = new URL(address);
      connection = (HttpURLConnection) url.openConnection();

      connection.setRequestMethod(method);
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      connection.setRequestProperty("Content-Length", Integer.toString(parameters.getBytes().length));
      connection.setRequestProperty("Content-Language", "es-ES");

      connection.setUseCaches(false);
      connection.setDoInput(true);
      connection.setDoOutput(true);

      // Enviar datos.
      DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
      wr.writeBytes(parameters);
      wr.flush();
      wr.close();
      
      // Recibir respuesta.
      String line;
      int resCode = connection.getResponseCode();
      BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      while ((line = rd.readLine()) != null) {
        response.append(line);
        response.append('\r');
      }
      rd.close();
      String answer = response.toString().trim();
      System.out.println("request() - " + address + " " + method
        + " - " + comments + " - " + answer);
      return answer;

    } catch (IOException e) {

      // En caso de no haber respuesta.
      return "ERROR";
    }
    finally {

      // Terminar conexión.
      if (connection != null) connection.disconnect();
    }
  }
  
}
