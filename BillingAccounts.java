//package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Scanner;

/**
 * @author Rodolphe N Sandjong
 * This class communicates between user interface and databse to complete
 * user required operation. The class is responsible to prompt the user
 * for the input, issue a SQL statement to the databse and finally print the
 * result to the console.
 */
public class BillingAccounts {

	private Connection connection;
	private Scanner input;
	private PrintTable printTable;

	//private int id = -1;
	private String billing_addr;
	private String payment_method;
	private String card_number;
	private int customer_id = -1;
	private int bstaff_id = -1;

	private String cname;
	private String bname;
	private boolean sameBillingAddr = false;
	//private boolean samePaymentMethod = false;
	//private boolean sameCardNumber = false;
	//private boolean isRoomAvailable = false;
	private StringBuilder billing_str;

	/**
	 * BillingAccounts constructor
	 * @param connection (open/active)
	 * @param input Scanner system.in
	 */
	public BillingAccounts(Connection connection, Scanner input) {
		this.connection = connection;
		this.input = input;
		this.printTable = new PrintTable(JDBCConnector.getConnection(), this.input);
	}

	/**
	 * Generate a new billing account from user inputs
	 */
	public void generateBillingAccount() {
		int ch = checkIfRoomAivail();
		while(ch == -1){
			ch = checkIfRoomAivail();
		}
		if(ch == 2)
			return;
		
		// get customer name and id
		boolean validname = getCustomerNameAndID();
		while (!validname) {
			System.out.println("invalid customer name ... Try again.");
			validname = getCustomerNameAndID();
		}
		
		//check if customer already has a billing
		String q1 = "SELECT id FROM billing WHERE customer_id = " + this.customer_id + "";
		int hasBilling =  getID(q1); //checkCustHasBilling(this.customer_id);
		if(hasBilling>-1){
			System.out.println("-------------------------------------------");
			System.out.println("Customer already has a billing account.");
			System.out.println("-------------------------------------------");
			return;
		}

		// get staff name and id
		boolean validname2 = getStaffnameAndID();
		while (!validname2) {
			System.out.println("invalid staff name ... Try again.");
			validname2 = getStaffnameAndID();
		}

		// get customer billing address
		boolean goodAddress = getBillingAddress();
		while (!goodAddress) {
			System.out.println("invalid Billing address ... Try again.");
			goodAddress = getBillingAddress();
		}

		// get customer payment method
		boolean goodPayMethod = getMethodPayment();
		while (!goodPayMethod) {
			System.out.println("invalid Payment method ... Try again.");
			goodPayMethod = getMethodPayment();
		}

		// get customer Card Number
		System.out.println("Enter the payment Card Number or N/A if paying with cash:");
		this.card_number = getUserInput();

		//print billing table before
		System.out.println("------------------BEFORE-------------------------");
		printBillingTable();
		
		// generate billing info
		boolean success = insertBilling(this.customer_id, this.bstaff_id, this.billing_addr, this.payment_method,
				this.card_number);
		if (success) {
			System.out.println("Billing generated");
		} else
			System.out.println("Billing not generated");
		
		//print billing table after
		System.out.println("------------------AFTER-------------------------");
		printBillingTable();
	}

	/**
	 * 
	 */
	private int checkIfRoomAivail() {
		int ret = -1;
		System.out.println("-------------------------------------------");
		System.out.println("Do You Want To Generate A Billing For One Of These Available Rooms?");
		System.out.println("Enter 1 (for, Room available in this list/table)");
		System.out.println("Enter 2 (for, Room Not available AND QUIT Billing Menu.)");
		System.out.println("-------------------------------------------");
		printTable.printRoom();
		String ch = getUserInput();
		
		try {
			if(Integer.parseInt(ch) == 1)
				return 1;
			else if(Integer.parseInt(ch) == 2){
				return 2;
			}
			else{
				return -1;
			}
		} catch (NumberFormatException e) {
			System.out.println("Bad Input.....Try again");
			checkIfRoomAivail();
		}
		return -1;
	}


