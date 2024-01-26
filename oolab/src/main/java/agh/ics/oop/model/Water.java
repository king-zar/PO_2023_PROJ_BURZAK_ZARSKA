package agh.ics.oop.model;

public class Water implements WorldElement {
    private final Vector2d position;
    private final int waterAreaId;

    public Water(Vector2d position, int waterAreaId) {
        this.position = position;
        this.waterAreaId = waterAreaId;
    }

    public int getWaterAreaId() {
        return waterAreaId;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "~";
    }
}
