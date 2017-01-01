import java.sql.*;
import java.util.*;
/*
 * This class communicates between user interface and databse to complete
 * user required operation. The class is responsible to prompt the user
 * for the input, issue a SQL statement to the databse and finally print the
 * result to the console.
 */
public class PrintTable {
  private Connection conn;
  private Scanner input;
  public PrintTable(Connection connection, Scanner input){
    this.conn = connection;
    this.input = input;
  }
  
  public void printHotel(){
    System.out.println();
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM hotel");
      ResultSet rs = ps.executeQuery();
      System.out.println("id|name|address|phone");
      while(rs.next()){
        System.out.println("" + rs.getInt("id") + "|" + rs.getString("name") + "|" + rs.getString("address") + "|" + rs.getString("phone"));
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    System.out.println();
  }
  
  public void printCustomer(){
    System.out.println();
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM customer");
      ResultSet rs = ps.executeQuery();
      System.out.println("id|name|ssn|gender|phone|email|address");
      while(rs.next()){
        System.out.println("" + rs.getInt("id") + "|" + rs.getString("name") + "|" + rs.getString("ssn") + "|" + rs.getString("gender") + "|" + rs.getString("phone") + "|" + rs.getString("email") + "|" + rs.getString("address"));
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    System.out.println();
  }
  
  public void printStaff(){
    System.out.println();
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM staff");
      ResultSet rs = ps.executeQuery();
      System.out.println("id|ssn|name|age|gender|title|department|phone|address|hotel_id");
      while(rs.next()){
        System.out.println("" + rs.getInt("id") + "|" + rs.getString("ssn") + "|" + rs.getString("name") + "|" + rs.getInt("age") + "|" + rs.getString("gender") + "|" + rs.getString("title") + "|" + rs.getString("department") + "|" + rs.getString("phone") + "|" + rs.getString("address") + "|" + rs.getInt("hotel_id"));
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    System.out.println();
  }
  
  public void printRoomType(){
    System.out.println();
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM room_type");
      ResultSet rs = ps.executeQuery();
      System.out.println("category|occupancy|nightly_rate");
      while(rs.next()){
        System.out.println("" + rs.getString("category") + "|" + rs.getInt("occupancy") + "|" + rs.getInt("nightly_rate"));
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    System.out.println();
  }
  
  public void printRoom(){
    System.out.println();
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM room");
      ResultSet rs = ps.executeQuery();
      System.out.println("room_number|category|occupancy|availability|hotel_id");
      while(rs.next()){
        System.out.println("" + rs.getInt("room_number") + "|" + rs.getString("category") + "|" + rs.getInt("occupancy") + "|" + rs.getInt("availability") + "|" + rs.getInt("hotel_id"));
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    System.out.println();
  }
  
  public void printCheckin(){
    System.out.println();
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM checkin");
      ResultSet rs = ps.executeQuery();
      System.out.println("id|current_occupancy|start_date|end_date|start_time|end_time|cstaff_id|sstaff_id|fdstaff_id|customer_id");
      while(rs.next()){
        System.out.print("" + rs.getInt("id") + "|" + rs.getInt("current_occupancy") + "|");
        String start_date = rs.getString("start_date");
        start_date = start_date.substring(0, start_date.indexOf(' '));
        System.out.print(start_date + "|");
        String end_date = rs.getString("end_date");
        end_date = end_date.substring(0, end_date.indexOf(' '));
        System.out.print(end_date + "|");
        System.out.println(rs.getString("start_time") + "|" + rs.getString("end_time") + "|" + rs.getString("cstaff_id") + "|" + rs.getString("sstaff_id") + "|" + rs.getString("fdstaff_id") + "|" + rs.getInt("customer_id"));
        
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    System.out.println();
  }
  
  public void printServiceRecord(){
    System.out.println();
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM service_record");
      ResultSet rs = ps.executeQuery();
      System.out.println("id|type|amount|checkin_id");
      while(rs.next()){
        System.out.println("" + rs.getInt("id") + "|" + rs.getString("type") + "|" + rs.getInt("amount") + "|" + rs.getInt("checkin_id"));
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    System.out.println();
  }
  
  public void printBillingAccount(){
    System.out.println();
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM billing");
      ResultSet rs = ps.executeQuery();
      System.out.println("id|billing_addr|payment_method|card_number|customer_id|bstaff_id");
      while(rs.next()){
        System.out.println("" + rs.getInt("id") + "|" + rs.getString("billing_addr") + "|" + rs.getString("payment_method") + "|" + rs.getString("card_number") + "|" + rs.getInt("customer_id") + "|" + rs.getInt("bstaff_id"));
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    System.out.println();
  }
  
  public void printReserveFor(){
    System.out.println();
    try{
      PreparedStatement ps = null;
      ps = conn.prepareStatement("SELECT * FROM reserve_for");
      ResultSet rs = ps.executeQuery();
      System.out.println("hotel_id|room_number|checkin_id");
      while(rs.next()){
        System.out.println("" + rs.getInt("hotel_id") + "|" + rs.getInt("room_number") + "|" + rs.getInt("checkin_id"));
      }
    } catch(SQLException e){
      e.printStackTrace();
    }
    System.out.println();
  }
}
