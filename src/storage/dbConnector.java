/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storage;

import battery.batteryCell;
import com.almworks.sqlite4java.SQLiteConnection;
import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sach
 */
public class dbConnector {

    private File myDb;
    private SQLiteConnection dbConnection;

    public dbConnector() throws SQLiteException, IOException {
        System.out.println("loading db");
        this.myDb = new File("./src/storage/batteryMonitor.db");
        if (!this.myDb.exists()) {
            boolean created = this.myDb.createNewFile();
            if (!created) {
                System.out.println("error in creating db file");
            }
            System.out.println("db created");
        }
        this.dbConnection = new SQLiteConnection(this.myDb);
        this.dbConnection.open(true);
        System.out.println("db loaded");
        createTable(true);
    }

    private void createTable(boolean exists) throws SQLiteException {
        if (!exists) {
            SQLiteStatement create = dbConnection.prepare("CREATE TABLE `Cells` (`pk` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,'id' INTEGER, `voltage`	REAL,`temperature`REAL, `health` INTEGER);");
            try {
                while (create.step()) {
                    System.out.println("executed one step");
                }
            } catch (SQLiteException ex) {
                Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                create.dispose();
            }
        }
    }

    public void setParams(batteryCell cell) throws SQLiteException {
        String sql = "INSERT INTO Cells(id,voltage,temperature,health) values (?,?,?,?)";
        SQLiteStatement setCell = dbConnection.prepare(sql);
        setCell.bind(1, cell.getId());
        setCell.bind(2, cell.getVoltage());
        setCell.bind(3, cell.getTemperature());
        setCell.bind(4, cell.getHealth());
        while (setCell.step()) {
            System.out.println("adding a cell");
        }
        setCell.dispose();
    }
}
