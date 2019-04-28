/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Appointment;
import Model.AppointmentList;
import Model.SQL_Appointment;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author austin.smith
 */
public class DetailedScheduleController implements Initializable {
    
    @FXML
    private TableView<Appointment> tbMonthlyView;
    //@FXML
    //private TableColumn<Appointment, String> colDayOfWeek;
    @FXML
    private TableColumn<Appointment, String> colTime;
    @FXML
    private TableColumn<Appointment, String> colTitle;
    @FXML
    private TableColumn<Appointment, String> colDescription;
    @FXML
    private TableColumn<Appointment, String> colContact;
    @FXML
    private ComboBox<String> cbMonth;
    
    @FXML
    private TableView<Appointment> tbWeeklyView;
    @FXML
    private TableColumn<Appointment, String> colWDay;
    @FXML
    private TableColumn<Appointment, String> colWTime;
    @FXML
    private TableColumn<Appointment, String> colWTitle;
    @FXML
    private TableColumn<Appointment, String> colWDescription;
    @FXML
    private TableColumn<Appointment, String> colWContact;
    @FXML
    private Button btnThisWeek;
    @FXML
    private Button btnNextWeek;
    @FXML
    private Button btnPrevWeek;
    @FXML
    private Button btnFourWeeks;
    
    ObservableList<String> monthsCombo = FXCollections.observableArrayList("January","February","March","April","May","June","July","August","September","October","Novemeber","December");   
    
    
    @FXML
    private void searchMonth(ActionEvent e){
        tbMonthlyView.getItems().clear();
        String monthSearch = cbMonth.getValue();
        SQL_Appointment.updateMonthlyView(monthSearch);
        //col1CustomerName.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getCustomerName()));        
        colTime.setCellValueFactory(appointment -> new SimpleObjectProperty(appointment.getValue().getStart()));
        colTitle.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getTitle()));
        colDescription.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getDescription()));
        colContact.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getContact()));
        tbMonthlyView.setItems(AppointmentList.getMonthlyAppointments());
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //Weekly View
    public static int numWeeks;
    @FXML
    private void searchWeeks(ActionEvent e){
        tbWeeklyView.getItems().clear();        
        if(e.getSource()==btnThisWeek){           
            numWeeks = 0;
        }else if(e.getSource()==btnNextWeek){
            numWeeks = 1;
        }else if(e.getSource()==btnPrevWeek){
            numWeeks = -1;
        }else if(e.getSource()==btnFourWeeks){
            numWeeks = 4;
        }        
        SQL_Appointment.updateWeeklyView(numWeeks);
        colWDay.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getDayNameStart()));
        colWTime.setCellValueFactory(appointment -> new SimpleObjectProperty(appointment.getValue().getStart()));
        colWTitle.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getTitle()));
        colWDescription.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getDescription()));
        colWContact.setCellValueFactory(appointment -> new SimpleStringProperty(appointment.getValue().getContact()));
        tbWeeklyView.setItems(AppointmentList.getWeeklyAppointments());
        colWTime.setComparator(colWTime.getComparator());
        tbWeeklyView.getSortOrder().add(colWTime);

    }
    
    
    
    
    
    
    @FXML
    void exitDetailView(ActionEvent e) throws IOException{
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
        
        cbMonth.setItems(monthsCombo);
    }    
    
}
