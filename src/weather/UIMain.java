/*!
 * Grupo de Desarrollo de Software Calumet
 * Weather | Application | Main Frame
 * Romel Pérez @prhonedev, 2014
 */

package weather;

import java.awt.*;
import javax.swing.*;

public final class UIMain extends JFrame {


    // Initialización
    public UIMain() {
        
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
    public static PImage IMG_Calumet;
    public static PImage IMG_UIS;
    public static JLabel TXT_Info;
    public static JLabel TXT_State;
    public static JTextArea TXA_State;
    public static JLabel TXT_Data;
    public static JTextArea TXA_Data;
    public static JButton BTN_Config;
    public static JButton BTN_Sync;
    
    
    // Configurar ventana
    public void configure() {
        
        Toolkit tools = Toolkit.getDefaultToolkit();
        
        // Principal
        this.setTitle(Data.title);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(Data.favicon));
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds((int) tools.getScreenSize().getWidth() / 2 - 500,
                       (int) tools.getScreenSize().getHeight() / 2 - 300,
                       1000, 600);
        
    }

    
    // Configurar componentes
    public void components() {

        // Iniciando
        IMG_UIS = new PImage(Data.uis, 72, 100);
        IMG_Calumet = new PImage(Data.calumet, 100, 100);
        TXT_Info = new JLabel("<html><b>Grupo de Desarrollo de Software Calumet de la Universidad Industrial de Santander</b>"
                            + "<br>Esta aplicaci&oacute;n observa los datos obtenidos constantemente por la estaci&oacute;n meteorol&oacute;gica"
                            + "<br>Estos son enviados a un servidor para su posterior procesamiento"
                            + "</html>");
        TXT_State = new JLabel("Estado del Servidor");
        TXA_State = new JTextArea();
        TXT_Data = new JLabel("Últimos Datos Enviados");
        TXA_Data = new JTextArea();
        BTN_Config = new JButton("Configurar");
        BTN_Sync = new JButton("Sincronizar");
        
        
        // Configurando
        IMG_UIS.setLocation(20, 20);
        IMG_Calumet.setLocation(112, 20);
        TXT_Info.setBounds(232, 20, 740, 100);
        TXT_Info.setFont(new Font("Serif", Font.PLAIN, 12));
        
        TXT_State.setBounds(20, 150, 600, 20);
        TXA_State.setBounds(20, 180, 940, 40);
        
        TXT_Data.setBounds(20, 250, 940, 20);
        TXA_Data.setBounds(20, 280, 940, 160);
        
        BTN_Config.setBounds(20, 520, 200, 30);
        BTN_Sync.setBounds(780, 520, 200, 30);
        
        
        // Agregando componentes a la ventana
        this.getContentPane().add(IMG_Calumet);
        this.getContentPane().add(IMG_UIS);
        this.getContentPane().add(TXT_Info);
        this.getContentPane().add(TXT_State);
        this.getContentPane().add(TXA_State);
        this.getContentPane().add(TXT_Data);
        this.getContentPane().add(TXA_Data);
        this.getContentPane().add(BTN_Config);
        this.getContentPane().add(BTN_Sync);

    }
    
    
    // Interacciones
    public void controllers() {
        
        /*IButton_1.setText("Synchronize");
        IButton_1.setBounds(20, 20, 100, 30);
        IButton_1.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                JOptionPane.showMessageDialog(null, "This is a normal message to show on here!", "Title", JOptionPane.INFORMATION_MESSAGE);
            }}
        );*/
        
    }
    
    
    // Image Class
    public class PImage extends JPanel {
        
        private Image img;
        private Dimension size;
        
        public PImage(String src, int width, int height) {
            img = new ImageIcon(src).getImage();
            size = new Dimension(width, height);
            this.setLayout(null);
            this.setSize(width, height);
        }
        
        @Override public void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, null);
        }
        
    }

}
