/*!
 * Grupo de Desarrollo de Software Calumet
 * Weather | Application | Interface
 * Romel Pérez @prhonedev, 2014
 */

package weather;

import java.awt.*;
import javax.swing.*;
import sun.applet.Main;

public final class UIMain extends JFrame {

    // Initializator
    public static void start() {
        
        // Enable integrated interface
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
        } catch (Exception ex) {       
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {}
        }
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        // Create new frame
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                new UIMain().setVisible(true);
            }
        });

    }
    
    
    // Initialization
    public UIMain() {
        configure();
        components();
    }
    
    
    // Components
    public JButton button1 = new JButton();
    
    
    // Configure frame
    public void configure() {
        
        Toolkit tools = Toolkit.getDefaultToolkit();
        
        setTitle(Data.title);
        setIconImage(Toolkit.getDefaultToolkit().getImage(Data.favicon));
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        setBounds( (int) tools.getScreenSize().getWidth() / 2 - 400,
                   (int) tools.getScreenSize().getHeight() / 2 - 250,
                   800, 500 );
        
    }

    
    // Create and configure components
    public void components() {

        button1.setText("Synchronize");
        button1.setBounds(20, 20, 100, 30);
        getContentPane().add(button1);
        
        button1.addActionListener(new java.awt.event.ActionListener() {
            @Override public void actionPerformed(java.awt.event.ActionEvent evt) {
                JOptionPane.showMessageDialog(null, "Message", "Title", JOptionPane.INFORMATION_MESSAGE);
            }}
        );

    }

}
