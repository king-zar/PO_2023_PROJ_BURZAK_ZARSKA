package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.variants.MapVariant;
import agh.ics.oop.model.variants.MutationVariant;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.StatisticsPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationApp extends Application {
    private SimulationConfig config;

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

    private boolean savingToFileWanted;

    public SimulationApp(Configuration configuration, boolean savingToFileWanted) {
        this.config = new SimulationConfig(configuration.width, configuration.height,
                configuration.simulationSteps, configuration.initialAnimalCount,
                configuration.initialAnimalEnergy, configuration.initialPlantCount,
                configuration.plantToGrowPerStep, MutationVariant.valueOf(configuration.mutationVariant),
                configuration.minMutations, configuration.maxMutations, MapVariant.valueOf(configuration.mapVariant),
                configuration.maxPlantNutrition, configuration.genomeLength, configuration.energyToReproduce,
                configuration.energyLostInReproduction, configuration.waterAreasCount,
                configuration.initialWaterAreaSize, configuration.inflowOutflowSize);

        this.savingToFileWanted = savingToFileWanted;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
            BorderPane viewRoot = loader.load();

            configureStage(primaryStage, viewRoot);

            FXMLLoader loaderStatistics = new FXMLLoader();
            loaderStatistics.setLocation(getClass().getClassLoader().getResource("StatisticsView.fxml"));
            VBox statisticsView = loaderStatistics.load(); // Załaduj widok statystyk

            viewRoot.setRight(statisticsView);

            new Thread(() -> {
                SimulationPresenter presenter = loader.getController();
                StatisticsPresenter statisticsPresenter = loaderStatistics.getController();

                Simulation simulation = new Simulation(config);
                TidesOutflowsMap worldMap = simulation.getWorldMap();
                presenter.setWorldMap(worldMap);
                presenter.setConfig(config);
                Statistics statistics = new Statistics(worldMap);
                statistics.update();

                if (savingToFileWanted) { // zapisuje statystyki symulacji do pliku tylko, jesli uzytkownik tego chce
                    try {
                        simulation.saveToFile();
                        statistics.initializeCsv();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                statisticsPresenter.initialize(statistics);
                simulation.setStatistics(statistics);

                for (int i = 0; i < config.simulationSteps(); i++) {
                    if (presenter.isSimulationRunning()) {
                        if (simulation.anyAlive()) {
                            simulation.simulateTimeStep(i);
                            presenter.mapChanged(worldMap, "Zmiana po kroku symulacji");
                            statisticsPresenter.updateStatisticsDisplay();
                        } else { // jesli wszystkie umra, konczymy symulacje
                            break;
                        }
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
