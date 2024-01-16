package agh.ics.oop.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap {
    private final int mapHeight;
    private final int mapWidth;
    private final int grassToGrowPerStep;

    private static Multimap<Vector2d, Animal> animalsMap = HashMultimap.create();
    private static Map<Vector2d, Grass> grassMap = new HashMap<>();

    private static final double PREFERRED_AREA_RATIO = 0.2;
    private static final double GROWTH_RATIO_AT_PREFERRED_AREA = 0.8;


    // default
    public WorldMap() {
        this(10, 10);
    }

    public WorldMap(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.grassToGrowPerStep = (int) Math.round(mapWidth * mapHeight * 0.1);
    }

    public static void addAnimal(Vector2d position, Animal animal) {
        animalsMap.put(position, animal);
    }

    public static void addGrass(Vector2d position, Grass grass) {
        grassMap.put(position, grass);
    }

    public void removeAnimal(Vector2d position, Animal animal) {
        animalsMap.remove(position, animal);
    }

    public void removeGrass(Vector2d position, Grass grass) {
        grassMap.remove(position, grass);
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public Collection<Animal> getAnimalsAt(Vector2d position) {
        return animalsMap.get(position);
    }

    public Grass getGrassAt(Vector2d position) {
        return grassMap.get(position);
    }

    public void performMove(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        Vector2d newPosition = animal.intendMove();

        if (isPositionWithinBounds(newPosition)) {
            newPosition = wrapPosition(newPosition);
            moveAnimal(oldPosition, newPosition, animal);
            resolveConflictAtPosition(newPosition);
        }
    }

    private Vector2d wrapPosition(Vector2d position) { // kula ziemska
        int wrappedX = position.getX() % mapWidth;
        if (wrappedX < 0) {
            wrappedX += mapWidth;
        }
        return new Vector2d(wrappedX, position.getY());
    }

    private boolean isPositionWithinBounds(Vector2d position) {
        return position.getY() >= 0 && position.getY() < mapHeight;
    }

    private void moveAnimal(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        removeAnimal(oldPosition, animal);
        addAnimal(newPosition, animal);
        animal.setPosition(newPosition);
        animal.loseEnergyAfterMove();
    }

    private void resolveConflictAtPosition(Vector2d position) {
        Collection<Animal> animalsAtPosition = getAnimalsAt(position);

        if (animalsAtPosition.size() > 1) {

            List<Animal> sortedAnimals = animalsAtPosition.stream()
                    .sorted(Comparator.comparing(Animal::getEnergyLevel).reversed())
                    .toList();

            Animal winner = sortedAnimals.get(0);

            Grass grass = getGrassAt(position);

            if (grass != null) {
                winner.eat(grass);
                // Usuwamy zjedzoną trawę z mapy
                removeGrass(position, grass);
            }

            if (sortedAnimals.size() > 1) {
                Animal second = sortedAnimals.get(1);
                Optional<Animal> childOptional = winner.reproduce(second);

                if (childOptional.isPresent()) {
                    Animal child = childOptional.get();
                    addAnimal(child.getPosition(), child);
                }
            }
        }
    }

    // wydzielic do nowej klasy
    public void simulateTimeStep() {
        deleteDeadAnimals();
        moveAnimals();
        handleAnimalReproductionAndEating();
        growGrass();
    }

    private void deleteDeadAnimals() {
        List<Animal> deadAnimals = new ArrayList<>();

        for (Animal animal : animalsMap.values()) {
            if (animal.getEnergyLevel() <= 0) {
                deadAnimals.add(animal);
            }
        }

        for (Animal deadAnimal : deadAnimals) {
            removeAnimal(deadAnimal.getPosition(), deadAnimal);
        }
    }

    private void moveAnimals() {
        List<Animal> animals = getAllAnimals();
        for (Animal animal : animals) {
            performMove(animal);
        }
    }

    private List<Animal> getAllAnimals() {
        return new ArrayList<>(animalsMap.values());
    }

    public void handleAnimalReproductionAndEating() {
        List<Animal> animals = getAllAnimals();

        for (Animal animal : animals) {
            if (animal.getEnergyLevel() > 0) {
                resolveConflictAtPosition(animal.getPosition()); // po przesunieciu juz
            }
        }
    }

    private void growGrass() {
        Random random = new Random();

        // rownik z dzungla
        int preferredAreaHeight = (int) (mapHeight * PREFERRED_AREA_RATIO);
        int preferredAreaYStart = (mapHeight - preferredAreaHeight) / 2;
        int preferredAreaYEnd = preferredAreaYStart + preferredAreaHeight;

        for (int i=0; i<grassToGrowPerStep; i++) {
            double probability = random.nextDouble();
            int x = random.nextInt(mapWidth);
            int y;

            if (probability <= GROWTH_RATIO_AT_PREFERRED_AREA) {
                y = random.nextInt(preferredAreaHeight) + preferredAreaYStart; // aby bylo w preferowanym miejscu

            } else {
                do {
                    y = random.nextInt(mapHeight);
                } while (y >= preferredAreaYStart && y < preferredAreaYEnd);
            }

            addGrass(new Vector2d(x, y));
        }
    }

    private void addGrass(Vector2d position) {
        if (getGrassAt(position) == null) { // na danej pozycji moze byc tylko jedna trawa
            Random random = new Random();
            int grassNutrition = random.nextInt(3) + 1; // Losowa liczba z zakresu 1-3
            Grass grass = new Grass(position, grassNutrition);
            grassMap.put(position, grass);
        }
    }
}
