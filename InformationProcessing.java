import java.sql.*;
import java.util.*;

/*
 * This class communicates between user interface and databse to complete
 * user required operation. The class is responsible to prompt the user
 * for the input, issue a SQL statement to the databse and finally print the
 * result to the console.
 *
 * Creator - Tsu-Hsin Yeh
 * Modifer 
 * Date: Nov.11 2016
 */
public class InformationProcessing {
  private Connection conn;
  private Scanner input;
  public InformationProcessing(Connection connection, Scanner input){
    this.conn = connection;
    this.input = input;
  }
  
  // Verified.
  public void newStaff(){
    
    boolean printSuccessMsg = true;
    
    // prompt for the information needed
    System.out.println("Creating new staff.");
    System.out.println("Staff General Category");
    System.out.println("1.Manager");
    System.out.println("2.Front Desk");
    System.out.println("3.Billing Staff");
    System.out.println("4.Service Staff");
    System.out.println("5.Catering Staff");
    System.out.print(":");
    int option = input.nextInt();
    input.nextLine(); // discard new line character
    System.out.print("Name: ");
    String name = input.nextLine();
    System.out.print("SSN: ");
    String ssn = input.nextLine();
    System.out.print("Age: ");
    int age = input.nextInt();
    input.nextLine(); // discard new line character
    System.out.print("Gender: ");
    String gender = input.nextLine().toLowerCase().charAt(0) + "";
    System.out.print("Title: ");
    String title = input.nextLine();
    System.out.print("Department: ");
    String department = input.nextLine();
    System.out.print("Phone: ");
    String phone = input.nextLine();
    System.out.print("Address: ");
    String address = input.nextLine();
    System.out.print("Hotel name: ");
    String hotel_name = input.next();
    input.nextLine();
    
    if(!(option >= 1 && option <= 5)){
      System.out.println("Invalid staff catefory.");
      return;
    }
    
    // retrieve the hotel id
    int hotel_id = -1;
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM hotel WHERE name = '" + hotel_name + "'");
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        hotel_id = rs.getInt("id");
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    
    // insert new staff record
    if(hotel_id != -1){
      // We do get the hotel id provided hotel name
      try{
        PreparedStatement ps = null;
        ps = conn.prepareStatement("INSERT INTO staff(id, ssn, name, age, gender, title, department, phone, address, hotel_id) VALUES (staff_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, ssn);
        ps.setString(2, name);
        ps.setInt(3, age);
        ps.setString(4, gender);
        ps.setString(5, title);
        ps.setString(6, department);
        ps.setString(7, phone);
        ps.setString(8, address);
        ps.setInt(9, hotel_id);
        ps.executeUpdate();
      } catch(SQLException e){
        e.printStackTrace();
        printSuccessMsg = false;
      }
    } else {
      System.out.println("Hotel with given name cannot be found.");
      printSuccessMsg = false;
    }
    
    // retrieve newly created staff's ID
    int staff_id = -1;
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM staff WHERE name = '" + name + "'");
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        staff_id = rs.getInt("id");
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    
    if(staff_id == -1){
      System.out.println("Cannot find newly inserted staff.");
    }else{
      // insert the id into corresponding subclass table
      if(option == 1){
        // manager
      }else if(option == 2){
        // front desk
        try{
          PreparedStatement ps = null;
          ps = conn.prepareStatement("INSERT INTO frontdesk_staff(id) VALUES (?)");
          ps.setInt(1, staff_id);
          ps.executeUpdate();
        } catch(SQLException e){
          e.printStackTrace();
          printSuccessMsg = false;
        }
      }else if(option == 3){
        // billing staff
        try{
          PreparedStatement ps = null;
          ps = conn.prepareStatement("INSERT INTO billing_staff(id) VALUES (?)");
          ps.setInt(1, staff_id);
          ps.executeUpdate();
        } catch(SQLException e){
          e.printStackTrace();
          printSuccessMsg = false;
        }
      }else if(option == 4){
        // service staff
        try{
          PreparedStatement ps = null;
          ps = conn.prepareStatement("INSERT INTO service_staff(id) VALUES (?)");
          ps.setInt(1, staff_id);
          ps.executeUpdate();
        } catch(SQLException e){
          e.printStackTrace();
          printSuccessMsg = false;
        }
      }else if(option == 5){
        // catering staff
        try{
          PreparedStatement ps = null;
          ps = conn.prepareStatement("INSERT INTO catering_staff(id) VALUES (?)");
          ps.setInt(1, staff_id);
          ps.executeUpdate();
        } catch(SQLException e){
          e.printStackTrace();
          printSuccessMsg = false;
        }
      }
    }
    
    if(printSuccessMsg){
      System.out.println();
      System.out.println("Staff Create successful!");
      System.out.println();
    }
    
  }
  
  // Verified.
  public void updateStaff(){
    
    boolean printSuccessMsg = true;
    
    // prompt for the information needed
    System.out.println("Updating existing staff.");
    System.out.print("Name for search: ");
    String old_name = input.nextLine(); // if user just hit enter then, old_name is string with length of 0
    System.out.print("new Name (if unchanged, type old name): ");
    String new_name = input.nextLine();
    System.out.print("new SSN: ");
    String new_ssn = input.nextLine();
    System.out.print("new Age: ");
    String new_age = input.nextLine();
    System.out.print("new Gender: ");
    String new_gender = input.nextLine();
    if(new_gender.length() != 0){
      new_gender = new_gender.toLowerCase().charAt(0) + "";
    }
    System.out.print("new Title: ");
    String new_title = input.nextLine();
    System.out.print("new Department: ");
    String new_department = input.nextLine();
    System.out.print("new Phone: ");
    String new_phone = input.nextLine();
    System.out.print("new Address: ");
    String new_address = input.nextLine();
    System.out.print("new Hotel name: ");
    String new_hotel_name = input.nextLine();
    
    // if the hotel staff works in changed, then find hotel id according to hotel name
    int hotel_id = -1;
    if(new_hotel_name.length() != 0){
      try{
        PreparedStatement ps = null;
        ps = conn.prepareStatement("SELECT * FROM hotel WHERE name = '" + new_hotel_name + "'");
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
          hotel_id = rs.getInt("id");
        }
      } catch(SQLException e){
        e.printStackTrace();
      }
    }
    
    // create the SQL statement dynamically according to user input
    StringBuilder builder = new StringBuilder("UPDATE staff SET ");
    if(new_hotel_name.length() != 0 && hotel_id == -1){
      System.out.println("Hotel with given name cannot be found.");
    } else {
      if(new_name.length() != 0){
        builder.append("name = '" + new_name + "' ");
      }
      if(new_ssn.length() != 0){
        builder.append(", ssn = '" + new_ssn + "' ");
      }
      
      if(new_age.length() != 0){
        builder.append(", age = " + new_age + " ");
      }
      if(new_gender.length() != 0){
        builder.append(", gender = '" + new_gender + "' ");
      }
      if(new_title.length() != 0){
        builder.append(", title = '" + new_title + "' ");
      }
      if(new_department.length() != 0){
        builder.append(", department = '" + new_department + "' ");
      }
      if(new_phone.length() != 0){
        builder.append(", phone = '" + new_phone + "' ");
      }
      if(new_address.length() != 0){
        builder.append(", address = '" + new_address + "' ");
      }
      if(hotel_id!= -1){
        builder.append(", hotel_id = " + hotel_id + " ");
      }
    }
    builder.append("WHERE name = '" + old_name + "'");
    
    // execute the SQL statement
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement(builder.toString());
      ps.executeUpdate();
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    if(printSuccessMsg){
      System.out.println();
      System.out.println("Staff Update successful!");
      System.out.println();
    }
  }
  
