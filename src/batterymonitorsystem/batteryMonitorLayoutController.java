/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batterymonitorsystem;

import battery.BatteryModule;
import battery.BatteryPacket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author sach
 */
public class batteryMonitorLayoutController implements Initializable {

    private BatteryPacket batpack;
    private List<Group> batteryModules;

    //HEADER
    @FXML
    private Label connected;

    @FXML
    private ProgressBar totalVoltageProgress;

    @FXML
    private Label totalVoltagePercentage;

    @FXML
    private Label totalVoltage;

    @FXML
    private Label totalTemperature;

    //ACCORDEON
    @FXML
    private TitledPane batpackOverview;

    @FXML
    private TitledPane ModuleOverview;

    @FXML
    private VBox batpackVbox;

    @FXML
    private HBox batpackHbox1;

    @FXML
    private HBox batpackHbox2;

    @FXML
    private HBox batpackHbox3;

    @FXML
    private Group Module1;

    @FXML
    private Group Module2;

    @FXML
    private Group Module3;

    @FXML
    private Group Module4;

    @FXML
    private Group Module5;

    @FXML
    private Group Module6;

    @FXML
    private Group Module7;

    @FXML
    private Group Module8;

    @FXML
    private Group Module9;

    //MENUBAR
    @FXML
    private void exit(ActionEvent event) {
        System.out.println("exiting");
        System.exit(0);
    }

    @FXML
    private void showAbout(ActionEvent event) {
        System.out.println("this is some software thingy");
    }

    @FXML
    private void connect(MouseEvent event) {
        System.out.println("test connect");
        updateTotalVoltage();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.batpack = BatteryMonitorSystem.getBatpack();
        batteryModules = new ArrayList<>();
        batteryModules.add(Module1);
        batteryModules.add(Module2);
        batteryModules.add(Module3);
        batteryModules.add(Module4);
        batteryModules.add(Module5);
        batteryModules.add(Module6);
        batteryModules.add(Module7);
        batteryModules.add(Module8);
        batteryModules.add(Module9);

        updateTotalVoltage();
        updateModules();
    }

    private void updateTotalVoltage() {
        if (this.batpack != null) {
            double progress = (this.batpack.getTotalVoltage() / 600);
            int percentage = (int) (progress * 100);

            totalVoltageProgress.setProgress(progress);
            totalVoltagePercentage.setText(percentage + "%");
            totalVoltage.setText(this.batpack.getTotalVoltageAsString());
            totalTemperature.setText(this.batpack.getAverageTemperatureAsString());
        } else {
            System.out.println("batpack is null");
        }
    }

    private void updateModules() {
        if (this.batpack != null) {
            if (this.batpack.getModuleCount() > 9) {
                System.out.println("batpack not compatible with 2017 layout");
            } else {
                for (int i = 0; i < this.batteryModules.size()&&i<this.batpack.getModuleCount(); i++) {
                    updateModule(this.batteryModules.get(i),this.batpack.getModules().get(i));
                }
            }
        } else {
            System.out.println("no batpack");
        }
    }

    private void updateModule(Group group, BatteryModule module) {
        System.out.println(group.getChildren());
        //voltage, percentage, progressbar, name(is ok)
        Label moduleVolt = (Label) group.getChildren().get(0);
        Label modulePercent = (Label) group.getChildren().get(1);
        ProgressBar progress = (ProgressBar) group.getChildren().get(2);
        
        double progressValue = (module.getVoltage() / 66.666);
        int percentage = (int) (progressValue * 100);
        
        moduleVolt.setText(module.getVoltageAsString());
        modulePercent.setText(percentage+" %");
        progress.setProgress(progressValue);
    }

}
