/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battery;

import static battery.doubleHelper.round;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sach
 */
public class BatteryCell {
    private Double temperature;
    private Double voltage;
    private int id;
    private int health;
    private List<Double> temperatureProgress;
    private List<Double> voltageProgress;
    private final Instant creation;
    private Instant lastMeasurement;
    
    /**
     *
     * @param temp:     temperature of the cell
     * @param volt:     voltage of the cell
     * @param id:       id of the cell
     * @param health:   cell halth
     */
    public BatteryCell(double temp, double volt, int id, int health){
        this.temperature = temp;
        this.voltage = volt;
        this.id = id;
        this.health = health;
        temperatureProgress = new ArrayList<>();
        voltageProgress = new ArrayList<>();
        this.creation = Instant.now();
    }

    /**
     *
     * @return the cells temperature
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * set the cell temperature
     * @param temperature 
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
        this.temperatureProgress.add(temperature);
        this.lastMeasurement = Instant.now();
    }

    /**
     *
     * @return the cells voltage
     */
    public double getVoltage() {
        return voltage;
    }

    /**
     * set the cell voltage
     * @param voltage
     */
    public void setVoltage(double voltage) {
        this.voltage = voltage;
        this.voltageProgress.add(voltage);
        this.lastMeasurement = Instant.now();
    }

    /**
     * get the cell id 
     * @return cell id
     */
    public int getId() {
        return id;
    }

    /**
     * set the cell id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get the health of the cell
     * @return health
     */
    public int getHealth() {
        return health;
    }

    /**
     * set the health of the cell
     * @param health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * get the voltage as a string with 2 decimal places
     * @return String of the voltage
     */
    public String getVoltageAsString() {
        String totalVoltageAsString;
        totalVoltageAsString = Double.toString(round(this.voltage,4));
        totalVoltageAsString += " V";
        return totalVoltageAsString;
    }

    /**
     * get the temperature of the string with 2 decimal places
     * @return String of the temperature
     */
    public String getTemperatureAsString() {
        String tempAsString;
        tempAsString = Double.toString(round(this.temperature,4));
        tempAsString += "°C";
        return tempAsString;
    }

    /**
     * set the measurement to a specific instant
     * @param now the instant the meaasurement took place
     */
    public void setLastMeasurement(Instant now) {
        this.lastMeasurement = now;
    }
    
    /**
     * get the last measurement of this cell
     * @return the last instant measured
     */
    public Instant getLastMeasurement(){
        return this.lastMeasurement;
    }
}
