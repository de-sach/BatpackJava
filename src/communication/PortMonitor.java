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

import battery.BatteryModule;
import battery.BatteryPacket;
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
import java.util.Random;
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
    private String BATPACK_IDENTIFIER = new String("C0_4129\r\n");
    private CountDownLatch latch;
    private List<String> messageList;
    private MessageBuilder builder;
    private MessageParser parser;
    private boolean ready;

    public PortMonitor(CountDownLatch latch) {

        commPorts = new ArrayList<>();
        this.builder = new MessageBuilder();
        this.parser = new MessageParser(batteryPack);
        /*
        this.batteryPack = new BatteryPacket(9);
        for (int module = 0; module < 9; module++) {
            BatteryModule mod = new BatteryModule(module, 16);
            this.batteryPack.addModule(mod);
            Random random = new Random();
            mod.getBatteryCells().stream().map((cell) -> {
                cell.setHealth(random.nextInt(10));
                return cell;
            }).map((cell) -> {
                cell.setId(random.nextInt(5000));
                return cell;
            }).map((cell) -> {
                cell.setTemperature(random.nextDouble() * 10 + 20);
                return cell;
            }).forEachOrdered((cell) -> {
                cell.setVoltage(3 + random.nextFloat());
            });
        }
        */
        this.latch = latch;
        this.ready=false;
        
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
            while (this.ready==false) {
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
                this.ready=parser.getBatpackReady();
                this.batteryPack=parser.getBatpack();
            }
            //BatteryPacket bp = getAllInfo();
            //this.batteryPack=parser.getBatpack();
           // System.out.println("portmonitor batpack set");
        }
        latch.countDown();
        
        
