package proyectoBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class Ventana extends JInternalFrame{
	
	protected Connection conexionBD = null;
	protected GUI gui;

	public void setGUI(GUI g) {
		gui = g;
	}
	
	protected void mostrarCartel(String msg) {
		JOptionPane.showMessageDialog(this,msg,"Error", JOptionPane.ERROR_MESSAGE);
	}
	
	protected DefaultListModel<String> armarLista(String consulta) {
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		Statement stmt;
		try {
			stmt = conexionBD.createStatement();
			ResultSet rs = stmt.executeQuery(consulta);
			while(rs.next()) {
				listModel.addElement(rs.getString(1));
		}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			mostrarCartel("Error de conexion");
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		return listModel;
	}
	
	protected JTable armarTabla(ResultSet rs) {
		JTable tabla = new JTable();
		try {
			ResultSetMetaData metadatos = rs.getMetaData();
			int size = metadatos.getColumnCount();
			String columnas[] = new String[size];
			for(int i = 0; i < size; i++) {
				columnas[i] = metadatos.getColumnLabel(i+1);
			}
			TableModel VuelosModel = new DefaultTableModel(new String[][] {},columnas) {
				public boolean isCellEditable(int rowIndex, int columnIndex) 
	                {
	                   return false;
	                };
	                
	               
			};
			tabla.setModel(VuelosModel);
			((DefaultTableModel) tabla.getModel()).setRowCount(0);
			int filas = 0;
			while(rs.next()) {
				((DefaultTableModel) tabla.getModel()).setRowCount(filas+1);
				for(int i = 0; i < size; i++) {
					tabla.setValueAt(rs.getString(i+1), filas, i);
				}
				filas++;
			}
			tabla.getTableHeader().setReorderingAllowed(false);
		}
		catch(SQLException e) {
			mostrarCartel(e.getMessage());
		}
		return tabla;
	}
	
	protected boolean conectarBD(String user, String password) {
	     if (this.conexionBD == null) {      
	    	 try {	
	    		 String servidor = "localhost:3306";
	    		 String baseDatos = "vuelos";
	    		 String uriConexion = "jdbc:mysql://" + servidor + "/" + 
	    				 baseDatos + "?serverTimezone=America/Argentina/Buenos_Aires";
	    		 this.conexionBD = DriverManager.getConnection(uriConexion, user, password);
	    	 }
	    	 catch (SQLException e) {
	    		 JOptionPane.showMessageDialog(this,
	    				 "Se produjo un error al intentar conectarse a la base de datos.\n" + e.getMessage(),
	    				 "Error", JOptionPane.ERROR_MESSAGE);
	    		 return false;
	    	 }
	     }
        return true;
	}
	
	protected void desconectarBD() {
		if (this.conexionBD != null) {
			try {
				this.conexionBD.close();
				this.conexionBD = null;
			}
			catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("VendorError: " + e.getErrorCode());
			}
		}
	}
	
	public void cerrarSesion() {
		desconectarBD();
		dispose();
		if(gui != null)
			gui.mostrarLogin();
	}
}
