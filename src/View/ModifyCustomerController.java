/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Customer;
import Model.SQL_Customer;
import Model.CustomerList;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author austin.smith
 */
public class ModifyCustomerController implements Initializable {
    
    @FXML
    private TextField txtModifyCustomerName;
    @FXML
    private TextField txtModifyCustomerPhone;
    @FXML
    private TextField txtModifyCustomerAddress1;
    @FXML
    private TextField txtModifyCustomerAddress2;
    @FXML
    private TextField txtModifyCustomerCity;
    @FXML
    private TextField txtModifyCustomerPostalCode;
    @FXML
    private TextField txtModifyCustomerCountry;
    private Customer customer;
    int modifyCustomerIndex = MainMenuController.getModifyCustomerIndex();
    private String errorMessage = new String();
    private int customerID;

    @FXML
    void submitCustomer(ActionEvent e){     
        customerID = customer.getCustomerID();
        String customerName = txtModifyCustomerName.getText();
        String address1 = txtModifyCustomerAddress1.getText();
        String address2 = txtModifyCustomerAddress2.getText();
        String city = txtModifyCustomerCity.getText();
        String country = txtModifyCustomerCountry.getText();
        String postalCode = txtModifyCustomerPostalCode.getText();
        String phone = txtModifyCustomerPhone.getText();
        errorMessage = Customer.customerValidation(customerName, address1, address2, postalCode, city, country, phone, errorMessage);
        if(errorMessage.length() > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Add Customer Error");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                errorMessage = "";
            }else{
               try{
                //public static int modifyCustomer(int customerID,String customerName, String address1, String address2, String city, String country, String postalCode, String phone){
                SQL_Customer.modifyCustomer(customerID, customerName, address1, address2, city, country, postalCode, phone);
                Parent mainMenuParent = FXMLLoader.load(getClass().getResource("/View/MainMenu.fxml"));
                Scene mainMenuScene = new Scene(mainMenuParent);
                Stage mainMenuStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                mainMenuStage.setScene(mainMenuScene);
                mainMenuStage.show();
                
                }catch(IOException exc){
                   System.out.println(exc.getMessage());
               }
            }

    }
    
   
    
    @FXML
     void cancelSubmit(ActionEvent e) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you wish to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene mainMenuScene = new Scene(mainMenuParent);
            Stage mainMenuStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            mainMenuStage.setScene(mainMenuScene);
            mainMenuStage.show();
        }
     }
     
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        customer = CustomerList.getAllCustomers().get(modifyCustomerIndex);        
        //customerID = CustomerList.getAllCustomers().get(modifyCustomerIndex).getCustomerID();
        txtModifyCustomerName.setText(customer.getCustomerName());
        txtModifyCustomerAddress1.setText(customer.getAddress1());
        txtModifyCustomerAddress2.setText(customer.getAddress2());
        txtModifyCustomerCity.setText(customer.getCity());
        txtModifyCustomerCountry.setText(customer.getCountry());
        txtModifyCustomerPostalCode.setText(customer.getPostalCode());
        txtModifyCustomerPhone.setText(customer.getPhone());
        
                
    }    
    
}
