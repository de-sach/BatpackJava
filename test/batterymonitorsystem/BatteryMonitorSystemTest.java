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
package batterymonitorsystem;

import battery.BatteryPacket;
import java.util.concurrent.CountDownLatch;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter
 */
public class BatteryMonitorSystemTest {

    private BatteryMonitorSystem bms;

    public BatteryMonitorSystemTest() {
        CountDownLatch latch = new CountDownLatch(0);
        bms = new BatteryMonitorSystem(latch);
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
     * Test of getConnected method, of class BatteryMonitorSystem.
     */
    @Test
    public void testGetConnected() {
        System.out.println("getConnected");
        boolean expResult = false;
        boolean result = bms.getConnected();
        assertEquals(expResult, result);
    }

    /**
     * Test of getBatpack method, of class BatteryMonitorSystem.
     */
    @Test
    public void testGetBatpack() {
        System.out.println("getBatpack");
        BatteryPacket expResult = null;
        BatteryPacket result = bms.getBatpack();
        assertEquals(expResult, result);

    }

    /**
     * Test of run method, of class BatteryMonitorSystem.
     */
}
