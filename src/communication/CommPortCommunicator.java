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

    public List<String> getMessageList() {
        return messageList;
    }

    CommPortCommunicator(CommPortIdentifier cpi, int baudrate, int DATABITS_8, int PARITY_NONE, int STOPBITS_1) {
        this.cpi = cpi;
        this.br = baudrate;
        this.db = DATABITS_8;
        this.par = PARITY_NONE;
        this.stb = STOPBITS_1;
        messageList = new ArrayList<>();
        this.outMessage = new String();
    }

    @Override
    public void run() {
        try {
            byte[] readbuffer = new byte[5000];
            byte[] writebuffer = new byte[5000];
            char[] messagebuffer;
            int len;

            CommPort cp = this.cpi.open("", this.br);
            SerialPort sp = (SerialPort) cp;
            sp.setSerialPortParams(this.br, this.db, this.stb, this.par);

            while (true) {

                InputStream in = cp.getInputStream();
                OutputStream out = cp.getOutputStream();
                len = in.read(readbuffer);
                received = new String(readbuffer);
                if (len > 0) {
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
                    }

                }
                if (outMessage.length() == 10) {
                    messagebuffer = outMessage.toCharArray();
                    for (int i = 0; i < 10; i++) {
                        writebuffer[i] = (byte) messagebuffer[i];
                    }
                    out.write(writebuffer, 0, 10);
                    lastMessage = outMessage;
                    this.outMessage = "";
                }
                //necessary to make sure all threads can be executed.
                Thread.sleep(100);
            }
        } catch (PortInUseException | UnsupportedCommOperationException | IOException | InterruptedException ex) {
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
}