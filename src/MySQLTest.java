import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Java program to connect to MySQL Server database running on localhost,
 * using JDBC type 4 driver.
 *
 * @author http://java67.blogspot.com
 */
public class MySQLTest{

    public static void main(String args[]) {
        String dbURL = "jdbc:mysql://localhost:3306/test";
        String username ="root";
        String password = "root";
       
        Connection dbCon = null;
        Statement stmt = null;
        ResultSet rs = null;
       
        String query ="select count(*) from stock";
       
        try {
            //getting database connection to MySQL server
            dbCon = DriverManager.getConnection(dbURL, username, password);
           
            //getting PreparedStatment to execute query
            stmt = dbCon.prepareStatement(query);
           
            //Resultset returned by query
            rs = stmt.executeQuery(query);
           
            while(rs.next()){
             int count = rs.getInt(1);
             System.out.println("count of stock : " + count);
            }
           
        } catch (SQLException ex) {
            //Logger.getLogger(CollectionTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
           //close connection ,stmt and resultset here
        }
       
    }  
   
}