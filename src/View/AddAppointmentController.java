/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Appointment;
import Model.Customer;
import Model.CustomerList;
import Model.SQL_Customer;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author austin.smith
 */
public class AddAppointmentController implements Initializable {
    
    @FXML
    private Pane paneSelectAddCustomer;
    @FXML
    private Pane paneAddAppointment;
    @FXML
    private TableView<Customer> tblCustomerAppt;
    @FXML
    private TableColumn<Customer, String> colCustomerName;
    @FXML
    private TableColumn<Customer, String> colCustomerAddress;
    @FXML
    private TableColumn<Customer, String> colCustomerCity;
    @FXML
    private TableColumn<Customer, String> colCustomerCountry;
    @FXML
    private TableColumn<Customer, String> colCustomerPhone;
    @FXML
    private Button btnCustomerNext;
    @FXML
    private TextField txtAppointmentTitle;
    @FXML
    private TextArea txtAppointmentDescription;
    @FXML
    private TextField txtAppointmentLocation;
    @FXML
    private TextField txtAppointmentType;
    @FXML
    private TextField txtAppointmentContact;
    @FXML
    private TextField txtAppointmentURL;
    @FXML
    private ComboBox<String> cbAppointmentStart;
    @FXML
    private ComboBox<String> cbAppointmentEnd;
    @FXML
    private DatePicker dateAppointmentDate;
    
    private String errorMessage = new String();
    private static int addApptCustomerIndex;
    private static Customer addApptCustomer;
    private ObservableList<Customer> customerSelected = FXCollections.observableArrayList();
        
    ObservableList<String> apptTimes = FXCollections.observableArrayList("8:00 AM","9:00 AM","10:00 AM","11:00 AM","12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM","5:00 PM");
    
    @FXML
    private void selectCustomerNext(ActionEvent e){
        addApptCustomer = tblCustomerAppt.getSelectionModel().getSelectedItem();      
        if(addApptCustomer == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No Customer Selected!");
            alert.showAndWait();            
        }else{        
        addApptCustomerIndex = CustomerList.getAllCustomers().indexOf(addApptCustomer);
        customerSelected.add(addApptCustomer);
        paneAddAppointment.toFront();
        }
                  
    }        
    
    public static int getAddApptCustomerIndex(){
        return addApptCustomerIndex;
    }
    
    @FXML
    private void addAppointment(ActionEvent e){ 
        Customer customer = null;
        if(customerSelected.size()==1){
             customer = customerSelected.get(0);
        }
        String title = txtAppointmentTitle.getText();
        String description = txtAppointmentDescription.getText();
        String location = txtAppointmentLocation.getText();
        String contact = txtAppointmentContact.getText();       
        String type = txtAppointmentType.getText();
        String url = txtAppointmentURL.getText();
        String start = cbAppointmentStart.getSelectionModel().getSelectedItem();
        String end = cbAppointmentEnd.getSelectionModel().getSelectedItem();
        LocalDate appointmentDate = dateAppointmentDate.getValue();
        errorMessage = Appointment.appointmentValidation(title, errorMessage);
        if(errorMessage.length()>0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Add Appointment Error");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            errorMessage = "";
        }
        SimpleDateFormat localFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a");
        localFormat.setTimeZone(TimeZone.getDefault());
        Date startFullDate = null;
        Date endFullDate = null;
        try{
            startFullDate = localFormat.parse(appointmentDate.toString()+" "+start);
            endFullDate = localFormat.parse(appointmentDate.toString()+" "+end);
        }catch(ParseException exc){
            exc.printStackTrace();
        }
        ZonedDateTime startDateTimeZone = ZonedDateTime.ofInstant(startFullDate.toInstant(), ZoneId.of("UTC"));
        ZonedDateTime endDateTimeZone = ZonedDateTime.ofInstant(endFullDate.toInstant(), ZoneId.of("UTC"));
        
        try{
            Parent mainMenuParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene mainMenuScene = new Scene(mainMenuParent);
            Stage mainMenuStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            mainMenuStage.setScene(mainMenuScene);
            mainMenuStage.show(); 
        }catch(IOException exc){
            exc.printStackTrace();
        }
        
                  
    }
    
    
    
    
    
    public void updateCustomerTable(){
        SQL_Customer.updateAllCustomers();
        tblCustomerAppt.setItems(CustomerList.getAllCustomers());
    }
    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colCustomerName.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCustomerName()));
        colCustomerAddress.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getAddress1()));
        colCustomerCity.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCity()));
        colCustomerCountry.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCountry()));
        colCustomerPhone.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getPhone()));
        updateCustomerTable();
        cbAppointmentStart.setItems(apptTimes);
        cbAppointmentEnd.setItems(apptTimes);
        
    }    
    
}
