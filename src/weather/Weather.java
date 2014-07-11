/*!
 * Grupo de Desarrollo de Software Calumet
 * Weather | Application | Main Class
 * Romel Pérez @prhonedev, 2014
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
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Weather {

    
    // Objetos principales
    public static UIMain uimain = null;
    public static UIConfig uiconfig = null;
    public static Timer update = null;
    

    // Controlador principal de aplicación
    public static void main(String[] args) {

        // Interfaces: Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(UIMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Sincronizar datos del usuario
        Data.start();
        Data.sync();
        
        // Iniciar interfaces
        uimain = new UIMain();  // Iniciar y mostrar
        uiconfig = new UIConfig();  // Sólo iniciar
        
        // Iniciar intervalo de sincronización
        interval(true);
        
    }
    
    
    // Intervalo de tiempo para sincronizar
    // @start si comienza a sincronizar o la cancela
    public static void interval(boolean start) {
        
        // Iniciar sincronizaciones cada intervalo
        if (start) {
            update = new Timer();
            update.schedule(new TimerTask() {
                @Override public void run() {
                    Weather.synchronize();
                }
            }, 3000, Data.updateTime);
        }
        // Detener sincronizaciones si ha sido iniciado
        else {
            if (update != null) {
                update.cancel();
            }
        }
        
    }
    
    
    // Revisar qué datos no han sido sincronizados si se encuentran, procesarlos y enviarlos
    public static void synchronize() {
        
        // Tiempo en el momento de sincronizar
        Calendar calendario = new GregorianCalendar();
        String momento = calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND);
        
        // Mostrar mensaje de sincronizando
        UIMain.TXA_State.setText("Procesando datos. Por favor espere...");
        
        // Buscar datos de acuerdo al datetime del último registro enviado
        String newData;
        if (Data.last == null || (Data.last != null && Data.last.split("-").length != 5)) {
            
            // No está configurado Data.last, configurarlo
            String lastRegisterSent = askLast();
            switch (lastRegisterSent) {
                case "NONE":
                    newData = search(true);
                    break;
                case "ERROR":
                    JOptionPane.showMessageDialog(null, "No se ha configurado el último dato enviado y ocurrió un error " +
                        "conectándose al servidor.\r\nIntente de nuevo. Si persiste el problema, contacte el administrador para configurarlo.", "Configuración", JOptionPane.ERROR_MESSAGE);
                    return;
                default:
                    Data.update("LAST=" + lastRegisterSent);
                    newData = search(false);
                    break;
            }
            
        } else {
            
            // Está configurado Data.last, buscar de acuerdo a su valor
            newData = search(false);
            
        }
        
        
        // Procesar nuevos datos
        String answer;
        int updates = 0;
        String log = "";
        while (!newData.equals("ERROR_READ") && !newData.equals("INFO_NODATA")) {
            
            String dataToSend = "";
            String[] dataParts = newData.split(",");
            String datetime = dataParts[0] + "-"+ dataParts[1];
            
            
            // Crear strings de datos para enviar y mostrar en el log
            dataToSend += "{";
            dataToSend += "\"" + Data.dataProps[0] + "\":\"" + dataParts[0] + "\"";
            log = Data.dataProps[0] + ": " + dataParts[0] + "\r\n";
            for (int d = 1; d < dataParts.length; d++) {
                dataToSend += ",\"" + Data.dataProps[d] + "\":\"" + dataParts[d] + "\"";
                log += Data.dataProps[d] + ": " + dataParts[d] + "\r\n";
            }
            dataToSend += "}";
            
            
            // Enviar registro de datos al servidor
            answer = sendRegister(datetime, dataToSend);
            if (answer.equals("PROCESSED")) {
                
                // Actualizar último registro enviado
                Data.update("LAST=" + datetime);
                
                // Sumar total enviados y volver a buscar
                updates++;
                newData = search(false);
                
            } else {
                
                // Mostrar mensaje de error con el servidor
                final String message = "Ha ocurrido un error conectándose al servidor.\r\n"
                                     + "Intente de nuevo en unos momentos. Si persiste el problema, revise la configuración.";
                JOptionPane.showMessageDialog(null, message, "Servidor", JOptionPane.ERROR_MESSAGE);
                UIMain.TXA_State.setText(message);
                return;
                
            }
            
        }
        
        
        // Mostrar resultado del procesamiento
        if (updates == 0 && newData.equals("ERROR_READ")) {
            
            // Error leyendo el archivo de datos
            final String mensaje = "Ha ocurrido un error leyendo los datos del clima de la carpeta de la estación a las " + momento + ".";
            JOptionPane.showMessageDialog(null, mensaje, "Datos", JOptionPane.ERROR_MESSAGE);
            UIMain.TXA_State.setText(mensaje);
            
        } else if (updates == 0 && newData.equals("INFO_NODATA")) {
            
            // No se encontraron datos a la primera buscada
            UIMain.TXA_State.setText("No se han encontrado datos del clima nuevos de la carpeta de la estación a las " + momento + ".");
            
        } else if (updates > 0) {
            
            // Se encontraron datos y se enviaron, al menos un registro
            if (updates == 1) {
                UIMain.TXA_State.setText("Ha sido enviado 1 nuevo registro de datos al servidor a las " + momento + ".");
            } else {
                UIMain.TXA_State.setText("Han sido enviados " + updates + " nuevos registros de datos al servidor a las " + momento + ".");
            }
            UIMain.TXA_Data.setText(log);
            
        }
        
    }


    // Buscar datos no enviados en la carpeta del servidor de acuerdo a Data.lastSent
    // Retorna el primer registro de datos nuevo encontrado
    // @whatever si se va a buscar el primer registro encontrado
    // @Data.last es utilizado
    public static String search(boolean whatever) {
        
        // Procesar datetime de últimos datos enviados
        String[] lastDateTime = {};
        String lastDate = "", lastTime = "";
        if (!whatever) {
            lastDateTime = Data.last.split("-");
            lastDate = lastDateTime[0] + "/" + lastDateTime[1] + "/" + lastDateTime[2];  // DD/MM/YY
            lastTime = lastDateTime[3] + ":" + lastDateTime[4];  // HH:MM
        }

        // Leer archivo de datos
        BufferedReader lector = null;
        try {
            
            boolean found = whatever;
            String line, date, time;
            
            // Leer linea a linea el archivo de la estación
            lector = new BufferedReader(new FileReader(Data.stationFolder + "download.txt"));
            while ((line = lector.readLine()) != null) {
                
                // Obtener fecha y horaMinuto de la linea en cuestión
                date = line.substring(0, 9).trim();
                time = line.substring(10, 16).trim();
                
                // Si existen datos nuevos procesarlos y retornarlos
                if (found && date.length() > 0 && date.indexOf("/") > 0) {
                    System.out.println("search() - leyendo datos del " + date + " " + time);
                    line = line.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", ",");
                    line = line.replace("/","-").replace("/","-").replace(":","-");
                    return line;
                }
                
                // Se encontró el último dato enviado
                if (date.equals(lastDate) && time.equals(lastTime)) {
                    found = true;
                }
                
            }
            
        } catch (IOException e) {
            
            try {
                if (lector != null) {
                    lector.close();
                }
            } catch (IOException ex) {}
            return "ERROR_READ";
            
        }
        
        return "INFO_NODATA";
        
    }

    
    // Enviar un registro de datos al servidor
    // @datetime es el momento con formato DD-MM-AA-hh-mm
    // @parameters son los datos a enviar
    // Retorna respuesta del servidor ó mensaje de error
    public static String sendRegister(String datetime, String parameters) {
        
        // Formatear datetime
        datetime = Data.formatter.toServer(datetime);
        
        try {
            
            // Crear query codificado
            String url = Data.server + Data.serverAdd;
            String method = "POST";
            String query = "key=" + Data.key + "&station=" + Data.station + "&datetime=" + datetime +
                           "&data=" + URLEncoder.encode(parameters, "UTF-8");

            // Requerir
            return request(url, method, query);
            
        } catch(UnsupportedEncodingException e) {
            
            // Error de codificación
            return "ERROR";
            
        }
        
    }
    
    
    // Pregunta al servidor cuál es el datetime del último registro enviado
    // Retorna la fecha en formato DD-MM-AA-hh-mm ó NONE
    public static String askLast() {
        
        String url = Data.server + Data.serverGetLast;
        String method = "GET";
        String query = "key=" + Data.key + "&station=" + Data.station;
        
        String answer = request(url, method, query);
        
        // Formatear datetime si llegó uno
        if (answer.indexOf("-") > 0) {
            answer = Data.formatter.fromServer(answer);
        }
        
        return answer;
        
    }

    
    // Pedir al servidor una solicitud
    // @address url de requisito
    // @method si es GET o POST
    // @parameters es el query
    // Retorna la respuesta del servidor
    public static String request(String address, String method, String parameters) {

        URL url;
        HttpURLConnection connection = null;

        try {

            // Crear conexión
            url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(parameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "es-ES");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);


            // Enviar datos
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();
            

            // Recibir respuesta
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
            System.out.println("request() - " + address + " " + method + " - " + answer);
            return answer;

        } catch (IOException e) {

            // En caso de no haber respuesta
            return "ERROR";

        } finally {

            // Terminar conexión
            if (connection != null) {
                connection.disconnect();
            }

        }

    }
    
}
