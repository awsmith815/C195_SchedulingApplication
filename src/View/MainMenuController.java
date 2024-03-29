package View;

import Model.Customer;
import Model.SQL_Customer;
import Model.CustomerList;
import Model.SQL_Appointment;
import Model.Appointment;
import Model.AppointmentList;
import Model.ReportCustomer;
import Model.ReportCustomerList;
import Model.ReportLocation;
import Model.ReportLocationList;
import Model.ReportType;
import Model.ReportTypeList;
import Model.SQL_Reporting;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
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
    private Label lblUpcomingAppt;
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
    private TableColumn<Appointment,String> colAppointmentStart;
    @FXML
    private TableColumn<Appointment,String> colAppointmentEnd;
    
    @FXML
    private TableView<ReportType> tblReport1;
    @FXML
    private TableColumn<ReportType, String> colReport1Month;
    @FXML
    private TableColumn<ReportType, Integer> colReport1Year;
    @FXML
    private TableColumn<ReportType, String> colReport1Description;
    @FXML
    private TableColumn<ReportType, Integer> colReport1NumAppts;
    @FXML
    private ComboBox<String> cbMonths;
    
    @FXML
    private TextField search;
    @FXML
    private TableView<ReportCustomer> tblReport2;
    @FXML
    private TableColumn<ReportCustomer,LocalDate> colReport2ApptDate;
    @FXML
    private TableColumn<ReportCustomer,String> colReport2ApptTime;
    @FXML
    private TableColumn<ReportCustomer,String> colReport2CustomerName;
    @FXML
    private TableColumn<ReportCustomer,String> colReport2ApptTitle;
    @FXML
    private TableColumn<ReportCustomer,String> colReport2ApptLocation;
    @FXML
    private TableColumn<ReportCustomer,String> colReport2ApptContact;
    
    @FXML
    private TableView<ReportLocation> tblReport3;
    @FXML
    private TableColumn<ReportLocation, String> colReport3Location;
    @FXML
    private TableColumn<ReportLocation, Integer> colReport3NumApptAT;
    @FXML
    private TableColumn<ReportLocation, Integer> colReport3NumUpcomingAppt;
    @FXML
    private TableColumn<ReportLocation, Integer> colReport3NumCustomers;
    
    
    private static int modifyCustomerIndex;
    private static Customer modifyCustomer;
    private static int modifyAppointmentIndex;
    private static Appointment modifyAppointment;
    
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
        
    private void mainMenuApptAlert(){
        int numAppts = SQL_Appointment.appointmentsAlert();
        if(numAppts==0){
            lblUpcomingAppt.setText("You have no appointments in the next 15 minutes!");
        }else if(numAppts==1){
            lblUpcomingAppt.setText("You have "+numAppts+" appointment in the next 15 minutes!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Appointment Alert");
            alert.setHeaderText("Appointment Alert");
            alert.setContentText("You have "+numAppts+" appointment in the next 15 minutes!");
            alert.showAndWait();
            return;
        }else if(numAppts>1){
            lblUpcomingAppt.setText("You have "+numAppts+" appointments in the next 15 minutes!");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Appointment Alert");
            alert.setHeaderText("Appointment Alert");
            alert.setContentText("You have "+numAppts+" appointments in the next 15 minutes!");
            alert.showAndWait();
        }
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
            alert.setContentText("No Customer Selected to Modify");
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
        modifyAppointment = tblAppointment.getSelectionModel().getSelectedItem();      
        if(modifyAppointment == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("No Appointment Selected to Modify");
            alert.showAndWait();
            return;
        }        
        modifyAppointmentIndex = AppointmentList.getAllAppointments().indexOf(modifyAppointment);
        try{
            Parent modifyAppointmentParent = FXMLLoader.load(getClass().getResource("ModifyAppointment.fxml"));
            Scene modifyAppointmentScene = new Scene(modifyAppointmentParent);
            Stage modifyAppointmentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            modifyAppointmentStage.setScene(modifyAppointmentScene);
            modifyAppointmentStage.show();
        }catch(IOException exc){
            System.out.println(exc.getMessage());
            System.out.println(exc.getLocalizedMessage());
            System.out.println(exc.initCause(exc));
            
        }
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
    void detailedSchedule(ActionEvent e){
        try{
            Parent detailedScheduleParent = FXMLLoader.load(getClass().getResource("DetailedSchedule.fxml"));
            Scene detailedScheduleScene = new Scene(detailedScheduleParent);
            Stage detailedScheduleStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            detailedScheduleStage.setScene(detailedScheduleScene);
            detailedScheduleStage.show();
        }catch(IOException exc){
            System.out.println(exc.getMessage());
            System.out.println(exc.getLocalizedMessage());
            System.out.println(exc.initCause(exc));

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
       
    public static int getModifyAppointmentIndex(){
        return modifyAppointmentIndex;
    }
    
    public void updateAppointmentTable(){
        SQL_Appointment.updateAllAppointments();
        tblAppointment.setItems(AppointmentList.getAllAppointments());
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Reporting
    //Report 1    
    
    
    public void updateReport1(){
        SQL_Reporting.apptByType();
        tblReport1.setItems(ReportTypeList.getAllTypes());
        
    }
    
    
    
    //Report 2
    //Use lambda function to filter list 
    FilteredList filterReport2 = new FilteredList(ReportCustomerList.getAllCustomersReport(), e->true);
    @FXML 
    private void search(KeyEvent event) {
       search.textProperty().addListener((observable, oldValue, newValue) ->{
           filterReport2.setPredicate((Predicate<? super ReportCustomer>) (ReportCustomer rc)->{
               if(newValue.isEmpty() || newValue==null){
                   return true;
               }else if(rc.getCustomerName().toLowerCase().contains(newValue.toLowerCase())){
                   return true;
               }               
               return false;
           });
       });
       SortedList sort = new SortedList(filterReport2);
       sort.comparatorProperty().bind(tblReport2.comparatorProperty());
       tblReport2.setItems(sort);
    }
    
    public void updateReport2(){
        SQL_Reporting.customerSchedule();        
        tblReport2.setItems(ReportCustomerList.getAllCustomersReport());
        colReport2ApptDate.setComparator(colReport2ApptDate.getComparator().reversed());
        tblReport2.getSortOrder().add(colReport2ApptDate); 
    }

    
    //Report 3
    public void updateReport3Location(){
        SQL_Reporting.locationReport();
        tblReport3.setItems(ReportLocationList.getAllLocations());
        colReport3NumApptAT.setComparator(colReport3NumApptAT.getComparator().reversed());
        tblReport3.getSortOrder().add(colReport3NumApptAT);
    }
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //initialize customer
        colCustomerName.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCustomerName()));
        colAddress.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getAddress1()));
        colCity.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCity()));
        colCountry.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCountry()));
        colPhone.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getPhone()));
        updateCustomerTable();
        //initialize appointment
        colAppointmentTitle.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getTitle()));
        colAppointmentLocation.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getLocation()));        
        colAppointmentContact.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getContact()));
        colAppointmentStart.setCellValueFactory(appointment -> new SimpleObjectProperty(appointment.getValue().getStartString()));
        colAppointmentEnd.setCellValueFactory(appointment -> new SimpleObjectProperty(appointment.getValue().getEndString()));
        updateAppointmentTable();
        //initialize report1
        colReport1Month.setCellValueFactory(reportType -> new SimpleStringProperty(reportType.getValue().getMonth()));
        colReport1Year.setCellValueFactory(reportType -> new SimpleIntegerProperty(reportType.getValue().getYear()).asObject());
        colReport1Description.setCellValueFactory(reportType -> new SimpleStringProperty(reportType.getValue().getDescription()));
        colReport1NumAppts.setCellValueFactory(reportType -> new SimpleIntegerProperty(reportType.getValue().getNumAppts()).asObject());
        updateReport1();
        //initialize report2       
        colReport2ApptDate.setCellValueFactory(reportCustomer -> new SimpleObjectProperty(reportCustomer.getValue().getApptDate()));
        colReport2ApptTime.setCellValueFactory(reportCustomer -> new SimpleStringProperty(reportCustomer.getValue().getApptTime()));
        colReport2CustomerName.setCellValueFactory(reportCustomer -> new SimpleStringProperty(reportCustomer.getValue().getCustomerName()));
        colReport2ApptTitle.setCellValueFactory(reportCustomer -> new SimpleStringProperty(reportCustomer.getValue().getTitle()));
        colReport2ApptLocation.setCellValueFactory(reportCustomer -> new SimpleStringProperty(reportCustomer.getValue().getLocation()));
        colReport2ApptContact.setCellValueFactory(reportCustomer -> new SimpleStringProperty(reportCustomer.getValue().getContact()));
        updateReport2();        
        //initialize report3
        colReport3Location.setCellValueFactory(reportLocation -> new SimpleStringProperty(reportLocation.getValue().getLocation()));
        colReport3NumApptAT.setCellValueFactory(reportLocation -> new SimpleIntegerProperty(reportLocation.getValue().getNumApptAT()).asObject());
        colReport3NumUpcomingAppt.setCellValueFactory(reportLocation -> new SimpleIntegerProperty(reportLocation.getValue().getNumUpcomingAppt()).asObject());
        colReport3NumCustomers.setCellValueFactory(reportLocation -> new SimpleIntegerProperty(reportLocation.getValue().getNumCustomers()).asObject());
        updateReport3Location();
        mainMenuApptAlert();
               
    }    

}
