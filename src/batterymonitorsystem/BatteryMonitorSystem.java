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

import battery.BatteryCell;
import battery.BatteryPacket;
import communication.MessageBuilder;
import communication.PortMonitor;
import java.io.IOException;
import java.util.Dictionary;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import storage.dbRunnable;

/**
 * Company: Formula Electric Belgium Author: sach Project: Umicore Nova Part:
 * BMS pc Created: Januari 2017
 */
public class BatteryMonitorSystem implements Runnable {

    static BatteryPacket batpack;
    private static PortMonitor portMonitor;
    private static dbRunnable database;
    private static boolean connected;

    static boolean getConnected() {
        return connected;
    }

    static Dictionary getVoltageLookupTable() {
        Dictionary lookupTable;
        if (database == null) {
            try {
                database = new dbRunnable(batpack);
                Thread dbThread = new Thread(database);
                dbThread.start();

            } catch (IOException ex) {
                Logger.getLogger(BatteryMonitorSystem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        lookupTable = database.getVoltageLookupTable();
        return lookupTable;
    }

    private final CountDownLatch latch;

    /**
     * A function to get the BatteryPacket object used in the program
     *
     * @return the main BatteryPacket
     */
    public static BatteryPacket getBatpack() {
        return BatteryMonitorSystem.batpack;
    }

    /**
     * The backend controller of all operations. It is used to control the state
     * of the program and all backend threads.
     *
     * @param latch the latch used to start the program when communication is
     * detected as to not crash the front end with insufficient data
     */
    public BatteryMonitorSystem(CountDownLatch latch) {
        this.latch = latch;
    }

    public BatteryMonitorSystem() {
        this.latch = null;
    }

    @Override
    public void run() {
        try {
            MessageBuilder test = new MessageBuilder();
//            System.out.println(test.buildVoltageMessage(0, 0));
            //SETUP communication
            BatteryMonitorSystem.portMonitor = new PortMonitor(latch);
            portMonitor.setBaudrate(500000);
            Thread monitorThread = new Thread(portMonitor);
            monitorThread.start();
//            try {
//                latch.await();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(BatteryMonitorSystem.class.getName()).log(Level.SEVERE, null, ex);
//            }
            connected = true;
            while (this.batpack == null) {
                batpack = portMonitor.getBatteryPack();
            }
            //SETUP Storage
            BatteryMonitorSystem.database = new dbRunnable(batpack);
            Thread databaseThread = new Thread(BatteryMonitorSystem.database);
            databaseThread.start();
            //MAIN CONTROL LOOP
            while (true) {
                //communication
                System.out.println("Refreshing data");
                portMonitor.refreshBatpack();
                connected = portMonitor.isConnected();
                Thread.sleep(2000);
                batpack = portMonitor.getBatteryPack();
                //storage
                if (connected) {
                    database.storeBatpack();
                }
            }

        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(BatteryMonitorSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
