/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Customer;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

/**
 * FXML Controller class
 *
 * @author austin.smith
 */
public class AddCustomerController implements Initializable {
    @FXML
    private TextField txtCustomerName;
    @FXML
    private TextField txtCustomerPhone;
    @FXML
    private TextField txtCustomerAddress1;
    @FXML
    private TextField txtCustomerAddress2;
    @FXML
    private TextField txtCustomerCity;
    @FXML
    private TextField txtCustomerPostalCode;
    @FXML
    private TextField txtCustomerCountry;
    private String errorMessage = new String();
    private int customerID;
    
    @FXML
    void submitCustomer(ActionEvent e){
        String customerName = txtCustomerName.getText();
        String phone = txtCustomerPhone.getText();
        String address1 = txtCustomerAddress1.getText();
        String address2 = txtCustomerAddress2.getText();
        String city = txtCustomerCity.getText();
        String postalCode = txtCustomerPostalCode.getText();
        
        try{
            errorMessage = Customer.customerValidation(customerName, address1, address2, postalCode, city, city, phone, errorMessage);
            if(errorMessage.length() > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Add Customer Error");
                alert.setContentText(errorMessage);
                alert.showAndWait();
                errorMessage = "";
            }else{
                Customer cust = new Customer();
                cust.setCustomerID(customerID);
                cust.setCustomerName(customerName);
                cust.setAddress1(address1);
                cust.setAddress2(address2);
                cust.setCity(city);
                cust.setPhone(phone);
                cust.setPostalCode(postalCode);
                
            }
            
        }catch(NumberFormatException exc){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Add Customer Error");
            alert.setContentText("Every field must have a value");
            alert.showAndWait();
        }
        
    }
    
    
    
    @FXML
     void cancelSubmit(ActionEvent e){
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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
