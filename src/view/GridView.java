package view;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import util.Point3D;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * Represents the game grid displayed in game.
 */
public class GridView extends JPanel {

    private int xOffset;
    private int yOffset;

    private ArrayList<HexagonView> hexagons = new ArrayList<HexagonView>(); // List of hexagons to be displayed

    public GridView(int maxHexagons) {
        int panelWidth = (int) (maxHexagons * 3.0 / 2 * HexagonView.size);
        int panelHeight = (int) (maxHexagons * Math.sqrt(3) * HexagonView.size);
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setLayout(null); // We will manually set the position of the hexagons
        xOffset = getPreferredSize().width / 2;// Offset for centering the (0, 0)
        yOffset = getPreferredSize().height / 2; // Offset for centering the (0, 0)
    }

    public Point2D convertGridPositionToPixelPosition(Point3D gridPosition) {
        int q = gridPosition.x; // column index
        int r = gridPosition.y; // row index
        int size = HexagonView.size / 2; // size of the hexagon
        int pixelX = (int) (size * 3.0 / 2 * q) + xOffset;
        int pixelY = (int) (size * Math.sqrt(3) * (r + q / 2.0)) + yOffset;
        return new Point2D.Double(pixelX, pixelY);
    }

    public void addHexagon(HexagonView hexagon) {
        // Find the position of the hexagon in pixels
        Point2D position = convertGridPositionToPixelPosition(hexagon.getPosition());
        hexagon.setLocation((int) Math.round(position.getX()), (int) Math.round(position.getY()));
        hexagons.add(hexagon);
        // Add it to the jpanel
        this.add(hexagon);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // This is just a test to see if the grid view works
            GridView gridView = new GridView(60);
            // Activate hardware acceleration
            System.setProperty("sun.java2d.opengl", "true");
            ScrollableGridView scrollableGridView = new ScrollableGridView(gridView);
            // Add some hexagons to the grid view
            for (int i = 0; i < 60; i++) {
                // Testing the limits of the grid
                gridView.addHexagon(new QuarrieView(i, 0, 1));
            }
            JFrame frame = new JFrame();
            frame.getContentPane().setBackground(java.awt.Color.BLACK);
            frame.setPreferredSize(new Dimension(1500, 844));
            frame.setResizable(false);
            frame.add(scrollableGridView);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
