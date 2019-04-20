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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javafx.collections.ObservableList;

/**
 *
 * @author austin.smith
 */
public class SQL_Reporting {       
    
    //Build Report 1
    public static void apptByType(){
        
    }
    
    
    //Build Report 2
    public static void customerSchedule(){
        try{
            Statement stmt = Database.getConnection().createStatement();
            ObservableList<ReportCustomer> allReportCustomers = ReportCustomerList.getAllCustomersReport();
            ArrayList<Integer> allAppointments = new ArrayList<>();
            allReportCustomers.clear();
            ResultSet initialAppt = stmt.executeQuery("select appointmentId from appointment");
            while(initialAppt.next()){
                allAppointments.add(initialAppt.getInt(1));
            }
            for(int appointmentID : allAppointments){
                ReportCustomer rc = new ReportCustomer();
                ResultSet rs = stmt.executeQuery("select \n" +
                                                "a.`start`\n" +
                                                ",c.customerName\n" +
                                                ",a.title\n" +
                                                ",a.location\n" +
                                                ",a.contact\n" +
                                                "from\n" +
                                                "appointment a \n" +
                                                "inner join customer c on c.customerId = a.customerId\n" +
                                                 "where appointmentId="+appointmentID);
                rs.next();
                Timestamp date = rs.getTimestamp(1);
                SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a");
                String apptTime = formatTime.format(date);
                //System.out.println("Report2Col1: "+apptTime);
                String customerName = rs.getString(2);
                //System.out.println("Report2Col2: "+customerName);
                String title = rs.getString(3);
                //System.out.println("Report2Col3: "+title);
                String location = rs.getString(4);
                //System.out.println("Report2Col4: "+location);
                String contact = rs.getString(5);
               // System.out.println("Report2Col5: "+contact);
                Date apptDate = date;
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.setTime(apptDate);        
                int appointmentDateYear = calendar.get(Calendar.YEAR);
                int appointmentDateMonth = calendar.get(Calendar.MONTH);
                int appointmentDateDay = calendar.get(Calendar.DAY_OF_MONTH);
                LocalDate apptLocalDate = LocalDate.of(appointmentDateYear, appointmentDateMonth, appointmentDateDay);
                //System.out.println("Report2Date: "+apptLocalDate);
                rc.setApptDate(apptLocalDate);
                rc.setApptTime(apptTime);
                rc.setCustomerName(customerName);
                rc.setTitle(title);
                rc.setLocation(location);
                rc.setContact(contact);
                allReportCustomers.add(rc);   
            }
            
        }catch(SQLException exc){
            System.out.println("SQLException: "+exc.getMessage());
            System.out.println("Error: "+exc.getLocalizedMessage());    
        }
    }
    
    
    //Build Report 3
    public static void locationReport(){
        try{            
            Statement stmt = Database.getConnection().createStatement();
            ObservableList<ReportLocation> allLocations = ReportLocationList.getAllLocations();
            allLocations.clear();
            ResultSet getInitialLocations = stmt.executeQuery("select distinct location from appointment");
            ArrayList<String> locationListArray = new ArrayList<>();
            while(getInitialLocations.next()){
                locationListArray.add(getInitialLocations.getString(1));
            }
            for(String location : locationListArray){
                ReportLocation reportLocation = new ReportLocation();
                ResultSet rs = stmt.executeQuery("select\n" +
                                                "a.location\n" +
                                                ",COUNT(distinct a.appointmentId) as numApptAT\n" +
                                                ",IF(a.`start` > curdate(), COUNT(distinct a.appointmentId), 0) as numUpcomingAppt\n" +
                                                ",COUNT(distinct a.customerId) as numCustomers\n" +
                                                "from appointment a\n" +
                                                "where a.location = '"+ location + "' group by a.location");
                rs.next();
                //String location = rs.getString(1);  
                System.out.println("Report3Col1: "+location);
                int numApptAT = rs.getInt(2);
                //System.out.println("Report3Col2: "+numApptAT);
                int numUpcomingAppt = rs.getInt(3);
                //System.out.println("Report3Col3: "+numUpcomingAppt);
                int numCustomers = rs.getInt(4);
                //System.out.println("Report3Col4: "+numCustomers);
                reportLocation.setLocation(location);
                reportLocation.setNumApptAT(numApptAT);
                reportLocation.setNumUpcomingAppt(numUpcomingAppt);
                reportLocation.setNumCustomers(numCustomers);
                allLocations.add(reportLocation);
            }
            //rs.close();            
        }catch(SQLException exc){
            System.out.println("SQLException: "+exc.getMessage());
            System.out.println("Error: "+exc.getLocalizedMessage());            
        }
    }
    
}
