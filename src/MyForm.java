import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;

public class MyForm extends JFrame {
	
	Connection connect = null;
	Statement s = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				MyForm frame = new MyForm();
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MyForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 579, 242);
		setTitle("ThaiCreate.Com Java GUI Tutorial");
		getContentPane().setLayout(null);
		
		// Customer Label
		JLabel lblCustomer = new JLabel("Customer List");
		lblCustomer.setBounds(231, 28, 95, 14);
		getContentPane().add(lblCustomer);
		
		// ScrollPane for Table
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(33, 61, 494, 90);
		getContentPane().add(scrollPane);
		
		// Table
		JTable table = new JTable();
				
		// Model for Table
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.addColumn("Name");
		//
		//
		//
		
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect =  DriverManager.getConnection("jdbc:mysql://www.manonnont.com/manonnon_lmtest:3306/database" + "?user=manonnon_lmtest & password=robotz");
			
			s = connect.createStatement();
			
			String sql = "SELECT * FROM  customer ORDER BY CustomerID ASC";
			
			ResultSet rec = s.executeQuery(sql);
			int row = 0;
			while((rec != null) && (rec.next())) {			
				model.addRow(new Object[0]);
				model.setValueAt(rec.getString("Name"), row, 0);
				row++;
            }
			rec.close();
             
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		
		try {
			if(s != null) {
				s.close();
				connect.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		scrollPane.setViewportView(table);		
	}
}
