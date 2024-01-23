package agh.ics.oop.model;

import agh.ics.oop.presenter.SimulationPresenter;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Simulation {
    private TidesOutflowsMap worldMap;
    private SimulationConfig config;
    private SimulationPresenter presenter;

    private static final double PREFERRED_AREA_RATIO = 0.2;
    private static final double GROWTH_RATIO_AT_PREFERRED_AREA = 0.8;

    private ConsoleMapDisplay consoleMapDisplay;

    public Simulation(SimulationConfig config) {
        this.config = config;
        this.consoleMapDisplay = new ConsoleMapDisplay();
        this.worldMap = initializeMap();
        this.worldMap.subscribe(consoleMapDisplay);
        initializeGrass();
        initializeAnimals();
    }

    public void setPresenter(SimulationPresenter presenter) {
        this.presenter = presenter;
    }

    private TidesOutflowsMap initializeMap() {
        return new TidesOutflowsMap(config.mapWidth(), config.mapHeight(), config.plantToGrowPerStep(),
                    config.waterAreasCount(), config.initialWaterAreaSize(), config.maxPlantNutrition());
    }

    private void initializeGrass() {
        growGrass(config.initialPlantCount());
    }

    private void growGrass(int grassToGrowPerStep) {
        Random random = new Random();
        int grassGrown = 0;

        int preferredAreaHeight = (int) (config.mapHeight() * PREFERRED_AREA_RATIO);
        int preferredAreaYStart = (config.mapHeight() - preferredAreaHeight) / 2;

        List<Vector2d> availablePositionsWholeMap = worldMap.getAvailableGrassPositions(0, worldMap.getMapHeight());
        List<Vector2d> availablePrefferedPositions = worldMap.getAvailableGrassPositions(preferredAreaYStart, preferredAreaHeight);

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
    }

    public Vector2d getRandomPositionFromList(List<Vector2d> positions) {
        Random random = new Random();
        int randomIndex = random.nextInt(positions.size());
        return positions.get(randomIndex);
    }

    private void initializeAnimals() {
        for (int i = 0; i < config.initialAnimalCount(); i++) {
            int x = (int) (Math.random() * config.mapWidth());
            int y = (int) (Math.random() * config.mapHeight());
            Animal animal = new Animal(new Vector2d(x, y), config.initialAnimalEnergy(), config.genomeLength());
            worldMap.addAnimal(new Vector2d(x, y), animal);
        }
    }

    public void runSimulation() {
        for (int i = 0; i < config.simulationSteps(); i++) {
            simulateTimeStep();
        }
    }

    public void simulateTimeStep() {
        deleteDeadAnimals();
        tideOrOutflow();
        moveAnimals();
        handleAnimalReproductionAndEating();
        growGrass(worldMap.getGrassToGrowPerStep());

        worldMap.mapChanged("Zmiana po kroku symulacji");
    }

    private void deleteDeadAnimals() {
        worldMap.removeDeadAnimals();
    }

    private void tideOrOutflow() {
        worldMap.tideOrOutflow(config.inflowOutflowSize());
    }

    private void moveAnimals() {
        worldMap.moveAnimals();
    }

    private void handleAnimalReproductionAndEating() {
        worldMap.handleAnimalReproductionAndEating(config.mutationVariant(), config.minMutations(),
                config.maxMutations(), config.energyToReproduce(), config.energyLostInReproduction());
    }

    public TidesOutflowsMap getWorldMap() {
        return worldMap;
    }

    public boolean anyAlive() {
        return worldMap.anyAlive();
    }
}