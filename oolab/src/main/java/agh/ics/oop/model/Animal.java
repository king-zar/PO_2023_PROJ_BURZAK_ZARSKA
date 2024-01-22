package agh.ics.oop.model;

import agh.ics.oop.model.utils.MovementHandler;
import agh.ics.oop.model.utils.RandomGenesGenerator;
import agh.ics.oop.model.variants.MutationVariant;

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

    private int age;
    private int childsNumber;

    // default
    public Animal (Vector2d position) {
        this(position, 10, 7);
    }

    public Animal(Vector2d position, int energyLevel, int genesNumber) {
        this(position, energyLevel, genesNumber, RandomGenesGenerator.generateRandomGenes(genesNumber));
    }

    // dla zwierzat dzieci ponizszy konstruktor dziala zawsze
    private Animal(Vector2d position, int energyLevel, int genesNumber, List<Integer> genes) {
        this.position = position;
        this.orientation = MapDirection.NORTH;
        this.energyLevel = energyLevel;
        this.genes = new ArrayList<>(genes); // kopia aby uniknąć wpływu zewnętrznych zmian
        this.currentGeneIndex = 0;
        this.age = 1; // 1 krok czasowy
        this.childsNumber = 0;
    }

    public void setCurrentGeneIndex(int currentGeneIndex) {
        this.currentGeneIndex = currentGeneIndex;
    }

    public void setPosition(Vector2d newPosition) {
        this.position = newPosition;
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public Vector2d intendMove() {
        return MovementHandler.intendMove(this);
    }

    // settery dla nowonarodzonego zwierzatka
    private void setRandomOrientation() {
        Random random = new Random();
        int rotation = random.nextInt(8); // Losujemy wartość od 0 do 7
        rotate(rotation); // Obracamy zwierzaka o wylosowaną ilość razy
    }

    private void setRandomCurrentGeneIndex() {
        this.currentGeneIndex = new Random().nextInt(this.genes.size());
    }

    public void rotate(int rotation) {
        for (int i = 0; i < rotation; i++) {
            this.orientation = this.orientation.next();
        }
    }

    public void eat(Grass grass) {
        energyLevel += grass.getPlantNutrition();
    }

    public void die() {
        energyLevel = 0;
    }

    public void loseEnergyAfterMove() {
        energyLevel -= 1;
    }

    public void getOlder() {
        age += 1;
    }

    public Optional<Animal> reproduce(Animal partner, MutationVariant mutationVariant, int minMutations, int maxMutations,
                                      int energyToReproduce, int energyLostInReproduction) {
        if (canReproduceWith(partner, energyToReproduce)) {
            return Optional.of(performReproductionWith(partner, mutationVariant, minMutations, maxMutations,
                                energyLostInReproduction));
        }
        return Optional.empty();
    }

    boolean canReproduceWith(Animal partner, int energyToReproduce) {
        return this.position.equals(partner.getPosition()) &&
                this.energyLevel >= energyToReproduce &&
                partner.getEnergyLevel() >= energyToReproduce;
    }

    private Animal performReproductionWith(Animal partner, MutationVariant mutationVariant,
                                           int minMutations, int maxMutations,
                                           int energyLostInReproduction) {
        this.energyLevel -= energyLostInReproduction;
        partner.energyLevel -= energyLostInReproduction;

        this.childsNumber += 1;
        this.childsNumber += 1;

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

        int mutationCount = random.nextInt(maxMutations - minMutations + 1) + minMutations;

        if (mutationVariant == MutationVariant.RANDOM) {
            childGenes = randomMutation(childGenes, mutationCount);
        } else if (mutationVariant == MutationVariant.SLIGHT_CORRECTION) {
            childGenes = slightCorrection(childGenes, mutationCount);
        }

        Animal child = new Animal(this.position, 2*energyLostInReproduction, childGenes.size(), childGenes);
        child.setRandomCurrentGeneIndex();
        child.setRandomOrientation();

        return child;
    }

    public List<Integer> randomMutation(List<Integer> childGenes, int mutationCount) {
        Random random = new Random();

        for (int i = 0; i < mutationCount; i++) {
            int mutatedGeneIndex = random.nextInt(childGenes.size());
            int newGeneValue = random.nextInt(8);
            childGenes.set(mutatedGeneIndex, newGeneValue);
        }

        return childGenes;
    }

    public List<Integer> slightCorrection(List<Integer> childGenes, int mutationCount) {
        Random random = new Random();

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

    // gettery
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
        return energyLevel;
    }

    public List<Integer> getGenes() {
        return genes;
    }

    public int getAge() {
        return age;
    }

    public int getChildsNumber() {
        return childsNumber;
    }
}
