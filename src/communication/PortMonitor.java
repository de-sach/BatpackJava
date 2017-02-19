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

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter
 */
public class PortMonitor implements Runnable {

    List<MySerialPort> serialPorts;
    MySerialPort batpackPort;

    public PortMonitor() {
        serialPorts = new ArrayList<>();
    }

    @Override
    public void run() {
        listPorts();
        System.out.println("Running monitor thread");
        addAllSerialPorts();
        System.out.println("added serial ports");
        try {
            batpackPort = connectBatpack();
            System.out.println("batteryPack Connected");
        } catch (PortInUseException | UnsupportedCommOperationException ex) {
            Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addAllSerialPorts() {
        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            System.out.println("port found");
            CommPortIdentifier portId = portEnum.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                MySerialPort sp = new MySerialPort(portId);
                System.out.println("new serial port added");
            }
        }
    }

    public void listPorts() {
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            System.out.println(portIdentifier.getName() + " - " + getPortTypeName(portIdentifier.getPortType()));
        }
    }

    static String getPortTypeName(int portType) {
        switch (portType) {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }

    private MySerialPort connectBatpack() throws PortInUseException, UnsupportedCommOperationException {
        MySerialPort batpack;
        batpack = null;
        for (MySerialPort port : serialPorts) {
            CommPort commport = port.getCommId().open("testMessages.txt", 2000);
            if (commport instanceof SerialPort) {
                InputStream in = null;
                try {
                    SerialPort sp = (SerialPort) commport;
                    sp.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    in = sp.getInputStream();
                    byte[] readBuffer = new byte[1024];
                    int len = -1;
                    try {
                        while ((len = in.read(readBuffer)) > -1) {
                            String message = new String(readBuffer, 0, len);
                            System.out.println(message);
                            if (message.equals("CB\n")) {//Connected BatteryPack, just a quick ID
                                System.out.println("batpack found");
                                batpack = port;
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return batpack;
    }
}
