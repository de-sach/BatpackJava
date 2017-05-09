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
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
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
    private ConcurrentLinkedQueue<String> messageList;
    private final MessageBuilder builder;
    private final MessageParser parser;
    private boolean ready;
    private boolean connected;
    private final ThreadEvent resultsReady;

    public PortMonitor(CountDownLatch latch) {

        commPorts = new ArrayList<>();
        this.builder = new MessageBuilder();
        this.parser = new MessageParser(batteryPack);
        this.latch = latch;
        this.ready = false;
        this.resultsReady = new ThreadEvent();
    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    @Override
    public void run() {
        //listPorts();
        //System.out.println("Running monitor thread");
        while (true) {
            addAllPorts();
            System.out.println("commports size =" + commPorts.size());
            if (commPorts.size() <= 0) {
                System.out.println("no comm port found");
                try {
                    Thread.sleep(2000);
                    System.out.println("trying to connect");
                } catch (InterruptedException ex) {
                    Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("commport found");
                //startComm();
                this.cpc = new CommPortCommunicator(this.commPorts.get(0), this.getBaudrate(), SerialPort.DATABITS_8, SerialPort.PARITY_NONE, SerialPort.STOPBITS_1, resultsReady);
                this.connected = cpc.isConnected();
                Thread commThread = new Thread(cpc);
                commThread.start();
                do {
                    System.out.println(connected);
                    this.messageList = cpc.getMessageQueue();
                    refreshAll();
                    this.connected = cpc.isConnected();
                    if (this.connected) {
                        this.ready = parser.getBatpackReady();
                        this.connected = cpc.isConnected();
                        if (this.ready) {
                            this.batteryPack = parser.getBatpack();
                            latch.countDown();
                            this.connected = cpc.isConnected();
                        }
                    } else {
                        this.ready = false;
                        this.connected = cpc.isConnected();
                    }
                    System.out.println("this.connected = " + this.connected);
                } while (this.ready == false && this.connected);
                System.out.println("outside while " + this.connected);
                if (!cpc.isConnected()) {
                    cpc.closeConnection();
                    try {
                        cpc.closeConnection();
                        commThread.join();
                        cpc = null;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println(commThread.getState());
                }
            }

        }
    }

    public void listPorts() {
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            //System.out.println(portIdentifier.getName() + " - " + getPortTypeName(portIdentifier.getPortType()));
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

    private void addAllPorts() {
        commPorts.clear();
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            //System.out.println("port id " + portIdentifier);
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

        switch (message) {
            case "Timeout":
                break;
            default:
                parser.parseMessage(message);
                if (parser.parseMessage(message) == 1) {
                    cpc.resendMessage();
                }
                break;
        }

    }

    public void refreshAll() {
        try {
            synchronized (this.resultsReady) {
                this.resultsReady.notify();
            }
            System.out.println("ref" + messageList);
            assert (cpc != null && builder != null);
            this.connected = cpc.isConnected();
            if (connected) {
                cpc.sendMessage(builder.buildBatpackMessage());
                synchronized (this.resultsReady) {
                    System.out.println("waiting");
                    this.resultsReady.wait(500);
                    //System.out.println("batpack free");
                    this.messageList = cpc.getMessageQueue();
                    System.out.println(messageList);
                    while (!this.messageList.isEmpty()) {
                        messageLoop(this.messageList.element());
                    }
                    cpc.sendMessage(builder.buildVoltageMessage(0, 0));
                }
            }
            this.connected = cpc.isConnected();
            if (connected) {
                synchronized (this.resultsReady) {
                    this.resultsReady.wait(500);
                    //System.out.println("voltage free");
                    this.messageList = cpc.getMessageQueue();
                    while (!this.messageList.isEmpty()) {
                        messageLoop(this.messageList.element());
                    }
                    cpc.sendMessage(builder.buildTemperatureMessage(0, 0));
                }
            }
            this.connected = cpc.isConnected();
            if (connected) {
                synchronized (this.resultsReady) {
                    this.resultsReady.wait(500);
                    //System.out.println("temp free");
                    this.messageList = cpc.getMessageQueue();
                    while (!this.messageList.isEmpty()) {
                        messageLoop(this.messageList.element());
                    }
                }
            }

            //cpc.sendMessage(builder.buildBalancingMessage(0, 0));
            this.ready = true;
            System.out.println("refresh done");
        } catch (InterruptedException ex) {
            Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void messageLoop(String message) {
        if (message != null && message.length() > 0) {
            assert (message.length() == 8);
            //System.out.println("handeling message: " + message);
            message = message.trim();
            parser.parseMessage(message);
        }
        this.messageList.remove();

    }

//    private void startComm() {
//        synchronized (resultsReady) {
//            this.resultsReady.notify();
//        }
//    }
}
