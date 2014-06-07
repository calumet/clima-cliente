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
import javax.swing.JOptionPane;

public class Weather {

    
    public static UIMain uimain;
    public static UIConfig uiconfig;
    

    // Controlador principal
    public static void main(String[] args) {

        // Sincronizar datos del usuario
        Data.start();
        Data.sync();
        
        // Iniciar interfaces
        uimain = new UIMain();
        uiconfig = new UIConfig();
        
        // INICIAR SINCRONIZACIÓN
        // REPETIR CADA Data.updateTime SEGUNDOS
        
    }
    
    
    // Revisar qué datos no han sido sincronizados si se encuentran, procesarlos y enviarlos
    public static void synchronize() throws UnsupportedEncodingException {
        
        // Tiempo en el momento de sincronizar
        Calendar calendario = new GregorianCalendar();
        String tiempo = calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND);
        
        // Buscar y procesar nuevos datos
        int updates = 0;
        String answer, list = "", newData = search();
        while (!newData.equals("NO_DATA") && !newData.equals("ERROR_READ")) {
            
            String dataToSend = "";
            String[] dataParts = newData.split(",");
            
            dataToSend += "{";
            dataToSend += "\"" + Data.dataProps[0] + "\":\"" + dataParts[0] + "\"";
            list = Data.dataProps[0] + ": " + dataParts[0] + "\r\n";
            for (int d = 1; d < dataParts.length; d++) {
                dataToSend += ",\"" + Data.dataProps[d] + "\":\"" + dataParts[d] + "\"";
                list += Data.dataProps[d] + ": " + dataParts[d] + "\r\n";
            }
            dataToSend += "}";
            
            answer = send(dataParts[0] + "-"+ dataParts[1], dataToSend);
            if (answer.equals("ERROR")) {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error conectandose al servidor. Intente de nuevo en unos momentos.", "Servidor", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                updates++;
                newData = search();
            }
            
        }
        
        // Mostrar resultado del procedimiento
        if (newData.equals("ERROR_READ") && updates == 0) {
            final String mensaje = "Ha ocurrido un error leyendo los datos del clima de la carpeta de la estación a las " + tiempo + ".";
            JOptionPane.showMessageDialog(null, mensaje, "Datos", JOptionPane.ERROR_MESSAGE);
            UIMain.TXA_State.setText(mensaje);
        }
        else if (newData.equals("NO_DATA") && updates == 0) {
            UIMain.TXA_State.setText("No se han encontrado datos del clima nuevos de la carpeta de la estación a las " + tiempo + ".");
        }
        else if (updates > 0) {
            final String message;
            if (updates == 1) {
                UIMain.TXA_State.setText("Ha sido actualizado 1 nuevo registro de datos en el servidor a las " + tiempo + ".");
            } else {
                UIMain.TXA_State.setText("Han sido actualizados " + updates + " nuevos registros de datos en el servidor a las " + tiempo + ".");
            }
            UIMain.TXA_Data.setText(list);
        }
        
    }


    // Buscar datos no enviados en la carpeta del servidor de acuerdo a Data.lastSent
    // Retorna el primer registro de datos nuevo encontrado
    // @Data.lastSent es utilizado
    public static String search() {

        String[] lastDateTime = Data.lastSent.split("-");
        String lastDate = lastDateTime[0] + "/" + lastDateTime[1] + "/" + lastDateTime[2];  // DD/MM/YY
        String lastTime = lastDateTime[3] + ":" + lastDateTime[4];  // HH:MM
        System.out.println("Los últimos datos procesados son del: " + lastDate + " " + lastTime);
        
        BufferedReader lector = null;
        try {
            boolean found = false;
            String line, date, time;
            
            // Leer linea a linea el archivo de la estación
            lector = new BufferedReader(new FileReader(Data.stationFolder + "download.txt"));
            while ((line = lector.readLine()) != null) {
                
                // Obtener fecha, hora y minuto de la linea en cuestión
                date = line.substring(0, 9).trim();
                time = line.substring(10, 16).trim();
                
                // Si existen datos nuevos procesarlos y retornarlos
                if (found && date.length() > 0) {
                    System.out.println("Datos procesados del: " + date + " " + time);
                    line = line.trim().replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll(" ", ",");
                    line = line.replace("/","-").replace("/","-").replace(":","-");
                    return line;
                }
                
                // Se encontró el último dato enviado
                if (date.equals(lastDate) && time.equals(lastTime)) {
                    found = true;
                }
                
            }
        }
        catch (IOException e) {
            try {
                if (lector != null) {
                    lector.close();
                }
            } catch (IOException ex) {}
            return "ERROR_READ";
        }
        
        return "NO_DATA";
        
    }

    
    // Send data to server
    // @parameters son los datos a enviar
    // Retorna respuesta del servidor o mensaje de error
    public static String send(String datetime, String parameters) {

        URL url;
        HttpURLConnection connection = null;

        try {
            
            // Crear conexión
            url = new URL(Data.server);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(parameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "es-ES");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);


            // Enviar datos
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            String query = "key=" + Data.key + "&station=" + Data.station;
            query += "&data=" + URLEncoder.encode(parameters, "UTF-8");
            wr.writeBytes(query);
            wr.flush();
            wr.close();
            
            
            // Actualizar registro de último dato enviado
            Data.update("DATA_LAST:" + datetime);


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
            return response.toString();

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
