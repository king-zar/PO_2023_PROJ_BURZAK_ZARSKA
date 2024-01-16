package agh.ics.oop.model;

import java.util.Random;

public class Simulation {
    private WorldMap worldMap;
    private SimulationConfig config;

    private static final double PREFERRED_AREA_RATIO = 0.2;
    private static final double GROWTH_RATIO_AT_PREFERRED_AREA = 0.8;

    private ConsoleMapDisplay consoleMapDisplay;

    public Simulation(SimulationConfig config) {
        this.config = config;
        this.consoleMapDisplay = new ConsoleMapDisplay();
        this.worldMap = initializeWorldMap();
        this.worldMap.subscribe(consoleMapDisplay);
        initializeGrass();
        initializeAnimals();
    }

    private WorldMap initializeWorldMap() {
        return new WorldMap(config.getMapWidth(), config.getMapHeight(), config.getInitialPlantCount());
    }

    private void initializeGrass() {
        growGrass(worldMap.getGrassToGrowPerStep());
    }

    private void growGrass(int grassToGrowPerStep) {
        Random random = new Random();

        int preferredAreaHeight = (int) (config.getMapHeight() * PREFERRED_AREA_RATIO);
        int preferredAreaYStart = (config.getMapHeight() - preferredAreaHeight) / 2;
        int preferredAreaYEnd = preferredAreaYStart + preferredAreaHeight;

        for (int i = 0; i < grassToGrowPerStep; i++) {
            double probability = random.nextDouble();
            int x = random.nextInt(config.getMapWidth());
            int y;

            if (probability <= GROWTH_RATIO_AT_PREFERRED_AREA) {
                y = random.nextInt(preferredAreaHeight) + preferredAreaYStart;
            } else {
                do {
                    y = random.nextInt(config.getMapHeight());
                } while (y >= preferredAreaYStart && y < preferredAreaYEnd);
            }

            worldMap.addGrass(new Vector2d(x, y));
        }
    }

    private void initializeAnimals() {
        for (int i = 0; i < config.getInitialAnimalCount(); i++) {
            int x = (int) (Math.random() * config.getMapWidth());
            int y = (int) (Math.random() * config.getMapHeight());
            Animal animal = new Animal(new Vector2d(x, y), config.getInitialAnimalEnergy(), config.getGenomeLength());
            worldMap.addAnimal(new Vector2d(x, y), animal);
        }
    }

    public void runSimulation() {
        for (int i = 0; i < config.getSimulationSteps(); i++) {
            simulateTimeStep();
        }
    }

    public void simulateTimeStep() {
        deleteDeadAnimals();
        moveAnimals();
        handleAnimalReproductionAndEating();
        growGrass(worldMap.getGrassToGrowPerStep());

        worldMap.mapChanged("Zmiana po kroku symulacji");
    }

    private void deleteDeadAnimals() {
        worldMap.removeDeadAnimals();
    }

    private void moveAnimals() {
        worldMap.moveAnimals();
    }

    private void handleAnimalReproductionAndEating() {
        worldMap.handleAnimalReproductionAndEating();
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }
}