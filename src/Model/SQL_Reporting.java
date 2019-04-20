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
public class SQL_Reporting {       
    
    //Build Report 1
    public static void apptByType(){
        
    }
    
    
    //Build Report 2
    public static void customerSchedule(){
        
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
                System.out.println("Report3Col2: "+numApptAT);
                int numUpcomingAppt = rs.getInt(3);
                System.out.println("Report3Col3: "+numUpcomingAppt);
                int numCustomers = rs.getInt(4);
                System.out.println("Report3Col4: "+numCustomers);
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
