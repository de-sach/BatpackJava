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
    
    
    public BatteryCell(double temp, double volt, int id, int health){
        this.temperature = temp;
        this.voltage = volt;
        this.id = id;
        this.health = health;
        temperatureProgress = new ArrayList<>();
        voltageProgress = new ArrayList<>();
        this.creation = Instant.now();
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
        this.temperatureProgress.add(temperature);
        this.lastMeasurement = Instant.now();
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
        this.voltageProgress.add(voltage);
        this.lastMeasurement = Instant.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getVoltageAsString() {
        String totalVoltageAsString;
        totalVoltageAsString = Double.toString(round(this.voltage,4));
        totalVoltageAsString += " V";
        return totalVoltageAsString;
    }

    public String getTemperatureAsString() {
        String tempAsString;
        tempAsString = Double.toString(round(this.temperature,4));
        tempAsString += "°C";
        return tempAsString;
    }

    public void setLastMeasurement(Instant now) {
        this.lastMeasurement = now;
    }
    
    public Instant getLastMeasurement(){
        return this.lastMeasurement;
    }
}
