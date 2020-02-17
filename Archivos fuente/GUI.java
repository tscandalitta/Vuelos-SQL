package proyectoBD;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.SwingConstants;

public class GUI {

	private VentanaAdministrador ventanaAdministrador;
	private VentanaEmpleado ventanaEmpleado;
	private VentanaReserva ventanaReserva;
	private JFrame frame;
	private JPasswordField txtLoginPasssword;
	private JTextField txtLoginUsuario;
	private JButton btnLogin;
	private JLabel lblPassword, lblUsuario;
	private JPanel panelLogin;
	private JLabel lblIngreseSusDatos;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1480, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ActionListener listenerLogin = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		};
		
		panelLogin = new JPanel();
		panelLogin.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelLogin.setBounds(583, 226, 310, 233);
		frame.getContentPane().add(panelLogin);
		panelLogin.setLayout(null);
		
		txtLoginUsuario = new JTextField();
		txtLoginUsuario.setBounds(116, 92, 116, 22);
		panelLogin.add(txtLoginUsuario);
		txtLoginUsuario.setColumns(10);
		
		txtLoginPasssword = new JPasswordField();
		txtLoginPasssword.setBounds(116, 137, 116, 22);
		panelLogin.add(txtLoginPasssword);
		txtLoginPasssword.addActionListener(listenerLogin);
		txtLoginPasssword.setToolTipText("");
		
		lblUsuario = new JLabel("User");
		lblUsuario.setBounds(26, 94, 56, 16);
		panelLogin.add(lblUsuario);
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblPassword = new JLabel("Password");
		lblPassword.setBounds(26, 139, 73, 16);
		panelLogin.add(lblPassword);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(109, 195, 97, 25);
		panelLogin.add(btnLogin);
		
		lblIngreseSusDatos = new JLabel("INGRESE SUS DATOS");
		lblIngreseSusDatos.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblIngreseSusDatos.setHorizontalAlignment(SwingConstants.CENTER);
		lblIngreseSusDatos.setBounds(12, 13, 286, 16);
		panelLogin.add(lblIngreseSusDatos);
		btnLogin.addActionListener(listenerLogin);
		btnLogin.setVisible(true);
		lblPassword.setVisible(true);
		lblUsuario.setVisible(true);
		txtLoginPasssword.setVisible(true);
		txtLoginUsuario.setVisible(true);
		frame.getContentPane().setLayout(null);
		
		ventanaReserva = new VentanaReserva();
		ventanaReserva.setGUI(this);
		ventanaReserva.setLocation(550, 175);
		ventanaReserva.setVisible(false);
		frame.getContentPane().add(ventanaReserva);
		
		ventanaEmpleado = new VentanaEmpleado();
		ventanaEmpleado.setGUI(this);
		ventanaEmpleado.setLocation(0, -20);
		ventanaEmpleado.setVisible(false);
		frame.getContentPane().add(ventanaEmpleado);
		
		ventanaAdministrador = new VentanaAdministrador();
		ventanaAdministrador.setGUI(this);
		ventanaAdministrador.setLocation(0, -20);
		ventanaAdministrador.setVisible(false);
		frame.getContentPane().add(ventanaAdministrador);
		
		JPanel panelInicio = new JPanel();
		panelInicio.setBounds(12, 13, 1438, 57);
		frame.getContentPane().add(panelInicio);
		
		JLabel lblSistemaDeGestion = new JLabel("SISTEMA DE GESTION DE BASES DE DATOS");
		panelInicio.add(lblSistemaDeGestion);
		lblSistemaDeGestion.setFont(new Font("Tahoma", Font.PLAIN, 25));
	}
	
	private void login() {
		String user = txtLoginUsuario.getText();
		String password = new String(txtLoginPasssword.getPassword());
		if(user.equals("admin")) {
			if(ventanaAdministrador.conectarBD(user, password)) {
				try {
					ventanaAdministrador.setMaximum(true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
				ventanaAdministrador.setVisible(true);
				ocultarLogin();
			}
			else
				txtLoginPasssword.setText("");
		}
		else {
			if(ventanaEmpleado.conectarBD(user, password)) {
				try {
					ventanaEmpleado.setMaximum(true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
				ventanaEmpleado.setVisible(true);
				ocultarLogin();
			}
			else
				txtLoginPasssword.setText("");
		}
	}
	public void mostrarLogin() {
		txtLoginUsuario.setText("");
		txtLoginPasssword.setText("");
		panelLogin.setVisible(true);
	}
	
	private void ocultarLogin() {
		panelLogin.setVisible(false);
	}
	
	public void mostrarVentanaReserva() {
		ventanaReserva.setVisible(true);
		
	}
	
	public VentanaEmpleado getVentanaEmpleado() {
		return ventanaEmpleado;
	}
}
