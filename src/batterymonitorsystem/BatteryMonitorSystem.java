/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batterymonitorsystem;

import battery.BatteryPacket;
import communication.PortMonitor;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import storage.dbConnector;
import storage.dbRunnable;

/**
 * @Company: Formula Electric Belgium
 * @Author: sach
 * @Project: Umicore Nova
 * @Part: BMS pc
 * @Created: Januari 2017
 */
public class BatteryMonitorSystem extends Application {

    private static BatteryPacket batpack;
    Parent root;
    
    @Override
    public void start(Stage stage) throws Exception {
//        this.root = FXMLLoader.load(getClass().getResource("batteryMonitorLayout.fxml"));
        Random random = new Random();
//        
//        batteryCell testCell = new batteryCell(20.0, 3.5, 1, 10);
//        batteryModule testModule = new batteryModule(1, 15);
//        conn.setParams(testCell);
//        for (batteryCell cell:testModule.getBatteryCells()){
//            conn.setParams(cell);
//        }
        //create random test battery packet & save to db
        /*batpack = new BatteryPacket(9);
        for (int module = 0; module < 9; module++) {
            BatteryModule mod = new BatteryModule(module, 16);
            batpack.addModule(mod);
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
        */
        dbRunnable r = new dbRunnable(1, batpack);
        Thread t = new Thread(r);
        System.out.println(batpack.getTotalVoltage());
        System.out.println(batpack.getTotalVoltageAsString());
        System.out.println(batpack);

        this.root = FXMLLoader.load(getClass().getResource("batteryMonitorLayout.fxml"));
        Scene scene = new Scene(root);

        dbConnector conn = new dbConnector();
        t.start();

        this.load();

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(1);
        PortMonitor portMonitor = new PortMonitor(latch);
        
        Thread monitorThread = new Thread(portMonitor);
        monitorThread.start();
        try {
            latch.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(BatteryMonitorSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        batpack = portMonitor.getBatteryPack();
        System.out.println(batpack.getModuleCount());
        launch(args);
        
    }

    private void load() {
    }

    public static BatteryPacket getBatpack() {
        return BatteryMonitorSystem.batpack;
    }

}
