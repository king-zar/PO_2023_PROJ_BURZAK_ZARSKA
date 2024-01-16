package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int totalUpdates = 0;

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        synchronized (this) {
            totalUpdates++;
            System.out.println(" #" + totalUpdates + " Aktualizacja mapy " + worldMap.getMapId() + ": " + message + "\n" + worldMap.toString());
        }
    }
}
