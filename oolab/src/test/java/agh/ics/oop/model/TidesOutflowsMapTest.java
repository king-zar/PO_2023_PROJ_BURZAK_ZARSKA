package agh.ics.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TidesOutflowsMapTest {
    private TidesOutflowsMap map;

    @BeforeEach
    void setUp() {
        map = new TidesOutflowsMap(10, 10, 2, 5, 5, 2);
    }

    @Test
    void waterAreaShouldReduceDuringOutflow() {
        // given
        map.changeTideTime(); // Przełączenie na czas odpływu
        int initialWaterSize = map.getAllWaters().size();

        // when
        map.tideOrOutflow(1);

        // then
        assertTrue(map.getAllWaters().size() < initialWaterSize);
    }
    @Test
    void shouldNotPlaceWaterOnOccupiedPositions() {
        // given
        Vector2d occupiedPosition = new Vector2d(2, 2);
        map.addAnimal(occupiedPosition, new Animal(occupiedPosition, 10, 7));

        // when
        Vector2d newWaterPosition = map.randomPlaceForNewWater(Arrays.asList(occupiedPosition));

        // then
        assertNotEquals(occupiedPosition, newWaterPosition);
    }

    @Test
    void noNewWaterAddedWhenNoSpaceAvailable() {
        // given
        // Wypełnij mapę zwierzętami, aby nie było miejsca na nowe obszary wodne
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                map.addAnimal(new Vector2d(x, y), new Animal(new Vector2d(x, y), 10, 7));
            }
        }
        int initialWaterSize = map.getAllWaters().size();

        // when
        map.tideOrOutflow(1);

        // then
        assertEquals(initialWaterSize, map.getAllWaters().size());
    }
    @Test
    void animalsShouldBeRemovedWhenEnteringWater() {
        // given
        Vector2d waterPosition = new Vector2d(1, 1);
        map.addWaterInVicinity(1);

        Animal animal = new Animal(waterPosition, 10, 7);
        map.addAnimal(waterPosition, animal);

        // when
        map.performMove(animal);

        // then
        assertFalse(map.getAnimalsAt(waterPosition).contains(animal));
    }

}
