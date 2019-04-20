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
public class ReportLocationList {
    
    private static ObservableList<ReportLocation> allLocations = FXCollections.observableArrayList();
    
    public static ObservableList<ReportLocation> getAllLocations(){
        return allLocations;
    }
}
