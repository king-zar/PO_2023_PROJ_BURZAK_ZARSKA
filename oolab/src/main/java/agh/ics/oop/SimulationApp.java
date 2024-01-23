package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.variants.MapVariant;
import agh.ics.oop.model.variants.MutationVariant;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationApp extends Application {
    private int mapWidth;
    private int mapHeight;
    private int simulationSteps;
    private int animalCount;
    private int initialPlantCount;
    private int plantToGrowPerStep;
    private MutationVariant mutationVariant;
    private int minMutations;
    private int maxMutations;
    private MapVariant mapVariant;
    private int maxPlantNutrition;
    private int initialAnimalEnergy;
    private int genomeLength;
    private int energyToReproduce;
    private int energyLostInReproduction;
    private int waterAreasCount;
    private int initialWaterAreaSize;
    private int inflowOutflowSize;

    public SimulationApp(Configuration configuration){
        this.mapWidth = configuration.width;
        this.mapHeight = configuration.height;
        this.simulationSteps = configuration.simulationSteps;
        this.animalCount = configuration.initialAnimalCount;
        this.initialAnimalEnergy = configuration.initialAnimalEnergy;
        this.initialPlantCount = configuration.initialPlantCount;
        this.plantToGrowPerStep = configuration.plantToGrowPerStep;
        this.mutationVariant = MutationVariant.valueOf(configuration.mutationVariant);
        this.minMutations = configuration.minMutations;
        this.maxMutations = configuration.maxMutations;
        this.mapVariant = MapVariant.valueOf(configuration.mapVariant);
        this.maxPlantNutrition =  configuration.maxPlantNutrition;
        this.genomeLength = configuration.genomeLength;
        this.energyToReproduce = configuration.energyToReproduce;
        this.energyLostInReproduction = configuration.energyLostInReproduction;
        this.waterAreasCount = configuration.waterAreasCount;
        this.initialWaterAreaSize = configuration.initialWaterAreaSize;
        this.inflowOutflowSize = configuration.inflowOutflowSize;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
            BorderPane viewRoot = loader.load();

            configureStage(primaryStage, viewRoot);

            new Thread(() -> {
                SimulationPresenter presenter = loader.getController();
                SimulationConfig config = new SimulationConfig(mapWidth, mapHeight, simulationSteps, animalCount,
                        initialAnimalEnergy, initialPlantCount, plantToGrowPerStep, mutationVariant,  minMutations,
                        maxMutations, mapVariant, maxPlantNutrition, genomeLength, energyToReproduce, energyLostInReproduction,
                        waterAreasCount, initialWaterAreaSize, inflowOutflowSize);

                Simulation simulation = new Simulation(config);
                TidesOutflowsMap worldMap = simulation.getWorldMap();
                presenter.setWorldMap(worldMap);
                presenter.setConfig(config);

                for (int i=0; i<simulationSteps; i++) {
                    if (presenter.isSimulationRunning() && simulation.anyAlive()) {
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
