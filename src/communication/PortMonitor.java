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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fazecast.jSerialComm.*;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Peter
 */
public class PortMonitor implements Runnable {

    private SerialPort[] commPorts;
    SerialPort batpackPort;
    private int baudrate;
    private final int databits;
    private final int stopbits;
    private final int pariteit;
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
        this.builder = new MessageBuilder();
        this.parser = new MessageParser(batteryPack);
        this.latch = latch;
        this.ready = false;
        this.resultsReady = new ThreadEvent();
        this.baudrate = 500000;
        this.databits = 8;
        this.stopbits = SerialPort.ONE_STOP_BIT;
        this.pariteit = SerialPort.NO_PARITY;
        this.cpc = new CommPortCommunicator(baudrate, databits, pariteit, stopbits, resultsReady);
        Thread commThread = new Thread(cpc);
        commThread.start();
    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    @Override
    public void run() {
        while (!ready) {
            addAllPorts();

            if (commPorts.length <= 0) {
                System.out.println("no comm port found");
                try {
                    Thread.sleep(2000);
                    System.out.println("trying to connect");
                } catch (InterruptedException ex) {
                    Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("commport found");
                if (commPorts[0] != null) {
                    this.cpc.updateCommPort(this.commPorts[0]);
                    this.connected = cpc.isConnected();
                    do {
                        this.connected = cpc.isConnected();
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
                } else {
                    System.out.println("commport is null");
                }
            }
        }
        System.out.println("__________________________SETUP_DONE______________________");
    }

    private void addAllPorts() {
        commPorts = SerialPort.getCommPorts();
    }

    public BatteryPacket getBatteryPack() {
        return batteryPack;
    }

    public void setBatteryPack(BatteryPacket batteryPack) {
        this.batteryPack = batteryPack;
    }

    public void refreshAll() {
        try {
            cpc.sendMessage(builder.buildBatpackMessage());
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
                    this.resultsReady.wait(100);
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
                    this.resultsReady.wait(100);
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
                    this.resultsReady.wait(100);
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
    private void testRead() throws IOException {
        SerialPort sp = commPorts[0];
        sp.setComPortParameters(baudrate, databits, stopbits, pariteit);
        sp.openPort();
        InputStream in = sp.getInputStream();
        byte[] buffer = new byte[1000];
        boolean end = false;
        String message = "";
        while (!end) {
            int len = in.read(buffer);
            if (len > 0) {
                message = new String(buffer);
                System.out.println(message);
            }
            len = 0;
            String messages[];
            messages = message.split("\r\n");
            for (int i = 0; i < messages.length; i++) {
                if (messages[i].equals("END")) {
                    end = true;
                }
            }
        }
        in.close();
        sp.closePort();
        System.out.println("done reading");
    }
}
