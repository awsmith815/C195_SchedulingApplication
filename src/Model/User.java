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
public class User {
    private String currentUser;
    
    public User(){
        
    }
    public String getUser(){
        return currentUser;
    }
    public void setUser(String currentUser){
        this.currentUser=currentUser;
    }
}
