package agh.ics.oop.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap {
    private final int mapHeight;
    private final int mapWidth;
    private static int grassToGrowPerStep;

    private static Multimap<Vector2d, Object> map = HashMultimap.create(); // WorldElement zamiast Object
    // albo same zwierzeta i rosliny w innej mapie

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

    public static void add(Vector2d position, Object item) {
        map.put(position, item);
    }

    public void remove(Vector2d position, Object item) {
        map.remove(position, item);
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public Collection<Object> getItemsAt(Vector2d position) {
        return map.get(position);
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
        remove(oldPosition, animal);
        add(newPosition, animal);
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
                remove(position, grass);
            }

            if (sortedAnimals.size() > 1) {
                Animal second = sortedAnimals.get(1);
                winner.reproduce(second);
            }
        }
    }

    private Collection<Animal> getAnimalsAt(Vector2d position) {
        return getItemsAt(position).stream()
                .filter(item -> item instanceof Animal)
                .map(item -> (Animal) item)
                .toList();
    }

    private Grass getGrassAt(Vector2d position) {
        return getItemsAt(position).stream()
                .filter(item -> item instanceof Grass)
                .map(item -> (Grass) item)
                .findFirst()
                .orElse(null);
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

        for (Object item : map.values()) {
            if (item instanceof Animal) {
                Animal animal = (Animal) item;
                if (animal.getEnergyLevel() <= 0) {
                    deadAnimals.add(animal);
                }
            }
        }

        for (Animal deadAnimal : deadAnimals) {
            remove(deadAnimal.getPosition(), deadAnimal);
        }
    }

    private void moveAnimals() {
        List<Animal> animals = getAllAnimals();
        for (Animal animal : animals) {
            performMove(animal);
        }
    }

    private List<Animal> getAllAnimals() {
        return map.values().stream()
                .filter(item -> item instanceof Animal)
                .map(item -> (Animal) item)
                .toList();
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
            WorldMap.add(position, grass);
        }
    }
}
