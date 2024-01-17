package agh.ics.oop.presenter;

import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.WorldMap;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SimulationPresenter implements MapChangeListener {
    private WorldMap worldMap;

    @FXML
    private Label infoLabel;

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        System.out.println("Map changed: " + message);
        drawMap();
    }

    public void drawMap() {
        infoLabel.setText(worldMap.toString());
    }
}
