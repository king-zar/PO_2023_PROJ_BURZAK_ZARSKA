package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal {
    private Vector2d position;
    private MapDirection orientation;

    private int energyLevel;
    private List<Integer> genes;

    // default
    public Animal () {
        this(new Vector2d(2, 2), 10, 7);
    }

    public Animal(Vector2d position, int energyLevel, int genesNumber) {
        this.position = position;
        this.orientation = MapDirection.NORTH;
        this.energyLevel = energyLevel;
        this.genes = generateRandomGenes(genesNumber);
    }

    private List<Integer> generateRandomGenes(int n) {
        List<Integer> randomGenes = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            // Dodaj losową wartość od 0 do 7 do listy genes
            randomGenes.add(random.nextInt(8));
        }

        System.out.println(randomGenes);
        return randomGenes;
    }

    public boolean isAt(Vector2d position) {
        return this.position == position;
    }

    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    void move() {
        // pamietamy na ktorym indeksie jest zwierze w danym ruchu czy jak?
    }

    void eat() { // 1 to troche malo jak na fakt, ze spotkanie rosliny nie jest az tak czeste
        energyLevel += 2;
    }

    void reproduce() {
        if (energyLevel >= 8) {
            // zeby mialo sens to mysle, ze energia minimum na poziomie 8
        }
    }
}
