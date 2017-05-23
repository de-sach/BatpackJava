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
package communication;

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
public class MessageBuilderTest {

    public MessageBuilderTest() {
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
     * Test of buildBatpackMessage method, of class MessageBuilder.
     */
    @Test
    public void testBuildBatpackMessage() {
        System.out.println("buildBatpackMessage");
        MessageBuilder instance = new MessageBuilder();
        String expResult = "M_0_0000\r\n";
        String result = instance.buildBatpackMessage();
        assertEquals(expResult, result);

    }

    /**
     * Test of buildVoltageMessage method, of class MessageBuilder.
     */
    @Test
    public void testBuildVoltageMessage() {
        System.out.println("buildVoltageMessage");
        int type = 0;
        int identifier = 0;
        MessageBuilder instance = new MessageBuilder();
        String expResult = "V_A_0000\r\n";
        String result = instance.buildVoltageMessage(type, identifier);
        assertEquals(expResult, result);

    }

    /**
     * Test of buildTemperatureMessage method, of class MessageBuilder.
     */
    @Test
    public void testBuildTemperatureMessage() {
        System.out.println("buildTemperatureMessage");
        int type = 0;
        int identifier = 0;
        MessageBuilder instance = new MessageBuilder();
        String expResult = "T_A_0000\r\n";
        String result = instance.buildTemperatureMessage(type, identifier);
        assertEquals(expResult, result);

    }

    /**
     * Test of buildBalancingMessage method, of class MessageBuilder.
     */
    @Test
    public void testBuildBalancingMessage() {
        System.out.println("buildBalancingMessage");
        int type = 0;
        int identifier = 0;
        MessageBuilder instance = new MessageBuilder();
        String expResult = "B_A_0000\r\n";
        String result = instance.buildBalancingMessage(type, identifier);
        assertEquals(expResult, result);

    }

}
