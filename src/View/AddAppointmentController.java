/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Appointment;
import Model.Customer;
import Model.CustomerList;
import Model.SQL_Appointment;
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
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
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
    private Pane paneAddAppointment;
    @FXML
    private Button btnBack;
    @FXML
    private TableView<Customer> tbl1CustomerAppt;
    @FXML
    private TableColumn<Customer, String> col1CustomerName;
    @FXML
    private TableColumn<Customer, String> col1CustomerAddress;
    @FXML
    private TableColumn<Customer, String> col1CustomerCity;
    @FXML
    private TableColumn<Customer, String> col1CustomerCountry;
    @FXML
    private TableView<Customer> tbl2CustomerAppt;
    @FXML
    private TableColumn<Customer, String> col2CustomerName;
    @FXML
    private TableColumn<Customer, String> col2CustomerAddress;
    @FXML
    private TableColumn<Customer, String> col2CustomerCity;
    @FXML
    private TableColumn<Customer, String> col2CustomerCountry;
    @FXML
    private TextField txtAppointmentTitle;
    @FXML
    private ComboBox<String> cbAppointmentLocation;
    @FXML
    private ComboBox<String> cbAppointmentDescription;
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
    ObservableList<String> apptLocations = FXCollections.observableArrayList("New York","London","Phoenix","Online");
    ObservableList<String> apptDescription = FXCollections.observableArrayList("Meeting","Documenting","Planning");
    
    @FXML
    private void selectCustomerNext(ActionEvent e){
        addApptCustomer = tbl1CustomerAppt.getSelectionModel().getSelectedItem();      
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
        updateActiveCustomer();
        }                  
    }    
    public void updateActiveCustomer(){
        tbl2CustomerAppt.setItems(customerSelected);
    }
    
    public static int getAddApptCustomerIndex(){
        return addApptCustomerIndex;
    }
    
    @FXML
    private void customerRemove(ActionEvent e){
        addApptCustomer = tbl2CustomerAppt.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you wish to remove this customer?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            customerSelected.remove(addApptCustomer);
        }else{
            System.out.println("You canceled exit.");
        }
    }
    
    @FXML
    private void addAppointment(ActionEvent e){ 
        Customer customer = null;
        if(customerSelected.size()==1){
             customer = customerSelected.get(0);             
        }
        String title = txtAppointmentTitle.getText();        
        String location = cbAppointmentLocation.getSelectionModel().getSelectedItem();
        String contact = txtAppointmentContact.getText();       
        String description = cbAppointmentDescription.getSelectionModel().getSelectedItem();
        String url = txtAppointmentURL.getText();
        String start = cbAppointmentStart.getSelectionModel().getSelectedItem();
        String end = cbAppointmentEnd.getSelectionModel().getSelectedItem();
        LocalDate appointmentDate = dateAppointmentDate.getValue();
        //public static String appointmentValidation(String title, String description, location String contact, String url, int customerID, String errorMessage)
        errorMessage = Appointment.appointmentValidation(title,description,location, contact, url, customer.getCustomerID(), errorMessage);
        if(errorMessage.length()>0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Add Appointment Error");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            errorMessage = "";
        }
        SimpleDateFormat localOutputFormat = new SimpleDateFormat("yyyy-MM-dd h:mm a");
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd KK:mm a");
        localOutputFormat.setTimeZone(TimeZone.getDefault());
        Date startFullDate = null;
        Date endFullDate = null;
        try{
            startFullDate = localOutputFormat.parse(appointmentDate.toString()+" "+start);            
            endFullDate = localOutputFormat.parse(appointmentDate.toString()+" "+end);
        }catch(ParseException exc){
            exc.printStackTrace();
        }
        ZonedDateTime startDateTimeZone = ZonedDateTime.ofInstant(startFullDate.toInstant(), ZoneId.of("UTC"));
        ZonedDateTime endDateTimeZone = ZonedDateTime.ofInstant(endFullDate.toInstant(), ZoneId.of("UTC"));
        if(SQL_Appointment.addVerifyNewAppointment(customer, title, description, location, contact, url, startDateTimeZone, endDateTimeZone)){
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
                  
    }
    
    @FXML
    void exitSubmit(ActionEvent e) throws IOException{
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
    
    public void updateCustomerTable(){
        SQL_Customer.updateAllCustomers();
        tbl1CustomerAppt.setItems(CustomerList.getAllCustomers());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        col1CustomerName.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCustomerName()));
        col1CustomerAddress.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getAddress1()));
        col1CustomerCity.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCity()));
        col1CustomerCountry.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCountry()));        
        updateCustomerTable();
        col2CustomerName.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCustomerName()));
        col2CustomerAddress.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getAddress1()));
        col2CustomerCity.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCity()));
        col2CustomerCountry.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCountry()));
        updateActiveCustomer();
        cbAppointmentStart.setItems(apptTimes);
        cbAppointmentEnd.setItems(apptTimes);
        cbAppointmentLocation.setItems(apptLocations);
        cbAppointmentDescription.setItems(apptDescription);
    }    
    
}
