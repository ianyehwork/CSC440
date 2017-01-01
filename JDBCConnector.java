import java.sql.*;

/*
 * This class links the program with NCSU oracle database. The class implements the 
 * singleton pattern for the Connection object. The interface of this class including 
 * get connection object, close statement, close connection and close result set.
 *
 * Creator - Tsu-Hsin Yeh
 * Modifer
 * Date: Nov.11 2016
 */
public class JDBCConnector {
  // Using SERVICE_NAME
  private static final String jdbcURL = "jdbc:oracle:thin:@//orca.csc.ncsu.edu:1521/orcl.csc.ncsu.edu";
  // Put your oracle ID and password here
  private static final String user = "gcjones";
  private static final String password = "001070481";
  
  private static Connection connection = null;
  
  static Connection getConnection(){
    if(connection == null){
      try {
        // Loading the driver. This creates an instance of the driver
        // and calls the registerDriver method to make Oracle Thin
        // driver, at ora.csc.ncsu.edu, available to clients.
        
        Class.forName("oracle.jdbc.driver.OracleDriver");
        
        Connection connection = null;
        
        try {
          // Get a connection instance from the first driver in the
          // DriverManager list that recognizes the URL jdbcURL
          connection = DriverManager.getConnection(jdbcURL, user, password);
        } catch(SQLException e){
          e.printStackTrace();
        }
        return connection;
      } catch(Throwable oops) {
        oops.printStackTrace();
      }
    }
    return connection;
  }
  
  static void closeConnection() {
    if(connection != null) {
      try {
        connection.close();
      } catch(Throwable whatever) {}
    }
  }
  
  static void close(Statement statement) {
    if(statement != null) {
      try {
        statement.close();
      } catch(Throwable whatever) {}
    }
  }
  
  static void close(ResultSet result) {
    if(result != null) {
      try {
        result.close();
      } catch(Throwable whatever) {}
    }
  }
}