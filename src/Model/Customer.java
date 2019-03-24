/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author austin.smith
 */
public class Customer {
    
    private int customerID, addressID, cityID, countryID, active;
    private String customerName, phone, address1, address2, postalCode, city, country;
    
    
    public Customer(){}
    /**
    public Customer(int customerID, String customerName, String address1, String postalCode, String city, String phone){
        this.customerID = customerID;
        this.customerName = customerName;
        this.address1=address1;
        this.postalCode=postalCode;
        this.city=city;
        this.phone=phone;
    }
    */
    //Getters
    public int getCustomerID(){
        return customerID;
    }
    public int getAddressID(){
        return addressID;
    }
    public int getCityID(){
        return cityID;
    }
    public int getCountryID(){
        return countryID;
    }
    public int getActive(){
        return active;
    }
    public String getCustomerName(){
        return customerName;
    }
    public String getPhone(){
        return phone;
    }
    public String getAddress1(){
        return address1;
    }
    public String getAddress2(){
        return address2;
    }
    public String getPostalCode(){
        return postalCode;
    }
    public String getCity(){
        return city;
    }
    public String getCountry(){
        return country;
    }
    
    //Setters
    public void setCustomerID(int customerID){
        this.customerID=customerID;
    }
    public void setAddressID(int addressID){
        this.addressID=addressID;
    }
    public void setCityID(int cityID){
        this.cityID=cityID;
    }
    public void setCountryID(int countryID){
        this.countryID=countryID;
    }
    public void setActive(int active){
        this.active=active;
    }
    public void setCustomerName(String customerName){
        this.customerName=customerName;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public void setAddress1(String address1){
        this.address1=address1;
    }
    public void setAddress2(String address2){
        this.address2=address2;
    }
    public void setPostalCode(String postalCode){
        this.postalCode=postalCode;
    }
    public void setCity(String city){
        this.city=city;
    }
    public void setCountry(String country){
        this.country=country;
    }
    
    public static String customerValidation(String customerName, String address1, String address2, String postalCode, String city, String country, String phone, String errorMessage){
        if(customerName.length() == 0){
            errorMessage = errorMessage + "Customer Name cannot be empty\n";
        }
        return errorMessage;
    }
    
}
