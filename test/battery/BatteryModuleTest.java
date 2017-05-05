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
 * @author sach
 */
public class BatteryModuleTest {
    
    public BatteryModuleTest() {
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
     * Test of updateStatus method, of class BatteryModule.
     */
    @Test
    public void testUpdateStatus() {
        System.out.println("updateStatus");
        BatteryModule instance = null;
        instance.updateStatus();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateVoltage method, of class BatteryModule.
     */
    @Test
    public void testUpdateVoltage() {
        System.out.println("updateVoltage");
        BatteryModule instance = null;
        instance.updateVoltage();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateTemperature method, of class BatteryModule.
     */
    @Test
    public void testUpdateTemperature() {
        System.out.println("updateTemperature");
        BatteryModule instance = null;
        instance.updateTemperature();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getId method, of class BatteryModule.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        BatteryModule instance = null;
        int expResult = 0;
        int result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNrOfCells method, of class BatteryModule.
     */
    @Test
    public void testGetNrOfCells() {
        System.out.println("getNrOfCells");
        BatteryModule instance = null;
        int expResult = 0;
        int result = instance.getNrOfCells();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatus method, of class BatteryModule.
     */
    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        BatteryModule instance = null;
        int expResult = 0;
        int result = instance.getStatus();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVoltage method, of class BatteryModule.
     */
    @Test
    public void testGetVoltage() {
        System.out.println("getVoltage");
        BatteryModule instance = null;
        double expResult = 0.0;
        double result = instance.getVoltage();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAverageTemperature method, of class BatteryModule.
     */
    @Test
    public void testGetAverageTemperature() {
        System.out.println("getAverageTemperature");
        BatteryModule instance = null;
        double expResult = 0.0;
        double result = instance.getAverageTemperature();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBatteryCells method, of class BatteryModule.
     */
    @Test
    public void testGetBatteryCells() {
        System.out.println("getBatteryCells");
        BatteryModule instance = null;
        List<BatteryCell> expResult = null;
        List<BatteryCell> result = instance.getBatteryCells();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVoltageAsString method, of class BatteryModule.
     */
    @Test
    public void testGetVoltageAsString() {
        System.out.println("getVoltageAsString");
        BatteryModule instance = null;
        String expResult = "";
        String result = instance.getVoltageAsString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAverageTemperatureAsString method, of class BatteryModule.
     */
    @Test
    public void testGetAverageTemperatureAsString() {
        System.out.println("getAverageTemperatureAsString");
        BatteryModule instance = null;
        String expResult = "";
        String result = instance.getAverageTemperatureAsString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
