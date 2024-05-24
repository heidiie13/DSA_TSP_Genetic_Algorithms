package genetic_algorithms_tsp.genetic_algorithm;

import java.util.*;

import genetic_algorithms_tsp.display.ViewGraph;
import genetic_algorithms_tsp.display.ViewProperties;
import genetic_algorithms_tsp.display.ViewResults;
import genetic_algorithms_tsp.display.ViewTSP;
import genetic_algorithms_tsp.genetic_objects.Chromosome;
import genetic_algorithms_tsp.genetic_objects.City;
import genetic_algorithms_tsp.genetic_objects.Population;
import genetic_algorithms_tsp.genetic_algorithm.genetic_operators.Crossover;
import genetic_algorithms_tsp.genetic_algorithm.genetic_operators.Mutation;
import genetic_algorithms_tsp.genetic_algorithm.genetic_operators.Selection;

public class GeneticAlgorithm {
    private final Random random;
    private Population population;
    private int maxGen;
    private double crossoverRate;
    private double mutationRate;
    private CrossoverType crossoverType;
    private MutationType mutationType;
    private int k;                  // For tournament selection.
    private int elitismValue;       // Quantity of Elite to carry along each generation.
    private boolean finished;
    private boolean forceUniqueness;
    private int averageDistanceOfFirstGeneration;
    private int bestDistanceOfFirstGeneration;
    private int averageDistanceOfLastGeneration;
    private int bestDistanceOfLastGeneration;
    private final ArrayList<Integer> averageDistanceOfEachGeneration;
    private final ArrayList<Integer> bestDistanceOfEachGeneration;
    private Chromosome mostFitLast;

    public GeneticAlgorithm() {
        population = Population.getRandomPopulation(100, 500, new Random());
        random = new Random();
        maxGen = 2000;
        k = 5;
        elitismValue = 3;
        crossoverRate = 1;
        mutationRate = 0.05;
        forceUniqueness(forceUniqueness);
        crossoverType = CrossoverType.UNIFORM_ORDER;
        mutationType = MutationType.INSERTION;
        forceUniqueness = true;
        finished = false;
        averageDistanceOfEachGeneration = new ArrayList<>();
        bestDistanceOfEachGeneration = new ArrayList<>();
    }

    public void setPopulation(Population population) {
        if (population == null) {
            throw new IllegalArgumentException("Population cannot be null.");
        }
        this.population = population.deepCopy();
        averageDistanceOfFirstGeneration = population.getAverageDistance();
        bestDistanceOfFirstGeneration = population.getMostFit().getDistance();
    }

    public void setMaxGen(int maxGen) {
        if (maxGen < 0) {
            throw new IllegalArgumentException("Max generation cannot be negative.");
        }
        this.maxGen = maxGen;
    }

    public void setK(int k) {
        if (k < 0) {
            throw new IllegalArgumentException("K cannot be negative.");
        }
        this.k = k;
    }

    public void setElitismValue(int elitismValue) {
        if (elitismValue > population.size()) {
            throw new IllegalArgumentException("Elitism value cannot be greater than population size.");
        }
        this.elitismValue = elitismValue;
    }

    public void setCrossoverRate(double crossoverRate) {
        if (crossoverRate < 0 || crossoverRate > 1) {
            throw new IllegalArgumentException("Crossover rate must be between 0 and 1 inclusive.");
        }
        this.crossoverRate = crossoverRate;
    }

    public void setMutationRate(double mutationRate) {
        if (mutationRate < 0 || mutationRate > 1) {
            throw new IllegalArgumentException("Mutation rate must be between 0 and 1 inclusive.");
        }
        this.mutationRate = mutationRate;
    }

    public void setCrossoverType(CrossoverType crossoverType) {
        this.crossoverType = crossoverType;
    }

    public void setMutationType(MutationType mutationType) {
        this.mutationType = mutationType;
    }

    public void forceUniqueness(boolean forceUniqueness) {
        this.forceUniqueness = forceUniqueness;
    }

    public int getAverageDistanceOfFirstGeneration() {
        if (!finished) {
            throw new IllegalArgumentException("Genetic algorithm was never run.");
        }
        return averageDistanceOfFirstGeneration;
    }

    public int getAverageDistanceOfLastGeneration() {
        if (!finished) {
            throw new IllegalArgumentException("Genetic algorithm was never run.");
        }
        return averageDistanceOfLastGeneration;
    }

    public int getBestDistanceOfFirstGeneration() {
        if (!finished) {
            throw new IllegalArgumentException("Genetic algorithm was never run.");
        }
        return bestDistanceOfFirstGeneration;
    }

    public int getBestDistanceOfLastGeneration() {
        if (!finished) {
            throw new IllegalArgumentException("Genetic algorithm was never run.");
        }
        return bestDistanceOfLastGeneration;
    }

