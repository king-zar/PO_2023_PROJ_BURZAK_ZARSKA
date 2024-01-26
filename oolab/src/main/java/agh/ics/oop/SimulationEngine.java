package agh.ics.oop;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class SimulationEngine {
    private final Object monitor = new Object();
    private final List<SimulationApp> simulations;
    private final List<Thread> threads;

    public SimulationEngine(List<SimulationApp> simulations) {
        this.simulations = simulations;
        this.threads = new ArrayList<>();
    }

    public void runAsync() {
        for (SimulationApp simulation : simulations) {
            Thread simulationThread = new Thread(() -> {
                Platform.runLater(() -> {
                    simulation.start(new Stage());
                });
            });
            threads.add(simulationThread);

            simulationThread.start();
        }
    }

    public void awaitSimulationsEnd() {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Error joining thread: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}
