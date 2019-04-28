/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author austin.smith
 */
public class AppointmentList {
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();
    
    public static ObservableList<Appointment> getAllAppointments(){
        return allAppointments;
    }
    public static ObservableList<Appointment> getMonthlyAppointments(){
        return monthlyAppointments;
    }
    public static ObservableList<Appointment> getWeeklyAppointments(){
        return weeklyAppointments;
    }
}
