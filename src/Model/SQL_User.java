/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Util.Database;
import Model.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.time.Instant;
import java.util.Arrays;

/**
 *
 * @author austin.smith
 */
public class SQL_User {
    private static User currentUser;
        
    //public static void setCurrentUser(String userName){
    //    currentUser = userName;
    //}
    private static int getCurrentUser(String userName){
        int userId = -1;
        try{
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select userId from user where userName = '" + userName + "'");
            while(rs.next()){
                userId = rs.getInt("userId");
            }
            if(rs != null){
                rs.close();
            }   
        }catch(SQLException exc){

        }
    return userId;
    }
    private static boolean confirmPassword(int userId, String password){
        try{
            String loginPassword = null;
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select password from user where userId = " + userId);
            if(rs.next()){
                loginPassword = rs.getString("password");
            }else{
                return false;
            }
            rs.close();
            if(loginPassword.equals(password)){
                return true;
            }else{
                return false;
            }
        }catch(SQLException exc){
            exc.printStackTrace();
            return false;
        }
    }
    
    public static boolean login(String userName, String password){        
        int userId = getCurrentUser(userName);
        boolean confirmPassword = confirmPassword(userId,password);
        if(confirmPassword){
            currentUser = new User();
            System.out.println("UserId: "+userId);            
            currentUser.setUser(userName);
            System.out.println("UserName: "+ userName);
            try{
                Path path = Paths.get("logger.txt");
                Files.write(path, Arrays.asList("User " + currentUser + " logged in at " + Date.from(Instant.now()).toString() + "."),
                        StandardCharsets.UTF_8, Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
            }catch(IOException exc){
                exc.printStackTrace();
            }
            return true;
        }else{
            return false;
        }
    }
    
    
}
