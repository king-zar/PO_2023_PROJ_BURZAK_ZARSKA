package agh.ics.oop.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class WorldMap {
    private final int mapHeight;
    private final int mapWidth;
    private static Multimap<Vector2d, Object> map = HashMultimap.create();

    public WorldMap(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public static void add(Vector2d position, Object item) {
        map.put(position, item);
    }

    public void remove(Vector2d position, Object item) {
        map.remove(position, item);
    }

    public Collection<Object> getItemsAt(Vector2d position) {
        return map.get(position);
    }

    public Collection<Animal> getAnimalsAt(Vector2d position) {  // tu chodzi o to zeby odroznic "typ" obiektu ktory sie znajduje na position
        Collection<Animal> animals = new ArrayList<>();
        for (Object item : getItemsAt(position)) {
            if (item instanceof Animal) {
                animals.add((Animal) item);
            }
        }
        return animals;
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

    private Vector2d wrapPosition(Vector2d position) {
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
    }

    private void resolveConflictAtPosition(Vector2d position) {
        Collection<Animal> animalsAtPosition = getAnimalsAt(position);

        if (animalsAtPosition.size() > 1) {

            List<Animal> sortedAnimals = animalsAtPosition.stream()
                    .sorted(Comparator.comparing(Animal::getEnergyLevel).reversed())
                    .toList();

            Animal winner = sortedAnimals.get(0);
            winner.eat();  // chyba bylo ze 1 moze zjesc tylko?

            if (sortedAnimals.size() > 1) {
                Animal second = sortedAnimals.get(1);
                winner.reproduce(second);
            }
        }
    }
}
