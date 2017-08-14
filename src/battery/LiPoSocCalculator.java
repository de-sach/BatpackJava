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

package battery;

import java.util.Dictionary;
import java.util.Enumeration;
import storage.dbConnector;

/**
 * Company: Formula Electric Belgium
 * Author: Sach
 * Project: Umicore Nova
 * Part: BMS pc
 * Created: Januari 2017
 */
public class LiPoSocCalculator {
    private double voltage;
    private final Dictionary lookupTable;
    private int soc;
    private final dbConnector dbcon;

    public LiPoSocCalculator(double voltage) {
        this.voltage = voltage;
        this.soc = 0;
        dbcon = new dbConnector();
        this.lookupTable =  dbcon.getVoltageLookupTable();
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public int getSoc() {
        return calculateSoc(voltage);
    }    
    
    public int getSoc(double voltage){
        this.setVoltage(voltage);
        return calculateSoc(this.voltage);
    }

    private int calculateSoc(double voltage) {
        Integer nearestLowerVolt, nearestHigherVolt;
        Integer nearestLowerPercent, nearestHigherPercent;
        int progress;
        nearestLowerVolt = 2800;
        nearestHigherVolt = 4300;
        Enumeration keys = lookupTable.keys();
        while(keys.hasMoreElements()){
            Integer key = (Integer) keys.nextElement();
            if(key<voltage*1000){
                if(key>nearestLowerVolt){
                    nearestLowerVolt=key;
                }
            }else if(key>voltage*1000){
                if(key<nearestHigherVolt){
                    nearestHigherVolt=key;
                }
            }
        }
//        System.out.println("nearest high & low: "+nearestHigherVolt+" "+nearestLowerVolt);
        nearestHigherPercent = (Integer) lookupTable.get(nearestHigherVolt);
        nearestLowerPercent = (Integer) lookupTable.get(nearestLowerVolt);
        progress = (int) ((((voltage*1000)-nearestLowerVolt)/(nearestHigherVolt-nearestLowerVolt)*(nearestHigherPercent-nearestLowerPercent))+nearestLowerPercent);
        this.soc = progress;
        return this.soc;
    
    }
}
