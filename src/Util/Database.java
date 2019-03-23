/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.sql.*;
/**
 *
 * @author austin.smith
 */
public class Database {
    private static final String url = "jdbc:mysql://52.206.157.109:3306/U050k8";
    private static final String usr = "U050k8";
    private static final String pswrd = "53688403912";
    private static Connection myConn;
    //constructor
    public Database(){
        
    }
    
    public static Connection getConnection(){
        return myConn;
    }
    public static void connect(){
        try{
            myConn = DriverManager.getConnection(url,usr,pswrd);
            System.out.println("Connected to SQL");
        }catch(SQLException exc){
            System.out.println("SQL Exception: "+exc.getMessage());
            System.out.println("SQL Error: "+exc.getErrorCode());
        }
    }
    public static void disconnect(){
        try{
            myConn.close();
            System.out.println("Connection Closed");
        }catch(SQLException exc){
            System.out.println("SQL Exception: "+exc.getMessage());
        }
    }

}
