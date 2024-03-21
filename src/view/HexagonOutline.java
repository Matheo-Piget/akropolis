package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Implements a hexagon outline view.
 */
public class HexagonOutline extends HexagonView {
    private static BasicStroke stroke = new BasicStroke(size / 25);

    public HexagonOutline(int x, int y, int z) {
        super(x, y, z);
        setOpaque(false);
    }

    @Override
    public HexagonOutline copy() {
        return new HexagonOutline(pos.x, pos.y, z);
    }

    private void renderHexagon() {
        // Dispose of the old image before creating a new one
        if (render != null) {
            render.flush();
        }

        // Create the new image
        render = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        render.setAccelerationPriority(1);
        Graphics2D g2d = render.createGraphics();

        // Draw the hexagon
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLUE);
        g2d.setStroke(stroke);
        g2d.draw(hexagon);

        g2d.dispose();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        if (render == null) {
            renderHexagon();
        }
        g.drawImage(render, 0, 0, null);
    }
}
