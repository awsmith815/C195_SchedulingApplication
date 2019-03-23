/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

/**
 *
 * @author austin.smith
 */
public class SQL_Customer {
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
    
    //Calculating IDs for Customer, Address, City, country
    public static int createAddressID(String address1, String address2, String postalCode, String phone, int cityID){
        try(Connection con = DriverManager.getConnection(url,usr,pswrd);
                Statement myStmt = con.createStatement()){
            ResultSet checkAddressID = myStmt.executeQuery("select addressID from address where address ='"+address1+"' and address2='"+address2+"' and cityId="+cityID+" and postalCode='"+postalCode+"' and phone='"+phone+"'");
            if(checkAddressID.next()){
                int addressID=checkAddressID.getInt(1);
                checkAddressID.close();
                return addressID;
            }else{
                checkAddressID.close();
                int addressID;
                ResultSet findAddressID = myStmt.executeQuery("select addressId from address order by addressId");
                if(findAddressID.next()){
                    addressID = findAddressID.getInt(1)+1;
                    findAddressID.close();
                }else{
                    findAddressID.close();
                    addressID = 1;
                }
                myStmt.executeUpdate("insert into address values("+addressID+",'"+address1+"','"+address2+"',"+cityID+",'"+postalCode+"','"+phone+"',current_date,"+"'"+createdBy+"',current_timestamp,'"+createdBy+"')");
                return addressID;
            }
            
        }catch(SQLException exc){
            exc.printStackTrace();
            return -1;
        }
    }
    
    public static int createCityID(String city, int countryID){
        try(Connection con = DriverManager.getConnection(url,usr,pswrd);
                Statement myStmt = con.createStatement()){
            ResultSet checkCityID = myStmt.executeQuery("select cityId from city where city = '"+city+"'and countryId = "+countryID);
            if(checkCityID.next()){
                int cityID=checkCityID.getInt(1);
                checkCityID.close();
                return cityID;
            }else{
                checkCityID.close();
                int cityID;
                ResultSet findCityID = myStmt.executeQuery("select cityId from city order by cityId");
                if(findCityID.next()){
                    cityID = findCityID.getInt(1)+1;
                    findCityID.close();
                }else{
                    findCityID.close();
                    cityID = 1;                    
                }
                myStmt.executeUpdate("insert into city values("+cityID+",'"+city+"',"+countryID+",current_date,"+"'"+createdBy+"',current_timestamp,'"+createdBy+"')");
                return cityID;
            }
            
        }catch(SQLException exc){
            exc.printStackTrace();
            return -1;
        }
    }
    
    public static int createCountryID(String country){
        try(Connection con = DriverManager.getConnection(url,usr,pswrd);
                Statement myStmt = con.createStatement()){
            ResultSet checkCountryID = myStmt.executeQuery("select countryId from country where country = '"+ country +"'");
            if(checkCountryID.next()){
                int countryID = checkCountryID.getInt(1);
                checkCountryID.close();
                return countryID;
            }else{
                checkCountryID.close();
                int countryID;
                ResultSet findCountryID = myStmt.executeQuery("select countryId from country order by countryId");
                if(findCountryID.next()){
                    countryID = findCountryID.getInt(1)+1;
                    findCountryID.close();
                }else{
                    findCountryID.close();
                    countryID = 1;
                }
                myStmt.executeUpdate("Insert into country values("+countryID+",'"+country+"',current_date,"+"'"+createdBy+"',current_timestamp,'"+createdBy+"')");
                return countryID;
            }
            
        }catch(SQLException exc){
            exc.printStackTrace();
            return -1;
        }
}
