/*
 * This class provides basic user interface for the hotel management system.
 * It displays a menu for the user to choose the operation they want. The 
 * actual operation performs is delegated to other class.
 * 
 * Creator - Tsu-Hsin Yeh
 * Modifer
 * Date: Nov.11 2016
 */
import java.util.*;

public class Main {
  
  private Scanner input = new Scanner(System.in);
  private boolean isFinish = false;
  
  private InformationProcessing info;
  private BillingAccounts billing;
  private Reports reports;
  private ServiceRecords service;
  private PrintTable printTable;
  
  // Constructor to
  public Main(){
    this.info = new InformationProcessing(JDBCConnector.getConnection(), input);
    this.billing = new BillingAccounts(JDBCConnector.getConnection(), input);
    this.reports = new Reports(JDBCConnector.getConnection(), input);
    this.service = new ServiceRecords(JDBCConnector.getConnection(), input);
    this.printTable = new PrintTable(JDBCConnector.getConnection(), input);
  }
  
  public void userInterface(){
    displayMenu();
    while(!isFinish){
      // display the operation list that the user can choose from
      topLevelOperationList();
    }
  }
  
  // T0: Redirect to Menu
  public void topLevelOperationList(){
    System.out.println("Choose one of the tasks and operations: ");
    System.out.println("1.Information processing");
    System.out.println("2.Maintaining Service availed records");
    System.out.println("3.Maintaining Billing accounts");
    System.out.println("4.Reports");
    System.out.println("5.Print table");
    System.out.println("6.Quit database");
    promptMessage();
    int option = input.nextLine().toUpperCase().charAt(0);
    switch(option){
      case '1': informationMenu();
        break;
      case '2': serviceMenu();
        break;
      case '3': billingMenu();
        break;
      case '4': reportMenu();
        break;
      case '5': printTableMenu();
        break;
      case '6':
        isFinish = true;
        break;
      default: System.out.println("Invalid input.");
    }
  }
  
  // T0.1: Print the table content
  public void printTableMenu(){
    System.out.println("Choose one of the table to print: ");
    System.out.println("1.Hotel");
    System.out.println("2.Customer");
    System.out.println("3.Staff");
    System.out.println("4.Room Type");
    System.out.println("5.Room");
    System.out.println("6.Check In");
    System.out.println("7.Service Record");
    System.out.println("8.Billing Account");
    System.out.println("9.Reserve For");
    System.out.println("10.Back");
    promptMessage();
    int option = Integer.parseInt(input.nextLine());
    switch(option){
      case 1: printTable.printHotel();
        break;
      case 2: printTable.printCustomer();
        break;
      case 3: printTable.printStaff();
        break;
      case 4: printTable.printRoomType();
        break;
      case 5: printTable.printRoom();
        break;
      case 6: printTable.printCheckin();
        break;
      case 7: printTable.printServiceRecord();
        break;
      case 8: printTable.printBillingAccount();
        break;
      case 9: printTable.printReserveFor();
        break;
      case 10:
        break;
      default: System.out.println("Invalid input.");
    }
  }
  
  // T1: Information processing
  public void informationMenu(){
    System.out.println("Choose one of the domain:");
    System.out.println("1.Staff");
    System.out.println("2.Customer");
    System.out.println("3.Room");
    System.out.println("4.Back");
    promptMessage();
    int option = input.nextLine().toUpperCase().charAt(0);
    switch(option){
      case '1': staffMenu();
        break;
      case '2': customerMenu();
        break;
      case '3': roomMenu();
        break;
      case '4':
        break;
      default: System.out.println("Invalid input.");
    }
  }
  
  // T1.1: Staff information processing
  public void staffMenu(){
    System.out.println("Choose one of the operation:");
    System.out.println("1.Create new staff");
    System.out.println("2.Update existing staff");
    System.out.println("3.Delete existing staff");
    System.out.println("4.Back");
    promptMessage();
    int option = input.nextLine().toUpperCase().charAt(0);
    switch(option){
      case '1': info.newStaff();
        break;
      case '2': info.updateStaff();
        break;
      case '3': info.deleteStaff();
        break;
      case '4':
        break;
      default: System.out.println("Invalid input.");
    }
  }
  
