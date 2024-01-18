package agh.ics.oop.presenter;

import agh.ics.oop.model.MapChangeListener;
import agh.ics.oop.model.WorldMap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class SimulationPresenter implements MapChangeListener {

    @FXML
    private Label infoLabel;

    private WorldMap worldMap;

    private boolean simulationRunning = true;

    public boolean isSimulationRunning () {
        return simulationRunning;
    }

    @Override
    public void mapChanged(WorldMap map, String message) {
        Platform.runLater(() -> {
            drawMap();
        });
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
        this.worldMap.subscribe(this);
        drawMap();
    }

    private void drawMap() {
        Platform.runLater(() -> {
            infoLabel.setText(worldMap.toString());
        });
    }

    @FXML
    public void startSimulation() {
        if (!simulationRunning) {
            // Rozpocznij symulację
            infoLabel.setText("Simulation started!");
            simulationRunning = true;
        }
    }

    @FXML
    public void pauseSimulation() {
        if (simulationRunning) {
            // Wstrzymaj symulację
            infoLabel.setText("Simulation paused.");
            simulationRunning = false;
        }
    }
}
