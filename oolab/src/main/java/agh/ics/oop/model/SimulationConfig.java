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
    private int energyToReproduce;
    private int energyLostInReproduction;
    private int minMutations;
    private int maxMutations;
    private MutationVariant mutationVariant;
    private int genomeLength;
    private MapVariant mapVariant;
    private int maxPlantNutrition;
    private int waterAreasCount;
    private int initialWaterAreaSize;
    private int inflowOutflowSize;

    public static final AnimalBehaviorVariant DEFAULT_ANIMAL_BEHAVIOR_VARIANT = AnimalBehaviorVariant.FULL_PREDESTINATION;
    public static final GrassGrowthVariant DEFAULT_PLANT_GROWTH_VARIANT = GrassGrowthVariant.RANDOM;


    // konstruktory, gettery, settery

//    Integer.parseInt(widthField.getText()),
//            Integer.parseInt(heightField.getText()),
//            Integer.parseInt(stepsField.getText()),
//            Integer.parseInt(animalCount.getText()),
//            Integer.parseInt(initialAnimalEnergy.getText()),
//            Integer.parseInt(initialPlantCount.getText()),
//            Integer.parseInt(plantToGrowPerStep.getText()),
//            MutationVariant.valueOf(mutationVariantChoiceBox.getValue()),
//            Integer.parseInt(minMutations.getText()),
//            Integer.parseInt(maxMutations.getText()),
//            MapVariant.valueOf(mapVariantChoiceBox.getValue()),
//            Integer.parseInt(maxPlantNutrition.getText()),
//            Integer.parseInt(genomeLength.getText()),
//            Integer.parseInt(energyToReproduce.getText()),
//            Integer.parseInt(energyLostInReproduction.getText()),
//            Integer.parseInt(waterAreasCount.getText()),
//            Integer.parseInt(initialWaterAreaSize.getText()),
//            Integer.parseInt(inflowOutflowSize.getText())

    public SimulationConfig(int mapWidth, int mapHeight, int simulationSteps, int initialAnimalCount, int initialAnimalEnergy,
                            int initialPlantCount, int plantToGrowPerStep, MutationVariant mutationVariant,  int minMutations,
                            int maxMutations, MapVariant mapVariant, int maxPlantNutrition, int genomeLength,
                            int energyToReproduce, int energyLostInReproduction,
                            int waterAreasCount, int initialWaterAreaSize, int inflowOutflowSize) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.simulationSteps = simulationSteps;
        this.initialPlantCount = initialPlantCount;
        this.plantToGrowPerStep = plantToGrowPerStep;
        this.initialAnimalCount = initialAnimalCount;
        this.initialAnimalEnergy = initialAnimalEnergy;
        this.energyToReproduce = energyToReproduce;
        this.energyLostInReproduction = energyLostInReproduction;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.mutationVariant = mutationVariant;
        this.genomeLength = genomeLength;
        this.mapVariant = mapVariant;
        this.maxPlantNutrition = maxPlantNutrition;
        this.waterAreasCount = waterAreasCount;
        this.initialWaterAreaSize = initialWaterAreaSize;
        this.inflowOutflowSize = inflowOutflowSize;
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

    public MapVariant getMapVariant() {
        return mapVariant;
    }

    public int getMaxPlantNutrition() {
        return maxPlantNutrition;
    }

    public int getEnergyToReproduce() {
        return energyToReproduce;
    }

    public int getEnergyLostInReproduction() {
        return energyLostInReproduction;
    }

    public int getWaterAreasCount() {
        return waterAreasCount;
    }

    public int getInflowOutflowSize() {
        return inflowOutflowSize;
    }

    public int getInitialWaterAreaSize() {
        return initialWaterAreaSize;
    }
}

