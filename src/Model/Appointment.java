
package Model;

import java.sql.Timestamp;
import java.util.Date;
/**
 *
 * @author austin.smith
 * 
 * Depreciate out "type" on April 20, 2019 per Wanda Burwick's instruction
 * Will be replaced by "description"
 */
public class Appointment {
    
    private int appointmentID, customerID;
    private String title, description, location, contact, url, createdBy;
    private Timestamp start, end, lastUpdated;
    private Date startDate, endDate, createdDate;
    
    public Appointment(){
        
    }
    //Getters
    public int getAppointmentID(){
        return appointmentID;
    }
    public int getCustomerID(){
        return customerID;
    }
    public String getTitle(){
        return title;
    }            
    public String getDescription(){
        return description;
    }
    public String getLocation(){
        return location;
    }
    public String getContact(){
        return contact;
    }
    public String getUrl(){
        return url;
    }
    public String getCreatedBy(){
        return createdBy;
    }
    public Timestamp getStart(){
        return start;
    }
    public Timestamp getEnd(){
        return end;
    }
    public Timestamp getLastUpdated(){
        return lastUpdated;
    }
    public Date getStartDate(){
        return startDate;
    }
    public Date getEndDate(){
        return endDate;
    }
    public Date getCreatedDate(){
        return createdDate;
    }
    //Setters
    public void setAppointmentID(int appointmentID){
        this.appointmentID=appointmentID;
    }
    public void setCustomerID(int customerID){
        this.customerID=customerID;
    }
    public void setTitle(String title){
        this.title=title;
    }            
    public void setDescription(String description){
        this.description=description;
    }
    public void setLocation(String location){
        this.location=location;
    }
    public void setContact(String contact){
        this.contact=contact;
    }
    public void setUrl(String url){
        this.url=url;
    }
    public void setCreatedBy(String createdBy){
        this.createdBy=createdBy;
    }
    public void setStart(Timestamp start){
        this.start=start;
    }
    public void setEnd(Timestamp end){
        this.end=end;
    }
    public void setLastUpdated(Timestamp lastUpdated){
        this.lastUpdated=lastUpdated;
    }
    public void setStartDate(Date startDate){
        this.startDate=startDate;
    }
    public void setEndDate(Date endDate){
        this.endDate=endDate;
    }
    public void setCreatedDate(Date createdDate){
        this.createdDate=createdDate;
    }
    
    public static String appointmentValidation(String title, String errorMessage){
        return errorMessage;
    }
}
