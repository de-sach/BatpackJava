/*
 * Copyright (C) 2017 Peter
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

import battery.BatteryCell;
import battery.BatteryModule;
import battery.BatteryPacket;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Peter
 */
public class dbConnectorTest {

    public dbConnectorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createTable method, of class dbConnector.
     */
    @Test
    public void testCreateTable() {
        System.out.println("createTable");
        boolean exists = false;
        dbConnector instance = new dbConnector();
        instance.createTable(exists);
    }

    /**
     * Test of insertCell method, of class dbConnector.
     */
    @Test
    public void testInsertCell() {
        System.out.println("insertCell");
        BatteryCell cell = new BatteryCell(20, 4.2, 0, 10);
        dbConnector instance = new dbConnector();
        instance.insertCell(cell);
    }

    /**
     * Test of insertModule method, of class dbConnector.
     */
    @Test
    public void testInsertModule() {
        System.out.println("insertModule");
        BatteryModule module = new BatteryModule(0, 16);
        dbConnector instance = new dbConnector();
        instance.insertModule(module);
    }

    /**
     * Test of insertBatpack method, of class dbConnector.
     */
    @Test
    public void testInsertBatpack() {
        System.out.println("insertBatpack");
        BatteryPacket packet = new BatteryPacket(9);
        BatteryModule module = new BatteryModule(0, 16);
        packet.addModule(module);
        dbConnector instance = new dbConnector();
        instance.insertBatpack(packet);
    }

}
