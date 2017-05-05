/*
 * Copyright (C) 2017 Peter
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
package batterymonitorsystem;

import java.util.concurrent.CountDownLatch;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Company: Formula Electric Belgium Author: Sach Project: Umicore Nova Part:
 * BMS pc Created: Januari 2017
 */
public class JavaFxRunner extends Application {

    private Parent root;

    private static BatteryMonitorSystem monitorSystem;

    @Override
    public void start(Stage stage) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        JavaFxRunner.monitorSystem = new BatteryMonitorSystem(latch);
        Thread monitorThread = new Thread(JavaFxRunner.monitorSystem);
        monitorThread.start();

        latch.await();

        this.root = FXMLLoader.load(getClass().getResource("batteryMonitorLayout.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
