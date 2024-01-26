package agh.ics.oop.model;

import agh.ics.oop.model.variants.MutationVariant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsTest {
    private TidesOutflowsMap worldMap;
    private Statistics statistics;

    @BeforeEach
    void setUp() {
        worldMap = new TidesOutflowsMap(10, 10, 2, 5, 5, 2);
        statistics = new Statistics(worldMap);
    }


    @Test
    void mostCommonGenotypeShouldBeIdentifiedCorrectly() {
        // given
        List<Integer> mostCommonGenotype = new ArrayList<>(Arrays.asList(4, 2, 2, 4, 1, 5, 1));

        Animal animal1 = new Animal(new Vector2d(1, 1), 10, 7, mostCommonGenotype);
        Animal animal2 = new Animal(new Vector2d(1, 2), 10, 7);
        Animal animal3 = new Animal(new Vector2d(3, 3), 10, 7, mostCommonGenotype);
        worldMap.addAnimal(animal1.getPosition(), animal1);
        worldMap.addAnimal(animal2.getPosition(), animal2);
        worldMap.addAnimal(animal3.getPosition(), animal3);

        // when
        statistics.update();

        // then
        assertEquals(mostCommonGenotype, statistics.getMostCommonGenotype());
    }

    @Test
    void shouldCorrectlyCountAnimalsAndPlants() {
        // given
        worldMap.addAnimal(new Vector2d(1, 1), new Animal(new Vector2d(1, 1), 10, 7));
        worldMap.addGrass(new Vector2d(2, 2));

        // when
        statistics.update();

        // then
        assertEquals(1, statistics.getTotalAnimals());
        assertEquals(1, statistics.getTotalPlants());
    }

    @Test
    void shouldCalculateAverageEnergyCorrectly() {
        // given
        worldMap.addAnimal(new Vector2d(1, 1), new Animal(new Vector2d(1, 1), 10, 7));
        worldMap.addAnimal(new Vector2d(2, 2), new Animal(new Vector2d(2, 2), 20, 7));

        // when
        statistics.update();

        // then
        assertEquals(15.0, statistics.getAverageEnergy(), 0.01);
    }

    @Test
    void shouldUpdateAverageChildrenCountCorrectly() {
        // given
        Animal parent1 = new Animal(new Vector2d(1, 1), 20, 7);
        Animal parent2 = new Animal(new Vector2d(1, 1), 20, 7);
        worldMap.addAnimal(parent1.getPosition(), parent1);
        worldMap.addAnimal(parent2.getPosition(), parent2);
        parent1.reproduce(parent2, MutationVariant.RANDOM, 1, 3, 10, 5);

        // when
        statistics.update();

        // then
        assertTrue(statistics.getAverageChildren() > 0);
    }

    @Test
    void shouldCorrectlyCalculateGenotypeDiversity() {
        // given
        Animal animal1 = new Animal(new Vector2d(1, 1), 10, 7);
        Animal animal2 = new Animal(new Vector2d(2, 2), 20, 7);
        worldMap.addAnimal(animal1.getPosition(), animal1);
        worldMap.addAnimal(animal2.getPosition(), animal2);

        // when
        statistics.update();

        // then
        assertTrue(statistics.getGenotypePopularity().size() > 0);
    }

}
