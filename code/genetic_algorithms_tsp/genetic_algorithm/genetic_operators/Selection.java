package genetic_algorithms_tsp.genetic_algorithm.genetic_operators;

import genetic_algorithms_tsp.genetic_objects.Chromosome;
import genetic_algorithms_tsp.genetic_objects.Population;

import java.util.ArrayList;
import java.util.Random;

public class Selection {

    // Picks k Chromosomes at random and then return the best one.
    public static Chromosome tournamentSelection(Population population, int k, Random random) {
        if (k < 1) {
            throw new IllegalArgumentException("K must be greater than 0.");
        }

        Chromosome[] populationAsArray = population.getChromosomes();
        ArrayList<Chromosome> kChromosomes = getKChromosomes(populationAsArray, k, random);
        return getBestChromosome(kChromosomes);
    }

    // Returns k randomly selected Chromosomes.
    private static ArrayList<Chromosome> getKChromosomes(Chromosome[] pop, int k, Random random) {

        ArrayList<Chromosome> kChromosomes = new ArrayList<>();

        for (int j = 0; j < k; j++) {
            Chromosome chromosome = pop[random.nextInt(pop.length)];
            kChromosomes.add(chromosome);
        }

        return kChromosomes;
    }

    private static Chromosome getBestChromosome(ArrayList<Chromosome> arrayList) {

        Chromosome bestC = null;

        for (Chromosome c : arrayList) {
            if (bestC == null) {
                bestC = c;
            } else if (c.getDistance() < bestC.getDistance()) {
                bestC = c;
            }
        }

        return bestC;
    }
}
