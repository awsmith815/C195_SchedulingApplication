/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Util.Database;
import Model.SQL_User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/**
 *
 * @author austin.smith
 */
public class SQL_Customer {
    
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static String currentUser = "Test";
    
    //Get a single customer in the database
    public static Customer getCustomer(int customerID){
        try{
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from customer where customerId="+customerID);
            if(rs.next()){
                Customer customerName = new Customer();
                customerName.setCustomerName(rs.getString("customerName"));
                stmt.close();
                return customerName;
            }
        }catch(SQLException exc){
            System.out.println("SQLExceptions: "+exc.getMessage());
        }
        return null;
    }
    /**
    public static ObservableList<Customer> getAllCustomers(){
        allCustomers.clear();
        try{
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select c.customerId, c.customerName, a.address, a.phone, a.postalCode, ci.city "
                    + "from customer c inner join address a on a.addressId = c.addressId inner join city ci on ci.cityId = a.cityId");
            while(rs.next()){
                //public Customer(int customerID, String customerName, String address1, String postalCode, String city, String phone){
                Customer customer = new Customer(
                        rs.getInt("customerId"),
                        rs.getString("customerName"),
                        rs.getString("address"),
                        rs.getString("postalCode"),
                        rs.getString("city"),
                        rs.getString("phone"));
                allCustomers.add(customer);
            }
            stmt.close();
            return allCustomers;
        }catch(SQLException exc){
            System.out.println("SQLExceptions: "+exc.getMessage());
            return null;
        }
    }
    */
    
    public static void addCustomer(String customerName, String address1, String address2, String city, String country, String postalCode, String phone){
        try{
            int countryID = addCountryID(country);
            int cityID = addCityID(city, countryID);
            int addressID = addAddressID(address1, address2, cityID, postalCode, phone);
            Statement stmt = Database.getConnection().createStatement();
            
            
        }catch(SQLException exc){
            System.out.println("SQLExceptions: "+exc.getMessage());
        }
    }
    
    public static int addCountryID(String country){
        try{
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select countryId from country where country='"+country+"'");
            if(rs.next()){
                int countryID = rs.getInt(1);
                rs.close();
                return countryID;
            }else{
                rs.close();
                int countryID;
                ResultSet newCountryID = stmt.executeQuery("select max(countryId) from country");
                if(newCountryID.next()){
                    countryID = newCountryID.getInt(1)+1;
                    newCountryID.close();
                }else{
                    newCountryID.close();
                    countryID = 1;
                }
                stmt.executeUpdate("Insert into country values("+countryID+",'"+country+"',current_date,'"+currentUser+"',current_timestamp,'"+currentUser+"')");
                return countryID;
            }
            
        }catch(SQLException exc){
            System.out.println("SQLException: "+exc.getMessage());    
            return -1;
        }
    }
    
    public static int addCityID(String city, int countryID){
        try{
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select cityId from city where city='"+city+"' and countryId="+countryID);
            if(rs.next()){
                int cityID = rs.getInt(1);
                rs.close();
                return cityID;
            }else{
                rs.close();
                int cityID;
                ResultSet newCityID = stmt.executeQuery("select max(cityId) from city");
                if(newCityID.next()){
                    cityID = newCityID.getInt(1)+1;
                    newCityID.close();
                }else{
                    newCityID.close();
                    cityID = 1;
                }
                stmt.executeUpdate("insert into city values("+cityID+",'"+city+"',"+countryID+",current_date,'"+currentUser+"',current_timestamp,'"+currentUser+"')");
                return cityID;
            }
        }catch(SQLException exc){
            System.out.println("SQLException: "+exc.getMessage());
            return -1;
        }
    }
    
