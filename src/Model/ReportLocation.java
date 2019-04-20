/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author austin.smith
 */
public class ReportLocation {
    
    private String location;
    private int numApptAT, numUpcomingAppt, numCustomers;
    
    public ReportLocation(){
        
    }
    
    public String getLocation(){
        return location;
    }
    public int getNumApptAT(){
        return numApptAT;
    }
    public int getNumUpcomingAppt(){
        return numUpcomingAppt;
    }
    public int getNumCustomers(){
        return numCustomers;
    }
    public void setLocation(String location){
        this.location=location;
    }
    public void setNumApptAT(int numApptAT){
        this.numApptAT=numApptAT;
    }
    public void setNumUpcomingAppt(int numUpcomingAppt){
        this.numUpcomingAppt=numUpcomingAppt;
    }
    public void setNumCustomers(int numCustomers){
        this.numCustomers=numCustomers;
    }
    
}
