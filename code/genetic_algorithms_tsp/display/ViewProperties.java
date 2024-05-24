package genetic_algorithms_tsp.display;

import javax.swing.*;
import java.awt.*;

// Draws GA properties to the screen.
public class ViewProperties extends JFrame {
    private final int WIDTH = 500;
    private final int HEIGHT = WIDTH / 18*9;
    private final JPanel mainPanel;
    private final StringBuilder results;

    public ViewProperties(StringBuilder results) {
        this.results = results;
        mainPanel = createPanel();
        add(mainPanel);
        setWindowProperties();
    }

    private MainPanel createPanel() {
        MainPanel panel = new MainPanel(results);
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        return panel;
    }
    private void setWindowProperties () {
        setLocation(1020, 15);
        setResizable(false);
        pack();
        setTitle("Genetic Algorithm Properties");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private static class MainPanel extends JPanel {
        StringBuilder text;

        public MainPanel(StringBuilder text) {
            this.text = text;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            drawText((Graphics2D) graphics);
        }

        private void drawText(Graphics2D graphics) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Font font = new Font("Arial", Font.PLAIN, 16);

            graphics.setFont(font);
            graphics.setColor(Color.BLACK);

            int x = 80;
            int y = 30;
            String[] lines = text.toString().split("\n");
            for (String line : lines) {
                graphics.drawString(line, x, y);
                y += graphics.getFontMetrics().getHeight();
            }
        }
    }
}
