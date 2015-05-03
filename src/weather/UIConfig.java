/*!
 * Universidad Industrial de Santander
 * Grupo de Desarrollo de Software Calumet
 * Clima | Aplicación Cliente | Ventana de Configuración
 * Creado por Romel Pérez (romelperez.blogspot.com), 2014
 * Actualizado por Romel Pérez, Mayo del 2015
 */

package weather;

import java.awt.Toolkit;
import javax.swing.*;

public final class UIConfig extends JDialog {

	/**
	 * Initialización.
	 * @return Instancia de la ventana.
	 */
	public UIConfig() {
		configure();
		components();
		controllers();
	}


	/**
	 * Componentes.
	 */
	public JLabel TXT_Station;
	public JLabel TXT_Key;
	public JLabel TXT_Station_Folder;
	public JLabel TXT_Server;
	public JLabel TXT_Last;
	public JTextField INP_Station;
	public JTextField INP_Key;
	public JTextField INP_Station_Folder;
	public JTextField INP_Server;
	public JTextField INP_Last;
	public JButton BTN_Update;


	/**
	 * Mostrar la ventana de configuración.
	 */
	public void render() {
		
		// El único dato que cambia en tiempo de ejecución.
		INP_Last.setText(Data.last);
		setVisible(true);
	}


	/**
	 * Configurar ventana.
	 */
	public void configure() {
		
		Toolkit tools = Toolkit.getDefaultToolkit();
		
		this.setTitle(Data.uiconfig_title);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(Data.favicon));
		this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		this.setBounds((int) tools.getScreenSize().getWidth() / 2 - 300,
					   (int) tools.getScreenSize().getHeight() / 2 - 160,
					   600, 320);
	}


	/**
	 * Configurar componentes.
	 */
	public void components() {

		// Configurando cada componente.
		TXT_Station = new JLabel("Nombre de estación");
		TXT_Station.setBounds(20, 20, 160, 25);
		INP_Station = new JTextField(Data.station);
		INP_Station.setBounds(200, 20, 375, 25);
		INP_Station.setToolTipText("Nombre técnico de la estación");
		
		TXT_Key = new JLabel("Identificación");
		TXT_Key.setBounds(20, 65, 160, 25);
		INP_Key = new JTextField(Data.key);
		INP_Key.setBounds(200, 65, 375, 25);
		INP_Key.setToolTipText("Llave de identificación con el servidor");
		
		TXT_Station_Folder = new JLabel("Carpeta de estación");
		TXT_Station_Folder.setBounds(20, 110, 160, 25);
		INP_Station_Folder = new JTextField(Data.stationFolder);
		INP_Station_Folder.setBounds(200, 110, 375, 25);
		INP_Station_Folder.setToolTipText("Carpeta donde se encuentra el download.txt, similar a C:\\ruta\\estacion\\");
		
		TXT_Server = new JLabel("Servidor");
		TXT_Server.setBounds(20, 155, 160, 25);
		INP_Server = new JTextField(Data.server);
		INP_Server.setBounds(200, 155, 375, 25);
		INP_Server.setToolTipText("Similar a http://albatros.uis.edu.co");
		
		TXT_Last = new JLabel("Último dato enviado");
		TXT_Last.setBounds(20, 200, 160, 25);
		INP_Last = new JTextField(Data.last);
		INP_Last.setBounds(200, 200, 375, 25);
		INP_Last.setToolTipText("NONE sino se han enviado ó DD-MM-YY-hh-mm que puede tener un 'a' o 'p' al final");
		
		BTN_Update = new JButton("Actualizar");
		BTN_Update.setBounds(452, 245, 120, 30);
		
		// Agregando componentes a la ventana.
		this.getContentPane().add(TXT_Station);
		this.getContentPane().add(TXT_Key);
		this.getContentPane().add(TXT_Station_Folder);
		this.getContentPane().add(TXT_Server);
		this.getContentPane().add(TXT_Last);
		this.getContentPane().add(INP_Station);
		this.getContentPane().add(INP_Key);
		this.getContentPane().add(INP_Station_Folder);
		this.getContentPane().add(INP_Server);
		this.getContentPane().add(INP_Last);
		this.getContentPane().add(BTN_Update);
	}


	/**
	 * Interacciones.
	 */
	public void controllers() {
				
		// Actualizar datos modificados.
		BTN_Update.addActionListener(new java.awt.event.ActionListener() {
			@Override public void actionPerformed(java.awt.event.ActionEvent evt) {

				// Conseguir datos de las cajas de texto.
				String data = "STATION=" + INP_Station.getText();
				data += ",KEY=" + INP_Key.getText();
				data += ",STATION_FOLDER=" + INP_Station_Folder.getText();
				data += ",SERVER=" + INP_Server.getText();
				data += ",LAST=" + INP_Last.getText();
				
				// Actualizar.
				boolean updated = Data.update(data);
				
				// Revisar si fue actualizado exitosamente o no.
				if (updated) {
					JOptionPane.showMessageDialog(null, "Se han actualizado correctamente "
					  + "los datos de la estación.", "Actualización", JOptionPane.INFORMATION_MESSAGE);
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "Ha ocurrido un error en la actualización. "
					  + "Revise los datos e intente de nuevo.", "Actualización", JOptionPane.ERROR_MESSAGE);
				}
			}}
		);
	}

}
