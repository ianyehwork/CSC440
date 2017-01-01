import java.sql.*;
import java.util.*;
/*
 * This class communicates between user interface and databse to complete
 * user required operation. The class is responsible to prompt the user
 * for the input, issue a SQL statement to the databse and finally print the
 * result to the console.
 */
public class Reports {
    private Connection connection;
    private Scanner input;
    private String defaultDate = "17-NOV-2016";
    
    public Reports(Connection connection, Scanner input){
        this.connection = connection;
        this.input = input;
    }
    
    /**
     * Prints the occupancy for a room category
     * User can enter a room category and/or a date range
     * If no room category is given, all categories will be grouped and returned
     * If no date range is given, it will print occupancy after default date
     */
    public void getOccupancyByRoomType(){
        try {
        	ResultSet result = null;
	        PreparedStatement statement = null;
	        
	        // ask user for room category
	        System.out.println("Enter room category (or nothing):");
	        String roomCategory = input.nextLine();
	        
	        // user gave room category
	        if (roomCategory.length() > 0) {
	        	
	        	// ask user for start date
	            System.out.println("Enter start date (form dd-MMM-yyyy OR leave blank for all starting today):");
	            String startDate = input.nextLine();
	            
	            // user will specify date range
	            if (startDate.length() > 0) {
	            	
	            	// ask user for end date
	                System.out.println("Enter end date (form dd-MMM-yyyy):");
	                String endDate = input.nextLine();
	                statement = connection.prepareStatement("SELECT SUM(current_occupancy) AS current_occupancy " +
                                                            "FROM checkin c, room r, reserve_for f " +
                                                            "WHERE c.id = f.checkin_id " +
                                                            "AND f.room_number = r.room_number " +
                                                            "AND f.hotel_id = r.hotel_id " +
                                                            "AND c.start_date >= ? " +
                                                            "AND c.end_date <= ? " +
                                                            "AND r.category = ?");
	                statement.setString(1, startDate);
	                statement.setString(2, endDate);
	                statement.setString(3, roomCategory);
	                result = statement.executeQuery();
	                
                    // default date will be used
	            } else {
                    statement = connection.prepareStatement("SELECT SUM(current_occupancy) AS current_occupancy " +
                                                            "FROM checkin c, room r, reserve_for f " +
                                                            "WHERE (c.id = f.checkin_id " +
                                                            "AND f.room_number = r.room_number " +
                                                            "AND f.hotel_id = r.hotel_id " +
                                                            "AND c.start_date >= ?) " +
                                                            "AND r.category= ?");
	            	statement.setString(1, defaultDate);
	            	statement.setString(2, roomCategory);
	            	result = statement.executeQuery();
	            }
	            
	            // print out returned data
	            System.out.println();
	            while (result.next()) {
	                int current_occupancy = result.getInt("current_occupancy");
	                System.out.println("category: " + roomCategory + " has " + current_occupancy + " occupants");
	            }
	            System.out.println();
	            
	            // close everything
                JDBCConnector.close(result);
                JDBCConnector.close(statement);
	            
                // group by room category
	        } else {
	        	
	        	// ask user for start date
	            System.out.println("Enter start date (form dd-MMM-yyyy OR leave blank for all starting today):");
	            String startDate = input.nextLine();
	            
	            // user will specify date range
	            if (startDate.length() > 0) {
	            	
	            	// ask user for end date
	                System.out.println("Enter end date (form dd-MMM-yyyy):");
	                String endDate = input.nextLine();
	                
	                statement = connection.prepareStatement("SELECT category, SUM(current_occupancy) AS current_occupancy " +
								                            "FROM checkin c, room r, reserve_for f " +
								                            "WHERE (c.id = f.checkin_id " +
								                            "AND f.room_number = r.room_number " +
								                            "AND f.hotel_id = r.hotel_id " +
								                            "AND c.start_date >= ? " +
								                            "AND c.end_date <= ?) " +
								                            "GROUP BY r.category");
	                statement.setString(1, startDate);
	                statement.setString(2, endDate);
	                result = statement.executeQuery();
	                
                    // default date will be used
	            } else {
                    statement = connection.prepareStatement("SELECT category, SUM(current_occupancy) AS current_occupancy " +
								                            "FROM checkin c, room r, reserve_for f " +
								                            "WHERE (c.id = f.checkin_id " +
								                            "AND f.room_number = r.room_number " +
								                            "AND f.hotel_id = r.hotel_id " +
								                            "AND c.start_date >= ?) " +
								                            "GROUP BY r.category");
                    statement.setString(1, defaultDate);
	                result = statement.executeQuery();
	            }
	            
                // print out returned data
	            System.out.println();
                while (result.next()) {
                    String category = result.getString("category");
                    int current_occupancy = result.getInt("current_occupancy");
                    System.out.println("category: " + category + " has " + current_occupancy + " occupants");
                }
                System.out.println();
	            
	        }
	        
	        // close everything
	        JDBCConnector.close(result);
	        JDBCConnector.close(statement);
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
        	// close connection
        	JDBCConnector.closeConnection();
        }
    }
    
