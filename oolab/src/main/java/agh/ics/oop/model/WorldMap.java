package agh.ics.oop.model;

import agh.ics.oop.model.utils.MapVisualizer;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

public class WorldMap {
    private final int mapHeight;
    private final int mapWidth;
    private final int grassToGrowPerStep;
    private final UUID mapId;

    private static Multimap<Vector2d, Animal> animalsMap = HashMultimap.create();
    private static Map<Vector2d, Grass> grassMap = new HashMap<>();

    private List<MapChangeListener> observers = new ArrayList<>();

    private int bornAnimals = 0; // ile zwierzatek zostalo urodzonych

    // default
    public WorldMap() {
        this(10, 10);
    }

    public WorldMap(int mapWidth, int mapHeight) {
        this(mapWidth, mapHeight, (int) Math.round(mapWidth * mapHeight * 0.1)); // liczba roslinek randomowa
    }

    public WorldMap(int mapWidth, int mapHeight, int grassToGrowPerStep) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.grassToGrowPerStep = grassToGrowPerStep;
        this.mapId = UUID.randomUUID();
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

    public int getGrassToGrowPerStep() {
        return grassToGrowPerStep;
    }

    public UUID getMapId() {
        return mapId;
    }

    public int getBornAnimals() {
        return bornAnimals;
    }

    public int getAnimalCount() {
        return animalsMap.size();
    }

    public int getGrassCount() {
        return grassMap.size();
    }

    public Collection<Animal> getAnimalsAt(Vector2d position) {
        return animalsMap.get(position);
    }

    public Grass getGrassAt(Vector2d position) {
        return grassMap.get(position);
    }

    private void performMove(Animal animal) {
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
                    bornAnimals += 1;
                }
            }
        }
    }

    public void removeDeadAnimals() {
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

    public void moveAnimals() {
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

    public void addGrass(Vector2d position) {
        if (getGrassAt(position) == null) { // na danej pozycji moze byc tylko jedna trawa
            Random random = new Random();
            int grassNutrition = random.nextInt(3) + 1; // Losowa liczba z zakresu 1-3
            Grass grass = new Grass(position, grassNutrition);
            grassMap.put(position, grass);
        }
    }

    public String toString() {
        Boundary bounds = getCurrentBounds();
        MapVisualizer visualizer = new MapVisualizer(this);

        return visualizer.draw(bounds.lowerLeft(), bounds.upperRight());
    }

    public Boundary getCurrentBounds() {
        Vector2d lowerLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Vector2d upperRight = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (Vector2d position : animalsMap.keys()) {
            lowerLeft = lowerLeft.lowerLeft(position);
            upperRight = upperRight.upperRight(position);
        }

        for (Vector2d position : grassMap.keySet()) {
            lowerLeft = lowerLeft.lowerLeft(position);
            upperRight = upperRight.upperRight(position);
        }

        return new Boundary(lowerLeft, upperRight);
    }

    // observable
    public void subscribe(MapChangeListener observer) {
        observers.add(observer);
    }

    public void unsubscribe(MapChangeListener observer) {
        observers.remove(observer);
    }

    public void mapChanged(String message) {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }
}
