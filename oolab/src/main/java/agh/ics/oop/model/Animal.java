package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement {
    private Vector2d position;
    private MapDirection orientation;

    private int energyLevel;
    private List<Integer> genes;
    private int currentGeneIndex;

    // default
    public Animal (Vector2d position) {
        this(position, 10, 7);
    }

    public Animal(Vector2d position, int energyLevel, int genesNumber) {
        this(position, energyLevel, genesNumber, generateRandomGenes(genesNumber));
    }

    // dla zwierzat dzieci ponizszy konstruktor dziala zawsze
    private Animal(Vector2d position, int energyLevel, int genesNumber, List<Integer> genes) {
        this.position = position;
        this.orientation = MapDirection.NORTH;
        this.energyLevel = energyLevel;
        this.genes = new ArrayList<>(genes); // kopia aby uniknąć wpływu zewnętrznych zmian
        this.currentGeneIndex = 0;
    }

    private static List<Integer> generateRandomGenes(int n) {
        List<Integer> randomGenes = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            // Dodaj losową wartość od 0 do 7 do listy genes
            randomGenes.add(random.nextInt(8));
        }

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

    public List<Integer> getGenes() {
        return genes;
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

    public void eat(Grass grass) {
        energyLevel += grass.getPlantNutrition();
    }
    public void reproduce(Animal partner) {
        if (canReproduceWith(partner)) {
            performReproductionWith(partner);
        }
    }

    public void loseEnergyAfterMove() {
        energyLevel -= 1;
    }

    boolean canReproduceWith(Animal partner) {
        return this.position.equals(partner.getPosition()) &&
                this.energyLevel >= 8 &&
                partner.getEnergyLevel() >= 8;
    }

    private void performReproductionWith(Animal partner) {
        this.energyLevel -= 5;
        partner.energyLevel -= 5;

        int totalEnergyBeforeReproduction = this.getEnergyLevel() + partner.getEnergyLevel();
        int thisGenesContribution = (this.getEnergyLevel() * genes.size()) / totalEnergyBeforeReproduction;
        int partnerGenesContribution = genes.size() - thisGenesContribution;

        Random random = new Random();
        boolean takeFromThisLeft = random.nextBoolean();

        List<Integer> childGenes = new ArrayList<>();
        for (int i = 0; i < genes.size(); i++) {
            if ((i < thisGenesContribution && takeFromThisLeft) || (i >= thisGenesContribution && !takeFromThisLeft)) {
                childGenes.add(this.genes.get(i));
            } else {
                childGenes.add(partner.genes.get(i));
            }
        }

        // mozemy zmutowac losowo od 0 do 3 genow dziecka
        int mutationCount = random.nextInt(4); // potem konfiguracyjnie !!!

        for (int i = 0; i < mutationCount; i++) {
            int mutatedGeneIndex = random.nextInt(genes.size());
            int newGeneValue = random.nextInt(8);
            childGenes.set(mutatedGeneIndex, newGeneValue);
        }

        Animal child = new Animal(this.position, 8, genes.size(), childGenes);
    }
}
