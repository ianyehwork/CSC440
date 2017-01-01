
import java.sql.*;
import java.util.*;
/*
 * This class communicates between user interface and databse to complete
 * user required operation. The class is responsible to prompt the user
 * for the input, issue a SQL statement to the databse and finally print the
 * result to the console.
 */
public class ServiceRecords {
  private Connection conn;
  private Scanner input;
  public ServiceRecords(Connection connection, Scanner input){
    this.conn = connection;
    this.input = input;
  }
  
  /***
   * Creates a new phone bill entry in the service_records table
   * @param amount amount of money owed
   * @param checkin_id the check-in id of the customer
   * @param staff_id the id of the staff that entered the record
   * @return the id of the service_record
   */
  public void newPhoneBill(){
	  int amount, checkin_id = -1;
	  String customerName = "";
	  
	  System.out.println("Add a phone bill:\nEnter amount billed:");
	  amount = input.nextInt();
	  input.nextLine(); //discard new line char
	  System.out.println("Enter the customer's name: ");
	  customerName = input.nextLine();
	  
	 // String statementAdd = "INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'phone', " + amount + ", " + checkin_id + ");";
	  //add service_record: id,type, amount, checkin_id
	  //add id, staff_id (ignore atm)
	  
	  //get the 

		try {
	  		PreparedStatement ps = null;
	  		ps = conn.prepareStatement("SELECT id FROM customer WHERE name = ?");
	  		ps.setString(1, customerName);
	  		ResultSet rs = ps.executeQuery();
	  		if(rs.next()) {
	  			checkin_id = rs.getInt("id");
	  		}
	  	}
	  	catch(SQLException e) {
	  		e.printStackTrace();
	  	}
	  
	  try {
		  PreparedStatement ps = null;
		  ps = conn.prepareStatement("INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'phone', ?, ?)");
		  ps.setInt(1, amount);
		  ps.setInt(2, checkin_id);
	  
		  ResultSet rs = ps.executeQuery();
	  }
	  catch(SQLException e) {
		  e.printStackTrace();
	  }
	  
  }
  
  public void updatePhoneBill(){
	  //use select* method
	  System.out.println("Update a phone bill:");
	  System.out.println("Enter Phone Bill Record ID: ");
	  int id = input.nextInt();
	  input.nextLine(); //discard new line char
	  System.out.println("Enter amount billed:");
	  Integer amount = input.nextInt();
	  input.nextLine(); //discard new line char
	  
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("UPDATE service_record SET amount = ? WHERE id = ?");
			ps.setInt(1, amount);
			ps.setInt(2, id);
			ResultSet rs = ps.executeQuery();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
  }
  
  public void newLaundryBill(){
	  int amount, checkin_id = 0;
	  String customerName = "";
	  
	  System.out.println("Add a laundry bill:\nEnter amount billed:");
	  amount = input.nextInt();
	  input.nextLine(); //discard new line char
	   System.out.println("Enter the customer's name: ");
	  customerName = input.nextLine();
	  
	  
	 // String statementAdd = "INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'phone', " + amount + ", " + checkin_id + ");";
	  //add service_record: id,type, amount, checkin_id
	  //add id, staff_id (ignore atm)
	  
	  //get the 

	  	try {
	  		PreparedStatement ps = null;
	  		ps = conn.prepareStatement("SELECT id FROM customer WHERE name = ?");
	  		ps.setString(1, customerName);
	  		ResultSet rs = ps.executeQuery();
	  		if(rs.next()) {
	  			checkin_id = rs.getInt("id");
	  		}
	  	}
	  	catch(SQLException e) {
	  		e.printStackTrace();
	  	}
	  try {
		  PreparedStatement ps = null;
		  ps = conn.prepareStatement("INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'laundry', ?, ?)");
		  ps.setInt(1, amount);
		  ps.setInt(2, checkin_id);
	  
		  ResultSet rs = ps.executeQuery();
	  }
	  catch(SQLException e) {
		  e.printStackTrace();
	  }
  }
  
  public void updateLaundryBill(){
	  //use select* method
	  System.out.println("Update a laundry bill:");
	  System.out.println("Enter laundry Bill Record ID: ");
	  int id = input.nextInt();
	  input.nextLine(); //discard new line char
	  System.out.println("Enter amount billed:");
	  Integer amount = input.nextInt();
	  input.nextLine(); //discard new line char
	  
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("UPDATE service_record SET amount = ? WHERE id = ?");
			ps.setInt(1, amount);
			ps.setInt(2, id);
			ResultSet rs = ps.executeQuery();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
  }
  
  public void newRestaurantBill(){
	  int amount, checkin_id = 0;
	  String customerName = "";
	  
	  System.out.println("Add a restaurant bill:\nEnter amount billed:");
	  amount = input.nextInt();
	  input.nextLine(); //discard new line char
	  System.out.println("Enter the customer's name: ");
	  customerName = input.nextLine();
	  
	 // String statementAdd = "INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'restaurant', " + amount + ", " + checkin_id + ");";
	  //add service_record: id,type, amount, checkin_id
	  //add id, staff_id (ignore atm)
	  
	  //get the 
	  	try {
	  		PreparedStatement ps = null;
	  		ps = conn.prepareStatement("SELECT id FROM customer WHERE name = ?");
	  		ps.setString(1, customerName);
	  		ResultSet rs = ps.executeQuery();
	  		if(rs.next()) {
	  			checkin_id = rs.getInt("id");
	  		}
	  	}
	  	catch(SQLException e) {
	  		e.printStackTrace();
	  	}
	  try {
		  PreparedStatement ps = null;
		  ps = conn.prepareStatement("INSERT INTO service_record(id, type, amount, checkin_id) VALUES (service_record_seq.nextval, 'restaurant', ?, ?)");
		  ps.setInt(1, amount);
		  ps.setInt(2, checkin_id);
	  
		  ResultSet rs = ps.executeQuery();
	  }
	  catch(SQLException e) {
		  e.printStackTrace();
	  }
  }
  
  public void updateRestaurantBill(){
	  //use select* method
	  System.out.println("Update a restaurant bill:");
	  System.out.println("Enter restaurant bill record ID: ");
	  int id = input.nextInt();
	 	  input.nextLine(); //discard new line char
	  System.out.println("Enter amount billed:");
	  Integer amount = input.nextInt();
	  input.nextLine(); //discard new line char
	  
		try {
			PreparedStatement ps = null;
			ps = conn.prepareStatement("UPDATE service_record SET amount = ? WHERE id = ?");
			ps.setInt(1, amount);
			ps.setInt(2, id);
			ResultSet rs = ps.executeQuery();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
  }
}
