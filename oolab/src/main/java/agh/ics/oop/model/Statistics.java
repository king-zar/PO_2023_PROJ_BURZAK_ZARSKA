package agh.ics.oop.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

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

    Path csvPath;

    public void update() {
        setUniquePositions();
        setFreeFields();
        updateGenotypePopularity();
        updateAverageEnergy();
        registerAnimalDeath();
        updateAverageChildren();
    }
//test
    public void initializeCsv() throws IOException {
        String statisticsDirectory = "Statistics";
        Path projectPath = Paths.get(System.getProperty("user.dir"));
        String baseCsvFileName = "simulation_stats" + worldMap.getMapId();
        String csvExtension = ".csv";

        Path statisticsFolderPath = Paths.get(System.getProperty("user.dir"), statisticsDirectory);
        if (!Files.exists(statisticsFolderPath)) {
            Files.createDirectories(statisticsFolderPath);
        }

        String csvFileName = baseCsvFileName + csvExtension;
        csvPath = statisticsFolderPath.resolve(csvFileName);
        int fileIndex = 1;

        while (Files.exists(csvPath)) {
            csvFileName = baseCsvFileName + "_" + fileIndex++ + csvExtension;
            csvPath = statisticsFolderPath.resolve(csvFileName);
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvPath.toFile(), true))) {
            writer.println("Step,Total Animals,Total Plants,Total Waters,Free Fields,Most Common Genotype,Average Energy,Average LifeSpan,Average Children");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendStatisticsToCsv(int step) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvPath.toFile(), true))) {
            String mostCommonGenotype = getMostCommonGenotype() != null
                    ? getMostCommonGenotype().stream().map(Object::toString).collect(Collectors.joining(" "))
                    : "None";
            String dataRow = String.join(",",
                    Integer.toString(step),
                    Integer.toString(totalAnimals),
                    Integer.toString(totalPlants),
                    Integer.toString(totalWaters),
                    Integer.toString(freeFields),
                    "\"" + mostCommonGenotype + "\"",
                    Double.toString(averageEnergy),
                    Double.toString(averageLifeSpan),
                    Double.toString(averageChildren)
            );

            writer.println(dataRow);
        } catch (IOException e) {
            e.printStackTrace();
        }
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


