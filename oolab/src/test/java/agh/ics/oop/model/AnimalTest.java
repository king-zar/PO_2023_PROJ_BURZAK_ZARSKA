package agh.ics.oop.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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
}
