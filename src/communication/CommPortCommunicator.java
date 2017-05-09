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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Company: Formula Electric Belgium Author: Sach Project: Umicore Nova Part:
 * BMS pc Created: Januari 2017
 */
public class CommPortCommunicator implements Runnable {

    private final int br;
    private final int db;
    private final int par;
    private final int stb;
    private final CommPortIdentifier cpi;
    private final List<String> messageList;
    private String outMessage;
    private String lastMessage;
    private String received;
    private final ThreadEvent ready;
    private final ConcurrentLinkedQueue<String> communicationqueue;
    private CommPort cp;
    private SerialPort sp;
    private boolean connected;

    public boolean isConnected() {
        return connected;
    }

    public ConcurrentLinkedQueue<String> getMessageQueue() {
        return communicationqueue;
    }

    CommPortCommunicator(CommPortIdentifier cpi, int baudrate, int DATABITS_8, int PARITY_NONE, int STOPBITS_1, ThreadEvent ready) {
        this.cpi = cpi;
        this.br = baudrate;
        this.db = DATABITS_8;
        this.par = PARITY_NONE;
        this.stb = STOPBITS_1;
        this.messageList = new ArrayList<>();
        this.communicationqueue = new ConcurrentLinkedQueue<>();
        this.outMessage = new String();
        this.ready = ready;
        this.connected = false;
        System.out.println("contructed communicator");
    }

    @Override
    public void run() {
        try {
            byte[] readbuffer = new byte[5000];
            byte[] writebuffer = new byte[5000];
            char[] messagebuffer;
            int len = -5;

            this.connected = true;
            cp = this.cpi.open("", this.br);
            sp = (SerialPort) cp;
            sp.setSerialPortParams(this.br, this.db, this.stb, this.par);

            while (true) {
                InputStream in = cp.getInputStream();
                OutputStream out = cp.getOutputStream();
                if (connected) {
                    len = in.read(readbuffer);

                    received = new String(readbuffer);

                    if (len > 0) {
                        //System.out.println("received: "+received);
                        String[] messages = received.split("\r\n");

                        for (int i = 0; i < messages.length; i++) {
                            //assure that messages sent out by me aren't returned to me
                            if (i > 1) {
                                messageList.remove(messages[0]);
                            }
                            //remove spaces
                            if (messages[i].trim().length() > 2) {
                                messageList.add(messages[i].trim());
                            }

                            communicationqueue.add(messages[i].trim());
                        }

                        System.out.println("message List length:" + communicationqueue.size());

                    }

                    //Notify @ messages
                    if (messageList.size() > 0) {
                        //new message arrived
                        if (messageList.get(messageList.size() - 1).equals("End")) {
                            messageList.remove(messageList.size() - 1);
                            synchronized (this.ready) {
                                this.ready.notify();
                                System.out.println("cpc" + messageList);
                            }
                            while (messageList.size() > 0) {
                                messageList.remove(0);
                            }
                        }
                    }
                    if (outMessage.length() == 10) {
                        messagebuffer = outMessage.toCharArray();
                        for (int i = 0; i < 10; i++) {
                            writebuffer[i] = (byte) messagebuffer[i];
                        }
                        if (connected) {
                            out.write(writebuffer, 0, 10);
                        }
                        lastMessage = outMessage;
                        this.outMessage = "";
                    }
                } else {
                    System.out.println("trying to read while not connected");
                }
            }

        } catch (IOException ex) {
            //Logger.getLogger(CommPortCommunicator.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
            System.out.println("connection lost");
            if (this.sp != null) {
                sp.removeEventListener();
                disconnect();
                this.connected = false;
                System.out.println("connected: " + this.connected);
                for (int i = 0; i < 3; i++) {
                    synchronized (this.ready) {
                        this.ready.notifyAll();
                    }
                }
            }
        } catch (PortInUseException ex) {
            Logger.getLogger(CommPortCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedCommOperationException ex) {
            Logger.getLogger(CommPortCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void sendMessage(String message) {
        this.outMessage = message;
    }

    void resendMessage() {
        if (lastMessage.length() > 7);
        this.outMessage = lastMessage;
    }

    void closeConnection() {
        if (this.cp != null) {
            this.cp.close();
        }
    }

    private void disconnect() {
        try {
            OutputStream out = sp.getOutputStream();
            out.flush();
            out.close();
            sp.close();
        } catch (IOException ex) {
            Logger.getLogger(CommPortCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
