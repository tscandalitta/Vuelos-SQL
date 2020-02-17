package proyectoBD;

import java.awt.Color;
import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.LineBorder;
import javax.swing.JPanel;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class VentanaAdministrador extends Ventana {

	/**
	 * Launch the application.
	 */
	private JTable tablaConsultas;
	private JList<String> listTablas, listAtributos;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaAdministrador frame = new VentanaAdministrador();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaAdministrador() {
		setBounds(100, 100, 1480, 700);
		getContentPane().setLayout(null);
		
		JTextArea txtConsultas = new JTextArea();
		txtConsultas.setWrapStyleWord(true);
		txtConsultas.setColumns(80);
		txtConsultas.setFont(new Font("Courier New", Font.PLAIN, 16));
		txtConsultas.setBounds(12, 68, 900, 287);
		txtConsultas.setTabSize(4);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		txtConsultas.setBorder(BorderFactory.createCompoundBorder(border,
	            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
	    
		getContentPane().add(txtConsultas);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 405, 903, 246);
		getContentPane().add(scrollPane);
		
		listTablas = new JList<String>();
		listTablas.setFont(new Font("Tahoma", Font.PLAIN, 16));
		listTablas.setBorder(new LineBorder(new Color(0, 0, 0)));
		listTablas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				String itemSeleccionado = (String) listTablas.getSelectedValue();
				String consulta = "describe "+itemSeleccionado+";";
				if(itemSeleccionado != null)
					listAtributos.setModel(armarLista(consulta));
			}
		});
		listTablas.setBounds(958, 250, 190, 357);
		getContentPane().add(listTablas);
		
		listAtributos = new JList<String>();
		listAtributos.setFont(new Font("Tahoma", Font.PLAIN, 16));
		listAtributos.setBorder(new LineBorder(new Color(0, 0, 0)));
		listAtributos.setBounds(1160, 250, 190, 357);
		getContentPane().add(listAtributos);
		
		tablaConsultas = new JTable();
		
		JButton btnEjecutar = new JButton("Ejecutar");
		btnEjecutar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String consulta = txtConsultas.getText().trim();
					try {
						Statement stmt = conexionBD.createStatement();
						boolean isResultSet = stmt.execute(consulta);
						if(isResultSet) {
							ResultSet rs = stmt.getResultSet();
							tablaConsultas = armarTabla(rs);
							scrollPane.setViewportView(tablaConsultas);
							tablaConsultas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
							stmt.close();
							rs.close();
						}
					} catch (SQLException e) {
						mostrarCartel("Consulta invalida\n"+e.getMessage());
					}
				
			}
		});
		btnEjecutar.setBounds(341, 368, 97, 25);
		getContentPane().add(btnEjecutar);
		
		JButton btnBorrarTodo = new JButton("Limpiar");
		btnBorrarTodo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtConsultas.setText("");
			}
		});
		btnBorrarTodo.setBounds(450, 368, 97, 25);
		getContentPane().add(btnBorrarTodo);
		
		JButton btnMostrarBd = new JButton("Mostrar tablas");
		btnMostrarBd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listTablas.setModel(armarLista("show tables;"));
			}
		});
		btnMostrarBd.setBounds(1088, 185, 125, 25);
		getContentPane().add(btnMostrarBd);
		
		JLabel lblTablas = new JLabel("Tablas");
		lblTablas.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTablas.setBounds(1026, 221, 56, 16);
		getContentPane().add(lblTablas);
		
		JLabel lblAtributos = new JLabel("Atributos");
		lblAtributos.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblAtributos.setBounds(1222, 221, 97, 16);
		getContentPane().add(lblAtributos);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 13, 1338, 42);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblPanelDeAdministrador = new JLabel("PANEL DE ADMINISTRADOR");
		lblPanelDeAdministrador.setFont(new Font("Tahoma", Font.PLAIN, 25));
		panel.add(lblPanelDeAdministrador);
		
		JButton btnCerrarSesion = new JButton("Cerrar sesion");
		btnCerrarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cerrarSesion();
			}
		});
		panel.add(btnCerrarSesion, BorderLayout.EAST);
	}
	
	public boolean conectarBD(String user, String password) {
		 if(!user.equals("admin")) {
			 JOptionPane.showMessageDialog(this,
					  "Datos de login inválidos.\n","Error", JOptionPane.ERROR_MESSAGE);
			 return false;
		 }
         return super.conectarBD(user, password);
	}
}
