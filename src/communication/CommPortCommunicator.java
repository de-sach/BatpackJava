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

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
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
    private final List<String> messageList;
    private String outMessage;
    private String lastMessage;
    private String received;
    private final ThreadEvent ready;
    private final ConcurrentLinkedQueue<String> communicationqueue;
    private final ConcurrentLinkedQueue<String> sendQueue;
    private SerialPort sp;
    private boolean connected;

    public boolean isConnected() {
        return connected;
    }

    public ConcurrentLinkedQueue<String> getMessageQueue() {
        return communicationqueue;
    }

    CommPortCommunicator(int baudrate, int DATABITS_8, int PARITY_NONE, int STOPBITS_1, ThreadEvent ready) {
        this.br = baudrate;
        this.db = DATABITS_8;
        this.par = PARITY_NONE;
        this.stb = STOPBITS_1;
        this.messageList = new ArrayList<>();
        this.communicationqueue = new ConcurrentLinkedQueue<>();
        this.outMessage = new String();
        this.ready = ready;
        this.connected = false;
        this.sendQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {
        try {
            this.connected = true;
            while (this.sp == null) {
                System.out.println("serial port not connected");
                Thread.sleep(500);
            }
            startCommLoop();
        } catch (InterruptedException ex) {
            System.out.println("run interrupted");
            Logger.getLogger(CommPortCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void sendMessage(String message) {
        sendQueue.add(message);
        //System.out.println("message: " + message);
    }

    void resendMessage() {
        this.lastMessage = "";
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(CommPortCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void closePort() {
        if (sp.isOpen()) {
            try {
                this.sp.getInputStream().close();
                this.sp.getOutputStream().close();
                this.sp.closePort();
            } catch (IOException ex) {
                Logger.getLogger(CommPortCommunicator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void updateCommPort(SerialPort commPort) {
        this.sp = commPort;
    }

    private void startCommLoop() {
        byte[] readbuffer = new byte[5000];
        byte[] writebuffer = new byte[5000];
        char[] messagebuffer;
        int len;
        if (sp != null) {
            while (!Thread.interrupted() && this.connected) {
                len = 0;
                sp.setComPortParameters(br, db, stb, par);
                sp.openPort();
                //check inputstream and break out of loop when none available
                if (sp.getInputStream() == null) {
                    System.out.println("inputstream is NULL");
                    sp.closePort();
                    this.connected = false;
                } else {
                    //we are sure it's connected
                    this.connected = true;
                    try (InputStream in = sp.getInputStream()) {
                        OutputStream out = sp.getOutputStream();
                        try {
                            if (sp.isOpen()) {
                                len = in.read(readbuffer);
                            }
                            if (len > 0) {
                                received = new String(readbuffer);
                                received = received.trim();
                                String[] messages = received.split("\r\n");
                                int count = 0;
                                while (count < messages.length && !messages[count].trim().equals("End")) {
                                    //check if the message is at least of plausible length
                                    if (messages[count].trim().length() > 2) {
                                        messageList.add(messages[count].trim());
                                        communicationqueue.add(messages[count].trim());
                                    }
                                    count++;
                                }
                                //Notify @ messages
                                synchronized (this.ready) {
                                    this.ready.notify();
                                }
                               //System.out.println("messageList: " + messageList);
                                messageList.clear();
                            }
                            if (sendQueue.size() > 0) {
                                messagebuffer = sendQueue.remove().toCharArray();
                                for (int i = 0; i < messagebuffer.length; i++) {
                                    writebuffer[i] = (byte) messagebuffer[i];
                                }
                                if (connected) {
                                    if (sp.isOpen() && sp.getOutputStream() != null && sp.getInputStream() != null) {
                                        out.write(writebuffer, 0, messagebuffer.length);
                                    }
                                }
                            }
                            Thread.sleep(50);
                        } catch (IOException | InterruptedException ex) {
                            this.sp = null;
                            this.connected = false;
                            break;
                        }
                        if (!sp.isOpen()) {
                            this.connected = false;
                            this.sp = null;
                            System.out.println("sp closed in loop");
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(CommPortCommunicator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            System.out.println("\n\n\ncommunication done\n\n\n");
        }
        this.connected = false;
        this.sp = null;
        System.out.println("communication finished");
    }
}
