package agh.ics.oop.model;

import agh.ics.oop.model.variants.MutationVariant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

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
        map.addAnimal(animal.getPosition(), animal);

        // then
        assertTrue(map.getAnimalsAt(animal.getPosition()).contains(animal));
    }

    @Test
    void shouldRemoveAnimalFromMap() {
        // given
        Animal animal = new Animal(new Vector2d(2, 2), 10, 7);
        map.addAnimal(animal.getPosition(), animal);

        // when
        map.removeAnimal(animal.getPosition(), animal);

        // then
        assertFalse(map.getAnimalsAt(animal.getPosition()).contains(animal));
    }

    @Test
    void animalCannotMoveBeyondUpperBound() {
        // given
        Animal animal = new Animal(new Vector2d(2, mapHeight - 1), 10, 7);
        map.addAnimal(animal.getPosition(), animal);

        // when
        Vector2d newPosition = new Vector2d(2, mapHeight);
        map.moveAnimals();

        // then
        // assertEquals(mapHeight - 1, animal.getPosition().getY());- wyrzucilabym bo powoduje bledy, bo nie wiemy jak obroci sie zwierze, to zalezne od jego genow
        assertTrue(animal.getPosition().getY() < mapHeight);
    }

    @Test
    void animalCannotMoveBeyondLowerBound() {
        // given
        Animal animal = new Animal(new Vector2d(0, 0), 10, 7);
        map.addAnimal(animal.getPosition(), animal);

        // when
        animal.rotate(MapDirection.SOUTH.ordinal());
        map.moveAnimals();

        // then
        // assertEquals(0, animal.getPosition().getY()); - wyrzucilabym bo powoduje bledy, bo nie wiemy jak obroci sie zwierze, to zalezne od jego genow
        assertTrue(animal.getPosition().getY() >= 0);
    }

    @Test
    void doesTotalEnergyBeforeReproductionEqualsAfterReproductionTotal () {
        // given
        WorldMap map = new WorldMap();

        Animal animal = new Animal(new Vector2d(0, 0), 20, 8);
        Animal partner = new Animal(new Vector2d(0, 0), 30, 8);

        map.addAnimal(animal.getPosition(), animal);
        map.addAnimal(partner.getPosition(), partner);

        int totalBefore = animal.getEnergyLevel() + partner.getEnergyLevel();

        // when
        map.handleAnimalReproductionAndEating(MutationVariant.RANDOM, 1, 3);

        int totalAfter = 0;
        Collection<Animal> animalsAtPosition = map.getAnimalsAt(animal.getPosition());

        for (Animal a : animalsAtPosition) {
            totalAfter += a.getEnergyLevel();
        }

        // then
        assertEquals(totalBefore, totalAfter);
    }
}