/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author austin.smith
 */
public class ReportCustomer {
    
    private String customerName, title, location, contact, apptTime;
    private LocalDate apptDate;
   
    
    public ReportCustomer(){
        
    }
    
    public String getCustomerName(){
        return customerName;
    }
    public String getTitle(){
        return title;
    }
    public String getLocation(){
        return location;
    }
    public String getContact(){
        return contact;
    }
    public LocalDate getApptDate(){
        return apptDate;
    }
    public String getApptTime(){
        return apptTime;
    }
    public void setCustomerName(String customerName){
        this.customerName=customerName;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public void setLocation(String location){
        this.location=location;
    }
    public void setContact(String contact){
        this.contact=contact;
    }
    public void setApptDate(LocalDate apptDate){
        this.apptDate=apptDate;
    }
    public void setApptTime(String apptTime){
        this.apptTime=apptTime;
    }
    
    
}
