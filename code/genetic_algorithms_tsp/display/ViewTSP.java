package genetic_algorithms_tsp.display;

import genetic_algorithms_tsp.genetic_objects.Chromosome;
import genetic_algorithms_tsp.genetic_objects.City;

import javax.swing.*;
import java.awt.*;

public class ViewTSP extends JFrame {

    private final int WIDTH = 1000;
    private final int HEIGHT = WIDTH / 13*9;
    private final int OFFSET = 40;
    private final int CITY_SIZE = 5;

    private final MainPanel panel;
    private final City[] cities;
    private Chromosome chromosome;
    private int maxX, maxY;
    private double scaleX, scaleY;

    public ViewTSP(City[] cities) {
        this.cities = cities;
        setScale();
        panel = createPanel();
        add(panel);
        setWindowProperties();
    }

    public void draw (Chromosome chromosome) {
        this.chromosome = chromosome;
        panel.repaint();
    }

    private MainPanel createPanel () {
        MainPanel panel = new MainPanel();
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        return panel;
    }

    private void setWindowProperties () {
        setLocation(20, 15);
        setResizable(false);
        pack();
        setTitle("Traveling Salesman Problem");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setScale () {
        for (City c : cities) {
            if (c.getX() > maxX) {
                maxX = c.getX();
            }
            if (c.getY() > maxY) {
                maxY = c.getY();
            }
        }
        scaleX = ((double)maxX) / ((double)WIDTH- OFFSET);
        scaleY = ((double)maxY) / ((double)HEIGHT- OFFSET);
    }

    private class MainPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            paintTravelingSalesman((Graphics2D)graphics);
        }

        private void paintTravelingSalesman (Graphics2D graphics) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            paintCityNames(graphics);
            if (chromosome != null) {
                paintChromosome(graphics);
            }
            paintCities(graphics);
        }


        private void paintChromosome(Graphics2D graphics) {
            graphics.setColor(Color.darkGray);
            City[] array = chromosome.getCitiesArray();

            for (int i = 1; i < array.length; i++) {
                int x1 = (int) (array[i - 1].getX() / scaleX + OFFSET / 2);
                int y1 = (int) (array[i - 1].getY() / scaleY + OFFSET / 2);
                int x2 = (int) (array[i].getX() / scaleX + OFFSET / 2);
                int y2 = (int) (array[i].getY() / scaleY + OFFSET / 2);

                graphics.drawLine(x1, y1, x2, y2);
            }

            int x1 = (int) (array[0].getX() / scaleX + OFFSET / 2);
            int y1 = (int) (array[0].getY() / scaleY + OFFSET / 2);
            int x2 = (int) (array[array.length - 1].getX() / scaleX + OFFSET / 2);
            int y2 = (int) (array[array.length - 1].getY() / scaleY + OFFSET / 2);

            graphics.drawLine(x1, y1, x2, y2);
        }

        private void paintCities (Graphics2D graphics) {
            graphics.setColor(Color.darkGray);
            for (City c : cities) {
                int x = (int)((c.getX()) / scaleX - CITY_SIZE/2 + OFFSET / 2);
                int y = (int)((c.getY()) / scaleY - CITY_SIZE/2 + OFFSET / 2);
                graphics.fillOval(x, y, CITY_SIZE, CITY_SIZE);
            }
        }

        private void paintCityNames (Graphics2D graphics) {
            graphics.setColor(new Color(185, 69, 69));
            for (City c : cities) {
                int x = (int)((c.getX()) / scaleX - CITY_SIZE/2 + OFFSET/2);
                int y = (int)((c.getY()) / scaleY - CITY_SIZE/2 + OFFSET/2);
                graphics.fillOval(x, y, CITY_SIZE, CITY_SIZE);

                Font originalFont = graphics.getFont();
                Font newFont = originalFont.deriveFont(10.0f);
                graphics.setFont(newFont);
                int fontOffset = getFontMetrics(graphics.getFont()).stringWidth(c.getName())/2-2;
                graphics.drawString(c.getName(), x-fontOffset, y-2);
            }
        }
    }
}
