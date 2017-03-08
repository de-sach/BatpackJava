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

    List<CommPortIdentifier> commPorts;
    List<SerialPort> serialPorts;
    SerialPort batpackPort;

    public PortMonitor() {
        commPorts = new ArrayList<>();
        serialPorts = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            listPorts();
            System.out.println("Running monitor thread");
            addAllSerialPorts();
            for (SerialPort sp : serialPorts) {
                System.out.println("serial: " + sp);
            }
            CommPortIdentifier bpp = commPorts.get(0);
            tryRead(bpp, 500000);
        } catch (UnsupportedCommOperationException | IOException | PortInUseException ex) {
            Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addAllSerialPorts() {
        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            System.out.println("port found");
            CommPortIdentifier portId = portEnum.nextElement();
            commPorts.add(portId);
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                String portName = portId.getName();
                System.out.println("new serial port found " + portName);
                try {
                    SerialPort sp = (SerialPort) portId.open("CommUtil", 50);
                    sp.close();
                    serialPorts.add(sp);
                } catch (PortInUseException ex) {
                    Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
                }
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

    private void tryRead(CommPortIdentifier cpi, int baudRate) throws UnsupportedCommOperationException, IOException, PortInUseException {
        CommPort cp = cpi.open("test.txt", baudRate);
        SerialPort sp = (SerialPort) cp;
        sp.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_2, SerialPort.PARITY_ODD);
        
        InputStream in = sp.getInputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        String message;
        while ((len = in.read(buffer)) > -1) {
            message = new String(buffer, 0, len);
            if (message!=null || !message.equals("")) {
                System.out.println(message);
            }

        }
    }

}
