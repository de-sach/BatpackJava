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

    public BatteryPacket(int moduleCount) {
        this.moduleCount = moduleCount;
        this.modules = new ArrayList();

    }

    public void updateTotalVoltage() {
        double totalVoltage = 0.0;
        for (BatteryModule module : modules) {
            totalVoltage += module.getVoltage();
        }
        this.totalVoltage = totalVoltage;
    }

    public void updateAvgTemp() {
        double avgTemp = 0;
        for (BatteryModule module : modules) {
            avgTemp += module.getAverageTemperature();
        }
        avgTemp = avgTemp / (modules.size());
        this.averageTemperature = avgTemp;
    }

    public int getModuleCount() {
        return moduleCount;
    }

    public double getTotalVoltage() {
        updateTotalVoltage();
        return totalVoltage;
    }

    public String getTotalVoltageAsString() {
        updateTotalVoltage();
        String totalVoltageAsString;
        totalVoltageAsString = Double.toString(round(totalVoltage,2));
        totalVoltageAsString += " V";
        return totalVoltageAsString;
    }

    public List<BatteryModule> getModules() {
        return modules;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }

    public void addModule(BatteryModule module) {
        moduleCount++;
        modules.add(module);
    }

     public String getAverageTemperatureAsString() {
        updateAvgTemp();
        String averageTemperatureAsString;
        averageTemperatureAsString = Double.toString(round(averageTemperature,2));
        averageTemperatureAsString+= " °C";
        return averageTemperatureAsString;
    }
    
   

   
}
