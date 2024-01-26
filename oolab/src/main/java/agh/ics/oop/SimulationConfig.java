package agh.ics.oop;

import agh.ics.oop.model.variants.MapVariant;
import agh.ics.oop.model.variants.MutationVariant;

public record SimulationConfig(int mapWidth, int mapHeight, int simulationSteps, int initialAnimalCount,
                               int initialAnimalEnergy, int initialPlantCount, int plantToGrowPerStep,
                               MutationVariant mutationVariant, int minMutations, int maxMutations,
                               MapVariant mapVariant, int maxPlantNutrition, int genomeLength, int energyToReproduce,
                               int energyLostInReproduction, int waterAreasCount, int initialWaterAreaSize,
                               int inflowOutflowSize) {
}

