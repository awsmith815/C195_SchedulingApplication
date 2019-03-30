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

/**
 *
 * @author austin.smith
 */
public class SQL_Appointment {
    
    public static void addAppointment(int customerID, String title, String description, String location, String contact, String type, String url, Timestamp start, Timestamp end){
        try{
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select appointmentId from appointment");
        }catch(SQLException exc){
            exc.printStackTrace();
        }
        
    }
    
}
