package agh.ics.oop;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.Vector2d;

public class World {
    public static void main(String[] args) {
        System.out.println("System wystartowal");

        Animal animal1 = new Animal(new Vector2d(2,4), 20, 7);
        Animal animal2 = new Animal();

        System.out.println("System zakonczyl dzialanie");
    }
}
