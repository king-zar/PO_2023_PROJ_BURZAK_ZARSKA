package agh.ics.oop.model;

@FunctionalInterface
public interface MapChangeListener {
    /**
     * Called when the map has changed.
     *
     * @param worldMap The world map that has changed.
     * @param message  A message describing the change.
     */
    void mapChanged(WorldMap worldMap, String message);
}