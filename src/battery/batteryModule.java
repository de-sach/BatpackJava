/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battery;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sach
 */
public class batteryModule {
    private List<batteryCell> module;
    private int id;
    private int nrOfCells;
    private int status;
    double voltage;
    double averageTemperature;
    
    public batteryModule(int id, int cellCount){
        this.id = id;
        this.nrOfCells = cellCount;
        module = new ArrayList();
        for (int i=0; i<this.nrOfCells;i++){
            //cells are loaded with given base characteristics
            batteryCell currentCell = new batteryCell(20.0, 3.5, i, 10);
            module.add(currentCell);
        }
    }   
    
    public void updateStatus(){
        int status = 10;
        for (batteryCell cell:module){
            if (cell.getHealth()<status){
                status = cell.getHealth();
            }
        }
        this.status = status;
    }
    
    public void updateVoltage(){
        double voltage = 0.0;
        voltage = module.stream().map((cell) -> cell.getVoltage()).reduce(voltage, (accumulator, _item) -> accumulator + _item);
        this.voltage=voltage;
    }
    
    public void updateTemperature(){
        double avgTemp=0.0;
        avgTemp = module.stream().map((cell) -> cell.getTemperature()).reduce(avgTemp, (accumulator, _item) -> accumulator + _item);
        this.averageTemperature=avgTemp;
    }

    public int getId() {
        return id;
    }

    public int getNrOfCells() {
        return nrOfCells;
    }

    public int getStatus() {
        return status;
    }

    public double getVoltage() {
        return voltage;
    }

    public double getAverageTemperature() {
        return averageTemperature;
    }
    
    public List<batteryCell> getBatteryCells(){
        return this.module;
    }
}
