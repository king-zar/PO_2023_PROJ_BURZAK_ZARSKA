package agh.ics.oop;

public class Configuration {
    public final int width;
    public final int height;
    public final int initialAnimalCount;
    public final int initialAnimalEnergy;
    public final int initialPlantCount;
    public final int plantToGrowPerStep;
    public final int simulationSteps;
    public final int maxPlantNutrition;
    public final int energyToReproduce;
    public final int energyLostInReproduction;
    public final int minMutations;
    public final int maxMutations;
    public final int genomeLength;
    public final String mapVariant;
    public final String mutationVariant;
    public final int waterAreasCount;
    public final int initialWaterAreaSize;
    public final int inflowOutflowSize;

    public Configuration(int width, int height, int simulationSteps, int initialPlantCount, int plantToGrowPerStep,
                         int initialAnimalCount, int initialAnimalEnergy, int maxPlantNutrition, int energyToReproduce,
                         int minMutations, int maxMutations, int genomeLength, String mapVariant,
                         String mutationVariant, int waterAreasCount, int initialWaterAreaSize, int inflowOutflowSize,
                         int energyLostInReproduction) {
        this.width = width;
        this.height = height;
        this.simulationSteps = simulationSteps;
        this.initialPlantCount = initialPlantCount;
        this.plantToGrowPerStep = plantToGrowPerStep;
        this.initialAnimalCount = initialAnimalCount;
        this.initialAnimalEnergy = initialAnimalEnergy;
        this.maxPlantNutrition = maxPlantNutrition;
        this.energyToReproduce = energyToReproduce;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.genomeLength = genomeLength;
        this.mapVariant = mapVariant;
        this.waterAreasCount = waterAreasCount;
        this.mutationVariant = mutationVariant;
        this.energyLostInReproduction = energyLostInReproduction;
        this.initialWaterAreaSize = initialWaterAreaSize;
        this.inflowOutflowSize = inflowOutflowSize;
    }
}
