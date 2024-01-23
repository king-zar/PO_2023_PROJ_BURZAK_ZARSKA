package agh.ics.oop.model;

import static org.junit.jupiter.api.Assertions.*;

import agh.ics.oop.model.variants.MutationVariant;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class AnimalTest {
    @Test
    public void doesAnimalRotateCorrectly() {
        // given
        Animal animal = new Animal(new Vector2d(2, 2), 10, 7);

        // when
        animal.rotate(3);

        // then
        assertEquals(MapDirection.SOUTHEAST, animal.getOrientation());
    }

    @Test
    void animalShouldLoseEnergyAfterMove() { // nie dziala wiec trzeba zrobic tracenie energii
        // given
        Animal animal = new Animal(new Vector2d(2, 2), 10, 7);
        int initialEnergy = animal.getEnergyLevel();

        // when
        animal.intendMove();

        // then
        assertEquals(initialEnergy - 1, animal.getEnergyLevel());
    }

    @Test
    void animalShouldEatGrassAndGainEnergy() {
        // given
        Animal animal = new Animal(new Vector2d(2, 2), 10, 7);
        Grass grass = new Grass(new Vector2d(2, 2), 2);
        int initialEnergy = animal.getEnergyLevel();

        // when
        animal.eat(grass);

        // then
        assertEquals(initialEnergy + grass.getPlantNutrition(), animal.getEnergyLevel());
    }
    @Test
    void animalsAfterMatingHaveLowerEnergy() {
        // given
        Animal animal1 = new Animal(new Vector2d(2, 2), 10, 7);
        Animal animal2 = new Animal(new Vector2d(2, 2), 10, 7);

        // when
//        animal1.reproduce(animal2, MutationVariant.RANDOM, 1, 3);

        // then
        assertEquals(5, animal1.getEnergyLevel());
        assertEquals(5, animal2.getEnergyLevel());
    }
}