////        sloppy read
//            CommPortIdentifier bpp = commPorts.get(0);
//        try {
//            tryRead(bpp, 500000);
//        } catch (UnsupportedCommOperationException | IOException | PortInUseException ex) {
//            Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        BatteryPacket bp = getAllInfo();

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
        CommPort cp = cpi.open("BatteryMonitor", baudRate);
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
            System.out.println("cpi: " + cpi);
            try {
                CommPort cp = cpi.open("BatteryMonitor", this.getBaudrate());
                if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    SerialPort sp = (SerialPort) cp;
                    System.out.println("serial port found");
                    /*
                    try {
                        sp.setSerialPortParams(this.getBaudrate(), SerialPort.DATABITS_8, SerialPort.STOPBITS_2, SerialPort.PARITY_ODD);
                        InputStream in = sp.getInputStream();
                        byte[] buffer = new byte[62500];//1s of data in buffer max
                        int len = -1;
                        String message;
                        boolean found;
                        found = false;
                        System.out.println("starting read");

                        len = in.read(buffer);
                        if (len < 0) {
                            System.out.println("message error");
                        } else if (len == 0) {
                            System.out.println("message length 0");
                        } else {
                            System.out.println("valid message length");

                            int begin = 0;
                            for (int i = begin; i < 10; i++) {
                                if ((char) buffer[i] == '\r' && (char) buffer[i + 1] == '\n') {
                                    System.out.println("for loop char " + (char) buffer[i]);
                                    begin = i + 2;
                                    System.out.println("first index " + begin);
                                }
                            }
                            while ((len = in.read(buffer)) > -1 && !found) {
                                System.out.println((char) buffer[0]);

                                message = new String(buffer, begin, 9);
                                if (message != null || !message.equals("")) {
//                                System.out.println("message = " + message);
//                                if (message.equals(BATPACK_IDENTIFIER)) {
//                                    found = true;
//                                    batpackId = cpi;
//                                }
                                    batpackId = cpi;
                                }
                            }
                        }
                    } catch (UnsupportedCommOperationException | IOException ex) {
                        Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
                    }*/

                }
                cp.close();
                batpackId = cpi;
            } catch (PortInUseException ex) {
                Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return batpackId;
    }

    private BatteryPacket createBatpack(CommPortIdentifier batteryPackCommportIdentifier) {
        //ask batpack for nr of modules;
        BatteryPacket bp = new BatteryPacket(9);
        return bp;
    }

    private void addAllPorts() {
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            System.out.println("port id " + portIdentifier);
            commPorts.add(portIdentifier);
        }
    }

    private void commPortSend(String message) throws PortInUseException {
        System.out.println("function CommPortSend");
        System.out.println("message = " + message);
        char[] messageBuffer = message.toCharArray();
        CommPortIdentifier batteryPackCommportIdentifier = checkAllCommports();
        CommPort cp = batteryPackCommportIdentifier.open("", this.getBaudrate());
        if (batteryPackCommportIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            try {
                SerialPort sp = (SerialPort) cp;
                System.out.println("serial port found");
                sp.setSerialPortParams(this.getBaudrate(), SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                OutputStream out = sp.getOutputStream();
                for (int i = 0; i < messageBuffer.length; i++) {
                    out.write(messageBuffer[i]);
                }
            } catch (UnsupportedCommOperationException | IOException ex) {
                Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        cp.close();
    }

    /*
    private BatteryPacket getAllInfo() {
        BatteryPacket batpack = null;
        int maxNumberOfChars = 10 * 144 + 1; //10 char's per message & 144 cells && endmessage;
        char[] receiveBuffer = new char[maxNumberOfChars];
        MessageBuilder messages = new MessageBuilder();
        MessageParser parser = new MessageParser(this.batteryPack);
        try {
            System.out.println("getting batpack");
            System.out.println(messages.buildBatpackMessage());
            commPortSend(messages.buildBatpackMessage());
            System.out.println("after commPortSend");
            receiveBuffer = commPortReceive(1, receiveBuffer);
            int numberOfModules = parser.parseModuleOverview(receiveBuffer);

            System.out.println("getting modules");
            commPortSend(messages.buildModuleMessage());
            System.out.println("after commPortSend");
            receiveBuffer = commPortReceive(1, receiveBuffer);
            int numberOfCells = parser.parseCellOverview(receiveBuffer);

            System.out.println("building modules");
            batpack = new BatteryPacket(numberOfModules);
            for (int i = 0; i < numberOfModules; i++) {
                BatteryModule module = new BatteryModule(i, numberOfCells / numberOfModules);
                batpack.addModule(module);
            }

            System.out.println("getting voltage");
            commPortSend(messages.buildVoltageMessage(0, 0));//get all
            receiveBuffer = commPortReceive(numberOfCells + 1, receiveBuffer);
            parser.parseAllVoltages(receiveBuffer, batpack);

            System.out.println("getting temp");
            commPortSend(messages.buildTemperatureMessage(0, 0));//get all
            receiveBuffer = commPortReceive(numberOfCells + 1, receiveBuffer);
            parser.parseAllTemperatures(receiveBuffer, batpack);

            System.out.println("getting blancing");
            commPortSend(messages.buildBalancingMessage(0, 0));//get all
            receiveBuffer = commPortReceive(numberOfCells + 1, receiveBuffer);
            parser.parseAllBalancing(receiveBuffer, batpack);

        } catch (PortInUseException ex) {
            Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return batpack;
    }
    */
    /*
    private char[] commPortReceive(int i, char[] receiveBuffer) {
        try {
            byte[] buffer = new byte[2000];
            CommPortIdentifier batteryPackCommportIdentifier = checkAllCommports();
            CommPort cp = batteryPackCommportIdentifier.open("", this.getBaudrate());
            if (batteryPackCommportIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {

                SerialPort sp = (SerialPort) cp;
                System.out.println("serial port found");
                sp.setSerialPortParams(this.getBaudrate(), SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                InputStream in = sp.getInputStream();
                int len = -1;

                System.out.println("starting read");
                int begin = 0;
                len = in.read(buffer);

                if (len < 0) {
                    System.out.println("message error");
                } else if (len == 0) {
                    System.out.println("message length 0");
                } else {
                    System.out.println("valid message length: " + len);
                }
                String message;
                char[] messageBuffer = new char[2000];
                for (int j = 0; j < 2000; j++) {
                    messageBuffer[j] = (char) buffer[j];
                }
                message = String.copyValueOf(messageBuffer);
                System.out.println(message);
                /*
                for (int index = begin; index < len; index++) {
                    if ((char) receiveBuffer[index] == '\r' && (char) receiveBuffer[index + 1] == '\n') {
                        System.out.println("for loop char " + (char) receiveBuffer[index]);
                        begin = index + 2;
                        System.out.println("first index " + begin);
                    }

                }
                for (int index = begin; index < (i * 10); index++) {
                    receiveBuffer[index] = (char) buffer[index];
                }
                 *//*
            }
            cp.close();

        } catch (PortInUseException | UnsupportedCommOperationException | IOException ex) {
            Logger.getLogger(PortMonitor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return receiveBuffer;
    }
*/

    public BatteryPacket getBatteryPack() {
        return batteryPack;
    }

    public void setBatteryPack(BatteryPacket batteryPack) {
        this.batteryPack = batteryPack;
    }

    private void handleMessage(String message) {
        assert (message.length() == 8);
        System.out.println("handeling message: " + message);
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
                if(parser.parseMessage(message)==1){
                    cpc.resendMessage();
                }
                break;
        }

    }

    private void refreshAll() {
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
        this.ready=true;
    }

}
