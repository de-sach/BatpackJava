/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batterymonitorsystem;

/*
temperatuur
connected at disconnect when running
connected kleur -__-
 */
import battery.BatteryCell;
import battery.BatteryModule;
import battery.BatteryPacket;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author sach
 */
public class batteryMonitorLayoutController implements Initializable {

    private BatteryPacket batpack;
    private List<Group> batteryModules;
    private List<Group> batteryCells;
    private int selectedModule;
    private static double xOffset;
    private static double yOffset;
    private boolean maximized;
    @FXML
    private MenuItem exitMenuItem;

    public int getSelectedModule() {
        return selectedModule;
    }

    public void setSelectedModule(int selectedModule) {
        this.selectedModule = selectedModule;
    }

    //MENUBAR
    @FXML
    private AnchorPane menuPane;

    @FXML
    private MenuBar menuBar;

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

    @FXML
    private ImageView formulaLogo;
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
        //System.out.println("exiting");
        System.exit(0);
    }

    @FXML
    private void showAbout(ActionEvent event) {
        System.out.println("this is some software thingy");
    }

    private void connect(MouseEvent event) {
        //System.out.println("test connect");
        updateTotalVoltage();
    }

    @FXML
    private void minimize(ActionEvent event) {
        Stage stage = (Stage) menuPane.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void maximize(ActionEvent event) {
        if (!maximized) {
            Stage stage = (Stage) menuPane.getScene().getWindow();
            stage.setMaximized(true);
            maximized = true;
        } else {
            Stage stage = (Stage) menuPane.getScene().getWindow();
            stage.setMaximized(false);
            maximized = false;
        }
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

    @FXML
    private Circle connectedIndicator;

    //Critical information
    @FXML
    private Group highVoltage;

    @FXML
    private Group lowVoltage;

    @FXML
    private Group highTemp;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.batpack = BatteryMonitorSystem.getBatpack();

//        System.out.println("batpack votlage = "+batpack.getTotalVoltageAsString());
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
        if (this.batpack != null) {
            buildMenuItem();
        }
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
        if (this.batpack != null) {
            for (int i = batpack.getModuleCount(); i < batteryModules.size(); i++) {
                batteryModules.get(i).setVisible(false);
            }
        }

        setIcon();

        bindModuleClick();
        checkConnection();

        bindWindowDrag();

        bindWebsite();

        updateTotalVoltage();
        updateModules();

        bindUpdates();

        accordion.setExpandedPane(batpackOverview);
    }

    public void bindModuleClick() {
        for (int i = 0; i < batteryModules.size(); i++) {
            selectedModule = i;
            batteryModules.get(i).setOnMouseClicked(openModule);
        }
    }

    private void updateTotalVoltage() {
        if (this.batpack != null) {
            double progress = ((this.batpack.getTotalVoltage() - 432) / (600 - 432)); //only real range (3V * 144 cells)
//            System.out.println("progress= " + progress);
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
        //System.out.println("updating modules");
        if (this.batpack != null) {
            if (this.batpack.getModuleCount() > 9) {
                System.out.println("batpack not compatible with 2017 layout, " + this.batpack.getModuleCount() + " modules found");
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
        //System.out.println(group.getChildren());
        //voltage, percentage, progressbar, name(is ok)
        Label moduleVolt = (Label) group.getChildren().get(0);
        Label modulePercent = (Label) group.getChildren().get(1);
        ProgressBar progress = (ProgressBar) group.getChildren().get(2);
        Label averageTemperature = (Label) group.getChildren().get(4);

        double progressValue = ((module.getVoltage() - 48) / (66.666 - 48));
        int percentage = (int) (progressValue * 100);

        moduleVolt.setText(module.getVoltageAsString());
        modulePercent.setText(percentage + " %");
        progress.setProgress(progressValue);
//        if(module.getId()==1){
//            System.out.println("module 1 average temp: "+module.getAverageTemperatureAsString());
//        }
        averageTemperature.setText(module.getAverageTemperatureAsString());
    }

    private void buildMenuItem() {
        for (int i = 0; i < this.batpack.getModuleCount(); i++) {
            Group module = batteryModules.get(i);
            String labelText = ((Label) module.getChildren().get(3)).getText();
            MenuItem menuItem = new MenuItem(labelText);
            menuItem.setId(Integer.toString(i));
            EventHandler<ActionEvent> updateCells;
            updateCells = (event) -> {
                MenuItem menu = (MenuItem) event.getSource();
                int id = Integer.parseInt(menu.getId());
                moduleChooser.setText("module " + (id + 1));
                updateCells(id);
            };
            menuItem.setOnAction(updateCells);
            List<MenuItem> menuItems = new ArrayList<>();
            menuItems.add(menuItem);
            moduleChooser.getItems().addAll(menuItems);
        }
    }

    private void updateCells(int id) {
        if (this.batpack != null) {
            BatteryModule module = this.batpack.getModules().get(id);
            synchronized (module) {
                Iterator i = module.getBatteryCells().iterator();
                while(i.hasNext()){
                    BatteryCell cell = (BatteryCell) i.next();
                    int cellNr = cell.getId();
                    updateCell(this.batteryCells.get(cellNr),cell);
                }
//                for (int i = 0; i < module.getNrOfCells(); i++) {
//                    System.out.println("this cells:" + this.batteryCells.get(i).toString());
//                    System.out.println("module cells:" + module.getBatteryCells().get(i).getId());
//                    updateCell(this.batteryCells.get(i), module.getBatteryCells().get(i));
//                }
            }
        }
    }

    private void updateCell(Group cellDisp, BatteryCell cell) {
        //disp: V, %, progressbar, name
        Label voltage = (Label) cellDisp.getChildren().get(0);
        Label percentage = (Label) cellDisp.getChildren().get(1);
        ProgressBar progressBar = (ProgressBar) cellDisp.getChildren().get(2);
        Label temperature = (Label) cellDisp.getChildren().get(4);

        double progress = (double) cell.getStateOfCharge();
        int percent = (int) (progress);

        voltage.setText(cell.getVoltageAsString());
        percentage.setText(percent + " %");
        progressBar.setProgress(progress / 100);
//        System.out.println("cell id: " + cell.getId());
        temperature.setText(cell.getTemperatureAsString());
    }

    EventHandler openModule = (EventHandler<MouseEvent>) (MouseEvent event) -> {
        for (int i = 0; i < batteryModules.size(); i++) {
            if (batteryModules.get(i).getChildren().contains(event.getSource()) || batteryModules.get(i).equals(event.getSource())) {
                setSelectedModule(i);
            }
        }
        updateCells(getSelectedModule());
        accordion.setExpandedPane(ModuleOverview);
        moduleChooser.setText("module " + (getSelectedModule() + 1));
    };

    private void bindUpdates() {
        final KeyFrame oneFrame = new KeyFrame(Duration.seconds(1), (ActionEvent evt) -> {
            if (this.batpack != null) {
                this.batpack = BatteryMonitorSystem.getBatpack();
                //System.out.println("layout: battery pack module 5 cell 5 voltage: " + batpack.getModules().get(4).getBatteryCells().get(4).getVoltageAsString());
                checkConnection();
                updateModules();
                updateTotalVoltage();
                updateTotalTemperature();
            }

        });
        Timeline timer = TimelineBuilder.create().cycleCount(Animation.INDEFINITE).keyFrames(oneFrame).build();
        timer.playFromStart();
    }

    private void checkConnection() {
        if (BatteryMonitorSystem.getConnected()) {
            Color c = Color.CHARTREUSE;
            connectedIndicator.setFill(c);
        } else {
            Color c = Color.RED;
            connectedIndicator.setFill(c);
        }
    }

    private void bindWindowDrag() {
        menuBar.setOnMousePressed((MouseEvent event) -> {
            Stage stage = (Stage) menuPane.getScene().getWindow();
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        menuBar.setOnMouseDragged((MouseEvent event) -> {
            Stage stage = (Stage) menuPane.getScene().getWindow();
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
    }

    private void bindWebsite() {
        formulaLogo.setOnMouseClicked((event) -> {
            try {
                Desktop.getDesktop().browse(new URI("http://www.formulaelectric.be"));
            } catch (IOException | URISyntaxException ex) {
                Logger.getLogger(batteryMonitorLayoutController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void setIcon() {

    }

    private void updateTotalTemperature() {
        if (this.batpack != null) {
            totalTemperature.setText(batpack.getAverageTemperatureAsString());
        }
    }

    private void updateCriticalValues(){
      if(this.batpack!=null){
        synchronized(this.batpack){
          BatteryCell maxVoltageCell = new BatteryCell(00.00, 3.00, 0 , 0);//0°C, 3V
          BatteryCell maxTemperatureCell = new BatteryCell(00.00,0.00,0,0);
          BattertCell minVoltageCell = new BatteryCell(00.00,50.00,0,0);
          for(BatteryModule module : this.batpack.getModules()){
            for(BatteryCell cell: module.getBatteryCells()){
              if(cell.getVoltage()>maxCell){
                maxCell = cell;
              }
              if(cell.getVoltage()<minVoltageCell){
                minVoltageCell = cell;
              }
              if(cell.getTemperature() > maxTemperatureCell){
                maxTemperatureCell = cell;
              }
            }
          }
        }
        Label highVoltageIdLabel = (Label) highVoltage.getChildren().get(1);
        Label highVoltageVoltageLabel = (Label) highVoltage.getChildren().get(2);
        highVoltageIdLabel.setText(maxVoltageCell.getId().toString());
        highVoltageVoltageLabel.setText(maxVoltageCell.getVoltageAsString());

        Label lowVoltageIdLabel = (Label) lowVoltage.getChildren().get(1);
        Label lowVoltageVoltageLabel = (Label) lowVoltage.getChildren().get(2);
        lowVoltageIdLabel.setText(minVoltageCell.getId().toString());
        lowVoltageVoltageLabel.setText(minVoltageCell.getVoltageAsString());

        Label maxTempIdLabel = (Label) highTemp.getChildren().get(1);
        Label maxTempTempLabel = (Label) highTemp.getChildren().get(2);
        maxTempIdLabel.setText(maxTemperatureCell.getId().toString());
        maxTempTempLabel.setText(maxTemperatureCell.getTemperatureAsString());
      }
    }

}
