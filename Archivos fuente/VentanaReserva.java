package proyectoBD;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class VentanaReserva extends JInternalFrame {
	private JTextField txtDocNro;
	private JTextField txtDocTipo;
	private GUI gui;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaReserva frame = new VentanaReserva();
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
	public VentanaReserva() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		JButton btnConfirmarReserva = new JButton("Confirmar");
		btnConfirmarReserva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				confirmarReserva();
			}
		});
		btnConfirmarReserva.setBounds(85, 190, 106, 25);
		getContentPane().add(btnConfirmarReserva);
		
		JButton btnCancelarReserva = new JButton("Cancelar");
		btnCancelarReserva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				txtDocNro.setText("");
				txtDocTipo.setText("");
			}
		});
		btnCancelarReserva.setBounds(224, 190, 97, 25);
		getContentPane().add(btnCancelarReserva);
		
		JLabel lblTipoDoc = new JLabel("Tipo DOC:");
		lblTipoDoc.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTipoDoc.setBounds(109, 89, 76, 16);
		getContentPane().add(lblTipoDoc);
		
		JLabel lblNroDoc = new JLabel("Nro DOC:");
		lblNroDoc.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNroDoc.setBounds(115, 128, 70, 16);
		getContentPane().add(lblNroDoc);
		
		txtDocNro = new JTextField();
		txtDocNro.setBounds(193, 126, 116, 22);
		getContentPane().add(txtDocNro);
		txtDocNro.setColumns(10);
		
		txtDocTipo = new JTextField();
		txtDocTipo.setBounds(193, 87, 116, 22);
		getContentPane().add(txtDocTipo);
		txtDocTipo.setColumns(10);
		
		JLabel lblIngreseLosDatos = new JLabel("Ingrese los datos del pasajero");
		lblIngreseLosDatos.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblIngreseLosDatos.setBounds(103, 24, 218, 16);
		getContentPane().add(lblIngreseLosDatos);

	}
	
	public void setGUI(GUI gui) {
		this.gui = gui;
	}
	
	private void confirmarReserva() {
		if (txtDocNro.getText().equals("") || txtDocTipo.getText().equals("") || txtDocTipo.getText().length() > 3) 
			JOptionPane.showMessageDialog(this,"Por favor, ingrese tipo y número de documento","Error", JOptionPane.ERROR_MESSAGE);
		else {
			try {
				gui.getVentanaEmpleado().confirmarReserva(txtDocNro.getText(),txtDocTipo.getText());				
				dispose();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this,e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
