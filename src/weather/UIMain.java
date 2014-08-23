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
    public static PImage IMG_GPH;
    public static PImage IMG_UIS;
    public static JLabel TXT_Info;
    public static JLabel TXT_State;
    public static JTextArea TXA_State;
    public static JScrollPane TXA_State_Scroll;
    public static JLabel TXT_Data;
    public static JTextArea TXA_Data;
    public static JScrollPane TXA_Data_Scroll;
    public static JLabel TXT_Calumet;
    public static JButton BTN_Config;
    public static JButton BTN_Sync;
    
    
    // Configurar ventana
    public void configure() {

        Toolkit tools = Toolkit.getDefaultToolkit();

        // Principal
        this.setTitle(Data.uimain_title);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(Data.favicon));
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);
        this.setBounds((int) tools.getScreenSize().getWidth() / 2 - 500,
                (int) tools.getScreenSize().getHeight() / 2 - 300,
                1000, 600);

    }

    
    // Configurar componentes
    public void components() {

        // Iniciando
        IMG_UIS = new PImage(Data.uis, 179, 90);
        IMG_GPH = new PImage(Data.gph, 100, 82);
        IMG_Calumet = new PImage(Data.calumet, 30, 30);
        TXT_Info = new JLabel("<html><b>Grupo de Investigación en Predicción y Modelamiento Hidroclimático</b>"
            + "<br>Esta aplicaci&oacute;n observa los datos obtenidos constantemente por la estaci&oacute;n meteorol&oacute;gica."
            + "<br>Estos son enviados a un servidor para su posterior procesamiento y publicaci&oacute;n web."
            + "</html>");
        TXT_State = new JLabel("Estado Aplicación - Servidor");
        TXA_State = new JTextArea("La aplicación no ha sincronizado datos hasta el momento.");
        TXT_Data = new JLabel("Últimos Datos Enviados");
        TXA_Data = new JTextArea("No se han enviado datos en esta sessión hasta el momento.");
        TXT_Calumet = new JLabel("Desarrollado por el Grupo Calumet");
        BTN_Config = new JButton("Configurar");
        BTN_Sync = new JButton("Sincronizar");
        
        
        // Configurando
        IMG_GPH.setLocation(20, 20);
        TXT_Info.setBounds(140, 20, 740, 90);
        TXT_Info.setFont(new Font("Serif", Font.PLAIN, 12));
        IMG_UIS.setLocation(780, 20);
        
        TXT_State.setBounds(20, 150, 600, 20);
        
        TXA_State.setEditable(false);
        TXA_State.setLineWrap(true);
        TXA_State.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        TXA_State_Scroll = new JScrollPane (TXA_State, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        TXA_State_Scroll.setBounds(20, 180, 940, 40);
        
        TXT_Data.setBounds(20, 250, 940, 20);
        
        TXA_Data.setEditable(false);
        TXA_Data.setLineWrap(true);
        TXA_Data.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        TXA_Data_Scroll = new JScrollPane (TXA_Data, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        TXA_Data_Scroll.setBounds(20, 280, 940, 200);
        
        IMG_Calumet.setLocation(20, 520);
        TXT_Calumet.setBounds(60, 520, 300, 30);
        
        BTN_Config.setBounds(540, 520, 200, 30);
        BTN_Sync.setBounds(760, 520, 200, 30);
        
        
        // Agregando componentes a la ventana
        this.getContentPane().add(IMG_UIS);
        this.getContentPane().add(IMG_GPH);
        this.getContentPane().add(IMG_Calumet);
        this.getContentPane().add(TXT_Info);
        this.getContentPane().add(TXT_State);
        this.getContentPane().add(TXA_State_Scroll);
        this.getContentPane().add(TXT_Data);
        this.getContentPane().add(TXA_Data_Scroll);
        this.getContentPane().add(TXT_Calumet);
        this.getContentPane().add(BTN_Config);
        this.getContentPane().add(BTN_Sync);

    }
    
    
    // Interacciones
    public void controllers() {
        
        // Mostrar ventana de configuración
        BTN_Config.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                Weather.uiconfig.render();
            }}
        );
        
        // Sincronizar datos
        BTN_Sync.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                Weather.synchronize();
            }}
        );
        
    }
    
    
    // Clase para crear imágenes
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
