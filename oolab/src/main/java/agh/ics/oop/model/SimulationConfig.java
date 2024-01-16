package agh.ics.oop.model;

public class SimulationConfig {
    private int mapWidth;
    private int mapHeight;
    private int simulationSteps;
    // private MapVariant mapVariant;
    private int initialPlantCount;
    private int initialAnimalCount;
    private int initialAnimalEnergy;
    // private int energyToReproduce;
    // private int energyLostInReproduction;
    // private int minMutations;
    // private int maxMutations;
    // private MutationVariant mutationVariant;
    private int genomeLength;

    public static final AnimalBehaviorVariant DEFAULT_ANIMAL_BEHAVIOR_VARIANT = AnimalBehaviorVariant.FULL_PREDESTINATION;
    public static final GrassGrowthVariant DEFAULT_PLANT_GROWTH_VARIANT = GrassGrowthVariant.RANDOM;


    // konstruktory, gettery, settery

    public SimulationConfig(int mapWidth, int mapHeight, int simulationSteps,
                            int initialPlantCount, int initialAnimalCount, int initialAnimalEnergy,
                            int genomeLength) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.simulationSteps = simulationSteps;
        //this.mapVariant = mapVariant;
        this.initialPlantCount = initialPlantCount;
        this.initialAnimalCount = initialAnimalCount;
        this.initialAnimalEnergy = initialAnimalEnergy;
//        this.energyToReproduce = energyToReproduce;
//        this.energyLostInReproduction = energyLostInReproduction;
//        this.minMutations = minMutations;
//        this.maxMutations = maxMutations;
//        this.mutationVariant = mutationVariant;
        this.genomeLength = genomeLength;
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
}

