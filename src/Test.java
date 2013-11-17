
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {
	public void main (String args[]){
		
	}
	public void connectDB(){
		Connection connect = null;
		Statement s = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect =  DriverManager.getConnection("jdbc:mysql://manonnont.com/database" + "?user=root&password=root");
			
			s = connect.createStatement();
			
			String sql = "INSERT INTO customer " +
					"(CustomerID,Name,Email,CountryCode,Budget,Used) " + 
					"VALUES ('C005','Chai Surachai','chai.surachai@thaicreate.com'" +
					",'TH','1000000','0') ";
			 s.execute(sql);
			
			 System.out.println("Record Inserted Successfully");
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		try {
			if(s != null) {
				s.close();
				connect.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}

//user  manonnon_lmtest
//pass  robotz
