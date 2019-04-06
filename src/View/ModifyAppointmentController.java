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
import Model.SQL_Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
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
    private TextArea txtAppointmentDescription;
    @FXML
    private ComboBox<String> cbAppointmentLocation;
    @FXML
    private ComboBox<String> cbAppointmentType;
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
    private TextField txtCustomerID;
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
    private Button btnChangeCustomer;
    @FXML
    private Button btnUpdateCustomer;
    @FXML
    private Button btnBack;
    @FXML
    private Pane paneModifyCustomer;
    @FXML
    private Pane paneModifyAppointment;
    
    
    private String errorMessage = new String();
    private static int modifyApptCustomerIndex;
    private static Customer modifyApptCustomer;
    private Appointment appointment;
    int modifyAppointmentIndex = MainMenuController.getModifyAppointmentIndex();
    private ObservableList<Customer> customerSelected = FXCollections.observableArrayList();
    

    ObservableList<String> apptTimes = FXCollections.observableArrayList("8:00 AM","9:00 AM","10:00 AM","11:00 AM","12:00 PM","1:00 PM","2:00 PM","3:00 PM","4:00 PM","5:00 PM");
    ObservableList<String> apptLocations = FXCollections.observableArrayList("New York","London","Phoenix","Online");
    ObservableList<String> apptType = FXCollections.observableArrayList("Meeting","Documenting","Planning");

    @FXML
    private void handleBackAction(ActionEvent e){
        if(e.getSource()==btnBack){
            paneModifyAppointment.toFront();
        }
        if(e.getSource()==btnChangeCustomer){
            paneModifyCustomer.toFront();
        }
    }
    
    @FXML
    private void selectCustomerNext(ActionEvent e){
        modifyApptCustomer = tblCustomerAppt.getSelectionModel().getSelectedItem();      
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
        paneModifyAppointment.toFront();
        }
                  
    }        
   
    public static int getAddApptCustomerIndex(){
        return modifyApptCustomerIndex;
    }
    
    @FXML
    void submitModifyCustomer(ActionEvent e){
        
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
        tblCustomerAppt.setItems(CustomerList.getAllCustomers());
    }
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        appointment = AppointmentList.getAllAppointments().get(modifyAppointmentIndex);
        
        String title = appointment.getTitle();
        String description = appointment.getDescription();
        String location = appointment.getLocation();
        String type = appointment.getType();
        String contact = appointment.getContact();
        String urlApt = appointment.getUrl();
        /*
        Date appointmentDate = appointment.getStartDate();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(appointmentDate);
        int appointmentDateYear = calendar.get(Calendar.YEAR);
        int appointmentDateMonth = calendar.get(Calendar.MONTH);
        int appointmentDateDay = calendar.get(Calendar.DAY_OF_MONTH);
        LocalDate appointmentLocalDate = LocalDate.of(appointmentDateYear, appointmentDateMonth, appointmentDateDay);
        Timestamp start = appointment.getStart();
        Timestamp end = appointment.getEnd();        
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a z");
        StringProperty startString = new SimpleStringProperty(formatTime.format(start));
        StringProperty endString = new SimpleStringProperty(formatTime.format(end));    
        */
        int customerID = appointment.getCustomerID();
        ObservableList<Customer> customerList = CustomerList.getAllCustomers();
        for(Customer customer:customerList){
            if(customer.getCustomerID() == customerID){
                customerSelected.add(customer);                
            }
        }        
        txtCustomerID.setText(Integer.toString(customerID));
        txtAppointmentTitle.setText(title);
        txtAppointmentDescription.setText(description);
        cbAppointmentLocation.setValue(location);
        txtAppointmentContact.setText(contact);
        txtAppointmentURL.setText(urlApt);
        /*
        cbAppointmentStart.setValue(startString);
        cbAppointmentEnd.setValue(endString);
        dateAppointmentDate.setValue(appointmentLocalDate);
        */
         
        colCustomerName.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCustomerName()));
        colCustomerAddress.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getAddress1()));
        colCustomerCity.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCity()));
        colCustomerCountry.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCountry()));
        colCustomerPhone.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getPhone()));
        updateCustomerTable();
       
        cbAppointmentStart.setItems(apptTimes);
        cbAppointmentEnd.setItems(apptTimes);
        cbAppointmentLocation.setItems(apptLocations);
        cbAppointmentType.setItems(apptType);
    }    
    
}
