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
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setVisible(true);
            }
        });
        
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
    
    
    // Configurar ventana
    public void configure() {
        
        Toolkit tools = Toolkit.getDefaultToolkit();
        
        // Principal
        this.setTitle(Data.uiconfig_title);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(Data.favicon));
        this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        this.setLayout(null);
        this.setBounds((int) tools.getScreenSize().getWidth() / 2 - 300,
                       (int) tools.getScreenSize().getHeight() / 2 - 145,
                       600, 290);
        
    }

    
    // Configurar componentes
    public void components() {

        // Iniciando
        TXT_Station = new JLabel("Nombre de Estación");
        TXT_Station.setBounds(20, 20, 160, 25);
        INP_Station = new JTextField(Data.station);
        INP_Station.setBounds(200, 20, 375, 25);
        
        TXT_Key = new JLabel("Identificación");
        TXT_Key.setBounds(20, 65, 160, 25);
        INP_Key = new JTextField(Data.key);
        INP_Key.setBounds(200, 65, 375, 25);
        
        TXT_Station_Folder = new JLabel("Carpeta de Estación");
        TXT_Station_Folder.setBounds(20, 110, 160, 25);
        INP_Station_Folder = new JTextField(Data.stationFolder);
        INP_Station_Folder.setBounds(200, 110, 375, 25);
        
        TXT_Server = new JLabel("Servidor");
        TXT_Server.setBounds(20, 155, 160, 25);
        INP_Server = new JTextField(Data.server);
        INP_Server.setBounds(200, 155, 375, 25);
        
        BTN_Update = new JButton("Actualizar");
        BTN_Update.setBounds(452, 210, 120, 30);
        
        
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
                JOptionPane.showMessageDialog(null, "Todavía no está disponible la funcionalidad de actualizar la configuración.", "Actualización", JOptionPane.WARNING_MESSAGE);
            }}
        );
        
    }
    
    
}
