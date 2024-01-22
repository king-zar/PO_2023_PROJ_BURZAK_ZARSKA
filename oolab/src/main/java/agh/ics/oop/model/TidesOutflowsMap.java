package agh.ics.oop.model;

import java.util.*;

public class TidesOutflowsMap extends WorldMap {
    private boolean tideTime;
    private int tidesCountdown = 0; // liczba kroków symulacji do kolejnych pływów/odpływów

    private Map<Vector2d, Water> waterAreas = new HashMap<>();

    private int currentWaterAreaId = 0; // zmienna pomocnicza

    public TidesOutflowsMap(int mapWidth, int mapHeight, int grassToGrowPerStep,
                            int waterAreasCount, int initializeWaterSize) {
        super(mapWidth, mapHeight, grassToGrowPerStep);
        initializeWaterAreas(waterAreasCount, initializeWaterSize);
        tideTime = true;
    }

    public boolean isTideTime() {
        return tideTime;
    }

    public void changeTideTime() {
        tideTime = !tideTime;
    }

    private void initializeWaterAreas(int waterAreasCount, int initialWaterSize) {
        Random random = new Random();

        List<Vector2d> availableWaterPositions = new ArrayList<>(getAvailableWaterPositions());

        for (int i = 0; i < waterAreasCount; i++) {
            if (availableWaterPositions.isEmpty()) {
                break; // Przerwij, jeśli nie ma już dostępnych pozycji
            }

            int randomIndex = random.nextInt(availableWaterPositions.size());
            Vector2d position = availableWaterPositions.get(randomIndex);

            waterAreas.put(position, new Water(position, currentWaterAreaId));

            currentWaterAreaId++;

            availableWaterPositions.remove(position);
        }

        addWaterInVicinity(initialWaterSize-1);
    }

    private List<Vector2d> getAvailableWaterPositions() {
        List<Vector2d> availableWaterPositions = new ArrayList<>();

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Vector2d position = new Vector2d(x, y);

                if (!waterAreas.containsKey(position)) {
                    availableWaterPositions.add(position);
                }
            }
        }

        return availableWaterPositions;
    }

    public void tideOrOutflow(int waterAmount) {
        if (isTideTime()) {
            if (tidesCountdown == 3) {
                performTide(waterAmount);
            } else if (tidesCountdown < 3) {
                tidesCountdown += 1;
            }

        } else {
            if (tidesCountdown == 0) {
                performOutflow(waterAmount);
            } else if (tidesCountdown > 0) {
                tidesCountdown -= 1;
            }
        }
    }

    private void performTide (int waterAmount) {
        addWaterInVicinity(waterAmount);
        tideTime = false;
    }

    private void performOutflow(int waterAmount) {
        Random random = new Random();

        for (int i = 0; i <= currentWaterAreaId; i++) {
            List<Vector2d> waterAreaPositions = getWaterPositionsByAreaId(i);

            waterAreaPositions.sort(Comparator.comparingInt(v -> v.getX() + v.getY()));

            if (!waterAreaPositions.isEmpty()) {
                for (int a = 0; a < waterAmount && !waterAreaPositions.isEmpty(); a++) {
                    // losowe usuniecie pozycji z poczatku lub konca listy
                    int randomIndex = random.nextBoolean() ? 0 : waterAreaPositions.size()-1;
                    Vector2d removedPosition = waterAreaPositions.remove(randomIndex);

                    waterAreas.remove(removedPosition);
                }
            }
        }

        tideTime = true;
    }

    private void addWaterInVicinity(int waterAmount) {
        for (int i=0; i<=currentWaterAreaId; i++) {
            List<Vector2d> waterAreaPositions = getWaterPositionsByAreaId(i);

            if (!waterAreaPositions.isEmpty()) {
                for (int a = 0; a < waterAmount; a++) {
                    Vector2d randomPlace = randomPlaceForNewWater(waterAreaPositions);
                    waterAreas.put(randomPlace, new Water(randomPlace, i));

                    waterAreaPositions.add(randomPlace);
                }
            }
        }
    }

    private List<Vector2d> getWaterPositionsByAreaId(int waterAreaId) {
        List<Vector2d> result = new ArrayList<>();

        for (Water water : waterAreas.values()) {
            if (water.getWaterAreaId() == waterAreaId) {
                result.add(water.getPosition());
            }
        }

        // Sortowanie listy pozycji wód po x, a następnie po y
        result.sort(Comparator.comparing(Vector2d::getX).thenComparing(Vector2d::getY));

        return result;
    }

    public Vector2d randomPlaceForNewWater(List<Vector2d> waterAreaPositions) {
        Random random = new Random();

        int randomIndex;
        Vector2d surroundingCheck;
        List<Vector2d> freePositions;
        do {
            randomIndex = random.nextInt(waterAreaPositions.size());
            surroundingCheck = waterAreaPositions.get(randomIndex);
            freePositions = getFreePositionsAround(surroundingCheck);
        } while (freePositions.isEmpty());

        int randomFree = random.nextInt(freePositions.size());

        return freePositions.get(randomFree);
    }

    private List<Vector2d> getFreePositionsAround(Vector2d centerPosition) {
        List<Vector2d> freePositions = new ArrayList<>();

        // Sprawdź otoczenie w promieniu 1 od pozycji centerPosition
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                if (xOffset == 0 && yOffset == 0) {
                    continue; // Pomijamy samą pozycję centerPosition
                }

                Vector2d newPosition = wrapPosition(centerPosition.add(new Vector2d(xOffset, yOffset)));

                // Sprawdź, czy nowa pozycja jest wolna
                if (isOutsideWaterArea(newPosition) && isPositionWithinBounds(newPosition)) {
                    freePositions.add(newPosition);
                }
            }
        }

        return freePositions;
    }

    @Override
    protected void performMove(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        Vector2d newPosition = animal.intendMove();

        if (isPositionWithinBounds(newPosition)) {
            newPosition = wrapPosition(newPosition);

            if (isOutsideWaterArea(newPosition)) {
                moveAnimal(oldPosition, newPosition, animal);
            } else {
                // zwierzę weszlo na obszar wodny, wywołaj jego metodę die()
                animal.die();
                removeAnimal(oldPosition, animal);
            }
        }
    }

    private boolean isOutsideWaterArea(Vector2d position) {
        return !waterAreas.containsKey(position);
    }

    public Water getWaterAt(Vector2d position) {
        return waterAreas.get(position);
    }

    public boolean getTideTime() {
        return tideTime;
    }
}