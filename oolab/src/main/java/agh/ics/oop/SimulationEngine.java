package agh.ics.oop;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final List<SimulationApp> simulations;
    private final ExecutorService executorService;

    public SimulationEngine() {
        this.simulations = new ArrayList<>();
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public void addSimulation(SimulationApp simulation) {
        simulations.add(simulation);
    }
    public void runAsyncInThreadPool() {
        for (SimulationApp simulation : simulations) {
            executorService.submit(() -> {
                Platform.runLater(() -> {
                    simulation.start(new Stage());
                });
            });
        }
    }

    public void awaitSimulationsEnd() {
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            System.err.println("Error awaiting termination: " + e.getMessage());
        }
    }
}
