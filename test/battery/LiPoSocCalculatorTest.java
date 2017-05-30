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
public class LiPoSocCalculatorTest {

    public LiPoSocCalculator calc;

    public LiPoSocCalculatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        calc = new LiPoSocCalculator(3.250);

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getVoltage method, of class LiPoSocCalculator.
     */
    @Test
    public void testGetVoltage() {
        System.out.println("getVoltage");
        LiPoSocCalculator instance = calc;
        double expResult = 3.250;
        double result = instance.getVoltage();
        assertEquals(expResult, result,0.0);
    }

    /**
     * Test of setVoltage method, of class LiPoSocCalculator.
     */
    @Test
    public void testSetVoltage() {
        System.out.println("setVoltage");
        double voltage = 3.450;
        LiPoSocCalculator instance = calc;
        instance.setVoltage(voltage);
        assertEquals(calc.getVoltage(), voltage,0.0);
    }

    /**
     * Test of getSoc method, of class LiPoSocCalculator.
     */
    @Test
    public void testGetSoc_0args() {
        System.out.println("getSoc");
        LiPoSocCalculator instance = calc;
        int expResult = 7;
        int result = instance.getSoc();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSoc method, of class LiPoSocCalculator.
     */
    @Test
    public void testGetSoc_int() {
        System.out.println("getSoc");
        double voltage = 3.860;
        LiPoSocCalculator instance = calc;
        int expResult = 78;
        int result = instance.getSoc(voltage);
        assertEquals(expResult, result);
    }

}
