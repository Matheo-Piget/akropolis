package view;

import model.Grid;
import model.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Panel for displaying the game board.
 */
public class BoardView extends JPanel {

    private Grid tileMap;

    /**
     * Constructs a new BoardView with the specified tile map.
     *
     * @param tileMap The grid representing the game board.
     */
    public BoardView(Grid tileMap) {
        this.tileMap = tileMap;
        setPreferredSize(new Dimension(1700, 1300)); // Arbitrary size for the example
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw each tile on the board
        for (Map.Entry<Point, List<Tile>> entry : tileMap.getTiles().entrySet()) {
            Point point = entry.getKey();
            List<Tile> tiles = entry.getValue();

            // Draw each tile from the list
            for (Tile tile : tiles) {
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
        }
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
