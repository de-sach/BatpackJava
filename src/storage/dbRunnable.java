/*
 * Copyright (C) 2017 sach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package storage;

import battery.BatteryPacket;
import java.io.IOException;
import java.util.Dictionary;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sach
 */
public class dbRunnable implements Runnable {

    private final BatteryPacket batpack;
    private final dbConnector dbcon;
    private int timeout = 30000; //save batpack 2* per minute

    /**
     * A thread runnable used to allow database access in a separate thread to
     * avoid waiting times
     *
     * @param packet the BatteryPacket who's data is to be periodically stored
     * @throws IOException An exception when database access is impossible
     */
    public dbRunnable(BatteryPacket packet) throws IOException {

        this.batpack = packet;
        if (this.batpack != null) {
            dbcon = new dbConnector();
            dbcon.createTable(true);
            System.out.println("size: " + packet.getModuleCount());
        } else {
            dbcon = null;
        }
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("db thread is running");
            storeBatpack();
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException ex) {
                System.out.println("sleep db runnable interrupted");
                Logger.getLogger(dbRunnable.class.getName()).log(Level.WARNING, null, ex);
            }
        }
    }

    /**
     * A function used to store all celldata in the batterypack. This is done by
     * sending the data to the database connector who stores it.
     */
    public void storeBatpack() {
        if (dbcon != null) {
            dbcon.insertBatpack(batpack);
        }
    }

    /**
     * A getter function for the voltage - state of charge lookup table
     *
     * @return the voltage - state of charge lookup table
     */
    public Dictionary getVoltageLookupTable() {
        return dbcon.getVoltageLookupTable();
    }

}
