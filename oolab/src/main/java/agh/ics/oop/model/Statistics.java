package agh.ics.oop.model;

import java.util.*;

public class Statistics {
    private TidesOutflowsMap worldMap;

    private int totalAnimals;
    private int totalPlants;
    private int totalWaters;
    private int freeFields;
    private Map<List<Integer>, Integer> genotypePopularity = new HashMap<>();
    private double averageEnergy;
    private double averageLifeSpan;
    private double averageChildren;

    private int deadAnimalsLifetimeSum;
    private int deadAnimalsCount;
    private int totalChildrenCount;

    private int uniquePositions;

    public Statistics(TidesOutflowsMap worldMap) {
        this.worldMap = worldMap;
    }

    public void update() {
        setUniquePositions();
        setFreeFields();
        updateGenotypePopularity();
        updateAverageEnergy();
        registerAnimalDeath();
        updateAverageChildren();
    }

    private void setUniquePositions() {
        Set<Vector2d> uniquePositionsSet = new HashSet<>();

        List<Animal> animals = worldMap.getAllAnimals();
        List<Grass> grasses = worldMap.getAllGrass();
        List<Water> waters = worldMap.getAllWaters();

        List<WorldElement> allElements = new ArrayList<>();
        allElements.addAll(animals);
        allElements.addAll(grasses);
        allElements.addAll(waters);

        setTotalAnimals(animals.size());
        setTotalPlants(grasses.size());
        setTotalWaters(waters.size());

        for (WorldElement element : allElements) {
            uniquePositionsSet.add(element.getPosition());
        }

        uniquePositions = uniquePositionsSet.size();
    }

    private void setTotalAnimals(int animalsCount) {
        totalAnimals = animalsCount;
    }

    private void setTotalPlants(int grassCount) {
        totalPlants = grassCount;
    }

    private void setTotalWaters(int waterCount) {totalWaters = waterCount;};

    private void setFreeFields() {
        freeFields = (worldMap.getMapWidth() * worldMap.getMapHeight()) - uniquePositions;
    }

    private void updateGenotypePopularity() {
        genotypePopularity.clear();
        for (Animal animal : worldMap.getAllAnimals()) {
            List<Integer> genotype = animal.getGenes();
            genotypePopularity.put(genotype, genotypePopularity.getOrDefault(genotype, 0) + 1);
        }
    }

    private void updateAverageEnergy() {
        if (totalAnimals > 0) {
            averageEnergy = worldMap.getAllAnimals().stream().mapToInt(Animal::getEnergyLevel).average().orElse(0);
        }
    }

    public void registerAnimalDeath() {
        for (Animal deadAnimal:worldMap.getDeadAnimals()) {
            deadAnimalsLifetimeSum += deadAnimal.getAge();
            deadAnimalsCount++;
        }

        updateAverageLifeSpan();
    }

    private void updateAverageLifeSpan() {
        if (deadAnimalsCount > 0) {
            averageLifeSpan = (double) deadAnimalsLifetimeSum / deadAnimalsCount;
        }
    }

    private void updateAverageChildren() {
        totalChildrenCount = worldMap.getAllAnimals().stream().mapToInt(Animal::getChildsNumber).sum(); // usunelam + bo dla aktualnej sytuacji
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

    public int getTotalWaters() { return totalWaters; }
    public int getFreeFields() { return freeFields; }
    public Map<List<Integer>, Integer> getGenotypePopularity() { return genotypePopularity; }
    public double getAverageEnergy() { return averageEnergy; }
    public double getAverageLifeSpan() { return averageLifeSpan; }
    public double getAverageChildren() { return averageChildren; }
}


