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
    

    // Main Controller
    public static void main(String[] args) {

        Data.sync();
        uimain = new UIMain();
        
    }
    
    
    // Begin synchronization with server
    public static void synchronize(boolean start) {
        
        if (start) {
            
            String response = send("key=" + Data.key + "&station=" + Data.station);
            System.out.println(response);
            
        } else {
            
            // stop
            
        }
        
    }


    // Search for new data in station folder
    public static String search() {

        return "";

    }

    
    // Send data to server
    public static String send(String parameters) {

        URL url;
        HttpURLConnection connection = null;

        try {
            
            // Create connection
            url = new URL(Data.server);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", Integer.toString(parameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "es-ES");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);


            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();


            // Get response
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

            // There is no answer
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }

        }

    }


}
