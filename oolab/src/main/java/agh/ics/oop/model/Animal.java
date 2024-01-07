package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal {
    private Vector2d position;
    private MapDirection orientation;

    private int energyLevel;
    private List<Integer> genes;
    private int currentGeneIndex;

    // default
    public Animal () {
        this(new Vector2d(2, 2), 10, 7);
    }

    public Animal(Vector2d position, int energyLevel, int genesNumber) {
        this.position = position;
        this.orientation = MapDirection.NORTH;
        this.energyLevel = energyLevel;
        this.genes = generateRandomGenes(genesNumber);
        this.currentGeneIndex = 0;
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

    public int getEnergyLevel() {
        return this.energyLevel;
    }

    public Vector2d intendMove() {   // to jest z wzorca projektowego gdzie Animal to obserwator i zgłasza zamiar rucho a WorldMap zarządza tym ruchem możesz zmienić to jak Ci się nie podoba XD
        int rotation = genes.get(currentGeneIndex);
        this.rotate(rotation);
        Vector2d movement = this.orientation.toUnitVector();
        Vector2d newPosition = this.position.add(movement);

        currentGeneIndex = (currentGeneIndex + 1) % genes.size();

        return newPosition;
    }

    public void rotate(int rotation) {
        for (int i = 0; i < rotation; i++) {
            this.orientation = this.orientation.next();
        }
    }

    public void setPosition(Vector2d newPosition) {
        this.position = newPosition;
    }


    void move() {
        // pamietamy na ktorym indeksie jest zwierze w danym ruchu czy jak?
    }

    void eat() { // 1 to troche malo jak na fakt, ze spotkanie rosliny nie jest az tak czeste
        energyLevel += 2;
    }

    void reproduce(Animal partner) {
        if (this.energyLevel > 8 && partner.getEnergyLevel() > 8) {
            this.energyLevel -= 4;
            partner.energyLevel -= 4;


            int totalEnergyBeforeReproduction = this.getEnergyLevel() + partner.getEnergyLevel();
            int thisGenesContribution = (this.getEnergyLevel() * genes.size()) / totalEnergyBeforeReproduction;
            int partnerGenesContribution = genes.size() - thisGenesContribution;

            Random random = new Random();
            boolean takeFromThisLeft = random.nextBoolean();

            List<Integer> childGenes = new ArrayList<>();
            for (int i = 0; i < genes.size(); i++) {
                if (i < thisGenesContribution && takeFromThisLeft || i >= thisGenesContribution && !takeFromThisLeft) {
                    childGenes.add(this.genes.get(i));
                } else {
                    childGenes.add(partner.genes.get(i));
                }
            }

            Animal child = new Animal(this.position, 8, genes.size()); // początkowa energia dziecka to 8 ?
            child.genes = childGenes;

        }
    }



}
