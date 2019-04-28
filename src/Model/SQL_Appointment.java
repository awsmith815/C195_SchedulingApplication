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
    //Link back to user login
    private static String currentUser = "Test";
    
    public static void addAppointment(int customerID, String title, String description, String location, String contact, String url, Timestamp start, Timestamp end){
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
            stmt.executeUpdate("insert into appointment values("+appointmentID+","+customerID+",'"+title+"','"+description+"','"+location+"','"+contact+"','"+url+"','"+start+"','"+end+"',"
                    + "current_timestamp,'"+currentUser+"',current_timestamp,'"+currentUser+"')");
        }catch(SQLException exc){            
            exc.printStackTrace();
            
        }        
    }
    public static boolean verifyAddAppointment(Timestamp start, Timestamp end){
        return false;
    }
    
    public static boolean addVerifyNewAppointment(Customer customer, String title, String description, String location, String contact, String url,ZonedDateTime start, ZonedDateTime end){
        LocalDateTime startWOTZ = start.toLocalDateTime();
        LocalDateTime endWOTZ = end.toLocalDateTime();
        Timestamp startTimestamp = Timestamp.valueOf(startWOTZ);
        Timestamp endTimestamp = Timestamp.valueOf(endWOTZ);
        //Add functionality to make sure no appointments overlap in DB
        if(verifyAddAppointment(startTimestamp, endTimestamp)==true){
            return false;
        }else{
            int customerID = customer.getCustomerID();
            addAppointment(customerID, title, description, location, contact, url, startTimestamp, endTimestamp);
            return true;
        }
    }
    
    public static boolean modifyAppointment(int appointmentID, Customer customer, String title, String description, String location, String contact, String url,ZonedDateTime start, ZonedDateTime end){
        LocalDateTime startWOTZ = start.toLocalDateTime();
        LocalDateTime endWOTZ = end.toLocalDateTime();
        Timestamp startTimestamp = Timestamp.valueOf(startWOTZ);
        Timestamp endTimestamp = Timestamp.valueOf(endWOTZ);
        //Add functionality to make sure no appointments overlap in DB
        if(verifyAddAppointment(startTimestamp, endTimestamp)==true){
            return false;
        }else{
            int customerID = customer.getCustomerID();
            updateAppointment(appointmentID, customerID, title, description, location, contact, url, startTimestamp, endTimestamp);
            return true;
        }
    }
    
    public static void updateAppointment(int appointmentID, int customerID, String title, String description, String location, String contact, String url, Timestamp start, Timestamp end){
        try{
            Statement stmt = Database.getConnection().createStatement();
            stmt.executeUpdate("update appointment set customerId="+customerID+",title='"+title+"',description='"+description+"',location='"+location+"',contact='"+contact+"',url='"+url+"',"
                    + "start='"+start+"',end='"+end+"',lastUpdate=current_timestamp, lastUpdateBy='"+currentUser+"' where appointmentId="+appointmentID);            
        }catch(SQLException exc){
            System.out.println("SQL Exception updating Appointment: "+ exc.getMessage());
        }
    }
    
    
    public static void deleteAppointment(Appointment appointment){
        int appointmentID = appointment.getAppointmentID();
        try{
            Statement stmt = Database.getConnection().createStatement();
            stmt.executeUpdate("delete from appointment where appointmentId="+appointmentID);
        }catch(SQLException exc){
            System.out.println("SQL Exception in Delete Appointment: "+ exc.getMessage());
        }
        updateAllAppointments();
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
                int customerID = rs.getInt("customerId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String contact = rs.getString("contact");                
                String url = rs.getString("url");                
                Timestamp start = rs.getTimestamp("start");
                //System.out.println("timestamp from SQL: "+start);
                Timestamp end = rs.getTimestamp("end");
                //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
                //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                //Date startFormat = dateFormat.parse(start.toString());
                //System.out.println("dateformat from SQL:"+startFormat);
                //Date endFormat = dateFormat.parse(end.toString());              
                Date startFormat = start;                              
                Date endFormat = end;
                appt.setAppointmentID(appointmentID);
                appt.setCustomerID(customerID);
                appt.setTitle(title);
                appt.setDescription(description);
                appt.setLocation(location);
                appt.setContact(contact);                
                appt.setUrl(url);
                appt.setStart(start);
                appt.setEnd(end);
                appt.setStartDate(startFormat);
                appt.setEndDate(endFormat);
                allAppointments.add(appt);                
            }           
        }catch(Exception exc){
            System.out.println("SQLException(UpdateAppointment): "+exc.getMessage());
        }
    }
    
    //Used to alert for appointments within 15 minutes
    public static int appointmentsAlert(){
        try{
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select count(distinct appointmentId) from appointment where `start` between current_timestamp() and date_add(current_timestamp(), INTERVAL 15 MINUTE)");
            while(rs.next()){
                int numAppts = rs.getInt(1);
                return numAppts;
            }
        }catch(SQLException exc){
            System.out.println("SQLException(Alert15MIN): "+exc.getMessage());
        }
        return 0;
    }
    
    
    public static void updateMonthlyView(String monthToSearch){
        try{
            ObservableList<Appointment> monthlyAppointments = AppointmentList.getMonthlyAppointments();            
            ArrayList<Integer> monthlyAppointmentIDs = new ArrayList<>();
            Statement stmt = Database.getConnection().createStatement();
            ResultSet allAppt = stmt.executeQuery("select appointmentId from appointment where monthname(start)='"+monthToSearch+"'");
            while(allAppt.next()){
                monthlyAppointmentIDs.add(allAppt.getInt(1));
            }
            for(int appointmentID:monthlyAppointmentIDs){                
                ResultSet rs = stmt.executeQuery("select start, title, description, contact from appointment where appointmentId="+appointmentID);
                rs.next();
                Appointment appt = new Appointment();
                Timestamp start = rs.getTimestamp(1);
                String title = rs.getString(2);
                String description = rs.getString(3);
                String contact = rs.getString(4);
                //appt.setMonthName(startMonth);
                appt.setStart(start);
                appt.setTitle(title);
                appt.setDescription(description);
                appt.setContact(contact);
                monthlyAppointments.add(appt);                
            }
        }catch(SQLException exc){
            System.out.println("SQLException(MonthlyView): "+exc.getMessage());
        }
    }
    public static void updateWeeklyView(int weeksToSearch){
        try{
            ObservableList<Appointment> weeklyAppointments = AppointmentList.getWeeklyAppointments();
            ArrayList<Integer> weeklyAppointmentIDs = new ArrayList<>();
            Statement stmt = Database.getConnection().createStatement();
            ResultSet allAppt = stmt.executeQuery("select appointmentId \n" +
                                                    "from appointment \n" +
                                                    "where year(start) = YEAR(date_add(curdate(), interval "+weeksToSearch+" WEEK)) and \n" +
                                                    "weekofyear(start) = weekofyear(date_add(curdate(),interval "+weeksToSearch+" WEEK));");
            while(allAppt.next()){
                weeklyAppointmentIDs.add(allAppt.getInt(1));
            }
            for(int appointmentID:weeklyAppointmentIDs){
                ResultSet rs = stmt.executeQuery("select dayname(start), start, title, description, contact from appointment where appointmentId="+appointmentID);
                rs.next();
                Appointment appt = new Appointment();
                String dayName = rs.getString(1);
                Timestamp start = rs.getTimestamp(2);
                String title = rs.getString(3);
                String description = rs.getString(4);
                String contact = rs.getString(5);
                appt.setDayNameStart(dayName);
                appt.setStart(start);
                appt.setTitle(title);
                appt.setDescription(description);
                appt.setContact(contact);
                weeklyAppointments.add(appt);
                
            }
            
        }catch(SQLException exc){
            System.out.println("SQLException(WeeklyView): "+exc.getMessage());
        }
    }
    
}
