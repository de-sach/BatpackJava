/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battery;

import static battery.doubleHelper.round;
import java.util.ArrayList;
import java.util.List;


/**
 * A class to show the battery modules.
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

    /**
     * get the balancing situation of the batterymodule
     * @return boolean with balancing state
     */
    public boolean isBalance() {
        return balance;
    }
    
    /**
     * constructor of Battery Module
     * @param id            id of the module
     * @param cellCount     count of cells
     * initial value of a cell is 20°C, 3.5V, id, and a health of 10
     */
    public BatteryModule(int id, int cellCount){
        this.id = id;
        this.nrOfCells = cellCount;
        module = new ArrayList();
        for (int i=0; i<this.nrOfCells;i++){
            //cells are loaded with given base characteristics
            BatteryCell currentCell = new BatteryCell(20.0, 3.5, i, 10);
            module.add(currentCell);
        }
    }   
    
    /**
     * update the health status of the cells
     */
    public void updateStatus(){
        int status = 10;
        for (BatteryCell cell:module){
            if (cell.getHealth()<status){
                status = cell.getHealth();
            }
        }
        this.status = status;
    }
    
    /**
     * update the voltage of the cells
     */
    public void updateVoltage(){
        double voltage = 0.0;
        voltage = module.stream().map((cell) -> cell.getVoltage()).reduce(voltage, (accumulator, _item) -> accumulator + _item);
        this.voltage=voltage;
    }
    
    /**
     * update the temperature of the cells
     */
    public void updateTemperature(){
        double avgTemp=0.0;
        avgTemp = module.stream().map((cell) -> cell.getTemperature()).reduce(avgTemp, (accumulator, _item) -> accumulator + _item);
        avgTemp = avgTemp/this.getNrOfCells();
        this.averageTemperature=avgTemp;
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
    public List<BatteryCell> getBatteryCells(){
        return this.module;
    }

    /**
     *
     * @return
     */
    public String getVoltageAsString() {
        updateVoltage();
        String totalVoltageAsString;
        totalVoltageAsString = Double.toString(round(this.voltage,2));
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
        averageTemperatureAsString = Double.toString(round(averageTemperature,2));
        averageTemperatureAsString+= " °C";
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
}
