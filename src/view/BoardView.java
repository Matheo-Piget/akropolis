package view;

import model.Grid;
import model.Tile;
import util.Point3D;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Panel for displaying the game board.
 */
public class BoardView extends JPanel {

    private Grid tileMap;
    private final int hexSize = 50; // Taille arbitraire pour la taille de l'hexagone
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
            Point3D point = entry.getKey();
            Tile tile = entry.getValue();

            // Draw each tile from the list
            int x = point.x * 50; // Arbitrary size for the example
            int y = point.y * 50; // Arbitrary size for the example

            // Draw the tile based on its type and level
            switch (tile.getType()) {
                case "StartingTile":
                    g.setColor(Color.RED);
                    break;
                case "Type2":
                    g.setColor(Color.BLUE);
                    break;
                default:
                    g.setColor(Color.GREEN);
                    break;
            }

            // Draw the hexagon representing the tile
            int[] xPoints = {x, x + 50, x + 75, x + 50, x, x - 25};
            int[] yPoints = {y + 25, y, y + 25, y + 50, y + 75, y + 50};
            g.fillPolygon(xPoints, yPoints, 6);
        }

        tileMap.display();
    }

    /**
     * Updates the displayed map with a new tile map.
     *
     * @param newTileMap The new grid representing the updated game board.
     */
    public void updateMap(Grid newTileMap) {
        this.tileMap = newTileMap;
        repaint(); // Redraw the map with the new data
    }

    /**
     * Returns the color for the specified tile.
     *
     * @param tile The tile to determine the color for.
     * @return The color for the tile.
     */
    private Color getTileColor(Tile tile) {
        return switch (tile.getType()) {
            case "StartingTile" -> Color.RED;
            case "Quarrie" -> Color.BLUE;
            default -> Color.GREEN;
        };
    }

    public static void main(String[] args) {
        // Example usage
        Grid initialMap = new Grid();

        JFrame frame = new JFrame("Board View Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new BoardView(initialMap));
        frame.pack();
        frame.setVisible(true);
    }
}
