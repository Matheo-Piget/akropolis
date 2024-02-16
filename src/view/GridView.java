package view;

import javax.swing.JPanel;

import model.DistrictColor;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.Graphics2D;
import util.Point3D;
import java.util.ArrayList;
import java.awt.Graphics;
import javax.swing.JFrame;

/**
 * Represents the game grid displayed in game.
 */
public class GridView extends JPanel {
    private int hexSize = 40; // Size of the hexagons
    private Dimension panelSize = new Dimension(1500, 900); // Size of the panel

    private int xOffset = (int) (panelSize.getWidth() / 2 - hexSize); // Offset for centering the (0, 0) coordinate
    private int yOffset = (int) (panelSize.getHeight() / 2 - hexSize); // Offset for centering the (0, 0) coordinate

    private ArrayList<HexagonView> hexagons = new ArrayList<HexagonView>(); // List of hexagons to be displayed

    public GridView() {
        setPreferredSize(panelSize);
    }

    public Point2D convertGridPositionToPixelPosition(Point3D gridPosition) {
        int q = gridPosition.x; // column index
        int r = gridPosition.y; // row index
        int size = 40; // size of the hexagon
        int xOffset = 1500 / 2;
        int yOffset = 900 / 2;
        int pixelX = (int) (size * 3.0 / 2 * q) + xOffset;
        int pixelY = (int) (size * Math.sqrt(3) * (r + q / 2.0)) + yOffset;
        return new Point2D.Double(pixelX, pixelY);
    }

    // Not sure if the paint works but it gives an idea of how to draw the hexagons
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // We need to iterate over the hexagons, getting their position relative to the
        // grid
        // Then we need to convert that position to a pixel position and draw the
        // hexagon there
        for (HexagonView hexagon : hexagons) {
            Point3D position = hexagon.getPosition();
            Point2D pixelPosition = convertGridPositionToPixelPosition(position);
            g2d.translate(pixelPosition.getX(), pixelPosition.getY());
            hexagon.paint(g2d);
            g2d.translate(-pixelPosition.getX(), -pixelPosition.getY());
        }
    }

    public static void main(String[] args) {
        // This is just a test to see if the grid view works
        GridView gridView = new GridView();
        QuarrieView test = new QuarrieView(0, 0, 40);
        test.setPosition(new Point3D(0, 0, 1));
        QuarrieView test2 = new QuarrieView(1, 0, 40);
        test2.setPosition(new Point3D(1, 0, 1));
        QuarrieView test3 = new QuarrieView(1, 1, 40);
        test3.setPosition(new Point3D(1, 1, 1));
        QuarrieView test4 = new QuarrieView(0, 1, 40);
        test4.setPosition(new Point3D(0, 1, 1));
        QuarrieView test5 = new QuarrieView(0, -1, 40);
        test5.setPosition(new Point3D(0, -1, 1));
        gridView.hexagons.add(test2);
        gridView.hexagons.add(test);
        gridView.hexagons.add(test3);
        gridView.hexagons.add(test4);
        gridView.hexagons.add(test5);
        JFrame frame = new JFrame();
        frame.add(gridView);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
