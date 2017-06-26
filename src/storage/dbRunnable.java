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

/**
 *
 * @author sach
 */
public class dbRunnable implements Runnable {

    private final BatteryPacket batpack;
    private final dbConnector dbcon;

    public dbRunnable(BatteryPacket packet) throws IOException {
        this.batpack = packet;
        dbcon = new dbConnector();
        dbcon.createTable(false);
        System.out.println("size: " + packet.getModuleCount());
    }

    @Override
    public void run() {
        System.out.println("db thread is running");
    }

    public void storeBatpack() {
        dbcon.insertBatpack(batpack);
    }

    public Dictionary getVoltageLookupTable() {
        return dbcon.getVoltageLookupTable();
    }

}
