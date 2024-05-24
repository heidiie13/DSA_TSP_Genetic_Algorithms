package genetic_algorithms_tsp.genetic_algorithm.genetic_operators;

import genetic_algorithms_tsp.genetic_objects.Chromosome;
import genetic_algorithms_tsp.genetic_objects.City;

import java.util.Random;

public class Mutation {

    public static Chromosome insertion(Chromosome chromosome, Random random) {
        City[] cities = chromosome.getCitiesArray();
        int randomIndex = random.nextInt(cities.length);
        int randomDestination = random.nextInt(cities.length);

        City temp = cities[randomIndex];
        if (randomIndex < randomDestination) {
            for (int i = randomIndex; i < randomDestination; i++) {
                cities[i] = cities[i + 1];
            }
        } else {
            for (int i = randomIndex; i > randomDestination; i--) {
                cities[i] = cities[i - 1];
            }
        }
        cities[randomDestination] = temp;
        return new Chromosome(cities);
    }

    public static Chromosome reciprocalExchange(Chromosome chromosome, Random random) {
        City[] cities = chromosome.getCitiesArray();
        int l = cities.length;
        swap(cities, random.nextInt(l), random.nextInt(l));
        return new Chromosome(cities);
    }

    public static Chromosome scrambleMutation(Chromosome chromosome, Random random) {

        City[] cities = chromosome.getCitiesArray();
        int randomIndexStart = random.nextInt(cities.length);
        int randomIndexEnd = random.nextInt(cities.length);

        if (randomIndexStart > randomIndexEnd) {
            int temp = randomIndexStart;
            randomIndexStart = randomIndexEnd;
            randomIndexEnd = temp;
        }

        for (int i = randomIndexStart; i <= randomIndexEnd; i++) {
            int r = random.nextInt(randomIndexEnd - i + 1);
            swap(cities, i, i + r);
        }

        return new Chromosome(cities);
    }

    private static void swap(City[] array, int i, int j) {
        City temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
