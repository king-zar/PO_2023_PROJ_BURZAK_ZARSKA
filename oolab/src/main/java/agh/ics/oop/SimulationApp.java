package agh.ics.oop;

import agh.ics.oop.model.MutationVariant;
import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.SimulationConfig;
import agh.ics.oop.model.WorldMap;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.application.Platform;
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
            SimulationConfig config = new SimulationConfig(mapWidth, mapHeight, simulationSteps, 2, 10, 50, MutationVariant.RANDOM, 32);

            configureStage(primaryStage, viewRoot);

            new Thread(() -> {
                Simulation simulation = new Simulation(config);
                WorldMap worldMap = simulation.getWorldMap();
                presenter.setWorldMap(worldMap);
                presenter.setConfig(config);
                worldMap.setMutationVariant(config.getMutationVariant());

                for (int i=0; i<simulationSteps; i++) {
                    if (presenter.isSimulationRunning()) {
                        simulation.simulateTimeStep();
                        presenter.mapChanged(worldMap, "Zmiana po kroku symulacji");
                    }
                    try {
                        Thread.sleep(500); // Dostosuj czas opóźnienia, jeśli potrzebujesz
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
