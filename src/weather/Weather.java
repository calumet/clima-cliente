/*!
 * Grupo de Desarrollo de Software Calumet
 * Weather | Application | Main Class
 * Romel Pérez @prhonedev, 2014
 */

package weather;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Weather {

    
    public static UIMain uimain;
    public static UIConfig uiconfig;
    

    // Controlador principal
    public static void main(String[] args) {

        // Sincronizar datos del usuario
        Data.sync();
        
        // Iniciar interfaz principal
        uimain = new UIMain();
        
    }
    
    
    // Revisar qué datos no han sido sincronizados si se encuentran, procesarlos y enviarlos
    public static void synchronize() {
        
        //String response = send("key=" + Data.key + "&station=" + Data.station);
        //System.out.println(response);

    }


    // Buscar datos no enviados en la carpeta del servidor
    // Retorna los datos encontrados
    public static String search() {

        return "";

    }

    
    // Send data to server
    // @parameters son los datos a enviar
    // Retorna respuesta del servidor o mensaje de error
    public static String send(String parameters) {

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
            return response.toString();

        } catch (IOException e) {

            // En caso de no haber respuesta
            return null;

        } finally {

            // Terminar conexión
            if (connection != null) {
                connection.disconnect();
            }

        }

    }


}
