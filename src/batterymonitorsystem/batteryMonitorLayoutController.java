/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batterymonitorsystem;

import battery.BatteryCell;
import battery.BatteryModule;
import battery.BatteryPacket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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
    private List<Group> batteryCells;
    private int selectedModule;

    public int getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(int selectedModule) {
        this.selectedModule = selectedModule;
    }

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

    @FXML
    private Group cell1;

    @FXML
    private Group cell2;

    @FXML
    private Group cell3;

    @FXML
    private Group cell4;

    @FXML
    private Group cell5;

    @FXML
    private Group cell6;

    @FXML
    private Group cell7;

    @FXML
    private Group cell8;

    @FXML
    private Group cell9;

    @FXML
    private Group cell10;

    @FXML
    private Group cell11;

    @FXML
    private Group cell12;

    @FXML
    private Group cell13;

    @FXML
    private Group cell14;

    @FXML
    private Group cell15;

    @FXML
    private Group cell16;

    @FXML
    private MenuButton moduleChooser;
    
    @FXML
    private Accordion accordion;
    
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

        buildMenuItem();

        batteryCells = new ArrayList<>();
        batteryCells.add(cell1);
        batteryCells.add(cell2);
        batteryCells.add(cell3);
        batteryCells.add(cell4);
        batteryCells.add(cell5);
        batteryCells.add(cell6);
        batteryCells.add(cell7);
        batteryCells.add(cell8);
        batteryCells.add(cell9);
        batteryCells.add(cell10);
        batteryCells.add(cell11);
        batteryCells.add(cell12);
        batteryCells.add(cell13);
        batteryCells.add(cell14);
        batteryCells.add(cell15);
        batteryCells.add(cell16);
        
        bindModuleClick();
        
        updateTotalVoltage();
        updateModules();
        accordion.setExpandedPane(batpackOverview);
        
        
    }
    
    public void bindModuleClick(){
        for (int i = 0; i < batteryModules.size();i++){
            selectedModule=i;
            batteryModules.get(i).setOnMouseClicked(openModule);
        }
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
                for (int i = 0; i < this.batteryModules.size() && i < this.batpack.getModuleCount(); i++) {
                    updateModule(this.batteryModules.get(i), this.batpack.getModules().get(i));
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
        Label averageTemperature = (Label) group.getChildren().get(4);

        double progressValue = (module.getVoltage() / 66.666);
        int percentage = (int) (progressValue * 100);

        moduleVolt.setText(module.getVoltageAsString());
        modulePercent.setText(percentage + " %");
        progress.setProgress(progressValue);
        averageTemperature.setText(module.getAverageTemperatureAsString());
    }


    private void buildMenuItem() {
        for (int i=0; i<batteryModules.size();i++) {
            Group module = batteryModules.get(i);
            String labelText = ((Label) module.getChildren().get(3)).getText();
            MenuItem menuItem = new MenuItem(labelText);
            menuItem.setId(Integer.toString(i));
            EventHandler<ActionEvent> updateCells;
            updateCells=(event) -> {
                MenuItem menu = (MenuItem)event.getSource();
                int id = Integer.parseInt(menu.getId());
                updateCells(id);
            };
            menuItem.setOnAction(updateCells);
            List<MenuItem> menuItems = new ArrayList<>();
            menuItems.add(menuItem);
            moduleChooser.getItems().addAll(menuItems);
        }
    }

    private void updateCells(int id) {
        BatteryModule module = this.batpack.getModules().get(id);
        for(int i=0;i<module.getNrOfCells();i++){
            updateCell(this.batteryCells.get(i),module.getBatteryCells().get(i));
        }
    }

    private void updateCell(Group cellDisp, BatteryCell cell) {
        //disp: V, %, progressbar, name
        Label voltage = (Label)cellDisp.getChildren().get(0);
        Label percentage = (Label)cellDisp.getChildren().get(1);
        ProgressBar progressBar = (ProgressBar)cellDisp.getChildren().get(2);
        Label temperature = (Label)cellDisp.getChildren().get(4);
        
        double progress = (cell.getVoltage() / 4.17);
        int percent = (int) (progress * 100);
        
        voltage.setText(cell.getVoltageAsString());
        percentage.setText(percent+" %");
        progressBar.setProgress(progress);
        temperature.setText(cell.getTemperatureAsString());
    }
    
    EventHandler openModule = (EventHandler<MouseEvent>) (MouseEvent event) -> {
        for(int i =0; i<batteryModules.size();i++){
            if(batteryModules.get(i).getChildren().contains(event.getSource())||batteryModules.get(i).equals(event.getSource())){
                setSelectedModule(i);
            }
        }
        updateCells(getSelectedModule());
        accordion.setExpandedPane(ModuleOverview);
        moduleChooser.setText("module "+ (getSelectedModule()+1));
    };
    
}
