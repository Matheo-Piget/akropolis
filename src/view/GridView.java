package view;

import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
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
        setPreferredSize(new Dimension(maxHexagons * HexagonView.size, maxHexagons * HexagonView.size));
        this.setLayout(null); // We will manually set the position of the hexagons
        xOffset = (int) (maxHexagons * HexagonView.size / 2 - HexagonView.size); // Offset for centering the (0, 0) coordinate
        yOffset = (int) (maxHexagons * HexagonView.size / 2 - HexagonView.size); // Offset for centering the (0, 0) coordinate

        // Create a JViewport
        JViewport viewport = new JViewport();
        viewport.setView(this);
        viewport.setPreferredSize(new Dimension(1500, 900)); // Set the size of the viewport

        // Add mouse listeners to make the view movable
        MouseAdapter ma = new MouseAdapter() {
            private Point origin;

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    origin = new Point(e.getPoint());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (origin != null) {
                    JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, GridView.this);
                    if (viewPort != null) {
                        int deltaX = origin.x - e.getX();
                        int deltaY = origin.y - e.getY();

                        Rectangle view = viewPort.getViewRect();
                        view.x += deltaX;
                        view.y += deltaY;

                        GridView.this.scrollRectToVisible(view);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                origin = null;
            }
        };

        addMouseListener(ma);
        addMouseMotionListener(ma);
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
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // This is just a test to see if the grid view works
            GridView gridView = new GridView(60);
            QuarrieView test = new QuarrieView(0, 0, 1);
            QuarrieView test2 = new QuarrieView(1, 0, 1);
            QuarrieView test3 = new QuarrieView(1, 1, 1);
            QuarrieView test4 = new QuarrieView(0, 1, 1);
            QuarrieView test5 = new QuarrieView(0, -1, 1);
            QuarrieView test6 = new QuarrieView(1, -1, 2);
            gridView.addHexagon(test);
            gridView.addHexagon(test2);
            gridView.addHexagon(test3);
            gridView.addHexagon(test4);
            gridView.addHexagon(test5);
            gridView.addHexagon(test6);

            TileView tile = new TileView(new QuarrieView(3,3,1), new QuarrieView(4,3,1), new QuarrieView(3,4,1));
            tile.setLocation(0, 0);
            gridView.add(tile);
            ScrollableGridView scrollableGridView = new ScrollableGridView(gridView);
            JFrame frame = new JFrame();
            frame.add(scrollableGridView);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
