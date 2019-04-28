package View;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Util.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;

/**
 *
 * @author austin.smith
 */
public class SchedulingApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Locale.setDefault(new Locale.Builder().setLanguage("de").setRegion("DE").build());
        //Locale.setDefault(new Locale.Builder().setLanguage("es").setRegion("MX").build());
        primaryStage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Database.connect();
        launch(args);
        Database.disconnect();
    }
    
}
