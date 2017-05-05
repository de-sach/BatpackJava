/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storage;

import battery.BatteryCell;
import battery.BatteryModule;
import battery.BatteryPacket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sach
 */
public class dbConnector {

    private Connection connection;

    public dbConnector() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        connection = null;
    }

    public void createTable(boolean exists) {
        if (!exists) {
            try {
                try {
                    Class.forName("org.sqlite.JDBC");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, null, ex);
                }
                connection = DriverManager.getConnection("jdbc:sqlite:./src/storage/batteryMonitor.db");
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(10);  // set timeout to 30 sec.
                statement.executeUpdate("drop table if exists cells");
                statement.executeUpdate("CREATE TABLE `cells` (`pk` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,	`id` INTEGER, `voltage` REAL, `temperature` REAL, `health` INTEGER);");
            } catch (SQLException e) {
                // if the error message is "out of memory", 
                // it probably means no database file is found
                System.err.println(e.getMessage());
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    // connection close failed.
                    System.err.println(e);
                }
            }
        }
    }

    public void insertCell(BatteryCell cell) {
        try {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            connection = DriverManager.getConnection("jdbc:sqlite:./src/storage/batteryMonitor.db");
            PreparedStatement setCell = connection.prepareStatement("insert into cells (id,voltage,temperature,health) values (?,?,?,?);");
            setCell.setInt(1, cell.getId());
            setCell.setInt(4, cell.getHealth());
            setCell.setDouble(2, cell.getVoltage());
            setCell.setDouble(3, cell.getTemperature());
            setCell.setQueryTimeout(10);
            setCell.execute();
        } catch (SQLException ex) {
            Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void insertModule(BatteryModule module) {
        try {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            connection = DriverManager.getConnection("jdbc:sqlite:./src/storage/batteryMonitor.db");
            for (BatteryCell cell : module.getBatteryCells()) {
                PreparedStatement setCell = connection.prepareStatement("insert into cells (id,voltage,temperature,health) values (?,?,?,?);");
                setCell.setInt(1, cell.getId());
                setCell.setInt(4, cell.getHealth());
                setCell.setDouble(2, cell.getVoltage());
                setCell.setDouble(3, cell.getTemperature());
                setCell.setQueryTimeout(10);
                setCell.execute();
            }
        } catch (SQLException ex) {
            Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertBatpack(BatteryPacket packet) {
        try {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            connection = DriverManager.getConnection("jdbc:sqlite:./src/storage/batteryMonitor.db");
            for (BatteryModule module : packet.getModules()) {
                for (BatteryCell cell : module.getBatteryCells()) {
                    PreparedStatement setCell = connection.prepareStatement("insert into cells (id,voltage,temperature,health) values (?,?,?,?);");
                    setCell.setInt(1, cell.getId());
                    setCell.setInt(4, cell.getHealth());
                    setCell.setDouble(2, cell.getVoltage());
                    setCell.setDouble(3, cell.getTemperature());
                    setCell.setQueryTimeout(10);
                    setCell.execute();
                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