	/**
	 * Update a billing account with user inputs
	 */
	public void updateBillingAccount() {
		// reset all billing field
		resetField();

		// get customer name and id
		boolean validname = getCustomerNameAndID();
		while (!validname) {
			System.out.println("invalid customer name ... Try again.");
			validname = getCustomerNameAndID();
		}


		// get customer billing address
		System.out.println("Enter 3 if the billing address is the same as the existing billing address");
		boolean choice = getChoise(true);
		if (this.sameBillingAddr) {
			String aquery = "SELECT billing_addr FROM billing WHERE customer_id = " + this.customer_id + "";
			this.billing_addr = getCustomerDataFromDatabase(aquery, "billing_addr");
		} else if (!choice) {
			System.out.println("Enter the billing address:");
			this.billing_addr = getUserInput();
		} else {
			String aquery = "SELECT ADDRESS FROM CUSTOMER WHERE NAME = '" + cname + "'";
			this.billing_addr = getCustomerDataFromDatabase(aquery, "address");//getAddress(aquery);
		}

		// get payment method
		boolean goodPayMethod = getMethodPayment();
		while (!goodPayMethod) {
			System.out.println("invalid Payment method ... Try again.");
			goodPayMethod = getMethodPayment();
		}


		// get customer Card Number
		System.out.println(
				"Enter 1 if Card Number Unchanged; Enter the payment Card Number or N/A if if paying with cash:");
		this.card_number = getUserInput();
		if (this.card_number != null && this.card_number.equals("1")) {
			String aquery = "SELECT card_number FROM billing WHERE CUSTOMER_ID = '" + this.customer_id + "'";
			this.card_number = getCustomerDataFromDatabase(aquery, "card_number");
		}

		
		//print billing table before
		System.out.println("------------------BEFORE-------------------------");
		printBillingTable();
		
		// update billing info

		// add check point here
		boolean success = updateBilling(this.customer_id, this.bstaff_id, this.billing_addr, this.payment_method,
				this.card_number);
		if (success) {
			System.out.println("Billing updated");
		} else
			System.out.println("Billing not updated");
		// commit or rollback
		
		//print billing table before
		System.out.println("------------------AFTER-------------------------");
		printBillingTable();

	}

	/**
	 * get the user payment method
	 * @return true if user enter CARD or CASH
	 */
	private boolean getMethodPayment() {
		System.out.println("Enter the payment method (CARD or CASH):");
		this.payment_method = getUserInput();
		if(payment_method != null){
			payment_method.trim();
		}
		if ("CARD".equalsIgnoreCase(payment_method) || "CASH".equalsIgnoreCase(payment_method)) {
			return true;
		}
		return false;
	}

	/**
	 * get customer billing address
	 * @return true if valid billing address entered
	 */
	private boolean getBillingAddress() {
		boolean choice = getChoise(false);
		if (!choice) {
			System.out.println("Enter the billing address:");
			this.billing_addr = getUserInput();

			if (this.billing_addr != null)
				this.billing_addr.trim();

			while (this.billing_addr == null || "".equals(this.billing_addr)) {
				System.out.println("Invalid billing address...Can't be null");
				getBillingAddress();
			}
			return true;
		} else {
			String aquery = "SELECT ADDRESS FROM CUSTOMER WHERE NAME = '" + cname + "'";
			this.billing_addr = getCustomerDataFromDatabase(aquery, "address");//getAddress(aquery);
			return true;
		}
		// return false;
	}


	/**
	 * get staff id from the table
	 * @return true if staff id found
	 */
	private boolean getStaffnameAndID() {
		String bquery;
		// get billing Staff name
		System.out.println("Enter the billing Staff full name:");
		this.bname = getUserInput();
		//System.out.println(bname);

		// get billing staff id
		bquery = "SELECT ID FROM staff WHERE NAME = '" + bname + "'";
		this.bstaff_id = getID(bquery);
		if (this.bstaff_id > -1) {
			return true;
		}
		//System.out.println(bstaff_id);
		return false;
	}

	/**
	 * Get customer name and retrieve customer id from database
	 * @return true if customer exist
	 */
	private boolean getCustomerNameAndID() {
		String cquery;
		// get customer name
		System.out.println("Enter the customer full name:");
		this.cname = getUserInput();
		//System.out.println(cname);

		// get customer id
		cquery = "SELECT ID FROM CUSTOMER WHERE NAME = '" + cname + "'";
		this.customer_id = getID(cquery);
		if (this.customer_id > -1) {
			return true;
		}
		//System.out.println(customer_id);
		return false;

	}
  

