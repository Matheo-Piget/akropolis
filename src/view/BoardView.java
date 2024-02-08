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
        for (Map.Entry<Point, List<Tile>> entry : tileMap.getTiles().entrySet()) {
            Point point = entry.getKey();
            List<Tile> tiles = entry.getValue();

            // Dessiner chaque tuile à partir de la liste
            for (Tile tile : tiles) {
                int x = point.x%2 == 0 ? point.x * hexSize + xOffset : point.x * hexSize - hexSize / 4 + xOffset;
                int y = point.x%2 == 0 ? -point.y * hexSize + yOffset : -point.y * hexSize + hexSize / 2 + yOffset;


                if(point.x < 0){ x += hexSize/2;}
                else if (point.x > 1){ x -= hexSize/2;}



                // Dessiner l'hexagone représentant la tuile
                g.setColor(getTileColor(tile));
                int[] xPoints = {x + hexSize / 4, x + (hexSize * 3 / 4), x + hexSize, x + (hexSize * 3 / 4), x + hexSize / 4, x};
                int[] yPoints = {y + hexSize / 2, y + hexSize / 2, y, y - hexSize / 2, y - hexSize / 2, y};
                g.fillPolygon(xPoints, yPoints, 6);

                // Dessiner le contour autour de l'hexagone
                g.setColor(Color.BLACK);
                g.drawPolygon(xPoints, yPoints, 6);

            }
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
