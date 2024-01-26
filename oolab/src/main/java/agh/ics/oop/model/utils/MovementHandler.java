package agh.ics.oop.model.utils;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Vector2d;

public class MovementHandler {
    public static Vector2d intendMove(Animal animal) {
        int rotation = animal.getGenes().get(animal.getCurrentGeneIndex());
        animal.rotate(rotation);
        Vector2d movement = animal.getOrientation().toUnitVector();
        Vector2d newPosition = animal.getPosition().add(movement);

        animal.setCurrentGeneIndex((animal.getCurrentGeneIndex() + 1) % animal.getGenes().size());

        return newPosition;
    }

}