    public static int addAddressID(String address1, String address2, int cityID, String postalCode, String phone){
        try{
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select addressId from address where address='"+address1+"' and address2='"+address2+"' and cityID="+cityID+" and postalCode='"+postalCode+"' and phone='"+phone+"'");
            if(rs.next()){
                int addressID = rs.getInt(1);
                rs.close();
                return addressID;
            }else{
                rs.close();
                int addressID;
                ResultSet newAddressID = stmt.executeQuery("select max(addressId) from address");
                if(newAddressID.next()){
                    addressID = newAddressID.getInt(1)+1;
                    newAddressID.close();
                }else{
                    newAddressID.close();
                    addressID = 1;
                }
                stmt.executeUpdate("insert into address values("+addressID+",'"+address1+"','"+address2+"',"+cityID+",'"+postalCode+"','"+phone+"',current_date,'"+currentUser+"',current_timestamp,'"+currentUser+"')");
                return addressID;
            }
        }catch(SQLException exc){
            System.out.println("SQLException: "+exc.getMessage());
            return -1;
        }
        
    }
    public static int newCustomerID(String customerName, int addressID){
        try{
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select customerId from customer where customerName='"+customerName+"' and addressId="+addressID);
            if(rs.next()){
                int customerID = rs.getInt(1);
                rs.close();
                return customerID;
            }else{
                
            }
        }catch(SQLException exc){
            System.out.println("SQLException: "+exc.getMessage());
            return -1;
    }
    
    /**
   
    public static void addCustomer(String customerName, String address1, String address2, String city, String country, String postalCode, String phone){
        try{
            int countryID = createCountryID(country);
            int cityID = createCityID(city, countryID);
            int addressID = createAddressID(address1, address2, postalCode, phone, cityID);
            if(existingCustomer(customerName, addressID)){
                try(Connection con = DriverManager.getConnection(url,usr,pswrd);
                        Statement myStmt = con.createStatement()){
                    ResultSet activeResult = myStmt.executeQuery("select active from customer where customerName ='"+customerName+"' and addressId="+addressID);
                    activeResult.next();
                    int active = activeResult.getInt(1);
                    if(active==1){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.initModality(Modality.NONE);
                        alert.setTitle("Customer Error");
                        alert.setHeaderText("Customer Error");
                        alert.setContentText("This customer is already active.");
                        alert.showAndWait();
                    }else{
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.initModality(Modality.NONE);
                        alert.setTitle("Activate Customer");
                        alert.setHeaderText("Activate Customer");
                        alert.setContentText("Do you wish to activate this customer?");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            myStmt.executeUpdate("update customer set active = 1, lastUpdate = current_timestamp, lastUpdateBy='"+createdBy+"' where customerName='"+customerName+"' and addressId="+addressID);                            
                        }                        
                    }
                }
            }else{
                newCustomerAdd(customerName, addressID);
            }
        }catch(SQLException exc){
            exc.printStackTrace();
        }
    }
    
    private static boolean existingCustomer(String customerName, int addressID) throws SQLException{
        try(Connection con = DriverManager.getConnection(url,usr,pswrd);
                    Statement myStmt = con.createStatement()){
                ResultSet checkCustomerID = myStmt.executeQuery("select customerId from customer where customerName='"+customerName+"' and addressId="+addressID);
                if(checkCustomerID.next()){
                    checkCustomerID.close();
                    return true;                    
                }else{
                    checkCustomerID.close();
                    return false;
                }
            }
    }
    
    private static void newCustomerAdd(String customerName, int addressID) throws SQLException{
        try(Connection con = DriverManager.getConnection(url,usr,pswrd);
                Statement myStmt = con.createStatement()){
            ResultSet checkCustomerID = myStmt.executeQuery("select customerId from customer order by customerId");
            int customerID;
            if(checkCustomerID.next()){
                customerID = checkCustomerID.getInt(1)+1;
                checkCustomerID.close();
            }else{
                checkCustomerID.close();
                customerID = 1;
            }
            myStmt.executeUpdate("insert into customer values("+customerID+",'"+customerName+"',"+addressID+",1,current_date,'"+createdBy+"',current_timestamp,'"+createdBy+"')");
        }
    }
    
        */ 
}