  // Verified.
  public void deleteStaff(){
    
    boolean printSuccessMsg = true;
    
    //prompt for the information needed
    System.out.println("Deleting existing staff.");
    System.out.print("Name: ");
    String name = input.nextLine();
    
    // execute the SQL statement
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("DELETE FROM staff WHERE name = ?");
      ps.setString(1, name);
      ResultSet rs = ps.executeQuery();
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    if(printSuccessMsg){
      System.out.println();
      System.out.println("Staff Delete successful!");
      System.out.println();
    }
    
  }
  
  // Verified.
  public void newRoom(){
    
    boolean printSuccessMsg = true;
    boolean anythingWrong = false;
    
    try{
      conn.setAutoCommit( false );
    }catch (Exception e){
      anythingWrong = true;
      printSuccessMsg = false;
    }
    
    // prompt for the information needed
    System.out.println("Creating new room.");
    System.out.print("Room number: ");
    int room_number = input.nextInt();
    input.nextLine(); // discard new line character
    System.out.print("Category: ");
    String category = input.nextLine();
    System.out.print("Occupancy: ");
    int occupancy = input.nextInt();
    input.nextLine(); // discard new line character
    System.out.print("Nightly rate: ");
    int nightly_rate = input.nextInt();
    input.nextLine(); // discard new line character
    System.out.print("Hotel name: ");
    String hotel_name = input.nextLine();
    
    // get hotel id according to hotel name
    int hotel_id = -1;
    if(hotel_name.length() != 0){
      try{
        PreparedStatement ps = null;
        ps = conn.prepareStatement("SELECT * FROM hotel WHERE name = '" + hotel_name + "'");
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
          hotel_id = rs.getInt("id");
        }
      } catch(SQLException e){
        e.printStackTrace();
        anythingWrong = true;
        printSuccessMsg = false;
      }
    }
    
