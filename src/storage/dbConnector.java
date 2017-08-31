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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sach
 */
public class dbConnector {

    private Connection connection;

    /**
     * A class used to connect to the database
     */
    public dbConnector() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        connection = null;
    }
    /**
     * a function used to create tables in the database
     * @param exists a boolean that has to be set when a table already exists to avoid dropping the table
     */
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

    /**
     * A method used to insert all relevant values of a cell in the database
     * @param cell a cell of the BatteryPacket
     */
    public void insertCell(BatteryCell cell) {
        try {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ex) {
                System.out.println("jdbc connector not found");
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
    
    /**
     * A wrapper method to automate inserting all cells in a module
     * @param module a BatteryPacket module consisting out of a set number of cells
     */
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
    
    /**
     * A wrapper method to insert all data of all cells directly into the database
     * @param packet The BatteryPacket who's celldata is to be inserted
     */
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
                    setCell.setInt(1, (cell.getId()+16*module.getId()));
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
    
    /**
     * A lookup function for the table with the voltages and states of charge of a lithium polymer cell used for state of charge calculations
     * @return a dictionary consisting of voltage - state of charge pairs.
     */
    public Dictionary getVoltageLookupTable(){
        Dictionary<Integer,Integer> lookupTable;
        lookupTable = new Hashtable<>();
        try {
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
            connection = DriverManager.getConnection("jdbc:sqlite:./src/storage/batteryMonitor.db");
            PreparedStatement ps = connection.prepareStatement("SELECT percentage, voltage FROM voltage_lookup");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                lookupTable.put(rs.getInt("voltage"),rs.getInt("percentage"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lookupTable;
    }
    
}