	/**
	 * get user choice for billing address (use address from customer, from billing, or enter new one)
	 * @param isUpdate true if call come from updateBillingAccount()
	 * @return
	 */
	private boolean getChoise(boolean isUpdate) {
		// 1=same=true
		// 2=not the same=false
		sameBillingAddr = false;
		boolean ch = false;
		System.out.println("Enter 1 if the billing address is the same as the Customer address");
		System.out.println("Enter 2 if the billing address is not the same as Customer address");
		String c = input.nextLine();
		if (c != null) {
			c.trim();
		}
		if (c == null || "".equals(c)) {
			System.out.println("Try again.....");
			getChoise(isUpdate);
		} else {
			if (c.equals("1"))
				return true;
			else if (c.equals("2"))
				return false;
			else if (c.equals("3") && isUpdate) {
				sameBillingAddr = true;
				return false;
			} else {
				System.out.println("Try again.....");
				if (!isUpdate)
					getChoise(isUpdate);
				else {
					System.out.println("Enter 3 if the billing address is the same as the existing billing address");
					getChoise(isUpdate);
				}
			}

		}
		return ch;
	}

	/**
	 * update the given customer's billing account with new data
	 * @param customer_id2
	 * @param bstaff_id2
	 * @param billing_addr2
	 * @param payment_method2
	 * @param card_number2
	 * @return true if successful
	 */
	private boolean updateBilling(int customer_id2, int bstaff_id2, String billing_addr2, String payment_method2,
			String card_number2) {

//		Savepoint update = null;
//		try {
//			connection.setAutoCommit(false);
//			update = connection.setSavepoint("update");
//		} catch (SQLException e1) {
//			
//			e1.printStackTrace();
//		}
		
		Statement statement = null;
		int result = -1;

		String q1 = "UPDATE billing  SET "
				+ "billing_addr = '"+billing_addr2+"', "
						+ "payment_method = '"+payment_method2+"', "
								+ "card_number = '"+card_number2+"' "
										+ "WHERE customer_id = "+customer_id2;

		try {
			statement = this.connection.createStatement();
			result = statement.executeUpdate(q1);
			if (result > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
//			try {
//				connection.rollback(update);
//				connection.setAutoCommit(false);
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
		}
		return false;

	}

	/**
	 * reset billing fields
	 */
	private void resetField() {
		//this.id = -1;
		this.billing_addr = null;
		this.payment_method = null;
		this.card_number = null;
		this.customer_id = -1;
		this.bstaff_id = -1;
		this.cname = null;
		this.bname = null;
		this.sameBillingAddr = false;
		//this.samePaymentMethod = false;
		//this.sameCardNumber = false;

	}

	/**
	 * get the user input
	 * @return user input as string (trimmed)
	 */
	private String getUserInput() {
		String name1 = null;
		name1 = this.input.nextLine();
		if(name1 != null)
			name1.trim();
		return name1;
	}

	/**
	 * get user id from the database
	 * @param query
	 * @return user id as integer
	 */
	private int getID(String query) {
		Statement statement = null;
		ResultSet result = null;
		int id1 = -1;

		try {
			statement = this.connection.createStatement();
			result = statement.executeQuery(query);
			if (result.next()) {
				//System.out.println("resul has next");
				id1 = result.getInt("id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id1;
	}
  
//	/**
//	 * @param query
//	 * @return
//	 */
//	private String getAddress(String query) {
//		Statement statement = null;
//		ResultSet result = null;
//		String addr = null;
//
//		try {
//			statement = this.connection.createStatement();
//			result = statement.executeQuery(query);
//			if (result.next()) {
//				//System.out.println("resul has next");
//				addr = result.getString("address");
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//
//		return addr;
//	}

	/**
	 * get the customer data from the billing table
	 * @param query
	 * @param column
	 * @return user data as string
	 */
	private String getCustomerDataFromDatabase(String query, String column) {
		Statement statement = null;
		ResultSet result = null;
		String addr = null;

		try {
			statement = this.connection.createStatement();
			result = statement.executeQuery(query);
			if (result.next()) {
				//System.out.println("resul has next");
				addr = result.getString(column);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return addr;
	}

	/**
	 * insert the given billing information into the database
	 * @param customer_id
	 * @param bstaff_id
	 * @param billing_addr
	 * @param payment_method
	 * @param card_number
	 * @return true if insertion was successful 
	 */
	private boolean insertBilling(int customer_id, int bstaff_id, String billing_addr, String payment_method,
			String card_number) {

//		Savepoint insert = null;
//		try {
//			connection.setAutoCommit(false);
//			insert = connection.setSavepoint("insert");
//		} catch (SQLException e1) {
//			
//			e1.printStackTrace();
//		}
		
		Statement statement = null;
		int result = -1;
		
	  String q = "INSERT INTO billing(id, billing_addr, payment_method, card_number, customer_id, bstaff_id) "
	  		+ "VALUES (billing_seq.nextval, "
	  		+ "'"+billing_addr+"', "
	  		+ "'"+payment_method+"', "
	  		+ "'"+card_number+"', "
	  		+ ""+customer_id+", "
	  		+ ""+bstaff_id+")";
	  
		try {
			statement = this.connection.createStatement();
			result = statement.executeUpdate(q);
			if (result > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {

//			try {
//			connection.rollback(insert);
//			connection.setAutoCommit(false);
//		} catch (SQLException e1) {
//			e1.printStackTrace();
//		}
//		e.printStackTrace();
			e.printStackTrace();
		}
		return false;

	}
	
	public void printBillingTable(){
		int id_length = 3;
		int billing_addr_length = 50;
		int payment_method_length = 15;
		int card_number_length = 16;
		int customer_id_length = 12;
		int bstaff_id_length = 10;
		int total_length = id_length + billing_addr_length + payment_method_length+
				card_number_length + customer_id_length+bstaff_id_length;

		int id = -1;
		String billing_addr;
		String payment_method;
		String card_number;
		int customer_id = -1;
		int bstaff_id = -1;
		
		Statement statement = null;
		ResultSet result = null;
		billing_str = new StringBuilder();
		String  query = "SELECT * FROM billing";
		
		try {
			statement = this.connection.createStatement();
			result = statement.executeQuery(query);
			
			//add table header
			addHeaderInfo(id_length, billing_addr_length, payment_method_length, card_number_length, customer_id_length,
					bstaff_id_length);
			while((total_length--)>0)
				billing_str.append('-');
			billing_str.append("\n");
			
			
			while (result.next()) {
				id = result.getInt("id");
				billing_addr = result.getString("billing_addr");
				payment_method = result.getString("payment_method");
				card_number = result.getString("card_number");
				customer_id = result.getInt("customer_id");
				bstaff_id = result.getInt("bstaff_id");
				
				billing_str.append("|");
				billing_str.append(id);
				padWithSpaceInt(id, id_length);
				billing_str.append("|");
				billing_str.append(billing_addr);
				padWithSpaceString(billing_addr, billing_addr_length);
				billing_str.append("|");
				billing_str.append(payment_method);
				padWithSpaceString(payment_method, payment_method_length);
				billing_str.append("|");
				billing_str.append(card_number);
				padWithSpaceString(card_number, card_number_length);
				billing_str.append("|");
				billing_str.append(customer_id);
				padWithSpaceInt(customer_id, customer_id_length);
				billing_str.append("|");
				billing_str.append(bstaff_id);
				padWithSpaceInt(bstaff_id, bstaff_id_length);
				billing_str.append("|");
				billing_str.append("\n");

			}
			System.out.println(billing_str.toString());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * add table header
	 * @param id_length
	 * @param billing_addr_length
	 * @param payment_method_length
	 * @param card_number_length
	 * @param customer_id_length
	 * @param bstaff_id_length
	 */
	private void addHeaderInfo(int id_length, int billing_addr_length, int payment_method_length,
			int card_number_length, int customer_id_length, int bstaff_id_length) {
		billing_str.append("|");
		billing_str.append("id");
		padWithSpaceString("id", id_length);
		billing_str.append("|");
		billing_str.append("billing_addr");
		padWithSpaceString("billing_addr", billing_addr_length);
		billing_str.append("|");
		billing_str.append("payment_method");
		padWithSpaceString("payment_method", payment_method_length);
		billing_str.append("|");
		billing_str.append("card_number");
		padWithSpaceString("card_number", card_number_length);
		billing_str.append("|");
		billing_str.append("customer_id");
		padWithSpaceString("customer_id", customer_id_length);
		billing_str.append("|");
		billing_str.append("bstaff_id");
		padWithSpaceString("bstaff_id", bstaff_id_length);
		billing_str.append("|");
		billing_str.append("\n");
	}

	private void padWithSpaceString(String string, int length) {
		if(string!=null){
			int l = length - string.length();
			for(int i = 1; i<=l; i++)
				billing_str.append(" ");
		}else{
			for(int i = 1; i<=length; i++)
				billing_str.append(" ");
		}

	}

	private void padWithSpaceInt(int idc, int id_length) {
		int pad = 0;
		if(idc<10){
			pad = id_length - 1;
		}
		else if(idc<100){
			pad = id_length - 2;
		}
		else if(idc<1000){
			pad = id_length - 3;
		}
		
		for(int i = 1; i<=pad; i++)
			billing_str.append(" ");
		
	}
  

}
