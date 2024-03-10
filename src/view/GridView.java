package view;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.geom.Point2D;

import model.Hexagon;
import util.Point3D;
import java.util.ArrayList;
import java.util.Map;
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

        MouseAdapter ms = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) { // Check if left button was clicked
                    Point2D pixelPosition = new Point2D.Double(e.getX(), e.getY());
                    System.out.println("Clic en coordonnées de pixel : " + pixelPosition);
                    Point2D gridPosition = convertPixelPositionToGridPosition(pixelPosition);
                    System.out.println("Clic en coordonnées de grille : " + gridPosition);
                }
            }
        };
        addMouseListener(ms);
    }

    public Point2D convertGridPositionToPixelPosition(Point3D gridPosition) {
        int q = gridPosition.x; // column index
        int r = gridPosition.y; // row index
        int size = HexagonView.size / 2; // size of the hexagon
        int pixelX = (int) (size * 3.0 / 2 * q) + xOffset;
        int pixelY = (int) (size * Math.sqrt(3) * (r + q / 2.0)) + yOffset;
        return new Point2D.Double(pixelX, pixelY);
    }
    
    //on obtient les coordonne qui peuveut etre sur n'importe quel partie de l'hexagone mais faut savoir à quel hexagone appartient 
    public Point2D convertPixelPositionToGridPosition(Point2D pixelPosition) { 
        int size = HexagonView.size / 2;
        double x = (pixelPosition.getX() - xOffset) / size;
        double y = (pixelPosition.getY() - yOffset) / size;

        double q = (2.0 / 3 * x);
        double r = (-1.0 / 3 * x + Math.sqrt(3) / 3 * y);

        return axialRound(q, r);//determiner dans quel hexagone se trouve un point donné et renvoyer les coordonné de cette hexagone
    
    }
    //determiner dans quel hexagone se trouve un point donné et renvoyer les coordonné de cette hexagone
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
        return new Point3D((int)rq, (int)rr, 0);
    }
    
    public void addHexagon(HexagonView hexagon) {
        // Find the position of the hexagon in pixels
        Point2D position = convertGridPositionToPixelPosition(hexagon.getPosition());
        // If there is already a hexagon with the same x and y, remove it
        for (HexagonView h : hexagons) {
            if (h.getPosition().x == hexagon.getPosition().x && h.getPosition().y == hexagon.getPosition().y) {
                hexagons.remove(h);
                this.remove(h);
                break;
            }
        }
        hexagon.setLocation((int) Math.round(position.getX()), (int) Math.round(position.getY()));
        hexagons.add(hexagon);
        // Add it to the jpanel
        this.add(hexagon);
    }
}
