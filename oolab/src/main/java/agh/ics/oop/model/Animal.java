package agh.ics.oop.model;

import agh.ics.oop.model.utils.MovementHandler;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        return this.position.equals(position);
    }

    public Vector2d getPosition() {
        return position;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public int getCurrentGeneIndex() {
        return currentGeneIndex;
    }

    public int getEnergyLevel() {
        return this.energyLevel;
    }

    public List<Integer> getGenes() {
        return genes;
    }

    public Vector2d intendMove() {
        return MovementHandler.intendMove(this);
    }

    public void setCurrentGeneIndex(int currentGeneIndex) {
        this.currentGeneIndex = currentGeneIndex;
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
    public Optional<Animal> reproduce(Animal partner, MutationVariant mutationVariant) {
        if (canReproduceWith(partner)) {
            return Optional.of(performReproductionWith(partner, mutationVariant));
        }
        return Optional.empty();
    }

    public void loseEnergyAfterMove() {
        energyLevel -= 1;
    }

    boolean canReproduceWith(Animal partner) {
        return this.position.equals(partner.getPosition()) &&
                this.energyLevel >= 8 &&
                partner.getEnergyLevel() >= 8;
    }

    private Animal performReproductionWith(Animal partner, MutationVariant mutationVariant) {
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


        if (mutationVariant == MutationVariant.RANDOM) {
            childGenes = randomMutation(childGenes);
        } else if (mutationVariant == MutationVariant.SLIGHT_CORRECTION) {
            childGenes = slightCorrection(childGenes);
        }

        return new Animal(this.position, 8, childGenes.size(), childGenes);
    }

    public List<Integer> randomMutation(List<Integer> childGenes) {
        Random random = new Random();

        int mutationCount = random.nextInt(4); // potem konfiguracyjnie !!!

        for (int i = 0; i < mutationCount; i++) {
            int mutatedGeneIndex = random.nextInt(childGenes.size());
            int newGeneValue = random.nextInt(8);
            childGenes.set(mutatedGeneIndex, newGeneValue);
        }

        return childGenes;
    }

    public List<Integer> slightCorrection(List<Integer> childGenes) {
        Random random = new Random();

        int mutationCount = random.nextInt(4); // potem konfiguracyjnie !!!

        for (int i = 0; i < mutationCount; i++) {
            int mutatedGeneIndex = random.nextInt(childGenes.size());
            int mutationDirection = random.nextBoolean() ? 1 : -1; // 1 - w górę, -1 - w dół
            int newGeneValue = (childGenes.get(mutatedGeneIndex) + mutationDirection + 8) % 8;
            childGenes.set(mutatedGeneIndex, newGeneValue);
        }

        return childGenes;
    }

    @Override
    public String toString() {
        return switch (orientation) {
            case NORTH -> "N";
            case NORTHEAST -> "NE";
            case EAST -> "E";
            case SOUTHEAST -> "SE";
            case SOUTH -> "S";
            case SOUTHWEST -> "SW";
            case WEST -> "W";
            case NORTHWEST -> "NW";
        };
    }
}
