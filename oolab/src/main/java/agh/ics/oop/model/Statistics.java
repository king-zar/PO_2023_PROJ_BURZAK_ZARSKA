package agh.ics.oop.model;

import java.util.*;

public class Statistics {
    private static Statistics instance = new Statistics(); //singleton
    private Statistics() {}
    public static Statistics getInstance() {
        return instance;
    }
    private int totalAnimals;
    private int totalPlants;
    private int freeFields;
    private final Map<List<Integer>, Integer> genotypePopularity  = new HashMap<>(); // jak biale kruki 16.11
    private double averageEnergy;
    private double averageLifeSpan;
    private double averageChildren;

    private int deadAnimalsLifetimeSum;
    private int deadAnimalsCount;
    private int totalChildrenCount;


    public void update(WorldMap worldMap) {
        updateTotalAnimals(worldMap.getAllAnimals());
        updateTotalPlants(worldMap.getGrassCount());
        updateFreeFields(worldMap.getMapWidth(), worldMap.getMapHeight(), totalAnimals);
        updateGenotypePopularity(worldMap.getAllAnimals());
        updateAverageEnergy(worldMap.getAllAnimals());
        updateAverageLifeSpan();
        updateAverageChildren(worldMap.getAllAnimals());
    }

    private void updateTotalAnimals(List<Animal> animals) {
        totalAnimals = animals.size();
    }

    private void updateTotalPlants(int grassCount) {
        totalPlants = grassCount;
    }

    private void updateFreeFields(int width, int height, int totalAnimals) {
        freeFields = (width * height) - totalAnimals - totalPlants;
    }

    private void updateGenotypePopularity(List<Animal> animals) {
        genotypePopularity.clear();
        for (Animal animal : animals) {
            List<Integer> genotype = animal.getGenes();
            genotypePopularity.put(genotype, genotypePopularity.getOrDefault(genotype, 0) + 1);
        }
    }

    private void updateAverageEnergy(List<Animal> animals) {
        if (totalAnimals > 0) {
            averageEnergy = animals.stream().mapToInt(Animal::getEnergyLevel).average().orElse(0);
        }
    }

    public void registerAnimalDeath(Animal animal) {
        deadAnimalsLifetimeSum += animal.getAge();
        deadAnimalsCount++;
        updateAverageLifeSpan();
    }

    private void updateAverageLifeSpan() {
        if (deadAnimalsCount > 0) {
            averageLifeSpan = (double) deadAnimalsLifetimeSum / deadAnimalsCount;
        }
    }

    private void updateAverageChildren(List<Animal> animals) {
        totalChildrenCount += animals.stream().mapToInt(Animal::getChildsNumber).sum();
        if (totalAnimals > 0) {
            averageChildren = (double) totalChildrenCount / totalAnimals;
        }
    }

    public List<Integer> getMostCommonGenotype() {
        return genotypePopularity.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
    public int getTotalAnimals() { return totalAnimals; }
    public int getTotalPlants() { return totalPlants; }
    public int getFreeFields() { return freeFields; }
    public Map<List<Integer>, Integer> getGenotypePopularity() { return genotypePopularity; }
    public double getAverageEnergy() { return averageEnergy; }
    public double getAverageLifeSpan() { return averageLifeSpan; }
    public double getAverageChildren() { return averageChildren; }
}