    // insert new room record
    if(hotel_id != -1){
      // We do get the hotel id provided hotel name
      try{
        PreparedStatement ps = null;
        ps = conn.prepareStatement("INSERT INTO room_type(category, occupancy, nightly_rate) VALUES (?, ?, ?)");
        ps.setString(1, category);
        ps.setInt(2, occupancy);
        ps.setInt(3, nightly_rate);
        ps.executeUpdate();
        
      } catch(SQLException e){
        if(e.getErrorCode() != 1){
          e.printStackTrace();
          printSuccessMsg = false;
        }
      }
      
      try{
        PreparedStatement ps = null;
        ps = conn.prepareStatement("INSERT INTO room(room_number, category, occupancy, availability, hotel_id) VALUES (?, ?, ?, 1, ?)");
        ps.setInt(1, room_number);
        ps.setString(2, category);
        ps.setInt(3, occupancy);
        ps.setInt(4, hotel_id);
        ps.executeUpdate();
        
      } catch(SQLException e){
        System.out.println("(Room number, Hotel name) combination already exists. Primary key violation!");
        anythingWrong = true;
        printSuccessMsg = false;
      }
    } else {
      System.out.println("Hotel with given name cannot be found.");
      printSuccessMsg = false;
      anythingWrong = true;
    }
    
    
    if(anythingWrong){
      try{
        conn.rollback();
        System.out.println("Transaction is rolled back!");
        System.out.println();
      }catch(SQLException e){
        
      }
    }else{
      try{
        conn.commit();
        System.out.println("Transaction is commited!");
        System.out.println();
      }catch(SQLException e){
        
      }
    }
    
    try{
      conn.setAutoCommit( true );
    }catch (Exception e){
      
    }
    
