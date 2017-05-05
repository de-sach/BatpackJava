/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batterymonitorsystem;

import battery.BatteryPacket;
import com.almworks.sqlite4java.SQLiteException;
import communication.PortMonitor;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.scene.Parent;
import storage.dbConnector;
import storage.dbRunnable;

/**
 * @Company: Formula Electric Belgium
 * @Author: sach
 * @Project: Umicore Nova
 * @Part: BMS pc
 * @Created: Januari 2017
 */
public class BatteryMonitorSystem implements Runnable{

    private static BatteryPacket batpack;
    private static PortMonitor portMonitor;
    private static dbRunnable database;
    private final CountDownLatch latch;
    
    public static BatteryPacket getBatpack() {
        return BatteryMonitorSystem.batpack;
    }
    public BatteryMonitorSystem(CountDownLatch latch){
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            //SETUP communication
            BatteryMonitorSystem.portMonitor = new PortMonitor(latch);
            portMonitor.setBaudrate(500000);
            Thread monitorThread = new Thread(portMonitor);
            monitorThread.start();
            try {
                latch.await();
            } catch (InterruptedException ex) {
                Logger.getLogger(BatteryMonitorSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
            batpack = portMonitor.getBatteryPack();
            //SETUP Storage
            BatteryMonitorSystem.database = new dbRunnable(1, batpack);
            Thread databaseThread = new Thread(BatteryMonitorSystem.database);
            databaseThread.start();
            //MAIN CONTROL LOOP
            while(true){
                System.out.println("Refreshing data");
                portMonitor.refreshAll();
                Thread.sleep(5000);
                batpack = portMonitor.getBatteryPack();
            }
            
        } catch (SQLiteException | IOException | InterruptedException ex) {
            Logger.getLogger(BatteryMonitorSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
