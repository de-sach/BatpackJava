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
import battery.BatteryPacket;
import java.util.Arrays;

/**
 *
 * @author sach
 */
class MessageParser {

    int parseModuleOverview(char[] receiveBuffer) {
        int numberOfModules=0;
        int maxNumberOfChars = receiveBuffer.length; //10 char's per message & 144 cells && endmessage;
        System.out.println(Arrays.toString(receiveBuffer));
        char[] buffer;
        int index = 0;
        while(index<maxNumberOfChars){
            buffer=Arrays.copyOfRange(receiveBuffer, index, index+10);
            index+=10;
            if(isValid(buffer)){
                numberOfModules = parseNumber(buffer);
            }
        }
        return numberOfModules;
    }

    int parseCellOverview(char[] receiveBuffer) {
        int numberOfCells=0;
        int maxNumberOfChars = receiveBuffer.length; //10 char's per message & 144 cells && endmessage;
        System.out.println(Arrays.toString(receiveBuffer));
        char[] buffer;
        int index = 0;
        boolean valid=true;
        while(index<maxNumberOfChars&&valid){
            buffer=Arrays.copyOfRange(receiveBuffer, index, index+10);
            index+=10;
            if(isValid(buffer)){
                numberOfCells = parseNumber(buffer);
            }else{
                valid=false;
            }
        }
        return numberOfCells;
    }

    void parseAllVoltages(char[] receiveBuffer, BatteryPacket batpack) {
        int maxNumberOfChars = receiveBuffer.length; //10 char's per message & 144 cells && endmessage;
        System.out.println(Arrays.toString(receiveBuffer));
        char[] buffer;
        int index = 0;
        boolean valid=true;
        while(index<maxNumberOfChars&&valid){
            buffer=Arrays.copyOfRange(receiveBuffer, index, index+10);
            index+=10;
            if(isValid(buffer)){
                int cellid = parseID(buffer);
                int module_id = (int)Math.floor(cellid/batpack.getModules().get(0).getNrOfCells());
                int cellInModule = (int)Math.floor(cellid%batpack.getModules().get(module_id).getNrOfCells());
                BatteryCell cell = batpack.getModules().get(module_id).getBatteryCells().get(cellInModule);
                cell.setVoltage(parseVoltage(buffer));
            }else{
                valid=false;
            }
        }
    }
    
    void parseAllTemperatures(char[] receiveBuffer, BatteryPacket batpack) {
        int maxNumberOfChars = receiveBuffer.length; //10 char's per message & 144 cells && endmessage;
        System.out.println(Arrays.toString(receiveBuffer));
        char[] buffer;
        int index = 0;
        boolean valid=true;
        while(index<maxNumberOfChars&&valid){
            buffer=Arrays.copyOfRange(receiveBuffer, index, index+10);
            index+=10;
            if(isValid(buffer)){
                int cellid = parseID(buffer);
                int module_id = (int)Math.floor(cellid/batpack.getModules().get(0).getNrOfCells());
                int cellInModule = (int)Math.floor(cellid%batpack.getModules().get(module_id).getNrOfCells());
                BatteryCell cell = batpack.getModules().get(module_id).getBatteryCells().get(cellInModule);
                cell.setTemperature(parseTemperature(buffer));
            }else{
                valid=false;
            }
        }
    }

    void parseAllBalancing(char[] receiveBuffer, BatteryPacket batpack) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean isValid(char[] buffer) {
        if(buffer[8]=='\r'&&buffer[9]=='\n'){
            return true;
        }
        return false;
    }

    private int parseNumber(char[] buffer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int parseVoltage(char[] buffer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int parseID(char[] buffer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private double parseTemperature(char[] buffer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
