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

    @Test
    void animalShouldChangePositionAfterMove() {
        // given
        Animal animal = new Animal(new Vector2d(2, 2), 10, 7);
        Vector2d initialPosition = animal.getPosition();

        // when
        Vector2d newPosition = animal.intendMove();

        // then
        assertNotEquals(initialPosition, newPosition);
    }

    @Test
    void animalShouldDieWhenEnergyIsZeroOrLess() {
        // given
        Animal animal = new Animal(new Vector2d(2, 2), 1, 7);

        // when
        animal.loseEnergyAfterMove();

        // then
        assertEquals(0, animal.getEnergyLevel());
    }

    @Test
    void animalCannotReproduceWithInsufficientEnergy() {
        // given
        Animal animal1 = new Animal(new Vector2d(2, 2), 5, 7);
        Animal animal2 = new Animal(new Vector2d(2, 2), 5, 7);

        // when
        Optional<Animal> child = animal1.reproduce(animal2, MutationVariant.RANDOM, 1, 3, 10, 5);

        // then
        assertFalse(child.isPresent());
    }

    @Test
    void animalGenesShouldMutateOnReproduction() {
        // given
        Animal animal1 = new Animal(new Vector2d(2, 2), 20, 7);
        Animal animal2 = new Animal(new Vector2d(2, 2), 20, 7);

        // when
        Optional<Animal> childOpt = animal1.reproduce(animal2, MutationVariant.RANDOM, 1, 3, 10, 5);
        Animal child = childOpt.orElseThrow();

        // then
        assertNotEquals(animal1.getGenes(), child.getGenes());
        assertNotEquals(animal2.getGenes(), child.getGenes());
    }

    @Test
    void animalShouldNotDieWhenEnergyIsAboveZero() {
        // given
        Animal animal = new Animal(new Vector2d(2, 2), 2, 7);

        // when
        animal.loseEnergyAfterMove();

        // then
        assertTrue(animal.getEnergyLevel() > 0);
    }

    @Test
    void animalShouldIncreaseChildrenCountOnReproduction() {
        // given
        Animal animal1 = new Animal(new Vector2d(2, 2), 20, 7);
        Animal animal2 = new Animal(new Vector2d(2, 2), 20, 7);
        int initialChildrenCount = animal1.getChildsNumber();

        // when
        animal1.reproduce(animal2, MutationVariant.RANDOM, 1, 3, 10, 5);

        // then
        assertEquals(initialChildrenCount + 1, animal1.getChildsNumber());
    }

}
