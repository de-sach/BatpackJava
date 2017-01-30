/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batterymonitorsystem;

import battery.batteryCell;
import battery.batteryModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import storage.dbConnector;

/**
 *
 * @author sach
 */
public class BatteryMonitorSystem extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("batteryMonitorLayout.fxml"));
        
        Scene scene = new Scene(root);
        dbConnector connector = new dbConnector();
        
        batteryCell testCell = new batteryCell(20.0, 3.5, 1, 10);
        batteryModule testModule = new batteryModule(1, 15);
        connector.setParams(testCell);
        for (batteryCell cell:testModule.getBatteryCells()){
            connector.setParams(cell);
        }
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
