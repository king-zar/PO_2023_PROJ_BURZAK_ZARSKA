package agh.ics.oop.model;

public class Grass implements WorldElement {
    private final Vector2d position;
    private final int plantNutrition;

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
