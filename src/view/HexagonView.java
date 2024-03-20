package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import util.Point3D;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

/**
 * Represents an hexagon on the game grid.
 */
public abstract class HexagonView extends JComponent {

    public static int size = 80; // Size of the hexagons
    protected boolean isHovered = false;
    protected TexturePaint texture;
    protected Point pos = new Point(0, 0);
    protected int z;
    protected Path2D.Double hexagon = new Path2D.Double();

    public HexagonView(int x, int y, int z) {
        this.setSize(size, size);
        setOpaque(false);
        // Calculate the coordinates of the six points of the hexagon
        pos = new Point(x, y);
        this.z = z;
        int center = size / 2 - 1; // Fuck you
        for (int i = 0; i < 6; i++) {
            double xval = center + center * Math.cos(i * 2 * Math.PI / 6);
            double yval = center + center * Math.sin(i * 2 * Math.PI / 6);
            if(i == 0){
                hexagon.moveTo(xval, yval);
            }
            else{
                hexagon.lineTo(xval, yval);
            }
        }
        hexagon.closePath();
        MouseAdapter ma = new MouseAdapter() {
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

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                // Do nothing
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                // Do nothing
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                // Do nothing
            }

        };
        this.addMouseListener(ma);
        this.addMouseMotionListener(ma);
    }

    public HexagonView(int x, int y, int z, BufferedImage img) {
        this.setSize(size, size);
        pos = new Point(x, y);
        this.z = z;
        int center = size / 2 - 1; // Dumb rounding
        for (int i = 0; i < 6; i++) {
            double xval = center + center * Math.cos(i * 2 * Math.PI / 6);
            double yval = center + center * Math.sin(i * 2 * Math.PI / 6);
            if(i == 0){
                hexagon.moveTo(xval, yval);
            }
            else{
                hexagon.lineTo(xval, yval);
            }
        }
        hexagon.closePath();
        this.texture = new TexturePaint(img, new java.awt.Rectangle(0, 0, size, size));
        MouseAdapter ma = new MouseAdapter() {
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

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                // Do nothing
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                // Do nothing
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                // Do nothing
            }

        };
        this.addMouseListener(ma);
        this.addMouseMotionListener(ma);
    }

    public HexagonView(int x, int y, int z, Color color) {
        this.setSize(size, size);
        pos = new Point(x, y);
        this.z = z;
        int center = size / 2 - 1; // Ligma balls
        for (int i = 0; i < 6; i++) {
            double xval = center + center * Math.cos(i * 2 * Math.PI / 6);
            double yval = center + center * Math.sin(i * 2 * Math.PI / 6);
            if(i == 0){
                hexagon.moveTo(xval, yval);
            }
            else{
                hexagon.lineTo(xval, yval);
            }
        }
        hexagon.closePath();
        MouseAdapter ma = new MouseAdapter() {
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

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                // Do nothing
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                // Do nothing
            }

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                // Do nothing
            }

        };
        this.addMouseListener(ma);
        this.addMouseMotionListener(ma);

        // Create a BufferedImage and fill it with the color
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(darken(color, z));
        g.fillRect(0, 0, 1, 1);
        g.dispose();
        this.texture = new TexturePaint(img, new java.awt.Rectangle(x, y, size, size));
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        getParent().dispatchEvent(SwingUtilities.convertMouseEvent(this, e, getParent()));
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);
        getParent().dispatchEvent(SwingUtilities.convertMouseEvent(this, e, getParent()));
    }

    /**
     * Copy the hexagon
     * @return a copy of the hexagon
     */
    public abstract HexagonView copy();

    public void rotate() {
        // Calculate the center of the hexagon
        Rectangle bounds = hexagon.getBounds();
        double centerX = bounds.getCenterX();
        double centerY = bounds.getCenterY();
    
        // Create an AffineTransform and rotate it 90 degrees around the center
        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(90), centerX, centerY);
    
        // Apply the rotation to the hexagon
        hexagon = (Path2D.Double) at.createTransformedShape(hexagon);
    
        // Repaint the component
        this.repaint();
    }

    public TexturePaint getTexture() {
        return texture;
    }

    public void setHovered(boolean hovered) {
        isHovered = hovered;
    }

    @Override
    public boolean contains(int x, int y) {
        return hexagon.contains(x, y);
    }

    public void setPosition(Point position) {
        this.pos = position;
    }

    public Point getPosition() {
        return pos;
    }

    public int getZ() {
        return z;
    }

    private Color darken(Color c, int z) {
        for (int i = 0; i < z; i++) {
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
