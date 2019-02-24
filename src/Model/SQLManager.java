/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Arrays;
/**
 *
 * @author austin.smith
 */
public class SQLManager {
    
    private static final String url = "jdbc:mysql://52.206.157.109:3306/U050k8";
    private static final String usr = "U050k8";
    private static final String pswrd = "53688403912";
    
    private static int getUser(String userName){
        int userId = -1;
        try{
            Connection myConn = null;
            Statement myStmt = null;
            ResultSet myRs = null;
            myConn = DriverManager.getConnection(url,usr,pswrd);
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery("select userId from user where userName = '" + userName + "'");
            while(myRs.next()){
                userId = myRs.getInt("userId");
            }
            if(myRs != null){
                myRs.close();
            }   
        }catch(SQLException exc){

        }
    return userId;
    }
    
    private static boolean confirmPassword(int userId, String password){
        try{
            Connection myConn = null;
            Statement myStmt = null;
            ResultSet myRs = null;
            String loginPassword = null;
            myConn = DriverManager.getConnection(url,usr,pswrd);
            myStmt = myConn.createStatement();
            myRs = myStmt.executeQuery("select password from user where userId = " + userId);
            if(myRs.next()){
                loginPassword = myRs.getString("password");
            }else{
                return false;
            }
            myRs.close();
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
    public static boolean confirmUserNamePassword(String userName, String password){
        String currentUser;
        int userId = getUser(userName);
        boolean confirmPassword = confirmPassword(userId,password);
        if(confirmPassword){
            currentUser = userName;
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
