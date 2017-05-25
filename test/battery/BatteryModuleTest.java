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
public class BatteryModuleTest {

    private BatteryModule testModule;

    public BatteryModuleTest() {
        testModule = new BatteryModule(1, 16);
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
     * Test of isBalance method, of class BatteryModule.
     */
    @Test
    public void testIsBalance() {
        System.out.println("isBalance");
        BatteryModule instance = testModule;
        boolean expResult = false;
        boolean result = instance.isBalance();
        assertEquals(expResult, result);
    }

    /**
     * Test of updateStatus method, of class BatteryModule.
     */
    @Test
    public void testUpdateStatus() {
        System.out.println("updateStatus");
        BatteryModule instance = testModule;
        instance.updateStatus();
    }

    /**
     * Test of updateVoltage method, of class BatteryModule.
     */
    @Test
    public void testUpdateVoltage() {
        System.out.println("updateVoltage");
        BatteryModule instance = testModule;
        instance.updateVoltage();
    }

    /**
     * Test of updateTemperature method, of class BatteryModule.
     */
    @Test
    public void testUpdateTemperature() {
        System.out.println("updateTemperature");
        BatteryModule instance = testModule;
        instance.updateTemperature();
    }

    /**
     * Test of getId method, of class BatteryModule.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        BatteryModule instance = testModule;
        int expResult = 1;
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNrOfCells method, of class BatteryModule.
     */
    @Test
    public void testGetNrOfCells() {
        System.out.println("getNrOfCells");
        BatteryModule instance = testModule;
        int expResult = 16;
        int result = instance.getNrOfCells();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStatus method, of class BatteryModule.
     */
    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        BatteryModule instance = testModule;
        int expResult = 0;
        int result = instance.getStatus();
        assertEquals(expResult, result);
    }

    /**
     * Test of getVoltage method, of class BatteryModule.
     */
    @Test
    public void testGetVoltage() {
        System.out.println("getVoltage");
        BatteryModule instance = testModule;
        double expResult = 56.0;
        double result = instance.getVoltage();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getAverageTemperature method, of class BatteryModule.
     */
    @Test
    public void testGetAverageTemperature() {
        System.out.println("getAverageTemperature");
        BatteryModule instance = testModule;
        double expResult = 20.0;
        double result = instance.getAverageTemperature();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getBatteryCells method, of class BatteryModule.
     */
    @Test
    public void testGetBatteryCells() {
        System.out.println("getBatteryCells");
        BatteryModule instance = testModule;
        BatteryCell cell1 = instance.getBatteryCells().get(0);
        System.out.println(cell1.getVoltage());
        assertTrue(cell1.getVoltage() == 3.5);
    }

    /**
     * Test of getVoltageAsString method, of class BatteryModule.
     */
    @Test
    public void testGetVoltageAsString() {
        System.out.println("getVoltageAsString");
        BatteryModule instance = testModule;
        String expResult = "56.0 V";
        String result = instance.getVoltageAsString();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAverageTemperatureAsString method, of class BatteryModule.
     */
    @Test
    public void testGetAverageTemperatureAsString() {
        System.out.println("getAverageTemperatureAsString");
        BatteryModule instance = testModule;
        String expResult = "20.0 °C";
        String result = instance.getAverageTemperatureAsString();
        assertEquals(expResult, result);
    }

    /**
     * Test of addCell method, of class BatteryModule.
     */
    @Test
    public void testAddCell() {
        System.out.println("addCell");
        BatteryModule instance = testModule;
        int originalNumberOfCells = instance.getNrOfCells();
        BatteryCell cell = new BatteryCell(20, 4.10, 1, 10);
        instance.addCell(cell);
        int newNumberOfCells = instance.getNrOfCells();
        assertTrue(newNumberOfCells == originalNumberOfCells + 1);
    }

    /**
     * Test of setBalancing method, of class BatteryModule.
     */
    @Test
    public void testSetBalancing() {
        System.out.println("setBalancing");
        boolean balance = false;
        BatteryModule instance = testModule;
        instance.setBalancing(balance);
        assertTrue(!instance.isBalance());
    }

}
