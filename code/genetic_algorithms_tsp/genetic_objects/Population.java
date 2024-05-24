package genetic_algorithms_tsp.genetic_objects;

import genetic_algorithms_tsp.import_data.ImportData;

import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

public class Population implements Iterable<Chromosome> {

    private final PriorityQueue<Chromosome> chromosomes;
    private final int maxSize;

    public Population(int maxSize) {
        this.maxSize = maxSize;
        chromosomes = new PriorityQueue<>();
    }

    public void add(Chromosome chromosome) {
        if (chromosomes.size() == maxSize) {
            throw new IllegalArgumentException("Over size.");
        }
        chromosomes.add(chromosome);
    }

    public void populate(City[] cities, Random random) {

        if (chromosomes.size() == maxSize) {
            throw new IllegalArgumentException("Over size.");
        }

        HashSet<Chromosome> hashSet = new HashSet<>();

        while (chromosomes.size() < maxSize) {
            Chromosome chromo = new Chromosome(cities, random);
            if (!hashSet.contains(chromo)) {
                hashSet.add(chromo);
                this.add(chromo);
            }
        }
    }

    public City[] getCities() {
        if (chromosomes.peek() == null) {
            throw new IllegalArgumentException("Peak is null");
        }
        return chromosomes.peek().getCitiesArray().clone();
    }

    public Chromosome[] getChromosomes() {
        Chromosome[] array = new Chromosome[chromosomes.size()];

        int i = 0;
        for (Chromosome chromo : chromosomes) {
            array[i++] = chromo;
        }

        return array;
    }

    public int size() {
        return chromosomes.size();
    }


    public int getAverageDistance() {

        int averageDistance = 0;

        for (Chromosome chromosome : chromosomes) {
            averageDistance += chromosome.getDistance();
        }

        return averageDistance / chromosomes.size();
    }

    public static Population fromDataSet(int popSize, Random r) {
        City[] cities = ImportData.getCities();
        Population population = new Population(popSize);
        population.populate(cities, r);
        return population;
    }

    public static Population getRandomPopulation(int numOfCities, int sizeOfPop, Random random) {
        City[] cities = new City[numOfCities];

        for (int i = 0; i < numOfCities; i++) {
            cities[i] = City.getRandomCity(random);
        }

        Population population = new Population(sizeOfPop);

        for (int i = 0; i < sizeOfPop; i++) {
            population.add(new Chromosome(cities, random));
        }

        return population;
    }

    // Get the Chromosome that has the path with the least distance.
    public Chromosome getMostFit() {
        return chromosomes.peek();
    }

    public Iterator<Chromosome> iterator() {
        return chromosomes.iterator();
    }

    public Population deepCopy() {
        Population population = new Population(maxSize);
        for (Chromosome chromosome : chromosomes) {
            population.add(chromosome);
        }
        return population;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Population:");

        for (Chromosome chromosome : chromosomes) {
            sb.append("\n");
            sb.append(chromosome);
            sb.append(" Value: ");
            sb.append(chromosome.getDistance());
        }

        return new String(sb);
    }

}
