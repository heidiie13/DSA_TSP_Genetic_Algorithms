package genetic_algorithms_tsp;

import java.util.Random;
import genetic_algorithms_tsp.genetic_algorithm.GeneticAlgorithm;
import static genetic_algorithms_tsp.genetic_objects.Population.fromDataSet;

public class Preset {
    public static GeneticAlgorithm getDefaultGA() {
        Random r = new Random();
        r.setSeed(new Random().nextLong());

        int popSize = 800;      // Size of the population.
        int maxGen = 1000;      // Number of generations to run.
        double crossoverRate = 0.95;
        double mutationRate = 0.05;

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();

        geneticAlgorithm.setPopulation(fromDataSet(popSize, r));
        geneticAlgorithm.setMaxGen(maxGen);
        geneticAlgorithm.setK(5);
        geneticAlgorithm.setElitismValue(1);
        geneticAlgorithm.setCrossoverRate(crossoverRate);
        geneticAlgorithm.setMutationRate(mutationRate);
        geneticAlgorithm.forceUniqueness(true);
        geneticAlgorithm.setCrossoverType(GeneticAlgorithm.CrossoverType.ONE_POINT);
        geneticAlgorithm.setMutationType(GeneticAlgorithm.MutationType.SCRAMBLE);

        return geneticAlgorithm;
    }
}