    /**
     * Prints the occupancy for a given date range
     */
    public void getOccupancyByDate(){
        try {
        	
        	// ask user for start date
	        System.out.println("Enter start date (form dd-MMM-yyyy):");
	        String startDate = input.nextLine();
	        
	        // ask user for end date
	        System.out.println("Enter end date (form dd-MMM-yyyy):");
	        String endDate = input.nextLine();
	        
	        PreparedStatement statement = connection.prepareStatement("SELECT SUM(current_occupancy) AS occupancy " +
                                                                      "FROM checkin " +
                                                                      "WHERE (? <= start_date " +
                                                                      "AND ? >= end_date)");
            statement.setString(1, startDate);
            statement.setString(2, endDate);
	        ResultSet result = statement.executeQuery();
	        
            System.out.println();
	        // print out returned data
	        while (result.next()) {
	            int occupancy = result.getInt("occupancy");
	            System.out.println(occupancy + " occupants");
	        }
            System.out.println();
	        
	        // close everything
	        JDBCConnector.close(result);
	        JDBCConnector.close(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	// close connection
	        JDBCConnector.closeConnection();
        }
    }
    
    /**
     * Prints the occupancy for a hotel
     * User can enter a hotel id and/or a date range
     * If no hotel id is given, all hotels will be grouped and returned
     * If no date range is given, it will print occupancy after default date
     */
    public void getOccupancyByHotel(){
    	try {
	        ResultSet result = null;
	        PreparedStatement statement = null;
	        
	        // ask user for hotel id
	        System.out.println("Enter hotel id (or nothing):");
	        String hotelIDstr = input.nextLine();
	        
	        // user provided hotel id
	        if (hotelIDstr.length() > 0) {
	            int hotelID = Integer.parseInt(hotelIDstr);
	            
	            // ask user for start date
	            System.out.println("Enter start date (form dd-MMM-yyyy OR leave blank for all starting today):");
	            String startDate = input.nextLine();
	            
	            // user will specify date range
	            if (startDate.length() > 0) {
	            	
	            	// ask user for end date
	                System.out.println("Enter end date (form dd-MMM-yyyy):");
	                String endDate = input.nextLine();
	                
                    statement = connection.prepareStatement("SELECT SUM(current_occupancy) AS current_occupancy " +
                                                            "FROM checkin c, reserve_for r " +
                                                            "WHERE c.id = r.checkin_id " +
                                                            "AND ? <= c.start_date " +
                                                            "AND ? >= c.end_date " +
                                                            "AND r.hotel_id=?");
                    statement.setString(1, startDate);
                    statement.setString(2, endDate);
                    statement.setInt(3, hotelID);
	                result = statement.executeQuery();
	                
                    // default date will be used
	            } else {
                    statement = connection.prepareStatement("SELECT SUM(current_occupancy) AS current_occupancy " +
                                                            "FROM checkin c, reserve_for r " +
                                                            "WHERE c.id = r.checkin_id " +
                                                            "AND ? <= c.start_date " +
                                                            "AND r.hotel_id=?");
                    statement.setString(1, defaultDate);
                    statement.setInt(2, hotelID);
	                result = statement.executeQuery();
	            }
	            
	            // print out returned data
                System.out.println();
	            while (result.next()) {
	                int current_occupancy = result.getInt("current_occupancy");
	                System.out.println("hotel id: " + hotelID + " has " + current_occupancy + " occupants");
	            }
                System.out.println();
	            
                // group by hotel
	        } else {
	        	
	        	// ask user for start date
	            System.out.println("Enter start date (form dd-MMM-yyyy OR leave blank for all starting today):");
	            String startDate = input.nextLine();
	            
	            // user will specify date range
	            if (startDate.length() > 0) {
	            	
	            	// ask user for end date
	                System.out.println("Enter end date (form dd-MMM-yyyy):");
	                String endDate = input.nextLine();
	                
                    statement = connection.prepareStatement("SELECT hotel_id, SUM(current_occupancy) AS current_occupancy " +
                                                            "FROM checkin c, reserve_for r " +
                                                            "WHERE (c.id = r.checkin_id " +
                                                            "AND ? <= c.start_date " +
                                                            "AND ? >= c.end_date) " +
                                                            "GROUP BY r.hotel_id");
                    statement.setString(1, startDate);
                    statement.setString(2, endDate);
	                result = statement.executeQuery();
	                
                    // default date will be used
	            } else {
                    statement = connection.prepareStatement("SELECT hotel_id, SUM(current_occupancy) AS current_occupancy " +
                                                            "FROM checkin c, reserve_for r " +
                                                            "WHERE (c.id = r.checkin_id " +
                                                            "AND ? <= c.start_date) " +
                                                            "GROUP BY r.hotel_id");
                    statement.setString(1, defaultDate);
	                result = statement.executeQuery();
	            }
	            
	            // print out returned data
                System.out.println();
	            while (result.next()) {
	                int hotel_id = result.getInt("hotel_id");
	                int current_occupancy = result.getInt("current_occupancy");
	                System.out.println("hotel id: " + hotel_id + " has " + current_occupancy + " occupants");
	            }
                System.out.println();
	        }
	        
	        // close everything
	        JDBCConnector.close(result);
	        JDBCConnector.close(statement);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		// close connection
    		JDBCConnector.closeConnection();
    	}
    }
    
    /**
     * Prints the occupant information
     * User can enter a date range
     * If no date range is given, it will print occupants after default date
     */
    public void getOccupants(){
    	try {
	        ResultSet result = null;
	        PreparedStatement statement = null;
	        
	        // ask user for  start date
	        System.out.println("Enter start date (form dd-MMM-yyyy OR leave blank for all starting today):");
	        String startDate = input.nextLine();
	        
	        // user will specify date range
	        if (startDate.length() > 0) {
	        	
	        	// ask user for end date
	            System.out.println("Enter end date (form dd-MMM-yyyy):");
	            String endDate = input.nextLine();
	            
                statement = connection.prepareStatement("SELECT c.* " +
                                                        "FROM customer c, checkin k " +
                                                        "WHERE c.id = k.customer_id " +
                                                        "AND ? <= k.start_date " +
                                                        "AND ? >= k.end_date");
                statement.setString(1, startDate);
                statement.setString(2, endDate);
	            result = statement.executeQuery();
	            
                // default date will be used
	        } else {
                statement = connection.prepareStatement("SELECT c.* " +
                                                        "FROM customer c, checkin k " +
                                                        "WHERE c.id = k.customer_id " +
                                                        "AND ? <= k.start_date");
                statement.setString(1, defaultDate);
	            result = statement.executeQuery();
	        }
	        
	        // print returned data
            System.out.println();
	        while (result.next()) {
	            int id = result.getInt("id");
	            String name = result.getString("name");
	            String ssn = result.getString("ssn");
	            String gender = result.getString("gender");
	            String phone = result.getString("phone");
	            String email = result.getString("email");
	            String address = result.getString("address");
	            System.out.println("id: " + id +
	                               ", name: " + name +
	                               ", ssn: " + ssn +
	                               ", gender: " + gender +
	                               ", phone: " + phone +
	                               ", email: " + email +
	                               ", address: " + address);
	        }
            System.out.println();
	        
	        // close everything
	        JDBCConnector.close(result);
	        JDBCConnector.close(statement);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		// close connection
    		JDBCConnector.closeConnection();
    	}
    }
    
    // TODO: divide by 0
    /**
     * Prints the percent of rooms occupied
     * User can enter a date range
     * If no date range is given, it will print percent occupied after default date
     */
    public void getPercentRoomOccupied(){
    	try {
	        ResultSet result = null;
	        PreparedStatement statement = null;
	        
	        // ask user for hotel id
	        System.out.println("Enter hotel id:");
	        int hotelID = input.nextInt();
	        input.nextLine();
	        
	        // ask user for start date
	        System.out.println("Enter start date (form dd-MMM-yyyy OR leave blank for all starting today):");
	        String startDate = input.nextLine();
	        
	        // user will specify date range
	        if (startDate.length() > 0) {
	        	
	        	// ask user for end date
	            System.out.println("Enter end date (form dd-MMM-yyyy):");
	            String endDate = input.nextLine();
	            
                statement = connection.prepareStatement("SELECT COUNT(DISTINCT m.room_number)/COUNT(DISTINCT r.room_number) * 100 AS percent_occupied " +
								                        "FROM room r, room m, reserve_for f, checkin c " +
								                        "WHERE m.hotel_id = ? " +
								                        "AND r.hotel_id = m.hotel_id " +
								                        "AND f.room_number = m.room_number " +
								                        "AND f.hotel_id = m.hotel_id " +
								                        "AND f.checkin_id = c.id " +
								                        "AND ? <= c.start_date " +
								                        "AND ? >= c.end_date");
                statement.setInt(1, hotelID);
                statement.setString(2, startDate);
                statement.setString(3, endDate);
	            result = statement.executeQuery();
	            
                // default date will be used
	        } else {
                statement = connection.prepareStatement("SELECT COUNT(DISTINCT m.room_number)/COUNT(r.room_number) * 100 AS percent_occupied " +
								                        "FROM room r, room m, reserve_for f, checkin c " +
								                        "WHERE m.hotel_id = ? " +
								                        "AND r.hotel_id = m.hotel_id " +
								                        "AND f.room_number = m.room_number " +
								                        "AND f.hotel_id = m.hotel_id " +
								                        "AND f.checkin_id = c.id " +
								                        "AND ? <= c.start_date");
                statement.setInt(1, hotelID);
                statement.setString(2, defaultDate);
	            result = statement.executeQuery();
	        }
	        
	        // print returned data
            System.out.println();
	        while (result.next()) {
	            double occupancy = result.getDouble("percent_occupied");
	            System.out.println(occupancy + "% occupied");
	        }
            System.out.println();
	        
	        // close everything
	        JDBCConnector.close(result);
	        JDBCConnector.close(statement);
    	} catch (SQLException e) {
    		if (e.getErrorCode() == 1476) {
                System.out.println("0% occupied");
            } else {
                e.printStackTrace();
            }
    	} finally {
    		// close connection
    		JDBCConnector.closeConnection();
    	}
    }
    
    /**
     * Prints staff information based on title
     * User can enter a title
     * If no title is given, it will print all titles grouped
     */
    public void getStaffGroupByRole(){
    	try {
	        ResultSet result = null;
	        PreparedStatement statement = null;
	        
	        // ask user for title
	        System.out.println("Enter title (or nothing):");
	        String title = input.nextLine();
	        
	        // user did not specify title
	        if (title.length() == 0) {
                statement = connection.prepareStatement("SELECT * " +
                                                        "FROM staff " +
                                                        "ORDER BY title");
	            result = statement.executeQuery();
	            
                // user specified title
	        } else {
                statement = connection.prepareStatement("SELECT * " +
                                                        "FROM staff " +
                                                        "WHERE title=?");
                statement.setString(1, title);
	            result = statement.executeQuery();
	        }
	        
	        // print returned data
            System.out.println();
	        while (result.next()) {
	            int id = result.getInt("id");
	            String name = result.getString("name");
	            String ssn = result.getString("ssn");
	            int age = result.getInt("age");
	            String gender = result.getString("gender");
	            String titlestr = result.getString("title");
	            String department = result.getString("department");
	            String phone = result.getString("phone");
	            String address = result.getString("address");
	            int hotelID = result.getInt("hotel_id");
	            System.out.println("id: " + id +
	                               ", name: " + name +
	                               ", ssn: " + ssn +
	                               ", age: " + age +
	                               ", gender: " + gender +
	                               ", title: " + titlestr +
	                               ", department: " + department +
	                               ", phone: " + phone +
	                               ", address: " + address +
	                               ", hotel id: " + hotelID);
	        }
            System.out.println();
	        
	        // close everything
	        JDBCConnector.close(result);
	        JDBCConnector.close(statement);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		// close connection
    		JDBCConnector.closeConnection();
    	}
    }
    
    /**
     * Prints customers for caterer
     * User can enter a staff name or staff id
     */
    public void getCustomerByCatering(){
    	try {
	        ResultSet result = null;
	        PreparedStatement statement = null;
	        
	        // ask user for staff name
	        System.out.println("Enter staff name (or nothing):");
	        String staffName = input.nextLine();
	        
	        // user did not specify name
	        if (staffName.length() == 0) {
	        	
	        	// ask user for staff id
	            System.out.println("Enter staff id:");
	            int staffID = Integer.parseInt(input.nextLine());
	            
                statement = connection.prepareStatement("SELECT c.* " +
                                                        "FROM customer c, checkin k " +
                                                        "WHERE c.id = k.customer_id " +
                                                        "AND k.cstaff_id = ?");
                statement.setInt(1, staffID);
	            result = statement.executeQuery();
	            
                // user specified name
	        } else {
                statement = connection.prepareStatement("SELECT c.* " +
                                                        "FROM customer c, checkin k " +
                                                        "WHERE (c.id = k.customer_id " +
                                                        "AND k.cstaff_id = (SELECT id FROM staff WHERE name=?))");
                statement.setString(1, staffName);
	            result = statement.executeQuery();
	        }
	        
	        // print returned data
            System.out.println();
	        while (result.next()) {
	            int id = result.getInt("id");
	            String name = result.getString("name");
	            String ssn = result.getString("ssn");
	            String gender = result.getString("gender");
	            String phone = result.getString("phone");
	            String email = result.getString("email");
	            String address = result.getString("address");
	            System.out.println("id: " + id +
	                               ", name: " + name +
	                               ", ssn: " + ssn +
	                               ", gender: " + gender +
	                               ", phone: " + phone +
	                               ", email: " + email +
	                               ", address: " + address);
	        }
            System.out.println();
	        
	        // close everything
	        JDBCConnector.close(result);
	        JDBCConnector.close(statement);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		// close connection
    		JDBCConnector.closeConnection();
    	}
    }
    
    /**
     * Prints customers for service staff
     * User can enter a staff name or staff id
     */
    public void getCustomerByService(){
    	try {
	        ResultSet result = null;
	        PreparedStatement statement = null;
	        
	        // ask user for staff name
	        System.out.println("Enter staff name (or nothing):");
	        String staffName = input.nextLine();
	        
	        // user did not specify name
	        if (staffName.length() == 0) {
	        	
	        	// ask user for staff id
	            System.out.println("Enter staff id:");
	            int staffID = Integer.parseInt(input.nextLine());
	            
                statement = connection.prepareStatement("SELECT c.* " +
                                                        "FROM customer c, checkin k " +
                                                        "WHERE (c.id = k.customer_id " +
                                                        "AND k.sstaff_id = ?)");
                statement.setInt(1, staffID);
	            result = statement.executeQuery();
	            
                // user specified name
	        } else {
                statement = connection.prepareStatement("SELECT c.* " +
                                                        "FROM customer c, checkin k " +
                                                        "WHERE (c.id = k.customer_id " +
                                                        "AND k.sstaff_id = (SELECT id FROM staff WHERE name=?))");
                statement.setString(1, staffName);
	            result = statement.executeQuery();
	        }
	        
	        // print returned data
            System.out.println();
	        while (result.next()) {
	            int id = result.getInt("id");
	            String name = result.getString("name");
	            String ssn = result.getString("ssn");
	            String gender = result.getString("gender");
	            String phone = result.getString("phone");
	            String email = result.getString("email");
	            String address = result.getString("address");
	            System.out.println("id: " + id +
	                               ", name: " + name +
	                               ", ssn: " + ssn +
	                               ", gender: " + gender +
	                               ", phone: " + phone +
	                               ", email: " + email +
	                               ", address: " + address);
	        }
            System.out.println();
	        
	        // close everything
	        JDBCConnector.close(result);
	        JDBCConnector.close(statement);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		// close connection
    		JDBCConnector.closeConnection();
    	}
    }
}
