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
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter
 */
public class PortMonitor implements Runnable {

    List<CommPortIdentifier> commPorts;
    SerialPort batpackPort;
    private int baudrate;
    private String BATPACK_IDENTIFIER = new String("C0_4129\r\n");

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public PortMonitor() {
        commPorts = new ArrayList<>();
    }

    @Override
    public void run() {

        listPorts();
        System.out.println("Running monitor thread");
        addAllPorts();
        //sloppy read
//            CommPortIdentifier bpp = commPorts.get(0);
//            tryRead(bpp, 500000);
//            
        BatteryPacket bp = findBatpack();

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
            if (message != null || !message.equals("")) {
                System.out.println(message);
            }

        }
    }

    private BatteryPacket findBatpack() {
        this.setBaudrate(500000);
        BatteryPacket batpack = null;
        CommPortIdentifier batteryPackCommportIdentifier = checkAllCommports();
        if (batteryPackCommportIdentifier != null) {
            System.out.println("batpack ID found");
            batpack = createBatpack(batteryPackCommportIdentifier);
        }
        return batpack;
    }

    private CommPortIdentifier checkAllCommports() {
        CommPortIdentifier batpackId = null;
        for (CommPortIdentifier cpi : commPorts) {
            try {
                CommPort cp = cpi.open("test.txt", this.getBaudrate());
                if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    SerialPort sp = (SerialPort) cp;
                    System.out.println("serial port found");
                    try {
                        sp.setSerialPortParams(this.getBaudrate(), SerialPort.DATABITS_8, SerialPort.STOPBITS_2, SerialPort.PARITY_ODD);
                        InputStream in = sp.getInputStream();
                        byte[] buffer = new byte[62500];//1s of data in buffer max
                        int len = -1;
                        String message;
                        boolean found;
                        found = false;
                        System.out.println("starting read");
                        int begin = 0;
                        len = in.read(buffer);
                        for (int i = begin; i < 10; i++) {
                            if ((char) buffer[i] == '\r' && (char) buffer[i + 1] == '\n') {
                                System.out.println("for loop char "+ (char) buffer[i]);
                                begin = i + 2;
                                System.out.println("first index "+ begin);
                            }
                        }
                        while ((len = in.read(buffer)) > -1 && !found) {
                            System.out.println((char) buffer[0]);

                            message = new String(buffer, begin, 9);
                            if (message != null || !message.equals("")) {
                                System.out.println("message = " + message);
                                if (message.equals(BATPACK_IDENTIFIER)) {
                                    found = true;
                                    batpackId = cpi;
                                }
                            }
                        }
                    } catch (UnsupportedCommOperationException | IOException ex) {
                        Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                cp.close();
            } catch (PortInUseException ex) {
                Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return batpackId;
    }

    private BatteryPacket createBatpack(CommPortIdentifier batteryPackCommportIdentifier) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void addAllPorts() {
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            commPorts.add(portIdentifier);
        }
    }

}
