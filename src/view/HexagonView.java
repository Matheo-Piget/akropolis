package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import util.Point3D;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Represents an hexagon on the game grid.
 */
public abstract class HexagonView extends JComponent {

    public static int size = 80; // Size of the hexagons
    protected boolean isHovered = false;
    protected TexturePaint texture;
    protected Point3D position;
    protected Polygon hexagon = new Polygon();

    public HexagonView(int x, int y, int z) {
        this.setSize(size, size);
        // Calculate the coordinates of the six points of the hexagon
        this.position = new Point3D(x, y, z);
        int center = size / 2;
        for (int i = 0; i < 6; i++) {
            int xval = (int) (center + center * Math.cos(i * 2 * Math.PI / 6));
            int yval = (int) (center + center * Math.sin(i * 2 * Math.PI / 6));
            hexagon.addPoint(xval, yval);
        }
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    public HexagonView(int x, int y, int z, BufferedImage img){
        this.setSize(size, size);
        this.position = new Point3D(x, y, z);
        int center = size / 2;
        for (int i = 0; i < 6; i++) {
            int xval = (int) (center + center * Math.cos(i * 2 * Math.PI / 6));
            int yval = (int) (center + center * Math.sin(i * 2 * Math.PI / 6));
            hexagon.addPoint(xval, yval);
        }
        this.texture = new TexturePaint(img, new java.awt.Rectangle(0, 0, size, size));
        // Add the mouse motion listener to change the color of the hexagon when hovered
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                isHovered = contains(e.getX(), e.getY());
                repaint();
            }
        });
    }

    public HexagonView(int x, int y, int z, Color color) {
        this.setSize(size, size);
        this.position = new Point3D(x, y, z);
        int center = (size / 2) - 1;
        for (int i = 0; i < 6; i++) {
            int xval = (int) (center + center * Math.cos(i * 2 * Math.PI / 6));
            int yval = (int) (center + center * Math.sin(i * 2 * Math.PI / 6));
            hexagon.addPoint(xval, yval);
        }
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });

        // Create a BufferedImage and fill it with the color
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(darken(color, z));
        g.fillRect(0, 0, 1, 1);
        g.dispose();
        this.texture = new TexturePaint(img, new java.awt.Rectangle(x, y, size, size));
    }

    public Polygon getPolygon() {
        return hexagon;
    }

    public TexturePaint getTexture() {
        return texture;
    }

    public ArrayList<Point> getPoints() {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            points.add(new Point(hexagon.xpoints[i], hexagon.ypoints[i]));
        }
        return points;
    }

    public Polygon setPolygon(Polygon hexagon) {
        return this.hexagon = hexagon;
    }

    @Override
    public boolean contains(int x, int y) {
        return hexagon.contains(x, y);
    }

    public void setPosition(Point3D position) {
        this.position = position;
    }

    public Point3D getPosition() {
        return this.position;
    }

    private Color darken(Color c, int z){
        for(int i = 0; i < z; i++){
            c = c.darker();
        }
        return c;
    }

    public TexturePaint darkenTexturePaint(TexturePaint texture, int floor) {
        BufferedImage img = texture.getImage();
        BufferedImage darkImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color color = new Color(img.getRGB(i, j), true);
                for (int k = 0; k < floor; k++) {
                    color = color.darker();
                }
                darkImg.setRGB(i, j, color.getRGB());
            }
        }
        return new TexturePaint(darkImg, texture.getAnchorRect());
    }
}
