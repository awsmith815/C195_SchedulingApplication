/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Appointment;
import Model.AppointmentList;
import Model.Customer;
import Model.CustomerList;
import Model.SQL_Appointment;
import Model.SQL_Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
public class ModifyAppointmentController implements Initializable {
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
    private Pane paneModifyAppointment;
    
    
    private String errorMessage = new String();
    private static int modifyApptCustomerIndex;
    private static Customer modifyApptCustomer;
    private Appointment appointment;
    int modifyAppointmentIndex = MainMenuController.getModifyAppointmentIndex();
    private ObservableList<Customer> customerSelected = FXCollections.observableArrayList();
    

    ObservableList<String> apptTimes = FXCollections.observableArrayList("08:00 AM","09:00 AM","10:00 AM","11:00 AM","12:00 PM","01:00 PM","02:00 PM","03:00 PM","04:00 PM","05:00 PM");
    ObservableList<String> apptLocations = FXCollections.observableArrayList("New York","London","Phoenix","Online");
    ObservableList<String> apptDescription = FXCollections.observableArrayList("Meeting","Documenting","Planning");

    
    @FXML
    private void selectCustomerNext(ActionEvent e){
        modifyApptCustomer = tbl1CustomerAppt.getSelectionModel().getSelectedItem();      
        if(modifyApptCustomer == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No Customer Selected!");
            alert.showAndWait();            
        }else{        
        modifyApptCustomerIndex = CustomerList.getAllCustomers().indexOf(modifyApptCustomer);
        customerSelected.add(modifyApptCustomer);
        updateActiveCustomer();
        }   
    }        
   
    public static int getAddApptCustomerIndex(){
        return modifyApptCustomerIndex;
    }
    public void updateActiveCustomer(){
        tbl2CustomerAppt.setItems(customerSelected);
    }
    @FXML
    private void customerRemove(ActionEvent e){
        modifyApptCustomer = tbl2CustomerAppt.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you wish to remove this customer?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            customerSelected.remove(modifyApptCustomer);
        }else{
            System.out.println("You canceled exit.");
        }
    }
    
    @FXML
    void submitModifyAppointment(ActionEvent e){
        Customer customer = null;
        if(customerSelected.size()==1){
            customer = customerSelected.get(0);
        }
        int appointmentID = appointment.getAppointmentID();
        String title = txtAppointmentTitle.getText();        
        String location = cbAppointmentLocation.getSelectionModel().getSelectedItem();
        String contact = txtAppointmentContact.getText();       
        String description = cbAppointmentDescription.getSelectionModel().getSelectedItem();
        String url = txtAppointmentURL.getText();
        String start = cbAppointmentStart.getSelectionModel().getSelectedItem();
        String end = cbAppointmentEnd.getSelectionModel().getSelectedItem();
        LocalDate appointmentDate = dateAppointmentDate.getValue();
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
        //public static boolean modifyAppointment(int appointmentID, Customer customer, String title, String description, String location, String contact, String url,ZonedDateTime start, ZonedDateTime end)
        if(SQL_Appointment.modifyAppointment(appointmentID, customer, title, description, location, contact, url, startDateTimeZone, endDateTimeZone)){
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
        
        appointment = AppointmentList.getAllAppointments().get(modifyAppointmentIndex);
        
        String title = appointment.getTitle();        
        String location = appointment.getLocation();
        String description = appointment.getDescription();
        String contact = appointment.getContact();
        String urlApt = appointment.getUrl();
        
        //get the date for the date picker
        Date appointmentDate = appointment.getStartDate();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(appointmentDate);        
        int appointmentDateYear = calendar.get(Calendar.YEAR);
        int appointmentDateMonth = calendar.get(Calendar.MONTH);
        int appointmentDateDay = calendar.get(Calendar.DAY_OF_MONTH);
        LocalDate appointmentLocalDate = LocalDate.of(appointmentDateYear, appointmentDateMonth, appointmentDateDay);
        
        //get the times for the appointment
        Timestamp start = appointment.getStart();
        Timestamp end = appointment.getEnd();        
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a");
        //System.out.println("formatTime == " + formatTime);
        String startString = formatTime.format(start);
        //System.out.println("startString == " + startString);
        String endString = formatTime.format(end); 
        //System.out.println("endString == " + endString);
        int customerID = appointment.getCustomerID();
        System.out.println("CustomerID: " + customerID);
        ObservableList<Customer> customerList = CustomerList.getAllCustomers();
        for(Customer customer:customerList){
            if(customer.getCustomerID() == customerID){
                customerSelected.add(customer);                
            }
        }        
        txtAppointmentTitle.setText(title);
        cbAppointmentDescription.setValue(description);
        cbAppointmentLocation.setValue(location);
        txtAppointmentContact.setText(contact);
        txtAppointmentURL.setText(urlApt);
        cbAppointmentStart.setValue(startString);
        cbAppointmentEnd.setValue(endString);
        dateAppointmentDate.setValue(appointmentLocalDate);                 
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
