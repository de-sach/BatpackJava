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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Company: Formula Electric Belgium Author: Sach Project: Umicore Nova Part:
 * BMS pc Created: Januari 2017
 */
public class SplashScreenController implements Initializable {
    
    @FXML
    private Text loading;
    
    @FXML
    private Text bms;
    
    private int loadingCounter;
    private int bmsCounter;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showLoading();
    }
    
    private void showLoading() {
        final KeyFrame oneFrame = new KeyFrame(Duration.millis(750), (ActionEvent evt) -> {
               loading.setText(getNextLoadingText());
               //bms.setText(getNextBmsText());
        });
        Timeline timer = TimelineBuilder.create().cycleCount(Animation.INDEFINITE).keyFrames(oneFrame).build();
        timer.playFromStart();
    }

    private String getNextLoadingText() {
        String loading = "Loading";
        loadingCounter++;
        if(loadingCounter>loading.length()){
            loadingCounter = 1;
        }
        return loading.substring(0,loadingCounter);
    }

    private String getNextBmsText() {
         String BMS = "BMS";
        bmsCounter++;
        if(bmsCounter>BMS.length()){
            bmsCounter = 1;
        }
        return BMS.substring(0,bmsCounter);
    }
}
