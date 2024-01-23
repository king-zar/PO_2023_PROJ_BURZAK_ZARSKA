package agh.ics.oop.model;

import agh.ics.oop.model.utils.MapVisualizer;
import agh.ics.oop.model.variants.MutationVariant;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

public class WorldMap {
    protected final int mapHeight;
    protected final int mapWidth;
    private final int grassToGrowPerStep;
    private final int maxGrassNutrition;
    private final UUID mapId;

    private static Multimap<Vector2d, Animal> animalsMap = HashMultimap.create();
    private static Map<Vector2d, Grass> grassMap = new HashMap<>();

    private List<MapChangeListener> observers = new ArrayList<>();

    // default
    public WorldMap() {
        this(10, 10);
    }

    public WorldMap(int mapWidth, int mapHeight) {
        this(mapWidth, mapHeight, (int) Math.round(mapWidth * mapHeight * 0.1), 2); // liczba roslinek randomowa
    }

    public WorldMap(int mapWidth, int mapHeight, int grassToGrowPerStep, int maxGrassNutrition) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.grassToGrowPerStep = grassToGrowPerStep;
        this.mapId = UUID.randomUUID();
        this.maxGrassNutrition = maxGrassNutrition;
    }

    public static void addAnimal(Vector2d position, Animal animal) {
        animalsMap.put(position, animal);
    }

    public void addGrass(Vector2d position) {
        if (getGrassAt(position) == null) { // na danej pozycji moze byc tylko jedna trawa
            Random random = new Random();
            int grassNutrition = random.nextInt(this.maxGrassNutrition) + 1; // Losowa liczba z zakresu 1-maxGrassNutrition
            Grass grass = new Grass(position, grassNutrition);
            grassMap.put(position, grass);
        }
    }

    public void removeAnimal(Vector2d position, Animal animal) {
        animalsMap.remove(position, animal);
    }

    public void removeGrass(Vector2d position, Grass grass) {
        grassMap.remove(position, grass);
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
            animal.getOlder(); // podczas ruchu zwierze sie starzeje
        }
    }

    protected void performMove(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        Vector2d newPosition = animal.intendMove();

        if (isPositionWithinBounds(newPosition)) {
            newPosition = wrapPosition(newPosition);
            moveAnimal(oldPosition, newPosition, animal);
        }
    }

    protected boolean isPositionWithinBounds(Vector2d position) {
        return position.getY() >= 0 && position.getY() < mapHeight;
    }

    protected Vector2d wrapPosition(Vector2d position) { // kula ziemska
        int wrappedX = position.getX() % mapWidth;
        if (wrappedX < 0) {
            wrappedX += mapWidth;
        }
        return new Vector2d(wrappedX, position.getY());
    }

    protected void moveAnimal(Vector2d oldPosition, Vector2d newPosition, Animal animal) {
        removeAnimal(oldPosition, animal);
        addAnimal(newPosition, animal);
        animal.setPosition(newPosition);
        animal.loseEnergyAfterMove();
    }

    public void handleAnimalReproductionAndEating(MutationVariant mutationVariant, int minMutations, int maxMutations,
                                                  int energyToReproduce, int energyLostInReproduction) {
        List<Animal> animals = getAllAnimals();

        for (Animal animal : animals) {
            if (animal.getEnergyLevel() > 0) {
                resolveConflictAtPosition(animal.getPosition(), mutationVariant, minMutations, maxMutations,
                        energyToReproduce, energyLostInReproduction); // po przesunieciu juz
            }
        }
    }

    private void resolveConflictAtPosition(Vector2d position, MutationVariant mutationVariant,
                                           int minMutations, int maxMutations,
                                           int energyToReproduce, int energyLostInReproduction) {
        Collection<Animal> animalsAtPosition = getAnimalsAt(position);
        Grass grass = getGrassAt(position);

        if (animalsAtPosition.size() == 1) {
            Animal singleAnimal = animalsAtPosition.iterator().next();

            if (grass != null) {
                eatGrass(singleAnimal, grass, position);
            }

        } else if (animalsAtPosition.size() > 1) {

            List<Animal> sortedAnimals = animalsAtPosition.stream()
                    .sorted(Comparator.comparing(Animal::getEnergyLevel)
                            .thenComparing(Animal::getAge)
                            .thenComparing(Animal::getChildsNumber)
                            .thenComparing(animal -> new Random().nextInt()))
                    .toList();

            Animal winner = sortedAnimals.get(0);

            if (grass != null) {
                eatGrass(winner, grass, position);
            }

            if (sortedAnimals.size() > 1) {
                Animal second = sortedAnimals.get(1);
                Optional<Animal> childOptional = winner.reproduce(second, mutationVariant, minMutations, maxMutations,
                                                                        energyToReproduce, energyLostInReproduction);

                if (childOptional.isPresent()) {
                    Animal child = childOptional.get();
                    addAnimal(child.getPosition(), child);
                }
            }
        }
    }

    private void eatGrass(Animal animal, Grass grass, Vector2d position) {
        animal.eat(grass);
        removeGrass(position, grass);
    }

    public String toString() {
        Boundary bounds = getCurrentBounds();
        MapVisualizer visualizer = new MapVisualizer(this);

        return visualizer.draw(bounds.lowerLeft(), bounds.upperRight());
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

    // gettery
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

    public List<Vector2d> getAvailableGrassPositions(int yStart, int areaHeight) {
        List<Vector2d> availablePositions = new ArrayList<>();
        for (int x = 0; x < mapWidth; x++) {
            for (int y = yStart; y < yStart + areaHeight; y++) {
                Vector2d position = new Vector2d(x, y);

                // gdy pozycja wolna i nalezy do obszaru
                if (getGrassAt(position) == null) {
                    availablePositions.add(position);
                }
            }
        }
        return availablePositions;
    }


    public Boundary getCurrentBounds() {
        Vector2d lowerLeft = new Vector2d(0, 0);
        Vector2d upperRight = new Vector2d(this.mapWidth, this.mapHeight);

        return new Boundary(lowerLeft, upperRight);
    }

    public List<Animal> getAllAnimals() {
        return new ArrayList<>(animalsMap.values());
    }

    public boolean anyAlive() {
        return !animalsMap.isEmpty();
    }
}
