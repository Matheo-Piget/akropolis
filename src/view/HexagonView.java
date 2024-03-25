package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import javax.swing.JComponent;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.awt.geom.Path2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Represents an hexagon on the game grid.
 */
public abstract class HexagonView extends JComponent {

    public static int size = 80; // Size of the hexagons
    private static BasicStroke stroke = new BasicStroke(size / 25);
    protected boolean isHovered = false;
    protected TexturePaint texture;
    protected Point pos = new Point(0, 0);
    protected int z;
    protected Path2D.Double hexagon = new Path2D.Double();
    protected BufferedImage render;

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
            if (i == 0) {
                hexagon.moveTo(xval, yval);
            } else {
                hexagon.lineTo(xval, yval);
            }
        }
        hexagon.closePath();
    }

    public HexagonView(int x, int y, int z, BufferedImage img) {
        this.setSize(size, size);
        pos = new Point(x, y);
        this.z = z;
        int center = size / 2 - 1; // Dumb rounding
        for (int i = 0; i < 6; i++) {
            double xval = center + center * Math.cos(i * 2 * Math.PI / 6);
            double yval = center + center * Math.sin(i * 2 * Math.PI / 6);
            if (i == 0) {
                hexagon.moveTo(xval, yval);
            } else {
                hexagon.lineTo(xval, yval);
            }
        }
        hexagon.closePath();
        this.texture = new TexturePaint(img, new java.awt.Rectangle(0, 0, size, size));
    }

    public HexagonView(int x, int y, int z, Color color) {
        this.setSize(size, size);
        pos = new Point(x, y);
        this.z = z;
        int center = size / 2 - 1; // Ligma balls
        for (int i = 0; i < 6; i++) {
            double xval = center + center * Math.cos(i * 2 * Math.PI / 6);
            double yval = center + center * Math.sin(i * 2 * Math.PI / 6);
            if (i == 0) {
                hexagon.moveTo(xval, yval);
            } else {
                hexagon.lineTo(xval, yval);
            }
        }
        hexagon.closePath();

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
     * Fill the hexagon based on the hexagon view passed
     * @param hexagon the hexagon view to fill
     */
    public void fill(HexagonView hexagon) {
        renderHexagon(Color.BLACK, hexagon.getTexture());
        repaint();
    }

    /**
     * Remove the fill from the hexagon view
     */
    public void unfill() {
        renderHexagon(Color.BLACK, texture);
        repaint();
    }

    /**
     * Render the hexagon with the given stroke color and texture
     * @param strokeColor
     * @param texture
     */
    protected void renderHexagon(Color strokeColor, TexturePaint texture) {
        // Dispose of the old image before creating a new one
        if (render != null) {
            render.flush();
        }

        // Create the new image
        render = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        render.setAccelerationPriority(1);
        Graphics2D g2d = render.createGraphics();

        // Enable Anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the hexagon
        g2d.setPaint(texture);
        g2d.fill(hexagon);

        // Draw the border of the hexagon
        if (isHovered) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(strokeColor);
        }
        g2d.setStroke(stroke);
        g2d.draw(hexagon);
        g2d.dispose();
    }

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
