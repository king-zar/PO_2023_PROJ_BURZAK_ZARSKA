package agh.ics.oop.model;

import agh.ics.oop.model.variants.AnimalBehaviorVariant;
import agh.ics.oop.model.variants.GrassGrowthVariant;
import agh.ics.oop.model.variants.MapVariant;
import agh.ics.oop.model.variants.MutationVariant;

import java.util.Map;

public class SimulationConfig {
    private int mapWidth;
    private int mapHeight;
    private int simulationSteps;
    private int initialPlantCount;
    private int plantToGrowPerStep;
    private int initialAnimalCount;
    private int initialAnimalEnergy;
    // private int energyToReproduce;
    // private int energyLostInReproduction;
    private int minMutations;
    private int maxMutations;
    private MutationVariant mutationVariant;
    private int genomeLength;
    private MapVariant mapVariant;

    public static final AnimalBehaviorVariant DEFAULT_ANIMAL_BEHAVIOR_VARIANT = AnimalBehaviorVariant.FULL_PREDESTINATION;
    public static final GrassGrowthVariant DEFAULT_PLANT_GROWTH_VARIANT = GrassGrowthVariant.RANDOM;


    // konstruktory, gettery, settery

    public SimulationConfig(int mapWidth, int mapHeight, int simulationSteps,
                            int initialPlantCount, int plantToGrowPerStep, int initialAnimalCount, int initialAnimalEnergy,
                            MutationVariant mutationVariant, int minMutations, int maxMutations,
                            int genomeLength, MapVariant mapVariant) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.simulationSteps = simulationSteps;
        this.initialPlantCount = initialPlantCount;
        this.plantToGrowPerStep = plantToGrowPerStep;
        this.initialAnimalCount = initialAnimalCount;
        this.initialAnimalEnergy = initialAnimalEnergy;
//        this.energyToReproduce = energyToReproduce;
//        this.energyLostInReproduction = energyLostInReproduction;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.mutationVariant = mutationVariant;
        this.genomeLength = genomeLength;
        this.mapVariant = mapVariant;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getInitialPlantCount() {
        return initialPlantCount;
    }

    public int getInitialAnimalCount() {
        return initialAnimalCount;
    }

    public int getInitialAnimalEnergy() {
        return initialAnimalEnergy;
    }

    public int getGenomeLength() {
        return genomeLength;
    }

    public int getSimulationSteps() {
        return simulationSteps;
    }

    public MutationVariant getMutationVariant() {
        return mutationVariant;
    }

    public int getMinMutations() {
        return minMutations;
    }

    public int getMaxMutations() {
        return maxMutations;
    }

    public int getPlantToGrowPerStep() {
        return plantToGrowPerStep;
    }
}

