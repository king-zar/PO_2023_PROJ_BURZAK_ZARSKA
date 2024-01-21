package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

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
        return new WorldMap(config.getMapWidth(), config.getMapHeight(), config.getPlantToGrowPerStep());
    }

    private void initializeGrass() {
        growGrass(config.getInitialPlantCount());
    }

    private void growGrass(int grassToGrowPerStep) {
        Random random = new Random();
        int grassGrown = 0;

        int preferredAreaHeight = (int) (config.getMapHeight() * PREFERRED_AREA_RATIO);
        int preferredAreaYStart = (config.getMapHeight() - preferredAreaHeight) / 2;

        List<Vector2d> availablePositionsWholeMap = worldMap.getAvailableGrassPositions(0, worldMap.getMapHeight());
        List<Vector2d> availablePrefferedPositions = worldMap.getAvailableGrassPositions(preferredAreaYStart, preferredAreaHeight);

        System.out.println("Available: " + availablePositionsWholeMap.size());
        // aby dostac tylko pozycje spoza preferowanego obszaru
        List<Vector2d> availablePositionsNotPreffered = new ArrayList<>(availablePositionsWholeMap);
        availablePositionsNotPreffered.removeAll(availablePrefferedPositions);

        int availablePositionsCount = availablePositionsWholeMap.size();

        for (int i = 0; i < Math.min(grassToGrowPerStep, availablePositionsCount); i++) { // gdy size = 0 nie doda w ogole
            double probability = random.nextDouble();

            if (probability <= GROWTH_RATIO_AT_PREFERRED_AREA && !availablePrefferedPositions.isEmpty()) {
                Vector2d preferredPosition = getRandomPositionFromList(availablePrefferedPositions);
                worldMap.addGrass(preferredPosition);
                grassGrown += 1;
                availablePrefferedPositions.remove(preferredPosition);

            } else {
                Vector2d notPreferredPosition = getRandomPositionFromList(availablePositionsNotPreffered);
                worldMap.addGrass(notPreferredPosition);
                grassGrown += 1;
                availablePositionsNotPreffered.remove(notPreferredPosition);
            }
        }

        System.out.println("Grown: " + grassGrown);
    }

    public Vector2d getRandomPositionFromList(List<Vector2d> positions) {
        Random random = new Random();
        int randomIndex = random.nextInt(positions.size());
        return positions.get(randomIndex);
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
        worldMap.handleAnimalReproductionAndEating(config.getMutationVariant(), config.getMinMutations(), config.getMaxMutations());
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }
}