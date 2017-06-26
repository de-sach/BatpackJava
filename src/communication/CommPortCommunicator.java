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
            //try {
            this.connected = true;
            while (this.sp == null) {
                System.out.println("serial port not connected");
                Thread.sleep(200);
            }
            startCommLoop();
        } catch (InterruptedException ex) {
            System.out.println("run interrupted");
            Logger.getLogger(CommPortCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void sendMessage(String message) {
        sendQueue.add(message);
        System.out.println("message: " + message);
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
        try {
            if (sp.isOpen()) {
                this.sp.getInputStream().close();
                this.sp.getOutputStream().close();
                this.sp.closePort();
            }
        } catch (IOException ex) {
            System.out.println("error at closing");
            Logger.getLogger(CommPortCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void updateCommPort(SerialPort commPort) {
        this.sp = commPort;
    }

    private void startCommLoop() {
        byte[] readbuffer = new byte[5000];
        byte[] writebuffer = new byte[5000];
        char[] messagebuffer;
        int len = -5;
        if (sp != null) {
            while (!Thread.interrupted() && this.connected) {
                sp.setComPortParameters(br, db, stb, par);
                sp.openPort();
                //System.out.println("sp:" + sp);
                if (sp.getInputStream() == null) {
                    System.out.println("inputstream is NULL");
                    sp.closePort();
                    this.connected = false;
                } else {
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
                                System.out.println(received);
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
//                                    System.out.println("received message");
                                }

                                System.out.println("message List length:" + communicationqueue.size());
//                                System.out.println("ml size" + messageList.size());
                                //Notify @ messages
                                if (messageList.size() > 0) {
                                    //new message arrived
                                    if (messageList.get(messageList.size() - 1).equals("End")) {
                                        messageList.remove(messageList.size() - 1);
                                        synchronized (this.ready) {
                                            this.ready.notify();
//                                            System.out.println("cpc" + messageList);
                                        }

                                    }
                                }
                                messageList.clear();
//                                System.out.println("ml.size = " + messageList.size());

                            }
                            if (sendQueue.size() > 0) {
                                messagebuffer = sendQueue.remove().toCharArray();
                                for (int i = 0; i < outMessage.length(); i++) {
                                    writebuffer[i] = (byte) messagebuffer[i];
                                }
                                if (connected) {
                                    if (sp.isOpen() && sp.getOutputStream() != null && sp.getInputStream() != null) {
                                        out.write(writebuffer, 0, outMessage.length());
                                        System.out.println("sent message " + outMessage);
                                    }
                                }
                            }
                            Thread.sleep(250);
                        } catch (IOException ex) {
                            this.sp = null;
                            this.connected = false;
                            break;
                            //Logger.getLogger(CommPortCommunicator.class.getName()).log(Level.SEVERE, null, ex);

                        } catch (InterruptedException ex) {
                            System.out.println("sleep interrupted");
                            Logger.getLogger(CommPortCommunicator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (!sp.isOpen()) {
                            connected = false;
                            System.out.println("sp closed in loop");
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(CommPortCommunicator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            System.out.println("\n\n\ncommunication done\n\n\n");
        }
        connected = false;
    }
}
