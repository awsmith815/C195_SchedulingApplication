/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static Model.SQLManager.confirmUserNamePassword;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author austin.smith
 */
public class LoginPageController {
    @FXML
    private TextField txtUsername; 
    @FXML
    private TextField txtPassword;
    @FXML
    private Label lblLoginError;
    public static int dbError = 0;
    
    @FXML
    private void clickLogin(ActionEvent e){
        String userName = txtUsername.getText();
        String password = txtPassword.getText();
        
        if(userName.equals("") || password.equals("")){
            lblLoginError.setText("No Username/Password");
            return;
        }
        boolean confirmLogin = confirmUserNamePassword(userName, password);
        if(confirmLogin == true){
            try{
                Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
                Scene mainMenuScene = new Scene(mainMenuParent);
                Stage mainMenuStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                mainMenuStage.setScene(mainMenuScene);
                mainMenuStage.show();
            }catch(IOException exc){
                exc.printStackTrace();
            }
        }else if(dbError > 0){
            lblLoginError.setText("Connection Error");
        }else{
            lblLoginError.setText("Wrong Username/Password");
        }
    }
    
    @FXML
    private void confirmExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you wish to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
    
    
    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
}
