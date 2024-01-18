package agh.ics.oop;

import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.SimulationConfig;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class SimulationApp extends Application {
    private int mapWidth;
    private int mapHeight;
    private int simulationSteps;

    public SimulationApp(int mapWidth, int mapHeight, int simulationSteps){
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.simulationSteps = simulationSteps;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
            BorderPane viewRoot = loader.load();

            SimulationPresenter presenter = loader.getController();
            WorldMap worldMap = initializeWorldMap();
            presenter.setWorldMap(worldMap);

            configureStage(primaryStage, viewRoot);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WorldMap initializeWorldMap() {
        SimulationConfig config = new SimulationConfig(mapWidth, mapHeight, simulationSteps, 20, 10, 50, 32);
        Simulation simulation = new Simulation(config);
        simulation.runSimulation();
        return simulation.getWorldMap();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
