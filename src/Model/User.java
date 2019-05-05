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
    private String currentUsername;
    
    public User(){
        
    }
    public String getCurrentUsername(){
        return currentUsername;
    }
    public void setCurrentUsername(String currentUsername){
        this.currentUsername=currentUsername;
    }
}
