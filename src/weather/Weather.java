/*!
 * Grupo de Desarrollo de Software Calumet
 * Weather | Application | Main Class
 * Romel Pérez @prhonedev, 2014
 */

package weather;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Weather {

    private static String key = null;
    private static String station = null;
    private static String serverURL = null;
    
    
    // Main Controller
    public static void main(String[] args) {
        
        // Initial configuration
        key = "T7D5E6P4AC7O56SA456A";
        station = "A1";
        serverURL = "http://localhost:6000/addData";
        
        // Data to send
        String weather = "NONE";
        String urlParameters = "key=" + key + "&station=" + station + "&data=" + weather;
        
        // Send data
        String response = sendData(urlParameters);
        System.out.println(response);
        
    }

    
    // Send data to shyncrinized server
    public static String sendData(String urlParameters) {
    
        URL url;
        HttpURLConnection connection = null;
        try {
            
            // Create connection
            url = new URL(serverURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "es-ES");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            
            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            
            // Get Response	
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder(); 
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (IOException e) {

            return null;

        } finally {
            
            if (connection != null) {
                connection.disconnect(); 
            }
            
        }
    
    }
    
}
