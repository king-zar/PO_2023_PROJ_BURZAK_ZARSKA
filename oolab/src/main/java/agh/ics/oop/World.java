package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.WorldMap;

public class World {
    public static void main(String[] args) {
        System.out.println("System wystartowal");

        WorldMap map = new WorldMap(5, 7);

        Animal animal1 = new Animal(new Vector2d(2,4), 20, 7);
        Animal animal2 = new Animal(new Vector2d(2,4));

        // reproduce
        animal1.reproduce(animal2);
        System.out.println(animal1.getEnergyLevel());
        System.out.println(animal2.getEnergyLevel());

        // move
        System.out.println(animal1.getPosition());
        animal1.move(map);
        System.out.println(animal1.getPosition());

        System.out.println("System zakonczyl dzialanie");
    }
}
