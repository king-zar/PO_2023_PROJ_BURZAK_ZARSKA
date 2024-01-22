package agh.ics.oop;

import agh.ics.oop.model.TidesOutflowsMap;
import agh.ics.oop.model.variants.MapVariant;
import agh.ics.oop.model.variants.MutationVariant;
import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.SimulationConfig;
import agh.ics.oop.model.WorldMap;
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

    public SimulationApp(int mapWidth, int mapHeight, int simulationSteps, int animalCount, int initialAnimalEnergy,
                         int initialPlantCount, int plantToGrowPerStep, MutationVariant variant, int minMutations,
                         int maxMutations, MapVariant mapVariant, int maxPlantNutrition, int genomeLength,
                         int energyToReproduce, int energyLostInReproduction,
                         int waterAreasCount, int initialWaterAreaSize, int inflowOutflowSize){
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.simulationSteps = simulationSteps;
        this.animalCount = animalCount;
        this.initialAnimalEnergy = initialAnimalEnergy;
        this.initialPlantCount = initialPlantCount;
        this.plantToGrowPerStep = plantToGrowPerStep;
        this.mutationVariant = variant;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.mapVariant = mapVariant;
        this.maxPlantNutrition = maxPlantNutrition;
        this.genomeLength = genomeLength;
        this.energyToReproduce = energyToReproduce;
        this.energyLostInReproduction = energyLostInReproduction;
        this.waterAreasCount = waterAreasCount;
        this.initialWaterAreaSize = initialWaterAreaSize;
        this.inflowOutflowSize = inflowOutflowSize;
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
                SimulationConfig config = new SimulationConfig(mapWidth, mapHeight, simulationSteps, initialPlantCount,
                        plantToGrowPerStep, energyToReproduce, energyLostInReproduction, animalCount, initialAnimalEnergy,
                        mutationVariant, minMutations, maxMutations, genomeLength, mapVariant, maxPlantNutrition,
                        waterAreasCount, initialWaterAreaSize, inflowOutflowSize);

                Simulation simulation = new Simulation(config);
                TidesOutflowsMap worldMap = simulation.getWorldMap();
                presenter.setWorldMap(worldMap);
                presenter.setConfig(config);

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
