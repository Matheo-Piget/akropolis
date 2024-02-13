package view;

import model.Grid;
import model.Tile;
import util.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Panel for displaying the game board.
 */
public class BoardView extends JPanel {

    private Grid tileMap; // The grid representing the game board
    private final int hexSize = 40; // Arbitrary size for the hexagon
    private final int xOffset; // Offset for centering the (0, 0) coordinate
    private final int yOffset; // Offset for centering the (0, 0) coordinate

    /**
     * Constructs a new BoardView with the specified tile map.
     *
     * @param tileMap The grid representing the game board.
     */
    public BoardView(Grid tileMap) {
        this.tileMap = tileMap;
        setPreferredSize(new Dimension(1920, 1080)); // Arbitrary size for the example

        // Calculate offsets to center the (0, 0) coordinate
        xOffset = 500 - hexSize; // 500 is half the width of the drawing window
        yOffset = 400 - hexSize; // 400 is half the height of the drawing window
    }

    /**
     * Paints the component, rendering the game board.
     *
     * @param g The Graphics object to paint on.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Sort tiles by their elevation (z-coordinate)
        List<Map.Entry<Point3D, Tile>> sortedTiles = tileMap.getTiles().entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> -entry.getKey().z)) // Sort by descending z-coordinate
                .toList().reversed();

        // Draw each tile on the board, starting from the highest elevation
        for (Map.Entry<Point3D, Tile> entry : sortedTiles) {
            Tile tile = entry.getValue();
            Point3D position = tile.getPosition();

            int x = position.x * hexSize + xOffset;
            int y = -position.y * hexSize + yOffset;

            // Calculate vertical offset based on elevation
            int z = position.z;
            int verticalOffset = 0;
            if (z > 1) {
                verticalOffset = z * hexSize / 4;
                System.out.println("debug");// Adjust the value as needed
            }

            int offsetX = 0;
            if (position.x % 2 == 1 || position.x % 2 == -1) {
                offsetX = -hexSize / 4 * position.x;
                if (position.y > 0) {
                    y += hexSize / 2;
                } else {
                    y -= hexSize / 2;
                }
            } else {
                if (position.x > 0) {
                    x -= hexSize / 4 * position.x;
                } else {
                    x += hexSize / 4 * -position.x;
                }
            }

            if ((position.y <= 0 && position.x % 2 == 1) || (position.y <= 0 && position.x % 2 == -1)) y += hexSize;

            x += offsetX;

            y -= verticalOffset;

            // Draw the hexagon representing the tile
            drawHexagon(g, x, y, getTileColor(tile), tile);
        }
    }

    /**
     * Draws a hexagon on the specified Graphics object.
     *
     * @param g     The Graphics object to draw on.
     * @param x     The x-coordinate of the hexagon's center.
     * @param y     The y-coordinate of the hexagon's center.
     * @param color The color to fill the hexagon with.
     * @param t     The tile associated with the hexagon.
     */
    private void drawHexagon(Graphics g, int x, int y, Color color, Tile t) {
        int[] xPoints = {x + hexSize / 4, x + (hexSize * 3 / 4), x + hexSize, x + (hexSize * 3 / 4), x + hexSize / 4, x};
        int[] yPoints = {y + hexSize / 2, y + hexSize / 2, y, y - hexSize / 2, y - hexSize / 2, y};

        // Fill the hexagon with the tile color
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, 6);

        // Add shadow effect by drawing a darker gradient below the tile
        int shadowIntensity = 5; // Adjust the intensity of the shadow
        if (t.getElevation() > 1) {
            for (int i = 0; i < 6; i++) {
                // Only draw shadow for the bottom sides of the hexagon
                if (yPoints[i] >= y) {
                    int[] shadowXPoints = {xPoints[i], xPoints[(i + 1) % 6], xPoints[(i + 1) % 6], xPoints[i]};
                    int[] shadowYPoints = {yPoints[i], yPoints[(i + 1) % 6], yPoints[(i + 1) % 6] + shadowIntensity, yPoints[i] + shadowIntensity};
                    Color shadowColor = darkenColor(color, 0.5f); // Adjust the transparency and color of the shadow
                    g.setColor(shadowColor);
                    g.fillPolygon(shadowXPoints, shadowYPoints, 4);
                }
            }
        }

        // Draw the borders of the hexagon
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 6);

        // Draw position text on the hexagon
        g.setColor(Color.BLACK);
        g.setFont(new Font("default", Font.BOLD, 8));
        g.drawString(t.getPosition().toString(), x, y);
    }

    /**
     * Returns a darker version of the specified color.
     *
     * @param color The color to darken.
     * @param factor The factor by which to darken the color. Must be between 0.0 and 1.0.
     * @return The darkened color.
     */
    private Color darkenColor(Color color, float factor) {
        int r = Math.max((int) (color.getRed() * factor), 0);
        int g = Math.max((int) (color.getGreen() * factor), 0);
        int b = Math.max((int) (color.getBlue() * factor), 0);
        return new Color(r, g, b, color.getAlpha());
    }


    /**
     * Returns the color for the specified tile.
     *
     * @param tile The tile to determine the color for.
     * @return The color for the tile.
     */
    private Color getTileColor(Tile tile) {
        switch (tile.getType()) {
            case "Barrack Place", "Barrack" -> {
                return Color.RED;
            }
            case "Building Place", "Building" -> {
                return Color.BLUE;
            }
            case "Garden Place", "Garden" -> {
                return Color.GREEN;
            }
            case "Market Place", "Market" -> {
                return Color.YELLOW;
            }
            case "Temple Place", "Temple" -> {
                return Color.MAGENTA;
            }
            case "Quarrie" -> {
                return Color.GRAY;
            }
            default -> {
                return Color.WHITE;
            }
        }
    }

    /**
     * Main method for testing the BoardView.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Example usage
        Grid initialMap = new Grid();

        JFrame frame = new JFrame("Board View Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new BoardView(initialMap));
        frame.pack();
        frame.setVisible(true);

        System.out.println(initialMap.getTiles().size());

        initialMap.display(); // This line may be uncommented for testing purposes
    }
}
