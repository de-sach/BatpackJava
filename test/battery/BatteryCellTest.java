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

import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class BatteryCellTest {
    
    private BatteryCell testCell;
    
    public BatteryCellTest() {
        testCell = new BatteryCell(0, 0, 0, 0);
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
     * Test of getTemperature method, of class BatteryCell.
     */
    @Test
    public void testGetTemperature() {
        System.out.println("getTemperature");
        BatteryCell instance = testCell;
        double expResult = 0.0;
        double result = instance.getTemperature();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setTemperature method, of class BatteryCell.
     */
    @Test
    public void testSetTemperature() {
        System.out.println("setTemperature");
        double temperature = 10.0;
        BatteryCell instance = testCell;
        instance.setTemperature(temperature);
        assertEquals(10.0, testCell.getTemperature(),0.0);
    }

    /**
     * Test of getVoltage method, of class BatteryCell.
     */
    @Test
    public void testGetVoltage() {
        System.out.println("getVoltage");
        BatteryCell instance = testCell;
        double expResult = 0.0;
        double result = instance.getVoltage();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setVoltage method, of class BatteryCell.
     */
    @Test
    public void testSetVoltage() {
        System.out.println("setVoltage");
        double voltage = 12.0;
        BatteryCell instance = testCell;
        instance.setVoltage(voltage);
        assertEquals(voltage, testCell.getVoltage(),0.0);
    }

    /**
     * Test of getId method, of class BatteryCell.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        BatteryCell instance = testCell;
        int expResult = 0;
        int result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of setId method, of class BatteryCell.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 5;
        BatteryCell instance = testCell;
        instance.setId(id);
        assertEquals(testCell.getId(),id);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getHealth method, of class BatteryCell.
     */
    @Test
    public void testGetHealth() {
        System.out.println("getHealth");
        BatteryCell instance = testCell;
        int expResult = 0;
        int result = instance.getHealth();
        assertEquals(expResult, result);
    }

    /**
     * Test of setHealth method, of class BatteryCell.
     */
    @Test
    public void testSetHealth() {
        System.out.println("setHealth");
        int health = 10;
        BatteryCell instance = testCell;
        instance.setHealth(health);
        assertEquals(health, testCell.getHealth(),0.0);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getVoltageAsString method, of class BatteryCell.
     */
    @Test
    public void testGetVoltageAsString() {
        System.out.println("getVoltageAsString");
        BatteryCell instance = testCell;
        testCell.setVoltage(12);
        String expResult = "12.0 V";
        String result = instance.getVoltageAsString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail
    }

    /**
     * Test of getTemperatureAsString method, of class BatteryCell.
     */
    @Test
    public void testGetTemperatureAsString() {
        System.out.println("getTemperatureAsString");
        BatteryCell instance = testCell;
        testCell.setTemperature(10);
        String expResult = "10.0°C";
        String result = instance.getTemperatureAsString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call 
    }

    /**
     * Test of setLastMeasurement method, of class BatteryCell.
     */
    @Test
    public void testSetLastMeasurement() {
        System.out.println("setLastMeasurement");
        Instant now = Instant.now();
        BatteryCell instance = testCell;
        instance.setLastMeasurement(now);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getLastMeasurement method, of class BatteryCell.
     */
    @Test
    public void testGetLastMeasurement() {
        try {
            System.out.println("getLastMeasurement");
            BatteryCell instance = testCell;
            testCell.setLastMeasurement(Instant.now());
            Thread.sleep(10);
            Instant expResult = Instant.now();
            Instant result = instance.getLastMeasurement();
            assertTrue(expResult.isAfter(result));
            // TODO review the generated test code and remove the default call to fail.
        } catch (InterruptedException ex) {
            Logger.getLogger(BatteryCellTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}