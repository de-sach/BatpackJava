/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batterymonitorsystem;

import battery.BatteryCell;
import battery.BatteryModule;
import battery.BatteryPacket;
import java.util.Random;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import storage.dbConnector;
import storage.dbRunnable;

/**
 *
 * @author sach
 */
public class BatteryMonitorSystem extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("batteryMonitorLayout.fxml"));

        Scene scene = new Scene(root);

        Random random = new Random();

        dbConnector conn = new dbConnector();
//        
//        batteryCell testCell = new batteryCell(20.0, 3.5, 1, 10);
//        batteryModule testModule = new batteryModule(1, 15);
//        conn.setParams(testCell);
//        for (batteryCell cell:testModule.getBatteryCells()){
//            conn.setParams(cell);
//        }

        //create random test battery packet & save to db
        BatteryPacket testPack = new BatteryPacket(10);
        for (int module = 0; module < 10; module++) {
            BatteryModule mod = new BatteryModule(module, 10);
            testPack.addModule(mod);
            mod.getBatteryCells().stream().map((cell) -> {
                cell.setHealth(random.nextInt(10));
                return cell;
            }).map((cell) -> {
                cell.setId(random.nextInt(5000));
                return cell;
            }).map((cell) -> {
                cell.setTemperature(random.nextDouble() * 10 + 20);
                return cell;
            }).forEachOrdered((cell) -> {
                cell.setVoltage(3 + random.nextFloat());
            });
        }
        dbRunnable r = new dbRunnable(1, testPack);
        Thread t = new Thread(r);
        t.start();

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
