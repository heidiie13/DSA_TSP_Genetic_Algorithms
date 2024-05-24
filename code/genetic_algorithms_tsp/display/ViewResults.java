package genetic_algorithms_tsp.display;

import javax.swing.*;
import java.awt.*;

// Draws GA results to the screen.
public class ViewResults extends JFrame {
    private final int WIDTH = 500;
    private final int HEIGHT = WIDTH / 10*9;
    private MainPanel mainPanel;
    private TextPanel textPanel;
    private final StringBuilder results;
    private final StringBuilder resultsName;

    public ViewResults(StringBuilder results, StringBuilder resultsName) {
        this.results = results;
        this.resultsName = resultsName;
        mainPanel = createPanel();
        textPanel = createTextPanel();
        setWindowProperties();
        add(textPanel);
        add(mainPanel);
    }

    private MainPanel createPanel() {
        mainPanel = new MainPanel(results);
        mainPanel.setBounds(0,0, WIDTH,150);
        return mainPanel;
    }

    private TextPanel createTextPanel() {
        textPanel = new TextPanel(resultsName);
        textPanel.setBounds(10,130, WIDTH - 15,HEIGHT);
        return textPanel;
    }
    private void setWindowProperties () {
        setLocation(1020, 9 + WIDTH / 16 * 9);
        setResizable(false);
        setSize(WIDTH + 15, HEIGHT);
        setTitle("Genetic Algorithm Results");
        setLayout(null);
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
            int y = 35;
            String[] lines = text.toString().split("\n");
            for (String line : lines) {
                graphics.drawString(line, x, y);
                y += graphics.getFontMetrics().getHeight();
            }
        }
    }

    private static class TextPanel extends JPanel {
        private final JTextArea textArea;

        public TextPanel(StringBuilder text) {
            textArea = new JTextArea();
            textArea.setText(text.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Arial", Font.PLAIN, 16));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(450, 230));
            add(scrollPane);
        }
    }
}
