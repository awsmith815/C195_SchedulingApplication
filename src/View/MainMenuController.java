/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Customer;
import Model.SQL_Customer;
import Model.CustomerList;
import Model.SQL_Appointment;
import Model.Appointment;
import Model.AppointmentList;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * MainMenuController class
 *
 * @author austin.smith
 */
public class MainMenuController implements Initializable {
    
    @FXML
    private Button btnMainMenu;
    @FXML
    private Button btnCustomer;
    @FXML
    private Button btnAppointment;
    @FXML
    private Button btnReport;
    @FXML
    private Pane paneMainMenu;
    @FXML
    private Pane paneCustomer;
    @FXML
    private Pane paneAppointment;
    @FXML
    private Pane paneReport;       
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
    @FXML
    private TableView<Appointment> tblAppointment;
    @FXML
    private TableColumn<Appointment,String> colAppointmentTitle;
    @FXML
    private TableColumn<Appointment,String> colAppointmentLocation;
    @FXML
    private TableColumn<Appointment,String> colAppointmentContact;
    @FXML
    private TableColumn<Appointment,Date> colAppointmentStart;
    @FXML
    private TableColumn<Appointment,Date> colAppointmentEnd;
    
    
    private static int modifyCustomerIndex;
    private static Customer modifyCustomer;
    
    @FXML
    private void handleButtonAction(ActionEvent e){
        if(e.getSource()==btnMainMenu){
            paneMainMenu.toFront();
        }else if(e.getSource()==btnCustomer){
            paneCustomer.toFront();
        }else if(e.getSource()==btnAppointment){
            paneAppointment.toFront();
        }else if(e.getSource()==btnReport){
            paneReport.toFront();
        }
    }
    
    DropShadow shadow = new DropShadow();
    @FXML
    private void handleMainButtonEnter(MouseEvent me){        
        btnMainMenu.setEffect(shadow);        
    }
    @FXML
    private void handleMainButtonExit(MouseEvent me){        
        btnMainMenu.setEffect(null);
    }
    @FXML
    private void handleCustomerButtonEnter(MouseEvent me){        
        btnCustomer.setEffect(shadow);        
    }
    @FXML
    private void handleCustomerButtonExit(MouseEvent me){        
        btnCustomer.setEffect(null);
    }
    @FXML
    private void handleAppointmentButtonEnter(MouseEvent me){        
        btnAppointment.setEffect(shadow);        
    }
    @FXML
    private void handleAppointmentButtonExit(MouseEvent me){        
        btnAppointment.setEffect(null);
    }    
    @FXML
    private void handleReportButtonEnter(MouseEvent me){        
        btnReport.setEffect(shadow);        
    }
    @FXML
    private void handleReportButtonExit(MouseEvent me){        
        btnReport.setEffect(null);
    }



    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Customer
    
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
    void removeCustomer(ActionEvent e){
        Customer customer = tblCustomer.getSelectionModel().getSelectedItem();
        if(customer!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Confirm");
            alert.setHeaderText("Confirm");
            alert.setContentText("Are you sure you wish to remove this customer?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get()==ButtonType.OK){
                SQL_Customer.setCustomerInactive(customer);
            }else{
                System.out.println("Customer was not removed");
            }
        }
    }
    
    @FXML
    private void modifyCustomers(ActionEvent e){
        modifyCustomer = tblCustomer.getSelectionModel().getSelectedItem();      
        if(modifyCustomer == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No Customer Selected to modify");
            alert.showAndWait();
            return;
        }        
        modifyCustomerIndex = CustomerList.getAllCustomers().indexOf(modifyCustomer);
        try{
            Parent modifyCustomerParent = FXMLLoader.load(getClass().getResource("ModifyCustomer.fxml"));
            Scene modifyCustomerScene = new Scene(modifyCustomerParent);
            Stage modifyCustomerStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            modifyCustomerStage.setScene(modifyCustomerScene);
            modifyCustomerStage.show();
        }catch(IOException exc){
            System.out.println(exc.getMessage());
            System.out.println(exc.getLocalizedMessage());
            System.out.println(exc.initCause(exc));
            
        }
    }
    
    public static int getModifyCustomerIndex(){
        return modifyCustomerIndex;
    }
    
    public void updateCustomerTable(){
        SQL_Customer.updateAllCustomers();
        tblCustomer.setItems(CustomerList.getAllCustomers());
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Appointment
    @FXML
    void addAppointment(ActionEvent e){
        try{
            Parent addAppointmentParent = FXMLLoader.load(getClass().getResource("AddAppointment.fxml"));
            Scene addAppointmentScene = new Scene(addAppointmentParent);
            Stage addAppointmentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            addAppointmentStage.setScene(addAppointmentScene);
            addAppointmentStage.show();            
        }catch(IOException exc){
            exc.printStackTrace();
        }
    }
    
    @FXML
    void modifyAppointment(ActionEvent e){
        
    }
    @FXML
    void deleteAppointment(ActionEvent e){
        Appointment appointment = tblAppointment.getSelectionModel().getSelectedItem();
        if(appointment !=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Confirm");
            alert.setHeaderText("Confirm");
            alert.setContentText("Are you sure you wish to cancel this appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get()==ButtonType.OK){
                //SQL_Customer.setCustomerInactive(customer);
                SQL_Appointment.deleteAppointment(appointment);
            }else{
                System.out.println("Appointment was not removed");
            }
        }
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
       
    
    public void updateAppointmentTable(){
        SQL_Appointment.updateAllAppointments();
        tblAppointment.setItems(AppointmentList.getAllAppointments());
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
        
        colAppointmentTitle.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getTitle()));
        colAppointmentLocation.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getLocation()));        
        colAppointmentContact.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getContact()));
        colAppointmentStart.setCellValueFactory(appointment -> new SimpleObjectProperty(appointment.getValue().getStart()));
        colAppointmentEnd.setCellValueFactory(appointment -> new SimpleObjectProperty(appointment.getValue().getEnd()));
        updateAppointmentTable();
        
        
    }    
    
}
