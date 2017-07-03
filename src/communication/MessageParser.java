/*
 * Copyright (C) 2017 sach
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

import battery.BatteryCell;
import battery.BatteryModule;
import battery.BatteryPacket;
import java.time.Instant;

/**
 *
 * @author sach
 */
class MessageParser {

    private BatteryPacket batpack;
    private int nrOfModules = 9;
    private int cellIndex;
    private int moduleIndex;

    public MessageParser(BatteryPacket batpack) {
        this.batpack = new BatteryPacket(0);

    }

    int parseMessage(String message) {
        int status = 0;
        String part = new String();
        BatteryModule module;
        if (message.length() > 2) {
            part = message.substring(0, 1);
            //System.out.println("part is: " + part);
            if (message.equals("End")) {
                return status;
            }
            if (checkMessage(message)) {

                if (part.equals("M")) {
                    if (message.split("_").length == 2) {
                        //System.out.println("parsing M");
                        String[] split = message.split("_");
                        this.nrOfModules = Integer.parseInt(split[split.length - 1]);

                        if (this.nrOfModules < batpack.getModuleCount()) {
                            for (int i = batpack.getModuleCount(); i < this.nrOfModules; i++) {
                                BatteryModule emptyModule = new BatteryModule(i, 16);
                                batpack.addModule(emptyModule);
                            }
                        }
                    }
                } else {
                    BatteryCell cell;
//                    System.out.println("crash on message "+message);
                    if (message.split("_")[0].length() > 1) {
                        this.cellIndex = Integer.parseInt(message.split("_")[0].substring(1, message.split("_")[0].length())) - 1;
                        int cellInModule = this.cellIndex % 16;
                        this.moduleIndex = this.cellIndex / 16;

//                    System.out.println("cell, cell in module and module: " + cellIndex + "---" + cellInModule + "---" + moduleIndex);
                        if (moduleIndex >= this.batpack.getModuleCount()) {
                            module = new BatteryModule(moduleIndex, 0);
                            batpack.addModule(module);
//                        System.out.println("added module");
                            cell = new BatteryCell(0, 0, cellInModule, 0);
                            module.addCell(cell);
                        } else {
                            module = this.batpack.getModules().get(moduleIndex);
                            if (cellInModule >= module.getNrOfCells()) {
                                cell = new BatteryCell(0, 0, cellInModule, 0);
                                module.addCell(cell);
                            } else {
                                cell = module.getBatteryCells().get(cellInModule);
                            }
                        }
                        cell.setLastMeasurement(Instant.now());
//                    System.out.println("last measurement set");
                        switch (part) {
                            case "T":
                                //String[] split = message.split("_");
                                if (message.split("_").length == 2) {
                                    double temp = Double.parseDouble(message.split("_")[1]) / 1000;
                                    cell.setTemperature(temp);
                                    break;
                                }
                            case "M":
                                if (message.split("_").length == 2) {
                                    //System.out.println("parsing M");
                                    String[] split = message.split("_");
                                    this.nrOfModules = Integer.parseInt(split[split.length - 1]);
                                    break;
                                }
                            case "V":
                                if (message.split("_").length == 2) {
                                    //System.out.println("parsing V");
                                    double voltage = Double.parseDouble(message.split("_")[1]) / 1000;
                                    //System.out.println("cellindex: "+cellIndex);
                                    this.moduleIndex = this.cellIndex / 16;
                                    //System.out.println("moduleIndex: "+moduleIndex);
//                            assert (this.batpack != null);
//                            assert (this.batpack.getModuleCount() != 0 && !this.batpack.getModules().isEmpty());
//                            assert (this.batpack.getModules().get(moduleIndex) != null && this.batpack.getModules().get(cellIndex % 16) != null);
                                    cell.setVoltage(voltage);
                                    break;
                                }
                            case "B":
                                if (message.split("_").length == 2) {

                                    boolean balance = false;
                                    if (Integer.parseInt(message.split("_")[1]) == 1) {
                                        balance = true;
                                    }
                                    this.batpack.getModules().get(moduleIndex).setBalancing(balance);
                                }
                            default:
                                System.out.println("unnknown command");
                                status = 1;
                        }
                    }
                }
                /*
        System.out.println("batpack: " + this.batpack);
        if (this.batpack != null) {
            System.out.println("nr of modules: " + this.batpack.getModuleCount());
            if (this.batpack.getModuleCount() != 0) {
                if (this.batpack.getModules().get(0) != null) {
                    System.out.println("nr of cells: " + this.batpack.getModules().get(0).getBatteryCells().size());
                }
            }
        }
                 */
            } else {
                status = 1;
            }
        } else {
            status = 1;
        }
//        System.out.println("message parsed");
        return status;
    }

    public BatteryPacket getBatpack() {
        return this.batpack;
    }

    boolean getBatpackReady() {
//        System.out.println("Checking batpack");
        boolean ready = false;
        if (this.batpack != null) {
//            System.out.println(this.batpack.getModuleCount());
//            System.out.println(this.nrOfModules);
            if (this.batpack.getModuleCount() == this.nrOfModules) {
//                System.out.println(this.nrOfModules + " modules found");
//                System.out.println(this.batpack.getModules().get(this.nrOfModules - 1).getNrOfCells());
                if (this.batpack.getModules().get(this.nrOfModules - 1).getNrOfCells() == 16) {
                    ready = true;
                    for (BatteryModule module : this.batpack.getModules()) {
                        for (BatteryCell cell : module.getBatteryCells()) {
                            if (cell.getVoltage() == 0) {
                                ready = false;
                            }
                        }
                    }
                }
            }
        }
        return ready;
    }

    private boolean checkMessage(String message) {
        String messageParts[] = message.split("_");
        System.out.println("message to check:" + message);
//        System.out.println("length = " + messageParts.length);
        if (messageParts.length > 1 || messageParts.length <= 3) {
//            System.out.println("length ok");
            if (messageParts.length == 2) {
                String prefix = messageParts[0];
                if (prefix != null && !prefix.equals("")) {
                    if (prefix.length() > 1) {
                        if (prefix.substring(0, 1).matches("[A-Z]+")) {
                            if (prefix.substring(1, prefix.length() - 1).matches("[0-9]+")) {
                                //received message is a measurement
                                if (messageParts[1].length() == 4) {
                                    if (messageParts[1].matches("[0-9]+")) {
//                                    System.out.println("message: " + message);
                                        return true;
                                    }
                                }
                            }
                        }
                    } else {
                        if (prefix.substring(0, 1).matches("[A-Z]+")) {
                            if (messageParts[1].matches("[0-9]+")) {
//                                System.out.println("message: " + message);
                                return true;
                            }
                        }
                    }
                }
            } else {
                System.out.println("command was returned");
                return false;
            }
        }
        return false;
//        
//        if (message.split("_").length == 2 || message.split("_").length == 3) {
//            if (message.substring(0, 1).matches("[A-Z]+")) {
//                if (message.split("_")[1].matches("[0-9]+")) {
//                    System.out.println("message accepted: " + message);
//                    return true;
//                }
//            }
//        }
//        return false;
    }

}
