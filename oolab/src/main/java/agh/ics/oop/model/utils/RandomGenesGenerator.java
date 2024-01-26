package agh.ics.oop.model.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGenesGenerator {
    public static List<Integer> generateRandomGenes(int n) {
        List<Integer> randomGenes = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            // Dodaj losową wartość od 0 do 7 do listy genes
            randomGenes.add(random.nextInt(8));
        }

        return randomGenes;
    }
}
