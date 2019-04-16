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
import static Model.SQL_User.login;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author austin.smith
 */
public class LoginPageController implements Initializable {
    @FXML
    private Label lblWelcome;
    @FXML
    private Label lblFooter;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnExit;
    @FXML
    private TextField txtUsername; 
    @FXML
    private TextField txtPassword;
    @FXML
    private Label lblLoginError;    
    public static int dbError = 0;       
    
    private void setLanguage(){
        ResourceBundle rb = ResourceBundle.getBundle("Resources/LoginPage", Locale.getDefault());
        lblWelcome.setText(rb.getString("lblWelcomeHome"));
        lblFooter.setText(rb.getString("lblFooter"));
        txtUsername.setPromptText(rb.getString("txtUsername"));
        txtPassword.setPromptText(rb.getString("txtPassword"));
        btnLogin.setText(rb.getString("btnLogin"));
        btnExit.setText(rb.getString("btnExit"));        
    }
    
    @FXML
    private void clickLogin(ActionEvent e){
        ResourceBundle rb = ResourceBundle.getBundle("Resources/LoginPage", Locale.getDefault());
        String userName = txtUsername.getText();
        String password = txtPassword.getText();
        
        if(userName.equals("") && password.equals("")){
            lblLoginError.setText(rb.getString("lblNoUserPass"));
            txtUsername.setStyle("-fx-border-color: red");
            txtPassword.setStyle("-fx-border-color: red");
            return;
        }else if(userName.equals("")){
            lblLoginError.setText(rb.getString("lblNoUser"));
            txtUsername.setStyle("-fx-border-color: red");
            return;
        }else if(password.equals("")){
            lblLoginError.setText(rb.getString("lblNoPass"));
            txtPassword.setStyle("-fx-border-color: red");
            return;
        }
        boolean confirmLogin = login(userName, password);
        if(confirmLogin == true){
            try{
                Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
                Scene mainMenuScene = new Scene(mainMenuParent);
                Stage mainMenuStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                mainMenuStage.setScene(mainMenuScene);
                mainMenuStage.centerOnScreen();
                mainMenuStage.show();                
            }catch(IOException exc){
                exc.printStackTrace();
            }
        }else if(dbError > 0){
            lblLoginError.setText(rb.getString("lblConnectionError"));
        }else{
            lblLoginError.setText(rb.getString("lblLoginError"));
        }
    }
    
    @FXML
    private void confirmExit(ActionEvent event) {
        ResourceBundle rb = ResourceBundle.getBundle("Resources/LoginPage", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(rb.getString("alertTitle"));
        alert.setHeaderText(rb.getString("alertHeader"));        
        alert.setContentText(rb.getString("alertContent"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */    
    public void initialize(URL url, ResourceBundle rb) {
        setLanguage();
        
        
        
        System.out.println("Login Page Initialized");
    }    
    
}
