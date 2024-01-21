package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.variants.MutationVariant;

public class World {
    public static void main(String[] args) {
        System.out.println("System wystartowal");

//        WorldMap map = new WorldMap(5, 7);
//
//        Animal animal1 = new Animal(new Vector2d(2,4), 20, 7);
//        Animal animal2 = new Animal(new Vector2d(2,4));
//
//        // reproduce
////        animal1.reproduce(animal2);
////        System.out.println(animal1.getEnergyLevel());
////        System.out.println(animal2.getEnergyLevel());
//
//        // move
//        System.out.println(animal1.getPosition());
//        System.out.println(animal1.getGenes());
//        System.out.println(animal1.getPosition());

        int mapWidth = 5;
        int mapHeight = 5;
        int simulationSteps = 5;
        int initialPlantCount = 5;
        int initialAnimalCount = 2;
        int initialAnimalEnergy = 10;
        int genomeLength = 8;

        SimulationConfig config = new SimulationConfig(mapWidth, mapHeight, simulationSteps, initialPlantCount,
                initialAnimalCount, initialAnimalEnergy, MutationVariant.RANDOM, 1, 3, genomeLength);

        // Create and run the simulation
        Simulation simulation = new Simulation(config);

        // Run the simulation
        simulation.runSimulation();

        // Access the world map after simulation
        WorldMap worldMap = simulation.getWorldMap();

        // You can now work with the resulting world map or extract information as needed
        // For example, you might want to print the final state of the world map
        System.out.println("Final state of the world map:");
        System.out.println(worldMap);
        System.out.println("Liczba wszystkich zwierzat: " + worldMap.getAnimalCount());

        System.out.println("System zakonczyl dzialanie");
    }
}
