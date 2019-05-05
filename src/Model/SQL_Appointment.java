package Model;

import Util.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

/**
 *
 * @author austin.smith
 */
public class SQL_Appointment {

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
            String userName = SQL_User.getActiveUsername();
            stmt.executeUpdate("insert into appointment values("+appointmentID+","+customerID+",'"+title+"','"+description+"','"+location+"','"+contact+"','"+url+"','"+start+"','"+end+"',"
                    + "current_timestamp,'"+userName+"',current_timestamp,'"+userName+"')");
        }catch(SQLException exc){            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Error adding appointment");
            alert.showAndWait();
            
        }        
    }
    public static boolean verifyAddAppointment(Timestamp start, Timestamp end){
        updateAllAppointments();
        ObservableList<Appointment> allAppointments = AppointmentList.getAllAppointments();
        for(Appointment appointment:allAppointments){
            Timestamp prevStart = appointment.getStart();
            Timestamp prevEnd = appointment.getEnd();
            if(end.after(prevStart)&&end.before(prevEnd)){
                return true;
            }else if(start.after(prevStart)&&end.before(prevEnd)){
                return true;
            }else if(start.before(prevEnd)&&end.after(prevEnd)){
                return true;
            }else if(start.equals(prevStart)){
                return true;
            }else if(end.equals(prevEnd)){
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean addVerifyNewAppointment(Customer customer, String title, String description, String location, String contact, String url,ZonedDateTime start, ZonedDateTime end){
        LocalDateTime startWOTZ = start.toLocalDateTime();
        System.out.println("SQLStart WOTZ: "+startWOTZ);
        LocalDateTime endWOTZ = end.toLocalDateTime();
        Timestamp startTimestamp = Timestamp.valueOf(startWOTZ);
        System.out.println("SQLStartTimestamp: "+startTimestamp);
        Timestamp endTimestamp = Timestamp.valueOf(endWOTZ);
        //Add functionality to make sure no appointments overlap in DB
        if(verifyAddAppointment(startTimestamp, endTimestamp)==true){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Appointment Time Overlaps with Another!");
            alert.showAndWait();            
            return false;
        }else{
            int customerID = customer.getCustomerID();
            addAppointment(customerID, title, description, location, contact, url, startTimestamp, endTimestamp);
            return true;
        }
    }
    
    public static boolean modifyAppointment(int appointmentID, Customer customer, String title, String description, String location, String contact, String url,ZonedDateTime start, ZonedDateTime end){
        LocalDateTime startWOTZ = start.toLocalDateTime();
        System.out.println("SQLStart WOTZ: "+startWOTZ);
        LocalDateTime endWOTZ = end.toLocalDateTime();
        Timestamp startTimestamp = Timestamp.valueOf(startWOTZ);
        System.out.println("SQLStartTimestamp: "+startTimestamp);
        Timestamp endTimestamp = Timestamp.valueOf(endWOTZ);
        //Add functionality to make sure no appointments overlap in DB
        if(verifyAddAppointment(startTimestamp, endTimestamp)==true){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Appointment Time Overlaps with Another or Itself!");
            alert.showAndWait();            
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
            String userName = SQL_User.getActiveUsername();
            stmt.executeUpdate("update appointment set customerId="+customerID+",title='"+title+"',description='"+description+"',location='"+location+"',contact='"+contact+"',url='"+url+"',"
                    + "start='"+start+"',end='"+end+"',lastUpdate=current_timestamp, lastUpdateBy='"+userName+"' where appointmentId="+appointmentID);            
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
                Timestamp end = rs.getTimestamp("end");
                DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
                LocalDateTime ldts = start.toLocalDateTime();
                LocalDateTime ldte = end.toLocalDateTime();
                ZonedDateTime currentUTCTimeS = ldts.atZone(ZoneId.of("UTC"));
                ZonedDateTime currentUTCTimeE = ldte.atZone(ZoneId.of("UTC"));
                ZonedDateTime currentETimeS = currentUTCTimeS.withZoneSameInstant(ZoneId.systemDefault());
                ZonedDateTime currentETimeE = currentUTCTimeE.withZoneSameInstant(ZoneId.systemDefault());
                Date startDateCal = Date.from(currentETimeS.toInstant());
                String startFormat = currentETimeS.format(formatTime);
                String endFormat = currentETimeE.format(formatTime);
                appt.setAppointmentID(appointmentID);
                appt.setCustomerID(customerID);
                appt.setTitle(title);
                appt.setDescription(description);
                appt.setLocation(location);
                appt.setContact(contact);                
                appt.setUrl(url);
                appt.setStart(start);
                appt.setEnd(end);
                appt.setStartString(startFormat);
                appt.setEndString(endFormat);
                appt.setStartDate(startDateCal);
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
