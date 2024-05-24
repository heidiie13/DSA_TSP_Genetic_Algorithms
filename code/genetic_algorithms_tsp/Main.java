package genetic_algorithms_tsp;

import genetic_algorithms_tsp.genetic_algorithm.GeneticAlgorithm;

public class Main {
    public static void main (String[] args) {
        GeneticAlgorithm geneticAlgorithm = Preset.getDefaultGA();
        geneticAlgorithm.run_VisualGA();
        geneticAlgorithm.showResults();
        geneticAlgorithm.showProperties();
        geneticAlgorithm.showGraphInWindow();
        geneticAlgorithm.printProperties();
        geneticAlgorithm.printResults();
    }
}