package genetic_algorithms_tsp.genetic_objects;

import java.util.Random;

public class City {

    private final String name;
    private final int x;
    private final int y;

    public City(String name, int x, int y) {
        this.name = name;
        this.x = x; // x coordinate
        this.y = y; // y coordinate
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static City getRandomCity(Random random) {
        String name = getRandomName(random);
        int x = random.nextInt(500);
        int y = random.nextInt(500);
        return new City(name, x, y);
    }

    private static String getRandomName(Random random) {
        int[] name = new int[random.nextInt(5) + 3];
        for (int i = 0; i < name.length; i++) {
            name[i] = random.nextInt(26) + 65;
        }

        StringBuilder sb = new StringBuilder();
        for (int i : name) {
            sb.append((char) i);
        }

        return new String(sb);
    }

    // Finds the Euclidean distance between two cities.
    public static double distance(City city1, City city2) {
        int xDiff = city2.getX() - city1.getX();
        int yDiff = city2.getY() - city1.getY();

        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        City city = (City) o;

        if (this.x != city.x) return false;
        if (this.y != city.y) return false;

        return name.equals(city.name);
    }

    @Override
    public String toString() {
        return name + " (" + x + ", " + y + ")";
    }
}
