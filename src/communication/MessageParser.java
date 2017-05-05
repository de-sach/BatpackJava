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
import java.util.Arrays;

/**
 *
 * @author sach
 */
class MessageParser {

    private BatteryPacket batpack;
    private int nrOfModules;
    private int cellIndex;

    public MessageParser(BatteryPacket batpack) {
        this.batpack = new BatteryPacket(0);

    }

    int parseMessage(String message) {
        int status = 0;
        String part = new String();
        BatteryModule module;
        part = message.substring(0, 1);
        System.out.println("part is: " + part);
        if (message.equals("End")) {
            return status;
        }
        switch (part) {
            case "T":
                //String[] split = message.split("_");
                this.cellIndex = Integer.parseInt(message.split("_")[0].substring(1, message.split("_")[0].length()));
                int moduleNr = this.cellIndex / 16;
                double temp = Double.parseDouble(message.split("_")[1]) / 1000;
                this.batpack.getModules().get(moduleNr).getBatteryCells().get(cellIndex % 16).setTemperature(temp);
                break;
            case "M":
                //System.out.println("parsing M");
                String[] split = message.split("_");
                this.nrOfModules = Integer.parseInt(split[split.length - 1]);
                break;
            case "V":
                //System.out.println("parsing V");
                this.cellIndex = Integer.parseInt(message.split("_")[0].substring(1, message.split("_")[0].length()));
                //System.out.println("cellindex: "+cellIndex);
                int moduleIndex = this.cellIndex / 16;
                //System.out.println("moduleIndex: "+moduleIndex);
                int cellInModule = this.cellIndex % 16;
                BatteryCell cell = new BatteryCell(0, 0, cellInModule, 0);
                if (moduleIndex == this.batpack.getModuleCount()) {
                    module = new BatteryModule(moduleIndex, 0);
                    batpack.addModule(module);
                } else {
                    module = this.batpack.getModules().get(moduleIndex);
                }
                module.addCell(cell);
                break;
            case "B":
                this.cellIndex = Integer.parseInt(message.split("_")[0].substring(1,message.split("_")[0].length()));
                int modulePos = this.cellIndex/16;
                boolean balance = false;
                if(Integer.parseInt(message.split("_")[1])==1){
                    balance = true;
                }
                this.batpack.getModules().get(modulePos).setBalancing(balance);
            default:
                System.out.println("unnknown command");
                status = 1;
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
        return status;
    }

    public BatteryPacket getBatpack() {
        return this.batpack;
    }

    boolean getBatpackReady() {
        boolean ready = false;
        if (this.batpack != null) {
            if (this.batpack.getModuleCount() == 9) {
                if (this.batpack.getModules().get(8).getNrOfCells() == 15) {
                    ready = true;
                }
            }
        }
        return ready;
    }

}