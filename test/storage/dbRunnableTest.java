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

import battery.BatteryPacket;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Peter
 */
public class dbRunnableTest {

    private dbRunnable runner;

    public dbRunnableTest() {

        try {
            BatteryPacket packet = new BatteryPacket(9);
            runner = new dbRunnable(packet);
        } catch (IOException ex) {
            Logger.getLogger(dbRunnableTest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
     * Test of run method, of class dbRunnable.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        runner.run();
    }

    /**
     * Test of storeBatpack method, of class dbRunnable.
     */
    @Test
    public void testStoreBatpack() {
        runner.storeBatpack();
    }
    
    /**
     * Test of getVoltageLookupTable method, of class dbRunnable.
     */
    @Test
    public void testGetVoltageLookupTable(){
        System.out.println(runner.getVoltageLookupTable());
    }

}
