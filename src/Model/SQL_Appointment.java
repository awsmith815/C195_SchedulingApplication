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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import javafx.collections.ObservableList;

/**
 *
 * @author austin.smith
 */
public class SQL_Appointment {
    
    private static String currentUser = "Test";
    
    public static void addAppointment(int customerID, String title, String description, String location, String contact, String type, String url, Timestamp start, Timestamp end){
        try{
            int appointmentID;
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select max(appointmentId) from appointment");            
            if(rs.next()){
                appointmentID = rs.getInt(1)+1;
                rs.close();
            }else{
                rs.close();
                appointmentID=1;
            }
            stmt.executeUpdate("insert into appointment values("+appointmentID+","+customerID+",'"+title+"','"+description+"','"+location+"','"+contact+"','"+type+"','"+url+"','"+start+"','"+end+"',"
                    + "current_timestamp,'"+currentUser+"',current_timestamp,'"+currentUser+"')");
        }catch(SQLException exc){
            exc.printStackTrace();
        }        
    }
    public static boolean verifyAddAppointment(Timestamp start, Timestamp end){
        return false;
    }
    
    public static boolean addVerifyNewAppointment(Customer customer, String title, String description, String location, String contact, String type, String url,ZonedDateTime start, ZonedDateTime end){
        LocalDateTime startWOTZ = start.toLocalDateTime();
        LocalDateTime endWOTZ = end.toLocalDateTime();
        Timestamp startTimestamp = Timestamp.valueOf(startWOTZ);
        Timestamp endTimestamp = Timestamp.valueOf(endWOTZ);
        //Add functionality to make sure no appointments overlap in DB
        if(verifyAddAppointment(startTimestamp, endTimestamp)==true){
            return false;
        }else{
            int customerID = customer.getCustomerID();
            addAppointment(customerID, title, description, location, contact, type, url, startTimestamp, endTimestamp);
            return true;
        }
    }
    
    public static void updateAllAppointments(){
        try{
            ObservableList<Appointment> allAppointments = AppointmentList.getAllAppointments();            
            ArrayList<Integer> currentAppointmentIDs = new ArrayList<>();
            Statement stmt = Database.getConnection().createStatement();
            allAppointments.clear();
            ResultSet rs = stmt.executeQuery("select appointmentId from appointment where start >= current_timestamp");
            while(rs.next()){
                currentAppointmentIDs.add(rs.getInt(1));
            }
            for(int appointmentID:currentAppointmentIDs){
                rs = stmt.executeQuery("select * from appointment where appointmentId="+appointmentID);
                rs.next();
                Appointment appt = new Appointment();
                int customerID = rs.getInt("appointmentId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String contact = rs.getString("contact");
                String type = rs.getString("type");
                String url = rs.getString("url");                
                Timestamp start = rs.getTimestamp("start");
                Timestamp end = rs.getTimestamp("end");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd h:mm a");
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date startFormat = dateFormat.parse(start.toString());
                Date endFormat = dateFormat.parse(end.toString());
                appt.setAppointmentID(appointmentID);
                appt.setCustomerID(customerID);
                appt.setTitle(title);
                appt.setDescription(description);
                appt.setLocation(location);
                appt.setContact(contact);
                appt.setType(type);
                appt.setUrl(url);
                appt.setStart(start);
                appt.setEnd(end);
                appt.setStartDate(startFormat);
                appt.setEndDate(endFormat);
                allAppointments.add(appt);                
            }           
        }catch(Exception exc){
            System.out.println("SQLException: "+exc.getMessage());
        }
    }
    
}
