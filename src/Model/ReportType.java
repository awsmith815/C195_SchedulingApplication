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
public class ReportType {
    private String month, description;
    private int year, numAppts;
    
    public ReportType(){
        
    }
    public String getMonth(){
        return month;
    }
    public String getDescription(){
        return description;
    }
    public int getYear(){
        return year;
    }
    public int getNumAppts(){
        return numAppts;
    }
    public void setMonth(String month){
        this.month=month;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public void setYear(int year){
        this.year=year;        
    }
    public void setNumAppts(int numAppts){
        this.numAppts=numAppts;
    }
}
