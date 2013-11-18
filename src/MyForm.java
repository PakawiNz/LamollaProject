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

import com.mysql.jdbc.ResultSetMetaData;

public class MyForm {

	protected static Connection connect = null;
	protected static Statement s = null;
	protected static DefaultTableModel model;
	protected static JScrollPane scrollPane;
	protected static JTable table;
	
	public MyForm(){
		
		
	}
	
	public static void main(String[] args) {
		String head[] = {"num","id","status","name","price_mul","conditions","term_of_payment",
						 "address","responsible_person","email","bracelet_length","ring_size",
						 "necklace_length","plating","stone_quality","remark"};
		connectDB();
		
		printDB("customer_profile","*",head,null);
		updateDB("customer_profile","price_mul","0.2","num","1234");
		showDB("customer_profile","*",head,head,null);
		
		String ans[][] = readDB("customer_profile", "name",null,null,null);
		for(int i = 0;i < ans.length;i++) {
			for(int j = 0;j < ans[i].length;j++) {
				if(ans[i][j] != null) {
					System.out.print(ans[i][j]);
					if(j != ans[i].length - 1) System.out.print(" - ");
				}
			}
			System.out.println();
		}
		
		
		
		
		
		try {
			if(s != null) {	s.close();	connect.close(); }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void connectDB(){
		String url = "jdbc:mysql://manonnont.com:3306/";
		String dbName = "manonnon_lmtest";
		String userName = "manonnon_lmtest"; 
		String password = "robotz";
	
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url + dbName,userName,password);
			s = connect.createStatement();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static int getColumnNumber(String tableName){
		int colN = 0;
		try {
			ResultSet rs = s.executeQuery("SELECT * FROM " + tableName);
			ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
			
			colN = md.getColumnCount();
			System.out.println("Number of Column : "+ colN);
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return colN;
	}
	
	public static int getRowNumber(String tableName){
		int rowN = 0;
		try {
			ResultSet rs = s.executeQuery("SELECT * FROM " + tableName);
		
			rowN = rs.getRow();
			System.out.println("Number of Row : " + rowN);
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return rowN;
	}
	
	public static String[] getColumnName(String tableName){
		String ans[] = null;
		try {
			// show all data in head_search from tableName
			// show     data in head_search from tableName that have head_main = value_main
			
			ResultSet rs = s.executeQuery("SELECT * FROM " + tableName);
			ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
			
			int col = md.getColumnCount();
			System.out.println("Number of Column : "+ col);
			
			ans = new String[getColumnNumber(tableName)];
			
			System.out.print("Columns Name: ");
			for (int i = 0;i < col;i++){
				String col_name = md.getColumnName(i);
				System.out.print(col_name + "  ");
				ans[i] = col_name;
			}
			System.out.println();


		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return ans;
	}
	
	public static void printDB(String tableName,String head_search,String modelHead[],String orderBy){	
		try {	
			String sql = "SELECT " + head_search +" FROM  " + tableName;
			if(orderBy != null)	sql = sql + " ORDER BY "  + orderBy;
			ResultSet rec = s.executeQuery(sql);			
			
			
			while((rec!=null) && (rec.next())) {
				for(int i = 0;i < modelHead.length;i++) {
					System.out.print(rec.getString(modelHead[i]));
					if(i != modelHead.length - 1) System.out.print(" - ");
				} 
				System.out.println("");
			}
			rec.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static String[][] readDB(String tableName,String head_search,String head_main,String value_main,String orderBy){
		//[row][column]
		String ans[][];
		if(head_search.equalsIgnoreCase("*")) ans = new String[10][2222222];  // how count row and column number
		else ans = new String[10][1];
		try {
			int colN = getColumnNumber(tableName);
			System.out.println(colN);
			
			// show all data in head_search from tableName
			// show     data in head_search from tableName that have head_main = value_main
			String sql = "SELECT " + head_search +" FROM  " + tableName;
			if((value_main != null) && (head_main != null)) sql = sql + " WHERE " + head_main + " = " + value_main;
			if(orderBy != null)	sql = sql + " ORDER BY "  + orderBy;
			ResultSet rec = s.executeQuery(sql);			
			
			int i = 0;
			int j = 0;
			while((rec!=null) && (rec.next())) {
				if(i == ans.length){ 
					// if have SQL instuction that can count row and column number  , delete this!!!
					String temp[][] = new String[ans.length * 2][];
					// row num = ans.length , column num = ans[j].length
					for(i = 0;i < ans.length;i++) for(j = 0;j < ans[i].length;j++) temp[i][j] = ans[i][j];
					ans = temp;
				}
				for(i = 0;i < ans.length;i++) for(j = 0;j < ans[i].length;j++) System.out.println(ans[i][j] = rec.getString(head_search));
				
			}
			//System.out.print(columnsNumber);
			//rec.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return ans;
	}
	
	public static void insertDB(){
		
	}
	
	public static void updateDB(String tableName,String head_change,String value_change,String head_main,String value_main){
		try {
			String sql = "UPDATE " + tableName +
					     " SET " + head_change + " = " + value_change +
					     " WHERE " + head_main + " = " + value_main;
			 s.execute(sql);
			 System.out.println("Record Update Successfully   >>  " + "" + sql );
			 
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}
	
	public static void deleteDB(){
		
	}
	
	public static void createGUITable(JFrame frame){
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(80, 100, 1200, 242);
		frame.setTitle("ThaiCreate.Com Java GUI Tutorial");
		frame.getContentPane().setLayout(null);
		
		// Customer Label
		JLabel lblCustomer = new JLabel("Customer List");
		lblCustomer.setBounds(231, 28, 95, 14);
		frame.getContentPane().add(lblCustomer);
		
		// ScrollPane for Table
		scrollPane = new JScrollPane();
		scrollPane.setBounds(33, 61, 1120, 90);
		frame.getContentPane().add(scrollPane);
		
		// Table
		table = new JTable();
				
		// Model for Table
		model = (DefaultTableModel)table.getModel();
		
	}
	
	public static void showDB(String tableName,String head_search,String head[],String modelHead[],String orderBy){
		//add Column's head to DefalutTableModel
		JFrame frame = new JFrame("Test JDBC BY EKKY");
		createGUITable(frame);
		for(int i = 0;i < head.length;i++) model.addColumn(head[i]); 
		
		
		try {
			String sql = "SELECT " + head_search +" FROM  " + tableName;
			if(orderBy != null)	sql = sql + " ORDER BY "  + orderBy;
			ResultSet rec = s.executeQuery(sql);
			
			int row = 0;
			while((rec != null) && (rec.next()) ) {
				int column = 0;
				model.addRow(new Object[0]);
				for(int i = 0;i < modelHead.length;i++,column++) model.setValueAt(rec.getString(modelHead[i]), row, column);
				row++;
            }
			rec.close();
         
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		
		scrollPane.setViewportView(table);	
		frame.setVisible(true);	
	}
}
