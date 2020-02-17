package proyectoBD;

import java.awt.EventQueue;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.MaskFormatter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class VentanaEmpleado extends Ventana {
	
	private JTextField txtCiudadOrigen;
	private JTextField txtCiudadDestino;
	private JFormattedTextField txtFechaIda, txtFechaVuelta;
	private JTable tablaVuelosIda, tablaVuelosVuelta, tablaPreciosIda, tablaPreciosVuelta;
	private JRadioButton rdbtnSoloIda, rdbtnIdaYVuelta;
	private JScrollPane panelVuelosIda, panelVuelosVuelta, panelInfoVuelosIda, panelInfoVuelosVuelta;
	private JLabel lblInfoVueloIda;
	private java.sql.Date fechaIdaSql, fechaVueltaSql;
	private int legajo;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaEmpleado frame = new VentanaEmpleado();
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
	public VentanaEmpleado() {
		setBounds(100, 100, 1480, 700);
        this.setResizable(true);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JPanel panelTituloVuelos = new JPanel();
		panelTituloVuelos.setBounds(12, 13, 1422, 42);
		getContentPane().add(panelTituloVuelos);
		panelTituloVuelos.setLayout(new BorderLayout(0, 0));
		
		JLabel lblPanel = new JLabel("PANEL DE EMPLEADOS");
		lblPanel.setFont(new Font("Tahoma", Font.PLAIN, 25));
		panelTituloVuelos.add(lblPanel);
		
		JButton btnCerrarSesion = new JButton("Cerrar sesion");
		btnCerrarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cerrarSesion();
			}
		});
		panelTituloVuelos.add(btnCerrarSesion, BorderLayout.EAST);
		
		JPanel panelDatosVuelos = new JPanel();
		panelDatosVuelos.setBorder(new LineBorder(Color.BLACK));
		panelDatosVuelos.setBounds(12, 68, 282, 478);
		getContentPane().add(panelDatosVuelos);
		panelDatosVuelos.setLayout(null);
		
		JLabel lblOrigen = new JLabel("ORIGEN");
		lblOrigen.setBounds(12, 22, 44, 16);
		panelDatosVuelos.add(lblOrigen);
		
		JLabel lblDestino = new JLabel("DESTINO");
		lblDestino.setBounds(12, 51, 56, 16);
		panelDatosVuelos.add(lblDestino);
		
		txtFechaIda = new JFormattedTextField();
		txtFechaVuelta = new JFormattedTextField();
		try {
			MaskFormatter mascara = new MaskFormatter("##/##/####");
			txtFechaIda = new JFormattedTextField(mascara);
			txtFechaIda.setFont(new Font("Tahoma", Font.PLAIN, 16));
			txtFechaIda.setBounds(134, 141, 96, 22);
			txtFechaIda.setEnabled(true);
			panelDatosVuelos.add(txtFechaIda);
			txtFechaVuelta = new JFormattedTextField(mascara);
			txtFechaVuelta.setFont(new Font("Tahoma", Font.PLAIN, 16));
			txtFechaVuelta.setBounds(134, 171, 96, 22);
			txtFechaVuelta.setEnabled(false);
			panelDatosVuelos.add(txtFechaVuelta);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		rdbtnSoloIda = new JRadioButton("SOLO IDA");
		rdbtnSoloIda.setSelected(true);
		rdbtnSoloIda.setBounds(8, 79, 127, 25);
		panelDatosVuelos.add(rdbtnSoloIda);
		rdbtnSoloIda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtFechaVuelta.setEnabled(false);
				txtFechaVuelta.setText("");
			}
		});
		
		rdbtnIdaYVuelta = new JRadioButton("IDA Y VUELTA");
		rdbtnIdaYVuelta.setBounds(8, 102, 127, 25);
		panelDatosVuelos.add(rdbtnIdaYVuelta);
		rdbtnIdaYVuelta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtFechaVuelta.setEnabled(true);
			}
		});
		
		ButtonGroup grupoRadioButtons = new ButtonGroup();
		grupoRadioButtons.add(rdbtnSoloIda);
		grupoRadioButtons.add(rdbtnIdaYVuelta);
		
		JLabel lblFechaDeIda = new JLabel("FECHA DE IDA");
		lblFechaDeIda.setBounds(12, 145, 96, 16);
		panelDatosVuelos.add(lblFechaDeIda);
		
		JLabel lblFechaDeVuelta = new JLabel("FECHA DE VUELTA");
		lblFechaDeVuelta.setBounds(12, 175, 123, 16);
		panelDatosVuelos.add(lblFechaDeVuelta);
		
		txtCiudadOrigen = new JTextField();
		txtCiudadOrigen.setBounds(94, 19, 175, 22);
		panelDatosVuelos.add(txtCiudadOrigen);
		txtCiudadOrigen.setColumns(10);
		
		txtCiudadDestino = new JTextField();
		txtCiudadDestino.setBounds(94, 48, 175, 22);
		panelDatosVuelos.add(txtCiudadDestino);
		txtCiudadDestino.setColumns(10);
		
		JButton btnBuscarVuelos = new JButton("Buscar vuelos");
		btnBuscarVuelos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buscarVuelos();
			}
		});
		btnBuscarVuelos.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnBuscarVuelos.setBounds(55, 229, 175, 25);
		panelDatosVuelos.add(btnBuscarVuelos);

		panelVuelosIda = new JScrollPane();
		panelVuelosIda.setBounds(306, 97, 708, 200);
		getContentPane().add(panelVuelosIda);
		
		panelVuelosVuelta = new JScrollPane();
		panelVuelosVuelta.setBounds(306, 346, 708, 200);
		getContentPane().add(panelVuelosVuelta);
		
		panelInfoVuelosIda = new JScrollPane();
		panelInfoVuelosIda.setBounds(1059, 97, 375, 200);
		getContentPane().add(panelInfoVuelosIda);
		
		JLabel lblVuelosDeIda = new JLabel("VUELOS DE IDA");
		lblVuelosDeIda.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblVuelosDeIda.setBounds(306, 68, 144, 16);
		getContentPane().add(lblVuelosDeIda);
		
		JLabel lblVuelosDeVuelta = new JLabel("VUELOS DE VUELTA");
		lblVuelosDeVuelta.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblVuelosDeVuelta.setBounds(306, 317, 156, 16);
		getContentPane().add(lblVuelosDeVuelta);
		
		lblInfoVueloIda = new JLabel("INFORMACION DEL VUELO DE IDA");
		lblInfoVueloIda.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInfoVueloIda.setBounds(1059, 68, 425, 16);
		getContentPane().add(lblInfoVueloIda);
		
		panelInfoVuelosVuelta = new JScrollPane();
		panelInfoVuelosVuelta.setBounds(1059, 348, 375, 198);
		getContentPane().add(panelInfoVuelosVuelta);
		
		JLabel lblInfoVueloVuelta = new JLabel("INFORMACION DEL VUELO DE VUELTA ");
		lblInfoVueloVuelta.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInfoVueloVuelta.setBounds(1059, 318, 425, 16);
		getContentPane().add(lblInfoVueloVuelta);
		
		JButton btnReservar = new JButton("Reservar");
		btnReservar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reservar();
			}
		});
		btnReservar.setBounds(1194, 578, 114, 42);
		getContentPane().add(btnReservar);
		
		tablaVuelosIda = new JTable();
		tablaVuelosVuelta = new JTable();
		tablaPreciosIda = new JTable();
		tablaPreciosVuelta = new JTable();
	}
	
	private void reservar() {
		gui.mostrarVentanaReserva();
	}
	
	private void buscarVuelos() {
		String ciudadOrigen = txtCiudadOrigen.getText();
		String ciudadDestino = txtCiudadDestino.getText();
		String fechaIda = txtFechaIda.getText();
		String fechaVuelta = txtFechaVuelta.getText();
		fechaIdaSql = new java.sql.Date(0);
		fechaVueltaSql = new java.sql.Date(0);
		
		boolean fechasCorrectas = true;
		
		if(fechasValidas(fechaIda, fechaVuelta)) {
			fechaIdaSql = Fechas.convertirStringADateSQL(fechaIda);
			if(rdbtnIdaYVuelta.isSelected()) {
				fechaVueltaSql = Fechas.convertirStringADateSQL(fechaVuelta);
				if(fechaVueltaSql.compareTo(fechaIdaSql) < 0)
					mostrarCartel("La fecha de vuelta debe ser posterior a la fecha de ida");
				else 
					fechasCorrectas = true;
			}
		}
		
		if(fechasCorrectas) {
			String consultaVuelosIda = "SELECT nro_vuelo, nombre_origen, salida, nombre_destino, llegada, avion, duracion " +
					"FROM vuelos_disponibles " +
					"WHERE fecha = '" + fechaIdaSql + "' AND ciudad_origen = '" + ciudadOrigen + 
							"' AND ciudad_destino = '" + ciudadDestino + "' " +
					"GROUP BY nro_vuelo, nombre_origen, salida, nombre_destino, llegada, avion, duracion;";
			try {
				Statement stmt = conexionBD.createStatement();
				ResultSet rs = stmt.executeQuery(consultaVuelosIda);
				tablaVuelosIda = armarTabla(rs);
				panelVuelosIda.setViewportView(tablaVuelosIda);
				tablaVuelosIda.addMouseListener(new MouseAdapter() {
					public void mouseReleased(MouseEvent arg0) {
						int fila = tablaVuelosIda.getSelectedRow();
						String nroVuelo = tablaVuelosIda.getValueAt(fila, 0).toString();
						tablaPreciosIda = armarPrecio(nroVuelo,fechaIdaSql,panelInfoVuelosIda);
					}
				});
				stmt.close();
				rs.close();
				panelVuelosIda.setViewportView(tablaVuelosIda);
			}
			catch(SQLException e) {
				System.out.print(e.getMessage());
			}
			
			if(rdbtnIdaYVuelta.isSelected()) {
				String consultaVuelosVuelta = "SELECT nro_vuelo, nombre_origen, salida, nombre_destino, llegada, avion, duracion " +
							"FROM vuelos_disponibles " +
							"WHERE fecha = '" + fechaVueltaSql + "' AND ciudad_origen = '" + ciudadDestino + 
									"' AND ciudad_destino = '" + ciudadOrigen + "' " +
							"GROUP BY nro_vuelo, nombre_origen, salida, nombre_destino, llegada, avion, duracion;";
				try {
					Statement stmt = conexionBD.createStatement();
					ResultSet rs = stmt.executeQuery(consultaVuelosVuelta);
					tablaVuelosVuelta = armarTabla(rs);
					panelVuelosVuelta.setViewportView(tablaVuelosVuelta);
					tablaVuelosVuelta.addMouseListener(new MouseAdapter() {
						public void mouseReleased(MouseEvent arg0) {
							int fila = tablaVuelosVuelta.getSelectedRow();
							String nroVuelo = tablaVuelosVuelta.getValueAt(fila, 0).toString();
							tablaPreciosVuelta = armarPrecio(nroVuelo,fechaVueltaSql,panelInfoVuelosVuelta);
						}
					});
					stmt.close();
					rs.close();
				}
				catch(SQLException e) {
					System.out.print(e.getMessage());
				}
			} 
		}
	}
	
	private boolean fechasValidas(String fechaIda, String fechaVuelta) {
		boolean fechasValidas = true;
		if(!Fechas.validar(fechaIda)) {
			mostrarCartel("Fecha de ida invalida");
			fechasValidas = false;
		}
		else
			if(rdbtnIdaYVuelta.isSelected()) 
				if(!Fechas.validar(fechaVuelta)) {
					mostrarCartel("Fecha de vuelta invalida");
					fechasValidas = false;
				}
		return fechasValidas;
	}
	
	public boolean conectarBD(String user, String password) {
		super.conectarBD("empleado", "empleado");
		if(!verificarEmpleado(user,password)) {
			mostrarCartel("Datos de login invalidos");
			desconectarBD();
			return false;
		}
		legajo = Integer.parseInt(user);
		return true;
	}
	
	private boolean verificarEmpleado(String user, String password) {
		String consulta = "SELECT legajo FROM empleados " +
						  "WHERE legajo = " + user + " AND password = md5('" + password + "');";
		try {
			Statement stmt = conexionBD.createStatement();
			ResultSet rs = stmt.executeQuery(consulta);
			if(rs.next()) //Hay una tupla
				return true;
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		}
		return false;
	}
	
	private JTable armarPrecio(String nroVuelo, java.sql.Date fechaVuelo, JScrollPane panel) {
		String consultaPrecios = "SELECT clase, precio, asientos_disponibles FROM vuelos_disponibles "
				+ "WHERE nro_vuelo = '"+nroVuelo+"' and fecha = '"+fechaVuelo+"';";
		JTable tablaPrecios = new JTable();
		try {
			Statement stmt = conexionBD.createStatement();
			ResultSet rsPrecios = stmt.executeQuery(consultaPrecios);
			tablaPrecios = armarTabla(rsPrecios);
			panel.setViewportView(tablaPrecios);
		}
		catch (SQLException e ) {
			mostrarCartel(e.getMessage());
		}
		return tablaPrecios;
	}
	
	public void confirmarReserva(String numeroDoc, String tipoDoc) throws SQLException{
		if(hayCosasQueNoEstanSeleccionadas())
			JOptionPane.showMessageDialog(this,"Por favor seleccione todos los items necesarios para "
					+ "poder realizar la reserva correctamente","Mensaje",JOptionPane.INFORMATION_MESSAGE);
		else {
			int nroDoc = Integer.parseInt(numeroDoc);
			
			int filaVueloIda = tablaVuelosIda.getSelectedRow();
			String nroVueloIda = tablaVuelosIda.getValueAt(filaVueloIda, 0).toString();
			int filaClaseIda = tablaPreciosIda.getSelectedRow();
			String claseVueloIda = tablaPreciosIda.getValueAt(filaClaseIda, 0).toString();
			
			String consulta;
			String nroVueloVuelta = "";
			if(rdbtnIdaYVuelta.isSelected()) {
				int filaVueloVuelta = tablaVuelosVuelta.getSelectedRow();
				nroVueloVuelta = tablaVuelosVuelta.getValueAt(filaVueloVuelta, 0).toString();
				int filaClaseVuelta = tablaPreciosVuelta.getSelectedRow();
				String claseVueloVuelta = tablaPreciosVuelta.getValueAt(filaClaseVuelta, 0).toString();
				consulta = "CALL reservar_ida_vuelta('"+nroVueloIda+"','"+nroVueloVuelta+"','"+fechaIdaSql+"','"+
				fechaVueltaSql+"','"+claseVueloIda+"','"+claseVueloVuelta+"',"+"'"+tipoDoc+"',"+nroDoc+","+legajo+");";
			}
			else 
				consulta = "CALL reservar_ida('"+nroVueloIda+"','"+fechaIdaSql+"','"+claseVueloIda+"','"+tipoDoc+
					"',"+nroDoc+","+legajo+");";
			
			Statement stmt = conexionBD.createStatement();
			ResultSet rs = stmt.executeQuery(consulta);
			rs.first();
			JOptionPane.showMessageDialog(this,rs.getString(1),"Mensaje",JOptionPane.INFORMATION_MESSAGE);
			actualizarPrecios(nroVueloIda,nroVueloVuelta);
			
		}
	}
	
	private void actualizarPrecios(String nroVueloIda, String nroVueloVuelta) {
		tablaPreciosIda = armarPrecio(nroVueloIda, fechaIdaSql, panelInfoVuelosIda);
		tablaPreciosVuelta = armarPrecio(nroVueloVuelta, fechaVueltaSql, panelInfoVuelosVuelta);
	}
	
	private boolean hayCosasQueNoEstanSeleccionadas() {
		boolean hayNoSeleccionados = tablaPreciosIda.getSelectedColumn() == -1 || 
									 tablaVuelosIda.getSelectedColumn() == -1;
		if(rdbtnIdaYVuelta.isSelected())
			hayNoSeleccionados = hayNoSeleccionados || tablaPreciosVuelta.getSelectedColumn() == -1 || 
								 tablaVuelosVuelta.getSelectedColumn() == -1;
		return hayNoSeleccionados;			
	}
}