    if(printSuccessMsg){
      System.out.println();
      System.out.println("Room Create successful!");
      System.out.println();
    }
    
  }
  
  // Verified.
  public void updateRoom(){
    
    boolean printSuccessMsg = true;
    
    // prompt for the information needed
    System.out.println("Updating existing room.");
    System.out.print("Current room number: ");
    int old_room_number = input.nextInt();
    input.nextLine();
    System.out.print("Current hotel name: ");
    String old_hotel_name = input.nextLine();
    System.out.print("new Nightly-rate: ");
    int new_nightly_rate = input.nextInt();
    input.nextLine();
    
    // get hotel id according to hotel name
    int hotel_id = -1;
    if(old_hotel_name.length() != 0){
      try{
        PreparedStatement ps = null;
        ps = conn.prepareStatement("SELECT * FROM hotel WHERE name = '" + old_hotel_name + "'");
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
          hotel_id = rs.getInt("id");
        }
      } catch(SQLException e){
        e.printStackTrace();
        printSuccessMsg = false;
      }
    }
    
    // get category and occupancy
    String category = null;
    int occupancy = -1;
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT category, occupancy FROM room WHERE hotel_id = ? AND room_number = ?");
      ps.setInt(1, hotel_id);
      ps.setInt(2, old_room_number);
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        category = rs.getString("category");
        occupancy = rs.getInt("occupancy");
      }
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    // update room table
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("UPDATE room_type SET nightly_rate = ? WHERE category = ? AND occupancy = ?");
      ps.setInt(1, new_nightly_rate);
      ps.setString(2, category);
      ps.setInt(3, occupancy);
      ps.executeUpdate();
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    if(printSuccessMsg){
      System.out.println();
      System.out.println("Room Create successful!");
      System.out.println();
    }
    
  }
  
  // Verified.
  public void deleteRoom(){
    
    boolean printSuccessMsg = true;
    
    // prompt for the information needed
    System.out.println("Deleting existing room.");
    System.out.print("Room number: ");
    int room_number = input.nextInt();
    input.nextLine(); // discard new line character
    System.out.print("Hotel name: ");
    String hotel_name = input.nextLine();
    
    // get hotel id according to hotel name
    int hotel_id = -1;
    if(hotel_name.length() != 0){
      try{
        PreparedStatement ps = null;
        ps = conn.prepareStatement("SELECT * FROM hotel WHERE name = '" + hotel_name + "'");
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
          hotel_id = rs.getInt("id");
        }
      } catch(SQLException e){
        e.printStackTrace();
        printSuccessMsg = false;
      }
    }
    
    // delete the room
    if(hotel_id != -1){
      // We do get the hotel id provided hotel name
      try{
        PreparedStatement ps = null;
        ps = conn.prepareStatement("DELETE FROM room WHERE(room_number = ? AND hotel_id = ?)");
        ps.setInt(1,room_number);
        ps.setInt(2,hotel_id);
        ps.executeUpdate();
        
      } catch(SQLException e){
        e.printStackTrace();
        printSuccessMsg = false;
      }
    } else {
      System.out.println("Hotel with given name cannot be found.");
      printSuccessMsg = false;
    }
    
    if(printSuccessMsg){
      System.out.println();
      System.out.println("Room Delete successful!");
      System.out.println();
    }
    
  }

  // Verified.
  public void newCustomer(){
    
    boolean printSuccessMsg = true;
    
    // prompt for the information needed
    System.out.println("Creating new customer.");
    System.out.print("Name: ");
    String name = input.nextLine();
    System.out.print("SSN: ");
    String ssn = input.nextLine();
    System.out.print("Gedner: ");
    String gender = input.nextLine().toLowerCase().charAt(0) + "";
    System.out.print("Phone: ");
    String phone = input.nextLine();
    System.out.print("Email: ");
    String email = input.nextLine();
    System.out.print("Address: ");
    String address= input.nextLine();
    
    // insert new customer
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("INSERT INTO customer(id, name, ssn, gender, phone, email, address) VALUES (cutomer_seq.nextval, ?, ?, ?, ?, ?, ?)");
      ps.setString(1, name);
      ps.setString(2, ssn);
      ps.setString(3, gender);
      ps.setString(4, phone);
      ps.setString(5, email);
      ps.setString(6, address);
      ps.executeUpdate();
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    if(printSuccessMsg){
      System.out.println();
      System.out.println("Customer Create successful!");
      System.out.println();
    }
    
  }
  
  // Verified.
  public void updateCustomer(){
    
    boolean printSuccessMsg = true;
    
    // prompt for the information needed
    System.out.println("Updating existing customer.");
    System.out.print("Name for search: ");
    String old_name = input.nextLine();
    System.out.print("new Name (if unchanged, type old name): ");
    String new_name = input.nextLine();
    System.out.print("new SSN: ");
    String new_ssn = input.nextLine();
    System.out.print("new Gender: ");
    String new_gender = input.nextLine();
    if(new_gender.length() != 0){
      new_gender = new_gender.toLowerCase().charAt(0) + "";
    }
    System.out.print("new Phone: ");
    String new_phone = input.nextLine();
    System.out.print("new Email: ");
    String new_email = input.nextLine();
    System.out.print("new Address: ");
    String new_address = input.nextLine();
  
    // create the SQL statement dynamically according to user input
    StringBuilder builder = new StringBuilder("UPDATE customer SET ");
    
    if(new_name.length() != 0){
      builder.append("name = '" + new_name + "' ");
    }
    if(new_ssn.length() != 0){
      builder.append(", ssn = '" + new_ssn + "' ");
    }
    if(new_gender.length() != 0){
      builder.append(", gender = '" + new_gender + "' ");
    }
    if(new_phone.length() != 0){
      builder.append(", phone = '" + new_phone + "' ");
    }
    if(new_email.length() != 0){
      builder.append(", email = '" + new_email + "' ");
    }
    if(new_address.length() != 0){
      builder.append(", address = '" + new_address + "' ");
    }
    
    builder.append("WHERE name = '" + old_name + "'");
    
    // execute the SQL statement
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement(builder.toString());
      ps.executeUpdate();
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    if(printSuccessMsg){
      System.out.println();
      System.out.println("Customer Update successful!");
      System.out.println();
    }
    
  }
  
  // Verified.
  public void deleteCustomer(){
    
    boolean printSuccessMsg = true;
    
    System.out.println("Deleting existing customer.");
    System.out.print("Name: ");
    String name = input.nextLine();
    
    // execute the SQL statement
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("DELETE FROM customer WHERE name = ?");
      ps.setString(1, name);
      ResultSet rs = ps.executeQuery();
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    if(printSuccessMsg){
      System.out.println();
      System.out.println("Customer Delete successful!");
      System.out.println();
    }
    
  }
  
  // Verified.
  public void checkAvailability(){
    
    System.out.println("Check available room for hotel.");
    System.out.print("Hotel name: ");
    String hotel_name = input.nextLine();
    
    // retrieve the hotel id
    int hotel_id = -1;
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM hotel WHERE name = '" + hotel_name + "'");
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        hotel_id = rs.getInt("id");
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    
    // execute the SQL statement
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM room WHERE availability = 1 AND hotel_id = ?");
      ps.setInt(1, hotel_id);
      ResultSet rs = ps.executeQuery();
      System.out.println();
      System.out.println("room_number|category|occupancy|availability|hotel_name");
      while(rs.next()){
        System.out.println(rs.getInt("room_number") + "|" + rs.getString("category") + "|" + rs.getInt("occupancy") + "|" + rs.getInt("availability") + "|" + hotel_name);
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    
    System.out.println();
  }
  
  public void assignRoom(){
    
    boolean printSuccessMsg = true;
    
    boolean anythingWrong = false;
    
    try{
      conn.setAutoCommit( false );
    }catch (Exception e){
      printSuccessMsg = false;
    }
    
    // prompt for the information needed
    System.out.println("Assign room to customer.");
    System.out.print("Customer name: ");
    String customer_name = input.nextLine();
    System.out.print("Hotel name: ");
    String hotel_name = input.nextLine();
    System.out.print("Room number: ");
    int room_number = input.nextInt();
    input.nextLine(); // discard new line character
    System.out.print("Current occupancy: ");
    int current_occupancy = input.nextInt();
    input.nextLine(); // discard new line character
    System.out.print("Start date (format example: 12-OCT-2016): ");
    String start_date = input.nextLine();
    System.out.print("End date: ");
    String end_date = input.nextLine();
    System.out.print("Start time (formate example: 11:11:11): ");
    String start_time = input.nextLine();
    System.out.print("End time: ");
    String end_time = input.nextLine();
    System.out.print("Front desk staff name(if any): ");
    String fd_name = input.nextLine();
    System.out.print("Catering staff assigned name(if any): ");
    String cs_name = input.nextLine();
    System.out.print("Service staff assigned name(if any): ");
    String ss_name = input.nextLine();
    
    // retrieve the hotel id
    int hotel_id = -1;
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM hotel WHERE name = '" + hotel_name + "'");
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        hotel_id = rs.getInt("id");
      }
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    // get the specified room availability
    int current_availability = -1;
    int max_occupancy = -1;
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * from room WHERE(room_number = ? AND hotel_id = ?)");
      ps.setInt(1, room_number);
      ps.setInt(2, hotel_id);
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        current_availability = rs.getInt("availability");
        max_occupancy = rs.getInt("occupancy");
      }
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    if(current_availability == -1){
      // if room specified does not exisits, then roll back
      System.out.println("The room specified does not exist!");
      anythingWrong = true;
      printSuccessMsg = false;
    }
    
    if(current_availability == 0){
      // if current availability = 0 (unavailable), then roll back
      System.out.println("The room specified is already occupied!");
      anythingWrong = true;
      printSuccessMsg = false;
    }
    if(current_occupancy > max_occupancy){
      // if current occupancy > room max occupancy, then roll back
      anythingWrong = true;
      printSuccessMsg = false;
    }
    
    
    
    // set the corresponding room availability to be 0
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("UPDATE room SET availability = 0 WHERE(room_number = ? AND hotel_id = ?)");
      ps.setInt(1, room_number);
      ps.setInt(2, hotel_id);
      ps.executeUpdate();
    } catch(SQLException e){
      e.printStackTrace();
      anythingWrong = true;
      printSuccessMsg = false;
    }
    
    // get service staff id if needed
    int ss_id = -1;
    if(ss_name.length() != 0){
      try{
        PreparedStatement ps = null;
        ps = conn.prepareStatement("SELECT id FROM staff WHERE name = ?");
        ps.setString(1, ss_name);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
          ss_id = rs.getInt("id");
        }
      } catch(SQLException e){
        e.printStackTrace();
        printSuccessMsg = false;
      }
    }
    
    // get catering staff id if needed
    int cs_id = -1;
    if(cs_name.length() != 0){
      try{
        PreparedStatement ps = null;
        ps = conn.prepareStatement("SELECT id FROM staff WHERE name = ?");
        ps.setString(1, cs_name);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
          cs_id = rs.getInt("id");
        }
      } catch(SQLException e){
        e.printStackTrace();
        printSuccessMsg = false;
      }
    }
    
    // get customer id
    int customer_id = -1;
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT id FROM customer WHERE name = ?");
      ps.setString(1, customer_name);
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        customer_id = rs.getInt("id");
      }
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    // get front desk id
    int fd_id = -1;
    if(fd_name.length() != 0){
      try{
        PreparedStatement ps = null;
        ps = conn.prepareStatement("SELECT id FROM staff WHERE name = ?");
        ps.setString(1, fd_name);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
          fd_id = rs.getInt("id");
        }
      } catch(SQLException e){
        e.printStackTrace();
        printSuccessMsg = false;
      }
    }
    
    if(ss_name.length() != 0 && ss_id == -1){
      System.out.println("Service staff with given name cannot be found!");
      anythingWrong = true;
      printSuccessMsg = false;
    }
    
    if(cs_name.length() != 0 && cs_id == -1){
      System.out.println("Catering staff with given name cannot be found!");
      anythingWrong = true;
      printSuccessMsg = false;
    }
    
    if(fd_name.length() != 0 && fd_id == -1){
      System.out.println("Front desk staff with given name cannot be found!");
      anythingWrong = true;
      printSuccessMsg = false;
    }
    
    // insert into checkin table
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("INSERT INTO checkin(id, current_occupancy, start_date, end_date, start_time, end_time, cstaff_id, sstaff_id, fdstaff_id, customer_id) VALUES (checkin_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      ps.setInt(1, current_occupancy);
      ps.setString(2, start_date);
      ps.setString(3, end_date);
      ps.setString(4, start_time);
      ps.setString(5, end_time);
      
      if(cs_id == -1){
        ps.setString(6, null);
      }else{
        ps.setInt(6, cs_id);
      }
      
      if(ss_id == -1){
        ps.setString(7, null);
      }else{
        ps.setInt(7, ss_id);
      }
      if(fd_id == -1){
        ps.setString(8, null);
      }else{
        ps.setInt(8, fd_id);
      }
      ps.setInt(9, customer_id);
      
      ps.executeUpdate();
    } catch(SQLException e){
      e.printStackTrace();
      anythingWrong = true;
      printSuccessMsg = false;
    }
  
    // retrieve the checkin id
    int checkin_id = -1;
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT id FROM checkin WHERE customer_id = ? AND start_date = ?");
      ps.setInt(1, customer_id);
      ps.setString(2, start_date);
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        checkin_id = rs.getInt("id");
      }
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    // insert into reserve for table
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("INSERT INTO reserve_for(hotel_id, room_number, checkin_id) VALUES (?, ?, ?)");
      ps.setInt(1, hotel_id);
      ps.setInt(2, room_number);
      ps.setInt(3, checkin_id);
      ps.executeUpdate();
    } catch(SQLException e){
      e.printStackTrace();
      anythingWrong = true;
      printSuccessMsg = false;
    }
    
    if(anythingWrong){
      try{
        conn.rollback();
        System.out.println("Transaction is rolled back!");
        System.out.println();
      }catch(SQLException e){
        
      }
    }else{
      try{
        conn.commit();
        System.out.println("Transaction is commited!");
        System.out.println();
      }catch(SQLException e){
        
      }
    }
    
    try{
      conn.setAutoCommit( true );
    }catch (Exception e){
    
    }
    
    if(printSuccessMsg){
      System.out.println();
      System.out.println("Assign Room successful!");
      System.out.println();
    }
  }

  // Verified
  public void releaseRoom(){
    
    boolean printSuccessMsg = true;
    
    // prompt for the information needed
    System.out.println("Release room for customer.");
    System.out.print("Customer name: ");
    String customer_name = input.nextLine();
    
    // get the customer_id from customer according to customer name
    int customer_id = -1;
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT id FROM customer WHERE name = ?");
      ps.setString(1, customer_name);
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        customer_id = rs.getInt("id");
      }
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    if(customer_id == -1){
      System.out.println("Customer with given name cannot be found!");
      System.out.println();
      printSuccessMsg = false;
    }
    
    // get the checkin_id from checkin table according to customer id
    int checkin_id = -1;
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM checkin WHERE customer_id = ?");
      ps.setInt(1, customer_id);
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        checkin_id = rs.getInt("id");
      }
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    // get the room_number and hotel_id from reserve_for table according to check_in id
    int hotel_id = -1;
    int room_number = -1;
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT hotel_id, room_number FROM reserve_for WHERE checkin_id = ?");
      ps.setInt(1, checkin_id);
      ResultSet rs = ps.executeQuery();
      if(rs.next()){
        hotel_id = rs.getInt("hotel_id");
        room_number = rs.getInt("room_number");
      }
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    // delete reserve_for according to checkin_id
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("DELETE FROM reserve_for WHERE checkin_id = ?");
      ps.setInt(1, checkin_id);
      ps.executeUpdate();
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    // set the corresponding roon availability to be 1
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("UPDATE room SET availability = 1 WHERE(room_number = ? AND hotel_id = ?)");
      ps.setInt(1, room_number);
      ps.setInt(2, hotel_id);
      ps.executeUpdate();
    } catch(SQLException e){
      e.printStackTrace();
      printSuccessMsg = false;
    }
    
    if(printSuccessMsg){
      System.out.println();
      System.out.println("Release Room successful!");
      System.out.println();
    }
  }
}
