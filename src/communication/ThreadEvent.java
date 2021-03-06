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

/**
 * Company: Formula Electric Belgium
 * Author: Sach
 * Project: Umicore Nova
 * Part: BMS pc
 * Created: Januari 2017
 */
public class ThreadEvent {
    private final Object lock = new Object();
    /** 
     * A function used to synchronize multithreading
     */
    public void signal(){
        synchronized(lock){
            lock.notify();
        }
    }
    /**
     * A function used to synchronize multithreading
     * @throws InterruptedException when waiting is suddenly interrupted
     */
    public void await() throws InterruptedException{
        synchronized(lock){
            lock.wait();
        }
    }
}
