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

import battery.BatteryPacket;
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
public class MessageParserTest {

    MessageParser parser;

    public MessageParserTest() {
        BatteryPacket bp = new BatteryPacket(2);
        parser = new MessageParser(bp);
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
     * Test of parseMessage method, of class MessageParser.
     */
    @Test
    public void testParseMessage() {
        System.out.println("parseMessage");
        String message = "M_0_0\r\n";
        MessageParser instance = parser;
        int expResult = 1;
        int result = instance.parseMessage(message);
        assertEquals(expResult, result);
    }

    /**
     * Test of getBatpack method, of class MessageParser.
     */
    @Test
    public void testGetBatpack() {
        System.out.println("getBatpack");
        MessageParser instance = parser;
        BatteryPacket result = instance.getBatpack();
        boolean res;
        if (result instanceof BatteryPacket && result.getModuleCount() == 0) {
            res = true;
        } else {
            res = false;
        }
        assertTrue(res);
    }

    /**
     * Test of getBatpackReady method, of class MessageParser.
     */
    @Test
    public void testGetBatpackReady() {
        System.out.println("getBatpackReady");
        MessageParser instance = parser;
        boolean expResult = false;
        boolean result = instance.getBatpackReady();
        assertEquals(expResult, result);
    }

}
