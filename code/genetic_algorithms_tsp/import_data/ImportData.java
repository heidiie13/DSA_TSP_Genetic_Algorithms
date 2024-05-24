package genetic_algorithms_tsp.import_data;

import genetic_algorithms_tsp.genetic_objects.City;

import java.io.*;

public class ImportData {

    public static City[] getCities() {

        String filePath = "dataset/bier127.tsp"; // change file path data, for example: "dataset/bier127.tsp" -> "dataset/att48.tsp"
        int startingLine = 6;

        String[] lines = read(filePath).split("\n");
        String[] words = lines[3].split(" ");
        int numOfCities = Integer.parseInt(words[words.length - 1]);
        City[] cities = new City[numOfCities];

        for (int i = startingLine; i < startingLine + numOfCities; i++) {
            String[] line = removeWhiteSpace(lines[i]).trim().split(" ");
            int x = (int) Double.parseDouble(line[1].trim());
            int y = (int) Double.parseDouble(line[2].trim());
            City city = new City(line[0], x, y);
            cities[i - startingLine] = city;
        }
        return cities;
    }

    private static String removeWhiteSpace(String s) {
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == ' ' && s.charAt(i - 1) == ' ') {
                s = s.substring(0, i) + s.substring(i + 1);
                i--;
            }
        }
        return s;
    }

    private static String read(String fileName) {
        InputStream stream = ImportData.class.getResourceAsStream(fileName);
        assert stream != null;
        java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
