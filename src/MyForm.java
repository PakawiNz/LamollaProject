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
		
		//updateDB("customer_profile","price_mul","0.2","num","1234");
		//showDB("customer_profile","*",head,head,null);
		/*
		String ans[][] = readWholeDB("customer_profile", "id");
		for(int i = 0;i < ans.length;i++) {
			for(int j = 0 ; j < ans[0].length ; j++){
				if(ans[i] != null) System.out.print(ans[i][j]);
				System.out.print(" - ");
			}
			System.out.println();
		}
		*/
		String value[] = {"1112","2111222","113","4","5","6","7","8","9","10","11","12","13","14","15","16"};
		insertDB("customer_profile", value);
		//deleteDB("customer_profile", "num", "1111");
//		String ans[] = readDB("customer_profile", "name");
//		for(int i = 0;i < ans.length;i++) {
//			if(ans[i] != null) System.out.println(ans[i]);
//		}
		showDB("customer_profile", "*", head, head, null);
		
		try {
			if(s != null) {	s.close();	connect.close(); }
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void connectDB(){
		String dbType = "jdbc:mysql://";
		String port = ":3306/";
		
		String url = "manonnont.com";
		String dbName = "manonnon_lmtest";
		String userName = "manonnon_lmtest"; 
		String password = "robotz";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(dbType + url + port + dbName,userName,password);
			s = connect.createStatement();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static int getColumnCount(ResultSet rs){
		int colN = 0;
		try {
			colN = rs.getMetaData().getColumnCount();
			System.out.println("Number of Column : "+ colN);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return colN;
	}
	
	public static int getRowCount(ResultSet rs){
		int rowN = 0;
		try {
			rs.last();
			rowN = rs.getRow();
			rs.beforeFirst();
			System.out.println("Number of Row : " + rowN);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return rowN;
	}
	
	public static String[] getColumnName(ResultSet rs){
		try {
			ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
			int col = md.getColumnCount();
			System.out.println("Number of Column : "+ col);
			
			String[] ans = new String[col];
			
			System.out.print("Columns Name: ");
			for (int i = 0;i < col;i++){
				String col_name = md.getColumnName(i+1);
				System.out.print(col_name + "  ");
				ans[i] = col_name;
			}
			System.out.println();

			return ans;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String[] readDB(String tableName,String head_search){
		return readDB(tableName, head_search,null ,null,null);
	}
	
	public static String[] readDB(String tableName,String head_search,String head_main,String value_main){
		return readDB(tableName, head_search,head_main ,value_main,null);
	}
	
	public static String[] readDB(String tableName,String head_search,String orderBy){
		return readDB(tableName, head_search,null ,null,orderBy);
	}
	
	public static String[] readDB(String tableName,String head_search,String head_main,String value_main,String orderBy){
		try {		
			String sql = "SELECT " + head_search +" FROM  " + tableName;
			if((value_main != null) && (head_main != null)) sql += " WHERE " + head_main + " = " + value_main;
			if(orderBy != null)	sql += " ORDER BY "  + orderBy;
			ResultSet rs = s.executeQuery(sql);			
			
			String[] ans = new String[getRowCount(rs)];
			
			int i = 0;
			while((rs!=null) && (rs.next()))
				ans[i++] = rs.getString(1);
			
			rs.close();
			return ans;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String[][] readDB(String tableName,String head_search[],String head_main,String value_main,String orderBy){
		String ans[][] = new String[head_search.length][];
		for(int i = 0;i < head_search.length;i++) ans[i] = readDB(tableName, head_search[i], head_main, value_main, orderBy);
		return ans;
	}
	
	public static String[][] readWholeDB(String tableName,String orderBy){
		try {		
			String sql = "SELECT * FROM  " + tableName;
			if(orderBy != null)	sql += " ORDER BY "  + orderBy;
			ResultSet rs = s.executeQuery(sql);			
			
			int rowN = getRowCount(rs);
			int colN = getColumnCount(rs);
			
			String ans[][] = new String[rowN][colN];
			int i = 0;
			while((rs!=null) && (rs.next())){
				for(int j = 0;j < colN;j++){
					ans[i][j] = rs.getString(j+1);
					System.out.println(ans[i][j]);
				}
				i++;
			}	
			rs.close();
			return ans;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	public static void insertDB(String tableName,String value[]){
		try {
			String sql = "SELECT * FROM  " + tableName;
			ResultSet rs = s.executeQuery(sql);
			String head[] = getColumnName(rs);
			
			insertDB(tableName, head, value);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void insertDB(String tableName,String head[],String value[]){
		try {
			if(head.length != value.length) {
				System.out.println("head.length != value.length");
				return;
			}
			
			String sql = "INSERT INTO " + tableName + "(";
			for(int i = 0;i < head.length;i++) {
				if(i != head.length - 1) sql += head[i] + ",";
				else sql += head[i] + ")";
			}
			sql += " VALUES(";
			for(int i = 0;i < value.length;i++) {
				if(i != value.length - 1) sql += value[i] + ",";
				else sql += value[i] + ") ;";
			}
			System.out.println("INSERT  >>"  +  sql);
			s.execute(sql);
			
			System.out.println("Record Inserted Successfully >>  " + " " + sql );
			 
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
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
	
	public static void deleteDB(String tableName,String head_main,String value_main){
		try {
			String sql = "DELETE FROM " + tableName + " WHERE " + head_main + " = " + value_main;

			s.execute(sql);
			System.out.println("Delete Successfully   >>  " + "" + sql );
			 
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

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
			ResultSet rs = s.executeQuery(sql);
			
			int row = 0;
			while((rs != null) && (rs.next()) ) {
				int column = 0;
				model.addRow(new Object[0]);
				for(int i = 0;i < modelHead.length;i++,column++) model.setValueAt(rs.getString(modelHead[i]), row, column);
				row++;
            }
			rs.close();
         
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		
		scrollPane.setViewportView(table);	
		frame.setVisible(true);	
	}
}
