/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*TODO
improve looks
communication speed
error handling
Testing
Documentation
*/
package batterymonitorsystem;

import battery.BatteryPacket;
import com.almworks.sqlite4java.SQLiteException;
import communication.PortMonitor;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static boolean connected;

    static boolean getConnected() {
        return connected;
    }
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
            connected = true;
            batpack = portMonitor.getBatteryPack();
            //SETUP Storage
            BatteryMonitorSystem.database = new dbRunnable(batpack);
            Thread databaseThread = new Thread(BatteryMonitorSystem.database);
            databaseThread.start();
            //MAIN CONTROL LOOP
            while(true){
                //communication
                System.out.println("Refreshing data");
                portMonitor.refreshAll();
                connected = portMonitor.isConnected();
                Thread.sleep(100);
                batpack = portMonitor.getBatteryPack();
                System.out.println("BMS: battery pack module 5 cell 5 voltage: "+batpack.getModules().get(4).getBatteryCells().get(4).getVoltageAsString());
                //storage
                database.storeBatpack();
                
            }
            
        } catch (InterruptedException ex) {
            Logger.getLogger(BatteryMonitorSystem.class.getName()).log(Level.SEVERE, null, ex);
          } catch (SQLiteException ex) {
            Logger.getLogger(BatteryMonitorSystem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BatteryMonitorSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
