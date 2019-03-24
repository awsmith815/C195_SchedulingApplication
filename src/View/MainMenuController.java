/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Customer;
import Model.SQL_Customer;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * MainMenuController class
 *
 * @author austin.smith
 */
public class MainMenuController implements Initializable {
    
    @FXML
    private TableView<Customer> tblCustomer;
    @FXML
    private TableColumn<Customer,String> colCustomerName;
    @FXML
    private TableColumn<Customer,String> colAddress;
    @FXML
    private TableColumn<Customer,String> colCity;
    @FXML
    private TableColumn<Customer,String> colCountry;
    @FXML
    private TableColumn<Customer,String> colPhone;
    private static int modifyCustomerIndex;
    
    
    
    @FXML
    void addCustomer(ActionEvent e){
        try{
            Parent addCustomerParent = FXMLLoader.load(getClass().getResource("AddCustomer.fxml"));
            Scene addCustomerScene = new Scene(addCustomerParent);
            Stage addCustomerStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            addCustomerStage.setScene(addCustomerScene);
            addCustomerStage.show();
        }catch(IOException exc){
            exc.printStackTrace();
        }
    }
    @FXML
    void modifyCustomer(ActionEvent e){
        Customer selectCustomer = tblCustomer.getSelectionModel().getSelectedItem();
        if(selectCustomer == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No Customer Selected to modify");
            alert.showAndWait();
            return;
        }
        modifyCustomerIndex = Customer.getAllCustomers().indexOf(modifyCustomerIndex);
        try{
            Parent modifyCustomerParent = FXMLLoader.load(getClass().getResource("ModifyCustomer.fxml"));
            Scene modifyCustomerScene = new Scene(modifyCustomerParent);
            Stage modifyCustomerStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            modifyCustomerStage.setScene(modifyCustomerScene);
            modifyCustomerStage.show();
        }catch(IOException exc){
            exc.printStackTrace();
        }
    }
    
    public static int getModifyCustomerIndex(){
        return modifyCustomerIndex;
    }
    
    public void updateCustomerTable(){
        SQL_Customer.updateAllCustomers();
        tblCustomer.setItems(Customer.getAllCustomers());
    }
    
    @FXML
    void exit(ActionEvent e){
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
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        colCustomerName.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCustomerName()));
        colAddress.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getAddress1()));
        colCity.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCity()));
        colCountry.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCountry()));
        colPhone.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getPhone()));
        updateCustomerTable();
    }    
    
}