  // T1.2: Customer information processing
  public void customerMenu(){
    System.out.println("Choose one of the operation:");
    System.out.println("1.Create new customer");
    System.out.println("2.Update existing customer");
    System.out.println("3.Delete existing customer");
    System.out.println("4.Back");
    promptMessage();
    int option = input.nextLine().toUpperCase().charAt(0);
    switch(option){
      case '1': info.newCustomer();
        break;
      case '2': info.updateCustomer();
        break;
      case '3': info.deleteCustomer();
        break;
      case '4':
        break;
      default: System.out.println("Invalid input.");
    }
  }
  
  // T1.3: Room information processing
  public void roomMenu(){
    System.out.println("Choose one of the operation:");
    System.out.println("1.Create new room");
    System.out.println("2.Update existing room");
    System.out.println("3.Delete existing room");
    System.out.println("4.Check available room");
    System.out.println("5.Assign room to customer");
    System.out.println("6.Release room for customer");
    System.out.println("7.Back");
    promptMessage();
    int option = input.nextLine().toUpperCase().charAt(0);
    switch(option){
      case '1': info.newRoom();
        break;
      case '2': info.updateRoom();
        break;
      case '3': info.deleteRoom();
        break;
      case '4': info.checkAvailability();
        break;
      case '5': info.assignRoom();
        break;
      case '6': info.releaseRoom();
        break;
      case '7':
        break;
      default: System.out.println("Invalid input.");
    }
  }
  
  // T2: Maintaining service availed records
  public void serviceMenu(){
    System.out.println("Choose one of the operation:");
    System.out.println("1.Enter phone bill");
    System.out.println("2.Update phone bill");
    System.out.println("3.Enter laundry bill");
    System.out.println("4.Update laundry bill");
    System.out.println("5.Enter restaurant bill");
    System.out.println("6.Update restaurant bill");
    System.out.println("7.Back");
    promptMessage();
    int option = input.nextLine().toUpperCase().charAt(0);
    switch(option){
      case '1': service.newPhoneBill();
        break;
      case '2': service.updatePhoneBill();
        break;
      case '3': service.newLaundryBill();
        break;
      case '4': service.updateLaundryBill();
        break;
      case '5': service.newRestaurantBill();
        break;
      case '6': service.updateRestaurantBill();
        break;
      case '7':
        break;
      default: System.out.println("Invalid input.");
    }
  }
  
  
  
  // T3: Maintaining billing accounts
  public void billingMenu(){
    System.out.println("Choose one of the operation:");
    System.out.println("1.Generate billing account");
    System.out.println("2.Update billing account");
    System.out.println("3.Back");
    promptMessage();
    int option = input.nextLine().toUpperCase().charAt(0);
    switch(option){
      case '1': billing.generateBillingAccount();
        break;
      case '2': billing.updateBillingAccount();
        break;
      case '3':
        break;
      default: System.out.println("Invalid input.");
    }
  }
  
  // T4: Reports
  public void reportMenu(){
    System.out.println("Choose one of the operation:");
    System.out.println("1.Report occupancy by \"Room type\"");
    System.out.println("2.Report occupancy by \"Date range\"");
    System.out.println("3.Report occupancy by \"Hotel\"");
    System.out.println("4.Report occupants");
    System.out.println("5.Report % room occupied");
    System.out.println("6.Report staff group by \"Role\"");
    System.out.println("7.Report customers given \"Catering staff\"");
    System.out.println("8.Report customers given \"Service staff\"");
    System.out.println("9.Back");
    promptMessage();
    int option = input.nextLine().toUpperCase().charAt(0);
    switch(option){
      case '1': reports.getOccupancyByRoomType();
        break;
      case '2': reports.getOccupancyByDate();
        break;
      case '3': reports.getOccupancyByHotel();
        break;
      case '4': reports.getOccupants();
        break;
      case '5': reports.getPercentRoomOccupied();
        break;
      case '6': reports.getStaffGroupByRole();
        break;
      case '7': reports.getCustomerByCatering();
        break;
      case '8': reports.getCustomerByService();
        break;
      case '9':
        break;
      default: System.out.println("Invalid input.");
    }
  }
  
  public void displayMenu(){
    System.out.println("Welcome to WolfVillas Hotel Database Management System.");
  }
  
  public void promptMessage(){
    System.out.print(":");
  }
  
  // The entry point for the program
  public static void main(String []args){
    new Main().userInterface();
  }
  
}