/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batterymonitorsystem;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author sach
 */
public class batteryMonitorLayoutController implements Initializable {
    @FXML
    private void exit(ActionEvent event){
        System.out.println("exiting");
        System.exit(0);
    }
    
    @FXML
    private void showAbout(ActionEvent event){
        System.out.println("this is some software thingy");
    }
    
    @FXML
    private void showConnected(ActionEvent e){
        System.out.println("is it connected?");
        //TODO
    }
    
    @FXML
    private void connect(ActionEvent event){
        try {
            System.out.println("connecting");
            //TODO
            //connectToBatterypack(); 
        } catch (Exception e) {
            System.out.println("not connected error");
        }
    }
    
    @FXML
    public Label totalVoltage;

    public Label getTotalVoltage() {
        return totalVoltage;
    }

    public void setTotalVoltage(Label totalVoltage) {
        this.totalVoltage = totalVoltage;
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
