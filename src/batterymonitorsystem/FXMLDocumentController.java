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
public class FXMLDocumentController implements Initializable {
    
//    @FXML
//    private Label label;
//    
//    @FXML
//    private void handleButtonAction(ActionEvent event) {
//        System.out.println("You clicked me!");
//        label.setText("Hello World!");
//    }
    @FXML
    private void exit(ActionEvent event){
        System.out.println("exiting");
        System.exit(0);      
    }
    
    @FXML
    private Label totalVoltage;

    public Label getTotalVoltage() {
        return totalVoltage;
    }

    public void setTotalVoltage(Label totalVoltage) {
        this.totalVoltage = totalVoltage;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
