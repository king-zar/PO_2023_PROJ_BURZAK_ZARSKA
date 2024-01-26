package agh.ics.oop;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final ExecutorService executorService;
    public SimulationEngine() {
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public void runAsyncInThreadPool(SimulationApp simulation) {
        executorService.submit(() -> {
            Platform.runLater(() -> {
                simulation.start(new Stage());
            });
        });
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