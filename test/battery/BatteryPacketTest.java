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
package battery;

import java.util.ArrayList;
import java.util.List;
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
public class BatteryPacketTest {

    private BatteryPacket batpack;

    public BatteryPacketTest() {
        batpack = new BatteryPacket(9);
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
     * Test of updateTotalVoltage method, of class BatteryPacket.
     */
    @Test
    public void testUpdateTotalVoltage() {
        System.out.println("updateTotalVoltage");
        BatteryPacket instance = batpack;
        instance.updateTotalVoltage();
    }

    /**
     * Test of updateAvgTemp method, of class BatteryPacket.
     */
    @Test
    public void testUpdateAvgTemp() {
        System.out.println("updateAvgTemp");
        BatteryPacket instance = batpack;
        instance.updateAvgTemp();
    }

    /**
     * Test of getModuleCount method, of class BatteryPacket.
     */
    @Test
    public void testGetModuleCount() {
        System.out.println("getModuleCount");
        BatteryPacket instance = batpack;
        int expResult = 9;
        int result = instance.getModuleCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTotalVoltage method, of class BatteryPacket.
     */
    @Test
    public void testGetTotalVoltage() {
        System.out.println("getTotalVoltage");
        BatteryPacket instance = batpack;
        double expResult = 0.0;
        double result = instance.getTotalVoltage();
        assertEquals(expResult, result, 0.0);

    }

    /**
     * Test of getTotalVoltageAsString method, of class BatteryPacket.
     */
    @Test
    public void testGetTotalVoltageAsString() {
        System.out.println("getTotalVoltageAsString");
        BatteryPacket instance = batpack;
        String expResult = "0.0 V";
        String result = instance.getTotalVoltageAsString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAverageTemperature method, of class BatteryPacket.
     */
    @Test
    public void testGetAverageTemperature() {
        System.out.println("getAverageTemperature");
        BatteryPacket instance = batpack;
        double expResult = 0.0;
        double result = instance.getAverageTemperature();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of addModule method, of class BatteryPacket.
     */
    @Test
    public void testAddModule() {
        System.out.println("addModule");
        BatteryModule module = new BatteryModule(0, 16);
        BatteryPacket instance = batpack;
        instance.addModule(module);
    }

    /**
     * Test of getAverageTemperatureAsString method, of class BatteryPacket.
     */
    @Test
    public void testGetAverageTemperatureAsString() {
        System.out.println("getAverageTemperatureAsString");
        BatteryPacket instance = batpack;
        BatteryModule module = new BatteryModule(0, 16);
        batpack.addModule(module);
        System.out.println(instance.getAverageTemperature());
        String expResult = "20.0 °C";
        String result = instance.getAverageTemperatureAsString();
        assertEquals(expResult, result);
    }

}
