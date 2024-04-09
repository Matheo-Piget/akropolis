package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import javax.swing.JComponent;
import java.awt.BasicStroke;
import java.awt.geom.Path2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.Font;

/**
 * Represents a hexagon on the game grid.
 */
public abstract class HexagonView extends JComponent {

    private final BasicStroke stroke;
    protected boolean isHovered = false;
    protected TexturePaint texture;
    protected Point pos;
    protected int z;
    protected Path2D.Double hexagon = new Path2D.Double();
    protected BufferedImage render;

    public HexagonView(int x, int y, int z, int size) {
        setSize(size, size);
        stroke = new BasicStroke((float) size / 25);
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

    /**
     * Fill the hexagon based on the hexagon view passed
     * 
     * @param hexagon the hexagon view to fill
     */
    public void fill(HexagonView hexagon) {
        renderHexagon(Color.BLACK, hexagon.texture);
        repaint();
    }

    /**
     * Remove the fill from the hexagon view
     */
    public void unfilled() {
        renderHexagon(Color.BLACK, texture);
        repaint();
    }

    /**
     * Render the hexagon with the given stroke color and texture
     * 
     * @param strokeColor The color of the border
     * @param texture The texture to fill the hexagon with
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

        // Enable antialiasing
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

        // Draw the height number only when it's not a hexagon outline
        if (!(this instanceof HexagonOutline)) {
            int fontSize = (int) (getHeight() * 0.8);
            g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
            String heightStr = String.valueOf(z);
            int stringWidth = g2d.getFontMetrics().stringWidth(heightStr);
            int x = (getWidth() - stringWidth) / 2;
            int y = getHeight() / 2 + fontSize / 3;

            // Change the color of the text to a darker version of the hexagon color
            Color hexagonColor = texture.getImage().getGraphics().getColor();
            g2d.setColor(hexagonColor.darker());

            g2d.drawString(heightStr, x, y);
        }
        g2d.dispose();
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
}
