/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battery;

import static battery.doubleHelper.round;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A class to show the battery modules.
 *
 * @author sach
 */
public class BatteryModule {

    private List<BatteryCell> module;
    private int id;
    private int nrOfCells;
    private int status;
    double voltage;
    double averageTemperature;
    private boolean balance;
    private double stateOfCharge;

    /**
     * get the balancing situation of the batterymodule
     *
     * @return boolean with balancing state
     */
    public boolean isBalance() {
        return balance;
    }

    /**
     * constructor of Battery Module
     *
     * @param id id of the module
     * @param cellCount count of cells initial value of a cell is 20°C, 3.5V,
     * id, and a health of 10
     */
    public BatteryModule(int id, int cellCount) {
        this.id = id;
        this.nrOfCells = cellCount;
        module = new ArrayList();
        for (int i = 0; i < this.nrOfCells; i++) {
            //cells are loaded with given base characteristics
            BatteryCell currentCell = new BatteryCell(20.0, 3.5, i, 10);
            module.add(currentCell);
        }
    }

    /**
     * update the health status of the cells
     */
    public void updateStatus() {
        int status = 10;
        for (BatteryCell cell : module) {
            if (cell.getHealth() < status) {
                status = cell.getHealth();
            }
        }
        this.status = status;
    }

    /**
     * update the voltage of the cells
     */
    public void updateVoltage() {
        double voltage = 0.0;
        List synchronizedModule = Collections.synchronizedList(module);
        synchronized (synchronizedModule) {
            Iterator i = synchronizedModule.iterator();
            while (i.hasNext()) {
                BatteryCell cell = (BatteryCell) i.next();
                voltage += cell.getVoltage();
            }
        }
        this.voltage = voltage;
    }

    /**
     * update the temperature of the cells
     */
    public void updateTemperature() {
        double avgTemp = 0.0;
        List synchronizedModule = Collections.synchronizedList(module);
        synchronized (synchronizedModule) {
            Iterator i = synchronizedModule.iterator();
            while (i.hasNext()) {
                BatteryCell cell = (BatteryCell) i.next();
                avgTemp += cell.getTemperature();
            }
        }
        avgTemp = avgTemp / this.getNrOfCells();
        this.averageTemperature = avgTemp;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public int getNrOfCells() {
        return nrOfCells;
    }

    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @return
     */
    public double getVoltage() {
        updateVoltage();
        return voltage;
    }

    /**
     *
     * @return
     */
    public double getAverageTemperature() {
        updateTemperature();
        return averageTemperature;
    }

    /**
     *
     * @return
     */
    public List<BatteryCell> getBatteryCells() {
        return this.module;
    }

    /**
     *
     * @return
     */
    public String getVoltageAsString() {
        updateVoltage();
        String totalVoltageAsString;
        totalVoltageAsString = Double.toString(round(this.voltage, 2));
        totalVoltageAsString += " V";
        return totalVoltageAsString;
    }

    /**
     *
     * @return
     */
    public String getAverageTemperatureAsString() {
        updateTemperature();
        String averageTemperatureAsString;
        averageTemperatureAsString = Double.toString(round(averageTemperature, 2));
        averageTemperatureAsString += " °C";
        return averageTemperatureAsString;
    }

    /**
     *
     * @param cell
     */
    public void addCell(BatteryCell cell) {
        this.nrOfCells++;
        this.module.add(cell);
    }

    /**
     *
     * @param balance
     */
    public void setBalancing(boolean balance) {
        this.balance = balance;
    }

     /**
     * get the state of charge of this module
     * @return the current state of charge
     */
    public double getStateOfCharge() {
        this.stateOfCharge = calculateSOC();
        return stateOfCharge;
    }

    private double calculateSOC() {
        double total = 0, average;
        synchronized (module) {
            Iterator cells = module.iterator();
            while (cells.hasNext()) {
                BatteryCell cell = (BatteryCell) cells.next();
                total += cell.getStateOfCharge();
            }
        }
        average = total / this.nrOfCells;
        return average;
    }

}
