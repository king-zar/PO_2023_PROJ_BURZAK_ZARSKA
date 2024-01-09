package agh.ics.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WorldMapTest {

    private WorldMap map;
    private final int mapWidth = 5;
    private final int mapHeight = 5;

    @BeforeEach
    void setUp() {
        map = new WorldMap(mapWidth, mapHeight);
    }

    @Test
    void shouldAddAnimalToMap() {
        // given
        Animal animal = new Animal(new Vector2d(2, 2), 10, 7);

        // when
        map.add(animal.getPosition(), animal);

        // then
        assertTrue(map.getItemsAt(animal.getPosition()).contains(animal));
    }

    @Test
    void shouldRemoveAnimalFromMap() {
        // given
        Animal animal = new Animal(new Vector2d(2, 2), 10, 7);
        map.add(animal.getPosition(), animal);

        // when
        map.remove(animal.getPosition(), animal);

        // then
        assertFalse(map.getItemsAt(animal.getPosition()).contains(animal));
    }
    @Test
    void animalCannotMoveBeyondUpperBound() {
        // given
        Animal animal = new Animal(new Vector2d(2, mapHeight - 1), 10, 7);
        map.add(animal.getPosition(), animal);

        // when
        Vector2d newPosition = new Vector2d(2, mapHeight);
        map.performMove(animal);

        // then
        assertEquals(mapHeight - 1, animal.getPosition().getY());
        assertTrue(animal.getPosition().getY() < mapHeight);
    }

    @Test
    void animalCannotMoveBeyondLowerBound() {
        // given
        Animal animal = new Animal(new Vector2d(2, 0), 10, 7);
        map.add(animal.getPosition(), animal);

        // when
        animal.rotate(MapDirection.SOUTH.ordinal());
        map.performMove(animal);

        // then
        assertEquals(0, animal.getPosition().getY());
        assertTrue(animal.getPosition().getY() >= 0);
    }


}
