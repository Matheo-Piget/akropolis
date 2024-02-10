package view;

import model.Grid;
import model.Tile;
import util.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Panel for displaying the game board.
 */
public class BoardView extends JPanel {

    private Grid tileMap;
    private final int hexSize = 40; // Taille arbitraire pour la taille de l'hexagone
    private final int xOffset;
    private final int yOffset;

    /**
     * Constructs a new BoardView with the specified tile map.
     *
     * @param tileMap The grid representing the game board.
     */
    public BoardView(Grid tileMap) {
        this.tileMap = tileMap;
        setPreferredSize(new Dimension(1000, 800)); // Taille arbitraire pour l'exemple

        // Calculer les décalages pour centrer la coordonnée (0, 0)
        xOffset = 500 - hexSize; // 500 est la moitié de la largeur de la fenêtre de dessin
        yOffset = 400 - hexSize; // 400 est la moitié de la hauteur de la fenêtre de dessin
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw each tile on the board
        for (Map.Entry<Point3D, Tile> entry : tileMap.getTiles().entrySet()) {
            Tile tile = entry.getValue();
            Point3D position = entry.getKey();

            int x = position.x * hexSize + xOffset;
            int y = -position.y * hexSize + yOffset;

            if(position.x % 2 == 1 && position.y > 0) {
                y += hexSize / 2;
                x -= (hexSize / 4)*position.x;
            }
            if(position.x % 2 == 1 && position.y <= 0) {
                y -= hexSize / 2;
                x -= (hexSize / 4) * position.x;
            }
            if(position.x %2 == 0 && position.x > 0) {
                x -= (hexSize / 4)*position.x;
            }
            if(position.x % 2 == -1 && position.y > 0) {
                y += hexSize / 2;
                x += (hexSize / 4)*-position.x;
            }

            if(position.x % 2 == -1 && position.y <= 0) {
                y -= hexSize / 2;
                x += (hexSize / 4)*-position.x;
            }
            if(position.x %2 == 0 && position.x < 0) {
                x += (hexSize / 4)*-position.x;
            }

            // Draw the hexagon representing the tile
            drawHexagon(g, x, y, getTileColor(tile));
        }
    }

    /**
     * Draws a hexagon on the specified Graphics object.
     *
     * @param g     The Graphics object to draw on.
     * @param x     The x-coordinate of the hexagon's center.
     * @param y     The y-coordinate of the hexagon's center.
     * @param color The color to fill the hexagon with.
     */
    private void drawHexagon(Graphics g, int x, int y, Color color) {
        int[] xPoints = {x + hexSize / 4, x + (hexSize * 3 / 4), x + hexSize, x + (hexSize * 3 / 4), x + hexSize / 4, x};
        int[] yPoints = {y + hexSize / 2, y + hexSize / 2, y, y - hexSize / 2, y - hexSize / 2, y};
        g.setColor(color);
        g.fillPolygon(xPoints, yPoints, 6);
        g.setColor(Color.BLACK); // Couleur des bordures
        g.drawPolygon(xPoints, yPoints, 6); // bordures
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

    public static void main(String[] args) {
        // Example usage
        Grid initialMap = new Grid();

        JFrame frame = new JFrame("Board View Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new BoardView(initialMap));
        frame.pack();
        frame.setVisible(true);

        initialMap.display();
    }
}
