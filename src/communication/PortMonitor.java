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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
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
    private BatteryPacket batteryPack;
    private CommPortCommunicator cpc;
    private final CountDownLatch latch;
    private List<String> messageList;
    private final MessageBuilder builder;
    private final MessageParser parser;
    private boolean ready;

    public PortMonitor(CountDownLatch latch) {

        commPorts = new ArrayList<>();
        this.builder = new MessageBuilder();
        this.parser = new MessageParser(batteryPack);
        this.latch = latch;
        this.ready = false;

    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    @Override
    public void run() {
        listPorts();
        System.out.println("Running monitor thread");
        addAllPorts();

        if (commPorts.size() <= 0) {
            System.out.println("no comm port found");
        } else {
            this.cpc = new CommPortCommunicator(this.commPorts.get(0), this.getBaudrate(), SerialPort.DATABITS_8, SerialPort.PARITY_NONE, SerialPort.STOPBITS_1);
            Thread commThread = new Thread(cpc);
            commThread.start();
            while (this.ready == false) {
                try {
                    Thread.sleep(10);
                    //System.out.println(cpc.getMessageList().size());
                    if (cpc.getMessageList().size() > 0) {
                        this.messageList = cpc.getMessageList();
                        //System.out.println("portmonitor " + this.messageList.get(0));
                        this.handleMessage(this.messageList.get(0));
                        this.messageList.remove(0);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
                }
                this.ready = parser.getBatpackReady();
                this.batteryPack = parser.getBatpack();
            }
        }
        latch.countDown();
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

    private CommPortIdentifier checkAllCommports() {
        CommPortIdentifier batpackId = null;
        for (CommPortIdentifier cpi : commPorts) {
            System.out.println("cpi: " + cpi);
            try {
                CommPort cp = cpi.open("BatteryMonitor", this.getBaudrate());
                if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    SerialPort sp = (SerialPort) cp;
                    System.out.println("serial port found");
                }
                cp.close();
                batpackId = cpi;
            } catch (PortInUseException ex) {
                Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return batpackId;
    }

    private void addAllPorts() {
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            System.out.println("port id " + portIdentifier);
            commPorts.add(portIdentifier);
        }
    }

    public BatteryPacket getBatteryPack() {
        return batteryPack;
    }

    public void setBatteryPack(BatteryPacket batteryPack) {
        this.batteryPack = batteryPack;
    }

    private void handleMessage(String message) {
        assert (message.length() == 8);
        //System.out.println("handeling message: " + message);
        message = message.trim();
        switch (message) {
            case "Timeout":
                break;
            case "SETUP_OK":
                this.refreshAll();
                break;
            case "End":
                //cpc.sendMessage(builder.buildModuleMessage());
                break;
            default:
                if (parser.parseMessage(message) == 1) {
                    cpc.resendMessage();
                }
                break;
        }

    }

    public void refreshAll() {
        assert(cpc!=null&&builder!=null);
        try {
            Thread.sleep(100);
            cpc.sendMessage(builder.buildBatpackMessage());
            Thread.sleep(100);
            cpc.sendMessage(builder.buildVoltageMessage(0, 0));
            Thread.sleep(100);
            cpc.sendMessage(builder.buildTemperatureMessage(0, 0));
            Thread.sleep(1000);
            //cpc.sendMessage(builder.buildBalancingMessage(0, 0));
        } catch (InterruptedException ex) {
            Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.ready = true;
    }

}
