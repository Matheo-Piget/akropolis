package view;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.Point;
import util.Point3D;
import java.awt.Component;
import java.util.HashMap;

/**
 * Represents the game grid displayed in game.
 */
public class GridView extends JPanel {

    private int xOffset;
    private int yOffset;
    public static int hexagonSize = 80;

    private HashMap<Point, HexagonView> hexagons = new HashMap<>();

    public GridView(int maxHexagons) {
        setDoubleBuffered(true);
        int panelWidth = (int) (maxHexagons * 3.0 / 2 * hexagonSize);

        int panelHeight = (int) (maxHexagons * Math.sqrt(3) * hexagonSize);
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setLayout(null); // We will manually set the position of the hexagons
        xOffset = getPreferredSize().width / 2;// Offset for centering the (0, 0)
        yOffset = getPreferredSize().height / 2; // Offset for centering the (0, 0)
    }

    public Point2D convertGridPositionToPixelPosition(Point gridPosition) {
        int q = gridPosition.x; // column index
        int r = gridPosition.y; // row index
        int size = hexagonSize / 2; // size of the hexagon
        int pixelX = (int) (size * 3.0 / 2 * q) + xOffset;
        int pixelY = (int) (size * Math.sqrt(3) * (r + q / 2.0)) + yOffset;
        return new Point2D.Double(pixelX, pixelY);
    }

    /**
     * Get the hexagon at the given pixel position
     * Be careful, it will return null if there is no hexagon at the given mouse position
     * @param pixelPosition
     * @return
     */
    public HexagonView getHexagonAtPixelPos(Point2D pixelPosition) {
        // We just need to use getComponentAt to get the hexagon at the pixel position
        Component component = getComponentAt((int) pixelPosition.getX(), (int) pixelPosition.getY());
        if (component instanceof HexagonView) {
            return (HexagonView) component;
        }
        return null;
    }

    public HexagonView getHexagonAtGridPos(Point gridPosition) {
        return hexagons.get(new Point(gridPosition.x, gridPosition.y));
    }

    public HexagonView getHexagonAtGridPos(int x, int y) {
        return hexagons.get(new Point(x, y));
    }

    // on obtient les coordonne qui peuveut etre sur n'importe quel partie de
    // l'hexagone mais faut savoir à quel hexagone appartient
    public Point2D convertPixelPositionToGridPosition(Point2D pixelPosition) {
        int size = hexagonSize / 2;
        double x = (pixelPosition.getX() - xOffset) / size;
        double y = (pixelPosition.getY() - yOffset) / size;

        double q = (2.0 / 3 * x);
        double r = (-1.0 / 3 * x + Math.sqrt(3) / 3 * y);

        return axialRound(q, r);// determiner dans quel hexagone se trouve un point donné et renvoyer les
                                // coordonné de cette hexagone

    }

    // determiner dans quel hexagone se trouve un point donné et renvoyer les
    // coordonné de cette hexagone
    private Point3D axialRound(double q, double r) {
        double s = -q - r;
        double rq = Math.round(q);
        double rr = Math.round(r);
        double rs = Math.round(s);

        double qDiff = Math.abs(rq - q);
        double rDiff = Math.abs(rr - r);
        double sDiff = Math.abs(rs - s);

        if (qDiff > rDiff && qDiff > sDiff) {
            rq = -rr - rs;
        } else if (rDiff > sDiff) {
            rr = -rq - rs;
        }
        System.out.println("Coordonnées de grille calculées: q=" + q + ", r=" + r);
        return new Point3D((int) rq, (int) rr, 0);
    }

    public void addHexagon(HexagonView hexagon) {
        boolean contains = hexagons.containsKey(hexagon.getPosition());
        if(contains && hexagon.z == 0){
            // It's an outline that will overlap
            return;
        }
        // Find the position of the hexagon in pixels
        Point2D position = convertGridPositionToPixelPosition(hexagon.getPosition());
        // If there is already a hexagon with the same x and y, remove it
        if (contains) {
            this.remove(hexagons.get(hexagon.getPosition()));
            hexagons.remove(hexagon.getPosition());
        }
        hexagon.setLocation((int) Math.round(position.getX()), (int) Math.round(position.getY()));
        hexagons.put(hexagon.getPosition(), hexagon);
        // Add it to the jpanel
        this.add(hexagon);
        // Repaint the area where the hexagon is
        this.repaint(hexagon.getBounds());
    }
}
