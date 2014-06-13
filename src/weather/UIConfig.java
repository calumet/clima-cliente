/*!
 * Grupo de Desarrollo de Software Calumet
 * Weather | Application | Config Frame
 * Romel Pérez @prhonedev, 2014
 */

package weather;

import java.awt.Toolkit;
import javax.swing.*;

public final class UIConfig extends JDialog {
    
    
    // Initialización
    public UIConfig() {
        configure();
        components();
        controllers();
    }
    
    
    // Componentes
    public static JLabel TXT_Station;
    public static JLabel TXT_Key;
    public static JLabel TXT_Station_Folder;
    public static JLabel TXT_Server;
    public static JTextField INP_Station;
    public static JTextField INP_Key;
    public static JTextField INP_Station_Folder;
    public static JTextField INP_Server;
    public static JButton BTN_Update;
    
    
    // Mostrar la ventana de configuración
    public void render() {
        setVisible(true);
    }
    
    
    // Configurar ventana
    public void configure() {
        
        Toolkit tools = Toolkit.getDefaultToolkit();
        
        this.setTitle(Data.uiconfig_title);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(Data.favicon));
        this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);
        this.setBounds((int) tools.getScreenSize().getWidth() / 2 - 300,
                       (int) tools.getScreenSize().getHeight() / 2 - 135,
                       600, 270);
        
    }

    
    // Configurar componentes
    public void components() {

        // Iniciando
        TXT_Station = new JLabel("Nombre de Estación");
        TXT_Key = new JLabel("Identificación");
        TXT_Station_Folder = new JLabel("Carpeta de Estación");
        TXT_Server = new JLabel("Servidor");
        BTN_Update = new JButton("Actualizar");
        
        // Configurando
        TXT_Station.setBounds(20, 20, 160, 25);
        INP_Station = new JTextField(Data.station);
        INP_Station.setBounds(200, 20, 375, 25);
        
        TXT_Key.setBounds(20, 65, 160, 25);
        INP_Key = new JTextField(Data.key);
        INP_Key.setBounds(200, 65, 375, 25);
        
        TXT_Station_Folder.setBounds(20, 110, 160, 25);
        INP_Station_Folder = new JTextField(Data.stationFolder);
        INP_Station_Folder.setBounds(200, 110, 375, 25);
        
        TXT_Server.setBounds(20, 155, 160, 25);
        INP_Server = new JTextField(Data.server);
        INP_Server.setBounds(200, 155, 375, 25);
        
        BTN_Update.setBounds(452, 200, 120, 30);
        
        
        // Agregando componentes a la ventana
        this.getContentPane().add(TXT_Station);
        this.getContentPane().add(TXT_Key);
        this.getContentPane().add(TXT_Station_Folder);
        this.getContentPane().add(TXT_Server);
        this.getContentPane().add(INP_Station);
        this.getContentPane().add(INP_Key);
        this.getContentPane().add(INP_Station_Folder);
        this.getContentPane().add(INP_Server);
        this.getContentPane().add(BTN_Update);

    }
    
    
    // Interacciones
    public void controllers() {
                
        // Actualizar datos modificados
        BTN_Update.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {

                // Conseguir datos de las cajas de texto
                String data = "STATION=" + INP_Station.getText();
                data += ",KEY=" + INP_Key.getText();
                data += ",STATION_FOLDER=" + INP_Station_Folder.getText();
                data += ",SERVER=" + INP_Server.getText();
                
                // Actualizar
                boolean updated = Data.update(data);
                
                // Revisar si fue actualizado exitosamente o no
                if (updated) {
                    JOptionPane.showMessageDialog(null, "Se han actualizado correctamente los datos de la estación.", "Actualización", JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error en la actualización. Revise los datos e intente de nuevo.", "Actualización", JOptionPane.ERROR_MESSAGE);
                }
            
            }}
        );
        
    }
    
    
}
