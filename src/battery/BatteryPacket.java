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
package battery;

import static battery.doubleHelper.round;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author sach
 */
public class BatteryPacket {

    private int moduleCount;
    private double totalVoltage;
    private List<BatteryModule> modules;
    private double averageTemperature;
    private double stateOfCharge;

    /**
     *
     * @param moduleCount
     */
    public BatteryPacket(int moduleCount) {
        this.moduleCount = moduleCount;
        this.modules = new ArrayList();

    }

  
    private void updateTotalVoltage() {
        double totalVoltage = 0.0;
        List synchronizedBatpack = Collections.synchronizedList(modules);
        synchronized (synchronizedBatpack) {
            Iterator i = synchronizedBatpack.iterator();
            while (i.hasNext()) {
                BatteryModule module = (BatteryModule) i.next();
                totalVoltage += module.getVoltage();
            }
        }
        this.totalVoltage = totalVoltage;
    }

    /**
     *
     */
    private void updateAvgTemp() {
        double avgTemp = 0;
        List synchronizedBatpack = Collections.synchronizedList(modules);
        synchronized (synchronizedBatpack) {
            Iterator i = synchronizedBatpack.iterator();
            while (i.hasNext()) {
                BatteryModule module = (BatteryModule) i.next();
                avgTemp += module.getAverageTemperature();
            }
        }
        avgTemp = avgTemp / (modules.size());
        this.averageTemperature = avgTemp;
    }

    /**
     *
     * @return
     */
    public int getModuleCount() {
        return moduleCount;
    }

    /**
     *
     * @return
     */
    public double getTotalVoltage() {
        updateTotalVoltage();
        return totalVoltage;
    }

    /**
     *
     * @return
     */
    public String getTotalVoltageAsString() {
        updateTotalVoltage();
        String totalVoltageAsString;
        totalVoltageAsString = Double.toString(round(totalVoltage, 2));
        totalVoltageAsString += " V";
        return totalVoltageAsString;
    }

    /**
     *
     * @return
     */
    public List<BatteryModule> getModules() {
        return modules;
    }

    /**
     *
     * @return
     */
    public double getAverageTemperature() {
        return averageTemperature;
    }

    /**
     *
     * @param module
     */
    public void addModule(BatteryModule module) {
        moduleCount++;
        modules.add(module);
    }

    /**
     *
     * @return
     */
    public String getAverageTemperatureAsString() {
        updateAvgTemp();
        String averageTemperatureAsString;
        averageTemperatureAsString = Double.toString(round(averageTemperature, 2));
        averageTemperatureAsString += " °C";
        return averageTemperatureAsString;
    }

    /**
     * A function to get the maximum temperature of the batpack as a string, usefull for interfacing with the use
     * @return the maximum temperature as String
     */
    public String getMaximumTemperatureAsString() {
        double maxCellTemperature = 0.00;

        synchronized (this.modules) {
            Iterator moduleIterator = modules.iterator();
            while (moduleIterator.hasNext()) {
                BatteryModule module = (BatteryModule) moduleIterator.next();
                Iterator cellIterator = module.getBatteryCells().iterator();
                while (cellIterator.hasNext()) {
                    BatteryCell cell = (BatteryCell) cellIterator.next();
                    if (cell.getTemperature() > maxCellTemperature) {
                        maxCellTemperature = cell.getTemperature();
                    }
                }
            }
        }
        String maximumTemperatureAsString = Double.toString(round(maxCellTemperature, 2));
        maximumTemperatureAsString += " °C";
        System.out.println(maximumTemperatureAsString);
        return maximumTemperatureAsString;
    }
    
    /**
     * A getter for the state of charge of the batterypack
     * @return the state of charge of the batterypack
     */
    public double getStateOfCharge(){
        this.stateOfCharge = calculateStateOfCharge();
        return this.stateOfCharge;
    }

    private double calculateStateOfCharge() {
        double total=0, average;
        synchronized(modules){
            Iterator module = modules.iterator();
            while(module.hasNext()){
                BatteryModule mod = (BatteryModule) module.next();
                total += mod.getStateOfCharge();
            }
        }
        average = total/this.moduleCount;
        return average;
    }

}