    public ArrayList<Integer> getAverageDistanceOfEachGeneration() {
        if (!finished) {
            throw new IllegalArgumentException("Genetic algorithm was never run.");
        }
        return averageDistanceOfEachGeneration;
    }

    public ArrayList<Integer> getBestDistanceOfEachGeneration() {
        if (!finished) {
            throw new IllegalArgumentException("Genetic algorithm was never run.");
        }
        return bestDistanceOfEachGeneration;
    }

    private void performElitism(Population nextGen) {
        PriorityQueue<Chromosome> priorityQueue = new PriorityQueue<>();

        for (Chromosome chromosome : population) {
            priorityQueue.add(chromosome);
        }

        for (int i = 0; i < elitismValue; i++) {

            Chromosome chromosome = priorityQueue.poll();

            if (random.nextDouble() <= mutationRate) {
                if (chromosome == null) {
                    throw new IllegalArgumentException("Chromosome is null.");
                }
                chromosome = performLocalSearch(chromosome);
            }

            nextGen.add(chromosome);
        }
    }

    private Chromosome performLocalSearch(Chromosome chromosome) {

        int bestDistance = chromosome.getDistance();
        City[] array = chromosome.getCitiesArray();
        City[] bestArray = array.clone();

        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                City[] temp = array.clone();
                reverse(temp, i, j);

                Chromosome c = new Chromosome(temp);

                int distance = c.getDistance();
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestArray = c.getCitiesArray();
                }
            }
        }

        return new Chromosome(bestArray);
    }

    private static void reverse(City[] array, int i, int j) {
        while (i < j) {
            City temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            i++;
            j--;
        }
    }

    public enum MutationType {
        INSERTION,
        RECIPROCAL_EXCHANGE,
        SCRAMBLE
    }

    // Mutate the Chromosome based on what type is selected.
    private Chromosome mutate(Chromosome chromosome) {
        if (mutationType == MutationType.INSERTION) {
            return Mutation.insertion(chromosome, random);
        } else if (mutationType == MutationType.RECIPROCAL_EXCHANGE) {
            return Mutation.reciprocalExchange(chromosome, random);
        } else if (mutationType == MutationType.SCRAMBLE) {
            return Mutation.scrambleMutation(chromosome, random);
        } else { // Default is insertion.
            return Mutation.insertion(chromosome, random);
        }
    }

    public enum CrossoverType {
        UNIFORM_ORDER,
        ONE_POINT,
        TWO_POINT
    }

    // Crossover the Chromosomes based on what type is selected.
    private ArrayList<Chromosome> crossover(Chromosome p1, Chromosome p2) {
        ArrayList<Chromosome> children;
        if (crossoverType == CrossoverType.UNIFORM_ORDER) {
            children = Crossover.uniformOrder(p1, p2, random);
        } else if (crossoverType == CrossoverType.ONE_POINT) {
            children = Crossover.onePointCrossover(p1, p2, random);
        } else {
            children = Crossover.twoPointCrossover(p1, p2, random);
        }
        return children;
    }

    /**
     * @return The new generation is generated using genetic operators
     */
    private Population createNextGeneration() {

        Population nextGen = new Population(population.size());

        performElitism(nextGen); // Add the fittest chromosome of old generation to the new generation
        HashSet<Chromosome> chromosomesAdded = new HashSet<>(); // Checking duplicates.
        while (nextGen.size() < population.size() - 1) {

            Chromosome p1 = Selection.tournamentSelection(population, k, random);
            Chromosome p2 = Selection.tournamentSelection(population, k, random);

            boolean doCrossover = (random.nextDouble() <= crossoverRate);
            boolean doMutate1 = (random.nextDouble() <= mutationRate);
            boolean doMutate2 = (random.nextDouble() <= mutationRate);

            if (doCrossover) {
                ArrayList<Chromosome> children = crossover(p1, p2);
                p1 = children.get(0);
                p2 = children.get(1);
            }

            if (doMutate1) p1 = mutate(p1);
            if (doMutate2) p2 = mutate(p2);

            if (forceUniqueness) {
                if (!chromosomesAdded.contains(p1)) {
                    chromosomesAdded.add(p1);
                    nextGen.add(p1);
                }

                if (!chromosomesAdded.contains(p2)) {
                    chromosomesAdded.add(p2);
                    nextGen.add(p2);
                }
            } else {
                nextGen.add(p1);
                nextGen.add(p2);
            }
        }

        if (nextGen.size() != population.size()) {
            nextGen.add(Selection.tournamentSelection(population, k, random));
        }

        if (nextGen.size() != population.size()) {
            throw new IllegalStateException("Next generation population must be full.");
        }

        return nextGen;
    }

    public void run() {
        mostFitLast = population.getMostFit();
        for (int i = 0; i < maxGen; i++) {
            population = createNextGeneration();

            mostFitLast = population.getMostFit();
            averageDistanceOfEachGeneration.add(population.getAverageDistance());
            bestDistanceOfEachGeneration.add(population.getMostFit().getDistance());
        }

        finished = true;
        averageDistanceOfLastGeneration = population.getAverageDistance();
        bestDistanceOfLastGeneration = population.getMostFit().getDistance();
    }

    public void run_VisualGA() {
        ViewTSP win = new ViewTSP(population.getCities());

        mostFitLast = population.getMostFit();
        win.draw(mostFitLast);
        for (int i = 0; i < maxGen; i++) {
            population = createNextGeneration();

            Chromosome mostFit = population.getMostFit();
            if (!mostFit.equals(mostFitLast)) {
                win.draw(mostFit);
            }
            mostFitLast = mostFit;
            averageDistanceOfEachGeneration.add(population.getAverageDistance());
            bestDistanceOfEachGeneration.add(population.getMostFit().getDistance());
        }

        finished = true;
        averageDistanceOfLastGeneration = population.getAverageDistance();
        bestDistanceOfLastGeneration = population.getMostFit().getDistance();
    }

    public void showGraphInWindow() {
        ArrayList<ArrayList<Integer>> yValues = new ArrayList<>();
        yValues.add(getAverageDistanceOfEachGeneration());
        yValues.add(getBestDistanceOfEachGeneration());
        ArrayList<String> legend = new ArrayList<>();
        legend.add("Average Evaluation of Entire Population");
        legend.add("Evaluation of Fittest Member");
        new ViewGraph(yValues, legend);
    }

    public Chromosome getMostFitLast() {
        return mostFitLast;
    }

    public void printProperties() {
        System.out.println("----------Genetic Algorithm Properties----------");
        System.out.println("Number of Cities:   " + population.getMostFit().getCitiesArray().length);
        System.out.println("Population Size:    " + population.size());
        System.out.println("Max. Generation:    " + maxGen);
        System.out.println("k Value:            " + k);
        System.out.println("Elitism Value:      " + elitismValue);
        System.out.println("Crossover Type:     " + crossoverType);
        System.out.println("Crossover Rate:     " + (crossoverRate * 100) + "%");
        System.out.println("Mutation Type:      " + mutationType);
        System.out.println("Mutation Rate:      " + (mutationRate * 100) + "%");
    }

    public void printResults() {
        if (!finished) {
            throw new IllegalArgumentException("Genetic algorithm was never run.");
        }

        System.out.println("-----------Genetic Algorithm Results------------");
        System.out.println("Average Distance of First Generation:  " +
                getAverageDistanceOfFirstGeneration());
        System.out.println("Average Distance of Last Generation:   " +
                getAverageDistanceOfLastGeneration());
        System.out.println("Best Distance of First Generation:     " +
                getBestDistanceOfFirstGeneration());
        System.out.println("Best Distance of Last Generation:      " +
                getBestDistanceOfLastGeneration());
        System.out.println("Best route: " + Arrays.toString(getMostFitLast().getCitiesArray()));
    }

    public void showProperties() {
        if (!finished) {
            throw new IllegalArgumentException("Genetic algorithm was never run.");
        }
        StringBuilder results = new StringBuilder();
        results.append("----------Genetic Algorithm Properties----------").append("\n")
                .append("Number of Cities:   " + population.getMostFit().getCitiesArray().length).append("\n")
                .append("Population Size:    " + population.size()).append("\n")
                .append("Max. Generation:    " + maxGen).append("\n")
                .append("k Value:            " + k).append("\n")
                .append("Elitism Value:      " + elitismValue).append("\n")
                .append("Crossover Type:     " + crossoverType).append("\n")
                .append("Crossover Rate:     " + (crossoverRate * 100) + "%").append("\n")
                .append("Mutation Type:      " + mutationType).append("\n")
                .append("Mutation Rate:      " + (mutationRate * 100) + "%");

        new ViewProperties(results);
    }

    public void showResults() {
        if (!finished) {
            throw new IllegalArgumentException("Genetic algorithm was never run.");
        }
        StringBuilder results = new StringBuilder();
        results.append("-----------Genetic Algorithm Results------------").append("\n")
                .append("Average Distance of First Generation:  ").append(getAverageDistanceOfFirstGeneration()).append("\n")
                .append("Average Distance of Last Generation:   " +
                        getAverageDistanceOfLastGeneration()).append("\n")
                .append("Best Distance of First Generation:     " +
                        getBestDistanceOfFirstGeneration()).append("\n")
                .append("Best Distance of Last Generation:      " +
                        getBestDistanceOfLastGeneration()).append("\n");

        StringBuilder resultsName = new StringBuilder();
        resultsName.append("Best route:\n");
        for (City city : getMostFitLast().getCitiesArray()) {
            resultsName.append(city.getName()).append("  ");
        }
        new ViewResults(results, resultsName);
    }
}