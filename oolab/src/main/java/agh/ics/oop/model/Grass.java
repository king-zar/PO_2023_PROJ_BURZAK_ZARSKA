package agh.ics.oop.model;

public class Grass implements WorldElement {
    private final Vector2d position;
    private final int plantNutrition;

    // default
    public Grass(Vector2d position) {
        this(position, 2);
    }

    public Grass(Vector2d position, int plantNutrition) {
        this.position = position;
        this.plantNutrition = plantNutrition;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getPlantNutrition() {
        return plantNutrition;
    }

}
