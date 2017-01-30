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
import battery.BatteryCell;
import battery.BatteryModule;
import com.almworks.sqlite4java.SQLiteException;
import java.io.IOException;

/**
 *
 * @author sach
 */
public class dbRunnable implements Runnable {

    private Thread t;
    private int id;
    private BatteryPacket batpack;
    private dbConnector dbcon;

    public dbRunnable(int id, BatteryPacket packet) throws SQLiteException, IOException {
        this.id = id;
        this.batpack = packet;
        dbcon = new dbConnector();
        dbcon.createTable(false);
        System.out.println("size: " + packet.getModuleCount());

    }

    @Override
    public void run() {
        System.out.println("db thread is running");
        //what the thread should do
//        for (BatteryModule module : batpack.getModules()) {
//            for (BatteryCell cell : module.getBatteryCells()) {
//                dbcon.insertCell(cell);
//            }
//        }
        dbcon.insertBatpack(batpack);
    }
}
