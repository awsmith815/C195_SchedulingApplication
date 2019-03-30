/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Util.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.collections.ObservableList;


/**
 *
 * @author austin.smith
 */
public class SQL_Customer {
    
    //private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static String currentUser = "Test";
    
    //Add Customer
    public static int addCustomer(String customerName, String address1, String address2, String city, String country, String postalCode, String phone){
        try{
            int countryID = addCountryID(country);
            int cityID = addCityID(city, countryID);
            int addressID = addAddressID(address1, address2, cityID, postalCode, phone);
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select customerId from customer where customerName='"+customerName+"' and addressId="+addressID);
            if(rs.next()){
                int customerID = rs.getInt(1);
                rs.close();     
                return customerID;
            }else{
                rs.close();
                int customerID;
                ResultSet newCustomerID = stmt.executeQuery("select max(customerID) from customer");
                if(newCustomerID.next()){
                    customerID = newCustomerID.getInt(1)+1;
                    newCustomerID.close();                  
                }else{
                    newCustomerID.close();
                    customerID = 1;
                }
                int active = 1;
                stmt.executeUpdate("Insert into customer values("+customerID+",'"+customerName+"',"+addressID+","+active+",current_date,'"+currentUser+"',current_timestamp,'"+currentUser+"')");
                return customerID;
            }
            
            
        }catch(SQLException exc){
            System.out.println("SQLExceptions: "+exc.getMessage());
            return -1;
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
   
    public static void updateAllCustomers(){
        try{
            Statement stmt = Database.getConnection().createStatement();
            ObservableList<Customer> allCustomers = CustomerList.getAllCustomers();
            allCustomers.clear();
            ResultSet activeCustomerID = stmt.executeQuery("select customerId from customer where active=1");
            ArrayList<Integer> customerIDlist = new ArrayList<>();
            while(activeCustomerID.next()){
                customerIDlist.add(activeCustomerID.getInt(1));
            }
            for(int customerID : customerIDlist){
                Customer customer = new Customer();
                ResultSet customerData = stmt.executeQuery("select customerName, active, addressId from customer where customerId="+customerID);                
                customerData.next();
                String customerName = customerData.getString("customerName");
                int active = customerData.getInt("active");
                int addressID = customerData.getInt("addressId");
                customer.setCustomerID(customerID);
                customer.setCustomerName(customerName);
                customer.setActive(active);
                customer.setAddressID(addressID);
                ResultSet addressData = stmt.executeQuery("select address, address2, postalCode, phone, cityId from address where addressId="+addressID);
                addressData.next();
                String address1 = addressData.getString("address");
                String address2 = addressData.getString("address2");
                String postalCode = addressData.getString("postalCode");
                String phone = addressData.getString("phone");
                int cityID = addressData.getInt("cityId");
                customer.setAddress1(address1);
                customer.setAddress2(address2);
                customer.setPostalCode(postalCode);
                customer.setPhone(phone);
                customer.setCityID(cityID);
                ResultSet cityData = stmt.executeQuery("select city, countryId from city where cityId="+cityID);
                cityData.next();
                String city = cityData.getString("city");
                int countryID = cityData.getInt("countryId");
                customer.setCity(city);
                customer.setCountryID(countryID);
                ResultSet countryData = stmt.executeQuery("select country from country where countryId="+countryID);
                countryData.next();
                String country = countryData.getString("country");
                customer.setCountry(country);
                allCustomers.add(customer);
                
            }
        }catch(SQLException exc){
            System.out.println("SQLException: "+exc.getMessage());
            System.out.println("Error: "+exc.getLocalizedMessage());
        }
    }

    //Modify Customer
    public static boolean modifyCustomer(int customerID,String customerName, String address1, String address2, String city, String country, String postalCode, String phone){
        try{
            int countryID = addCountryID(country);
            int cityID = addCityID(city, countryID);
            int addressID = addAddressID(address1, address2, cityID, postalCode, phone);
            Statement stmt = Database.getConnection().createStatement();
            updateCustomerTable(customerID, customerName, addressID);
            return true;
        }catch(SQLException exc){
            System.out.println("SQLException: "+exc.getMessage());            
        }
        return false;
    }
    
    private static void updateCustomerTable(int customerID, String customerName, int addressID){
        try{            
            Statement stmt = Database.getConnection().createStatement();                        
            stmt.executeUpdate("update customer set customerName='"+customerName+"',addressId="+addressID+", lastUpdate= current_timestamp, "
                        + "lastUpdateBy='"+currentUser+"' where customerId="+customerID);
        }catch(SQLException exc){
            exc.printStackTrace();
        }
    }
    
    //Set customer to inactive
    public static void setCustomerInactive(Customer customer){
        int customerID = customer.getCustomerID();
        try{
            Statement stmt = Database.getConnection().createStatement();
            stmt.executeUpdate("update customer set active=0 where customerId="+customerID);
        }catch(SQLException exc){
            System.out.println("SQLException: "+exc.getMessage());
        }
        updateAllCustomers();        
    }

    
    
}