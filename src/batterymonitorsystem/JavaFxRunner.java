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

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Company: Formula Electric Belgium Author: Sach Project: Umicore Nova Part:
 * BMS pc Created: Januari 2017
 */
public class JavaFxRunner extends Application {

    private Parent root;

    private static BatteryMonitorSystem monitorSystem;

    @Override
    public void start(Stage stage) {
        final CountDownLatch latch = new CountDownLatch(1);
        boolean loaded = false;

//        try {
//            this.root = FXMLLoader.load(getClass().getResource("SplashScreen.fxml"));
//        } catch (IOException ex) {
//            Logger.getLogger(JavaFxRunner.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        Scene loading = new Scene(root);
//        loading.getStylesheets().add(getClass().getResource("animated-gradient.css").toExternalForm());
//        stage.setScene(loading);
//        stage.show();
//        
//
//        JavaFxRunner.monitorSystem = new BatteryMonitorSystem(latch);
//        Thread monitorThread = new Thread(JavaFxRunner.monitorSystem);
//        monitorThread.start();
//        while (!loaded) {
//            try {
//                latch.await(1, TimeUnit.NANOSECONDS);
//            } catch (InterruptedException ex) {
//                loaded = true;
//            }
//        }

//        stage.hide();

        try {
            this.root = FXMLLoader.load(getClass().getResource("batteryMonitorLayout.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(JavaFxRunner.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("BatteryMonitorLayout.css").toExternalForm());
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("file:../../resources/IconFB.png"));
        stage.setTitle("BMS MONITOR UMICORE NOVA");
        stage.setScene(scene);
        stage.show();
        System.out.println("bms fxml runner done");
    }
}
